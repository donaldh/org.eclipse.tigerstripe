package org.eclipse.tigerstripe.workbench.convert;

import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.model.WorkbenchPartLabelProvider;

public class SuggestConvertationDialog extends Dialog {

	private final boolean askParent;
	private final boolean askChildren;
	private final Collection<? extends IEditorPart> toSave;
	private final boolean hasRelations;
	private final String fqn;
	private Button convertParents;
	private Button convertChildren;
	private Result result;

	public SuggestConvertationDialog(Shell shell, String fqn,
			boolean askParent, boolean askChildren, boolean hasRelations,
			Collection<? extends IEditorPart> toSave) {
		super(shell);
		this.fqn = fqn;
		this.askParent = askParent;
		this.askChildren = askChildren;
		this.hasRelations = hasRelations;
		this.toSave = toSave;
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.RESIZE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		parent = (Composite) super.createDialogArea(parent);

		getShell().setText("Convert " + fqn);

		if (hasRelations) {
			Label warning = new Label(parent, SWT.WRAP);
			warning.setText(String
					.format("All associations or dependencies related to artifact will be removed",
							fqn));
			GridDataFactory.fillDefaults().grab(true, true).hint(250, -1)
					.applyTo(warning);
		}

		if (askParent) {
			convertParents = makeConvertDeleteChoice(parent, "Artifact parents");
		}

		if (askChildren) {
			convertChildren = makeConvertDeleteChoice(parent,
					"Artifact children");
		}

		if (!toSave.isEmpty()) {

			Group panel = new Group(parent, SWT.TITLE);
			panel.setLayoutData(new GridData(GridData.FILL_BOTH));
			GridLayoutFactory.swtDefaults().applyTo(panel);

			Label label = new Label(panel, SWT.WRAP);
			label.setText(String.format(
					"Following editors related to artifact will be saved", fqn));
			GridDataFactory.fillDefaults().grab(true, true).hint(250, -1)
					.applyTo(label);

			TableViewer viewer = new TableViewer(panel);
			viewer.setLabelProvider(new WorkbenchPartLabelProvider());
			viewer.setContentProvider(ArrayContentProvider.getInstance());
			viewer.setInput(toSave);
			viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
			GridDataFactory.fillDefaults().grab(true, true).hint(300, -1)
					.applyTo(viewer.getTable());

		}

		return parent;
	}

	private Button makeConvertDeleteChoice(Composite parent, String title) {
		Group panel = new Group(parent, SWT.TITLE);
		panel.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(panel);
		panel.setText(title);

		Button convert = new Button(panel, SWT.RADIO);
		convert.setText("Convert");
		convert.setSelection(true);

		Button remove = new Button(panel, SWT.RADIO);
		remove.setText("Remove");

		return convert;
	}

	@Override
	protected void okPressed() {
		result = new Result(convertParents != null
				&& convertParents.getSelection(), convertChildren != null
				&& convertChildren.getSelection());
		super.okPressed();
	}

	public Result getResult() {
		return result;
	}

	public static class Result {
		public final boolean convertParents;
		public final boolean convertChildren;

		public Result(boolean convertParents, boolean convertChildren) {
			this.convertParents = convertParents;
			this.convertChildren = convertChildren;
		}
	}
}
