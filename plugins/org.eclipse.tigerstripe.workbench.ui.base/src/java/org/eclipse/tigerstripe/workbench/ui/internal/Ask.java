package org.eclipse.tigerstripe.workbench.ui.internal;

import static org.eclipse.jface.dialogs.IDialogConstants.NO_LABEL;
import static org.eclipse.jface.dialogs.IDialogConstants.YES_ID;
import static org.eclipse.jface.dialogs.IDialogConstants.YES_LABEL;
import static org.eclipse.tigerstripe.workbench.ui.internal.preferences.GeneralPreferencePage.P_DONT_REMIND_MEMBERS_REMOVING;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

public class Ask {

	public static boolean aboutMemebersRemoving(Shell shell, String title,
			String message) {

		IPreferenceStore store = EclipsePlugin.getDefault()
				.getPreferenceStore();
		if (store.getBoolean(P_DONT_REMIND_MEMBERS_REMOVING)) {
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
					store.setValue(P_DONT_REMIND_MEMBERS_REMOVING, true);
				}
				return true;
			} else {
				return false;
			}
		}
	}

}
