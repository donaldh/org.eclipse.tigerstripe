package org.eclipse.tigerstripe.workbench.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.utils.AdaptHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ExcludeArtifactService {

	private static final String FILE_NAME = ".buildinfo";
	private static final String EXCLUDES = "excludes";
	private static final String EXCLUDE = "exclude";
	private static final String FQN = "fqn";

	public synchronized static void exclude(IAbstractArtifact artifact) {
		try {
			ITigerstripeModelProject project = artifact.getProject();
			Set<String> exclude = getExcluded(project);
			exclude.add(artifact.getFullyQualifiedName());
			save(exclude, project);
			touch(artifact);
		} catch (Exception e) {
			BasePlugin.log(e);
		}
	}

	public synchronized static void include(IAbstractArtifact artifact) {
		try {
			ITigerstripeModelProject project = artifact.getProject();
			Set<String> exclude = getExcluded(project);
			exclude.remove(artifact.getFullyQualifiedName());
			save(exclude, project);
			touch(artifact);
		} catch (Exception e) {
			BasePlugin.log(e);
		}
	}

	private static void touch(IAbstractArtifact artifact) throws CoreException {
		IResource resource = AdaptHelper.adapt(artifact, IResource.class);
		if (resource != null) {
			resource.touch(null);
		}
	}

	public synchronized static Set<String> getExcluded(
			ITigerstripeModelProject project) {
		Set<String> result = new LinkedHashSet<String>();
		try {
			IFile file = getBuildInfoFile(project);
			if (file == null || !file.exists()) {
				return result;
			}
			InputStream contents = file.getContents();
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(contents);
				Element documentElement = document.getDocumentElement();
				NodeList exludes = documentElement
						.getElementsByTagName(EXCLUDES);
				for (int i = 0; i < exludes.getLength(); ++i) {
					try {
						NodeList exludeNodes = ((Element) exludes.item(i))
								.getElementsByTagName(EXCLUDE);
						for (int j = 0; j < exludeNodes.getLength(); ++j) {
							Element exclude = (Element) exludeNodes.item(j);
							String fqn = exclude.getAttribute(FQN);
							result.add(fqn);
						}
					} catch (Exception e) {
						BasePlugin.log(e);
					}
				}
			} finally {
				contents.close();
			}
		} catch (Exception e) {
			BasePlugin.log(e);
		}
		return result;
	}

	private static void save(Set<String> exclude,
			ITigerstripeModelProject project) {
		try {
			
			IFile file = getBuildInfoFile(project);

			if (file == null) {
				return;
			}

			if (exclude.isEmpty()) {
				file.delete(true, null);
				return;
			}
			
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();

			Element root = document.createElement("buildinfo");
			document.appendChild(root);

			Element excludes = document.createElement(EXCLUDES);
			root.appendChild(excludes);

			for (String ex : exclude) {
				Element excludeNode = document.createElement(EXCLUDE);
				excludeNode.setAttribute(FQN, ex);
				excludes.appendChild(excludeNode);
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(document);

			StreamResult result = new StreamResult(outputStream);
			transformer.transform(source, result);
			if (!file.exists()) {
				file.create(
						new ByteArrayInputStream(outputStream.toByteArray()),
						true, null);
			} else {
				file.setContents(
						new ByteArrayInputStream(outputStream.toByteArray()),
						IFile.FORCE, null);
			}
		} catch (Exception e) {
			BasePlugin.log(e);
		}

	}

	private static IFile getBuildInfoFile(ITigerstripeModelProject project) {
		IProject proj = AdaptHelper.adapt(project, IProject.class);
		if (proj == null) {
			return null;
		}
		return proj.getFile(FILE_NAME);
	}

}
