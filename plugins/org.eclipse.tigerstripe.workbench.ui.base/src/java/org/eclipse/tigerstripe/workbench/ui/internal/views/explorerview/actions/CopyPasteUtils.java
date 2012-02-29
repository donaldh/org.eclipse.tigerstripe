package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import static java.util.Collections.emptyList;
import static org.eclipse.tigerstripe.workbench.model.ArtifactUtils.getManager;
import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.persist.AbstractArtifactPersister;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMember;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaSource;

public class CopyPasteUtils {

	public static final String MEMBER_CONTAINER_NAME = "org_eclipse_tigerstripe_MemberContainer";

	public static boolean containsMembers(Clipboard clipboard) {
		TransferData[] availableTypes = clipboard.getAvailableTypes();
		if (isAvailable(TextTransfer.getInstance(), availableTypes)) {
			Object contents = clipboard.getContents(TextTransfer.getInstance());
			if (contents instanceof String) {
				return ((String) contents).contains(MEMBER_CONTAINER_NAME);
			}
		}
		return false;
	}

	public static boolean doPaste(IAbstractArtifact artifact,
			Clipboard clipboard, boolean saveArtifact) {
		
		if (saveArtifact) {
			try {
				artifact = artifact.makeWorkingCopy(new NullProgressMonitor());
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			} 
		}
		
		ArtifactManager manager = getManager(artifact);
		if (manager != null) {
			TransferData[] availableTypes = clipboard.getAvailableTypes();
			if (isAvailable(TextTransfer.getInstance(), availableTypes)) {
				Object contents = clipboard.getContents(TextTransfer
						.getInstance());
				if (contents instanceof String) {
					StringReader reader = new StringReader((String) contents);
					try {
						JavaDocBuilder builder = new JavaDocBuilder();
						JavaSource source = builder.addSource(reader);
						ManagedEntityArtifact container = new ManagedEntityArtifact(
								source.getClasses()[0], manager,
								new NullProgressMonitor());

						Collection<IField> fields = container.getFields();
						Collection<IMethod> methods = container.getMethods();
						Collection<ILiteral> literals = container.getLiterals();

						for (IField f : fields) {
							if (!hasSameName(artifact.getFields(), f)) {
								artifact.addField(f);
							}
						}
						for (IMethod m : methods) {
							if (!hasSameSignature(artifact.getMethods(), m)) {
								artifact.addMethod(m);
							}
						}
						for (ILiteral l : literals) {
							if (!hasSameName(artifact.getLiterals(), l)) {
								if (artifact instanceof IEnumArtifact) {
									IEnumArtifact enm = (IEnumArtifact) artifact;
									IType type = l.getType();
									IType baseType = enm.getBaseType();
									if (type == null || baseType == null) {
										continue;
									}
									if (!type.equals(baseType)) {
										l.setType(baseType);
									}
								}
								artifact.addLiteral(l);
							}
						}
						if (saveArtifact) {
							artifact.doSave(new NullProgressMonitor());
						}
						container.dispose();
						return true;
					} catch (Exception e) {
						// Ignore
					}
				}

			}
		}
		return false;
	}

	private static boolean hasSameSignature(Collection<IMethod> methods,
			IMethod m) {
		for (IMethod method : methods) {
			if (m.getMethodId().equals(method.getMethodId())) {
				return true;
			}
		}
		return false;
	}

	private static boolean hasSameName(Collection<? extends IMember> members,
			IMember member) {
		for (IMember m : members) {
			if (member.getName().equals(m.getName())) {
				return true;
			}
		}
		return false;
	}

	private static boolean isAvailable(Transfer transfer,
			TransferData[] availableDataTypes) {
		for (int i = 0; i < availableDataTypes.length; i++) {
			if (transfer.isSupportedType(availableDataTypes[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean copyMemebers(Object[] selected, Clipboard clipboard) {
		List<IMember> members = collectMemebers(selected);

		try {

			if (!members.isEmpty()) {
				IAbstractArtifact artifact = members.get(0)
						.getContainingArtifact();
				if (artifact != null) {
					ITigerstripeModelProject project = artifact.getProject();
					if (project != null) {
						IArtifactManagerSession session = project
								.getArtifactManagerSession();
						if (session != null) {
							IAbstractArtifactInternal transportArtifact = new ManagedEntityArtifact(
									null);
							transportArtifact.setName(MEMBER_CONTAINER_NAME);

							for (IMember member : members) {
								member = member.clone();
								if (member instanceof IField) {
									transportArtifact.addField((IField) member);
								} else if (member instanceof ILiteral) {
									transportArtifact
											.addLiteral((ILiteral) member);
								} else if (member instanceof IMethod) {
									transportArtifact
											.addMethod((IMethod) member);
								}
							}

							StringWriter writer = new StringWriter();
							AbstractArtifactPersister persister = transportArtifact
									.getArtifactPersister(writer);
							try {
								persister.applyTemplate();
								Object[] data = { writer.toString() };
								Transfer[] transfers = { TextTransfer
										.getInstance() };
								clipboard.setContents(data, transfers);
							} catch (TigerstripeException e) {
								EclipsePlugin.log(e);
							}
							return true;
						}
					}
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return false;
	}

	private static List<IMember> collectMemebers(Object[] objects) {
		List<IMember> result = new ArrayList<IMember>(objects.length);
		for (Object object : objects) {
			if (object instanceof IAdaptable) {
				IModelComponent modelComponent = adapt(((IAdaptable) object),
						IModelComponent.class);
				if ((modelComponent instanceof IMember)) {
					result.add((IMember) modelComponent);
				} else {
					return emptyList();
				}
			} else {
				return emptyList();
			}
		}
		return result;
	}
	
	public static boolean allMemebers(Object[] objects) {
		for (Object object : objects) {
			if (object instanceof IAdaptable) {
				IModelComponent modelComponent = adapt(((IAdaptable) object),
						IModelComponent.class);
				if (!(modelComponent instanceof IMember)) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}
}
