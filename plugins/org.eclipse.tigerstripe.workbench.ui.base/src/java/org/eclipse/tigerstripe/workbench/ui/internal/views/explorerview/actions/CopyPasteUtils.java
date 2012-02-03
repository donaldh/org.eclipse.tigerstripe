package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import static org.eclipse.tigerstripe.workbench.model.ArtifactUtils.getManager;

import java.io.StringReader;
import java.util.Collection;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMember;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;

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

}
