package org.eclipse.tigerstripe.workbench.ui.internal;

import static org.eclipse.jface.dialogs.IDialogConstants.NO_LABEL;
import static org.eclipse.jface.dialogs.IDialogConstants.YES_ID;
import static org.eclipse.jface.dialogs.IDialogConstants.YES_LABEL;

import java.text.MessageFormat;
import java.util.Collection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.annotation.properties.ModelComponentsLabelProvider;
import org.eclipse.tigerstripe.workbench.ui.internal.preferences.GeneralPreferencePage;

public class Ask {

	public static boolean aboutMemebersRemovingFields(Shell shell,
			String title, String message) {
		return aboutMemebersRemoving(
				GeneralPreferencePage.P_DONT_REMIND_FILEDS_REMOVING, shell,
				title, message);
	}

	public static boolean aboutMemebersRemovingMethods(Shell shell,
			String title, String message) {
		return aboutMemebersRemoving(
				GeneralPreferencePage.P_DONT_REMIND_METHODS_REMOVING, shell,
				title, message);
	}

	public static boolean aboutMemebersRemovingLiterals(Shell shell,
			String title, String message) {
		return aboutMemebersRemoving(
				GeneralPreferencePage.P_DONT_REMIND_LITERAL_REMOVING, shell,
				title, message);
	}

	public static boolean aboutMemebersRemovingStereotypes(Shell shell,
			String title, String message) {
		return aboutMemebersRemoving(
				GeneralPreferencePage.P_DONT_REMIND_STEREOTYPES_REMOVING,
				shell, title, message);
	}

	public static boolean aboutMemebersRemoving(String key, Shell shell, String title,
			String message) {

		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();
		if (store.getBoolean(key)) {
			return true;
		} else {
			MessageDialogWithToggle msgDialog = new MessageDialogWithToggle(
					shell, title, null, message,
					MessageDialog.QUESTION,
					new String[] { YES_LABEL, NO_LABEL }, 1,
					"Don't remind me again", false);
			int open = msgDialog.open();
			if (open == YES_ID) {
				if (msgDialog.getToggleState()) {
					store.setValue(key, true);
				}
				return true;
			} else {
				return false;
			}
		}
	}
	
	private static class CascadeRemovingWarningDialog extends
			MessageDialogWithToggle {

		private final Collection<?> elements;

		private CascadeRemovingWarningDialog(Shell shell, 
				Collection<?> elements) {
			super(shell, "Confirm Cascade Delete", null, null,
					MessageDialog.QUESTION,
					new String[] { YES_LABEL, NO_LABEL }, 1,
					"Remember my choice", false);
			
			this.elements = elements;

			if (elements.size() == 1) {
				message ="Found dangling reference. Do you want to remove it?";
			} else {
				message = MessageFormat
						.format("Found {0} dangling references. Do you want to remove them?",
								1);
			}
		}

		@Override
		protected Control createCustomArea(Composite parent) {
			Control control = createViewer(parent);
			createPlaceHolder(parent);
			return control;
		}

		private Control createViewer(Composite parent) {
			TableViewer viewer = new TableViewer(parent);
			viewer.setContentProvider(new ArrayContentProvider());
			viewer.setLabelProvider(new ModelComponentsLabelProvider());
			viewer.setInput(elements);
			GridData data = new GridData(GridData.FILL_BOTH);
			data.horizontalSpan = 2;
			data.horizontalIndent = 50;
			data.minimumHeight = 100;
			Control control = viewer.getControl();
			control.setLayoutData(data);
			return control;
		}

		private void createPlaceHolder(Composite parent) {
			Label placeHolder = new Label(parent, SWT.NONE);
			GridData data = new GridData(GridData.FILL_BOTH);
			data.minimumHeight = 20;
			placeHolder.setLayoutData(data);
		}
	};

	public static boolean aboutCascadeRemoving(Shell shell,
			final Collection<?> elements) {
		if (elements.size() == 0) {
			return false;
		}
		String key = GeneralPreferencePage.P_CASCADE_DELETE;
		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();
		String value = store.getString(key);
		if (MessageDialogWithToggle.ALWAYS.equals(value)) {
			return true;
		} else if (MessageDialogWithToggle.NEVER.equals(value)) {
			return false;
		}
		MessageDialogWithToggle msgDialog = new CascadeRemovingWarningDialog(
				shell, elements);
		msgDialog.setPrefKey(key);
		msgDialog.setPrefStore(store);
		return msgDialog.open() == YES_ID;
	}
}
