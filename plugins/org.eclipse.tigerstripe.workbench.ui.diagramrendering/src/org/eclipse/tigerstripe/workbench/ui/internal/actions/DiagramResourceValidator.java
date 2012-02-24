package org.eclipse.tigerstripe.workbench.ui.internal.actions;

import static java.lang.String.format;
import static org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.Activator.PLUGIN_ID;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.annotation.core.Helper;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.utils.AdaptHelper;

public class DiagramResourceValidator {

	private final IResource diagramFile;
	private final Diagram diagram;
	private final XMIResourceImpl dresource;
	private final List<IStatus> statuses;
	private final IFile modelFile;
	private final String[] modelSegments;
	private final URI modelFileUri;
	private boolean needSave = false;
	
	public DiagramResourceValidator(IResource diagramFile, List<IStatus> status) {
		this.diagramFile = diagramFile;
		this.statuses = status;
		
		URI duri = Helper.makeUri(diagramFile);
		dresource = new XMIResourceImpl(duri);
		try {
			dresource.load(null);
		} catch (IOException e) {
			EclipsePlugin.log(e);
		}
		diagram = findDiagram();
		modelFile = findModelFile();
		if (modelFile != null) {
			modelFileUri = Helper.makeUri(modelFile);
			modelSegments = modelFileUri.segments();
		} else {
			modelFileUri = null;
			modelSegments = null;
		}
	}

	public boolean canBeApply() {
		return diagram != null;
	}
	
	public void fix() {
		if (!canBeApply()) {
			return;
		}
		fixProxies();
		fixName();
		fixBasePackage();
		if (needSave) {
			try {
				dresource.save(null);
			} catch (IOException e) {
				EclipsePlugin.log(e);
			}
		}
	}
	
	private void fixBasePackage() {
		 if (modelFileUri == null) {
			 return;
		 }
		 
		XMIResourceImpl mresource = new XMIResourceImpl(modelFileUri);
		try {
			mresource.load(null);
		} catch (IOException e) {
			EclipsePlugin.log(e);
		}

		String basePackage = getBasePackage();
		EList<EObject> contents = mresource.getContents();
		if (contents.size() != 0) {
			EObject eObject = contents.get(0);
			boolean needSaveModel = false;
			if (eObject instanceof Map) {
				Map map = (Map) eObject;
				String oldBasePkcg = map.getBasePackage();
				if (!basePackage.equals(oldBasePkcg)) {
					map.setBasePackage(basePackage);
					addStatus(format(
							"Wrong base package has been changed from '%s' to '%s' in file '%s'",
							oldBasePkcg, basePackage, modelFile.getFullPath()));
					needSaveModel = true;
				}
			} else if (eObject instanceof InstanceMap) {
				InstanceMap map = (InstanceMap) eObject;
				String oldBasePkcg = map.getBasePackage();
				if (!basePackage.equals(oldBasePkcg)) {
					map.setBasePackage(basePackage);
					addStatus(format(
							"Wrong base package has been changed from '%s' to '%s' in file '%s'",
							oldBasePkcg, basePackage, modelFile.getFullPath()));
					needSaveModel = true;
				}
			}
			if (needSaveModel) {
				try {
					mresource.save(null);
				} catch (IOException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

	private void fixName() {
		if (diagramFile instanceof IFile) {
			String name = ((IFile) diagramFile).getName();
			String oldName = diagram.getName();
			if (!name.equals(oldName)) {
				diagram.setName(name);
				needSave = true;
				addStatus(format(
						"Wrong diagram name been changed from '%s' to '%s' in file '%s'",
						oldName, name, diagramFile.getFullPath()));
			}
		}
	}

	private Diagram findDiagram() {
		EList<EObject> contents = dresource.getContents();
		if (contents.size() > 0) {
			EObject first = contents.get(0);
			if (first instanceof Diagram) {
				return (Diagram) first; 
			}
		}
		return null;
	}
	
	private void fixProxies() {
		if (modelSegments == null) {
			return;
		}
		needSave |= fixProxy(diagram.getElement());
		EList<?> children = diagram.getChildren();
		Iterator<?> it = children.iterator();
		while (it.hasNext()) {
			Object next = it.next();
			if (next instanceof View) {
				needSave  |= fixProxy( ((View) next).getElement());
			}
		}
	}
	
	private boolean fixProxy(EObject element) {
		if (element instanceof InternalEObject && element.eIsProxy()) {
			InternalEObject io = (InternalEObject) element;
			URI uri = io.eProxyURI();
			if (!Arrays.equals(uri.segments(), modelSegments)) {
				URI newUri = modelFileUri.appendFragment(uri.fragment());
				io.eSetProxyURI(newUri);
				addStatus("Wrong reference to model has been fixed in diagram file "
						+ diagramFile.getFullPath());
			}
			
    		return true;
		}
		return false;
	}

	private void addStatus(String msg) {
		statuses.add(new Status(Status.INFO, PLUGIN_ID, msg));
	}

	public IFile findModelFile() {
		String modelFileName = findModelFileName(diagramFile);
		if (modelFileName != null) {
			IContainer parent = diagramFile.getParent();
			if (parent != null) {
				IResource found = parent.findMember(modelFileName);
				if (found instanceof IFile) {
					return (IFile) found;
				}
			}
		}
    	return null;
	}
	
	private String findModelFileName(IResource file) {
		String ext = diagramFile.getFileExtension();
		if (ext == null) {
			return null;
		}
		String modelExt;
		if ("wvd".equals(ext)) {
			modelExt = "vwm";
		} else  if ("wod".equals(ext)) {
			modelExt = "owm";
		} else {
			return null;
		}
		
		int extlength = ext.length() + 1;
		if (file != null) {
			String name = file.getName();
			int length = name.length();
			if (length < extlength) {
				return null;
			}
			return name.substring(0, length - extlength) + "." + modelExt;
		}
		return null;
	}
	
	public String getBasePackage() {

		IProject eproject = modelFile.getProject();
		ITigerstripeModelProject project = AdaptHelper.adapt(eproject, ITigerstripeModelProject.class);

		IResource res = modelFile;
		
		try {
			String baseRepository = ((TigerstripeProjectHandle) project)
					.getBaseRepository();
			IResource repo = res.getProject().getFolder(baseRepository);
			if (repo != null && res.getParent() instanceof IFolder) {
				IPath resRelPath = res.getParent().getProjectRelativePath();
				IPath repoPath = repo.getProjectRelativePath();

				int matchingSegments = resRelPath
						.matchingFirstSegments(repoPath);
				if (matchingSegments != 0) {
					IPath packagePath = resRelPath
							.removeFirstSegments(matchingSegments);
					String packageStr = packagePath.toOSString();
					if (packageStr != null && packageStr.length() != 0) {
						packageStr = packageStr
								.replace(File.separatorChar, '.');
					}

					if (packageStr != null && packageStr.length() != 0)
						return packageStr;
				}
			}

			String basePackage = project.getProjectDetails().getProperty(
					IProjectDetails.DEFAULTARTIFACTPACKAGE_PROP,
					"com.mycompany");

			return basePackage;
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		return "com.mycompany";
	}

}
