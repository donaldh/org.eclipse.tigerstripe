package org.eclipse.tigerstripe.workbench.ui.internal;

import static org.eclipse.jface.dialogs.IDialogConstants.NO_LABEL;
import static org.eclipse.jface.dialogs.IDialogConstants.YES_ID;
import static org.eclipse.jface.dialogs.IDialogConstants.YES_LABEL;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
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
}
