package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import static org.eclipse.tigerstripe.workbench.model.ArtifactUtils.getManager;
import static org.eclipse.tigerstripe.workbench.utils.AdaptHelper.adapt;

import java.io.StringReader;
import java.util.Collection;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.internal.ui.refactoring.reorg.PasteAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMember;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.ui.IWorkbenchSite;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaSource;

public class TSPasteAction extends PasteAction {

	private final Clipboard clipboard;

	public TSPasteAction(IWorkbenchSite site, Clipboard clipboard) {
		super(site, clipboard);
		this.clipboard = clipboard;
	}

	@Override
	public void run(IStructuredSelection selection) {
		Object selected = selection.getFirstElement();
		if (selected instanceof IAdaptable) {
			IAbstractArtifact artifact = adapt((IAdaptable) selected,
					IAbstractArtifact.class);

			if (artifact != null) {
				ArtifactManager manager = getManager(artifact);
				if (manager != null) {
					TransferData[] availableTypes = clipboard
							.getAvailableTypes();
					if (isAvailable(TextTransfer.getInstance(), availableTypes)) {
						Object contents = clipboard.getContents(TextTransfer
								.getInstance());
						if (contents instanceof String) {
							StringReader reader = new StringReader(
									(String) contents);
							try {
								JavaDocBuilder builder = new JavaDocBuilder();
								JavaSource source = builder.addSource(reader);
								ManagedEntityArtifact container = new ManagedEntityArtifact(
										source.getClasses()[0], manager,
										new NullProgressMonitor());

								Collection<IField> fields = container
										.getFields();
								Collection<IMethod> methods = container
										.getMethods();
								Collection<ILiteral> literals = container
										.getLiterals();

								for (IField f : fields) {
									if (!hasSameName(artifact.getFields(), f)) {
										artifact.addField(f);
									}
								}
								for (IMethod m : methods) {
									if (!hasSameSignature(
											artifact.getMethods(), m)) {
										artifact.addMethod(m);
									}
								}
								for (ILiteral l : literals) {
									if (!hasSameName(artifact.getLiterals(), l)) {
										artifact.addLiteral(l);
									}
								}
								artifact.doSave(new NullProgressMonitor());
								manager.removeArtifact(container);
								container.dispose();
								return;
							} catch (TigerstripeException e) {
								// Ignore
							}
						}

					}
				}

			}
		}
		super.run(selection);
	}

	private boolean hasSameSignature(Collection<IMethod> methods, IMethod m) {
		for (IMethod method : methods) {
			if (m.getMethodId().equals(method.getMethodId())) {
				return true;
			}
		}
		return false;
	}

	private boolean hasSameName(Collection<? extends IMember> members,
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
