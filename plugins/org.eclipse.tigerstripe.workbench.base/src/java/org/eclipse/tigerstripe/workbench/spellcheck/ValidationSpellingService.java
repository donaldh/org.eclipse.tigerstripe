package org.eclipse.tigerstripe.workbench.spellcheck;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.ui.texteditor.spelling.SpellingService;

public class ValidationSpellingService extends SpellingService {

	private static final IPreferenceStore preferences;

	static {
		preferences = new PreferenceStore();
		preferences.setValue(SpellingService.PREFERENCE_SPELLING_ENABLED, true);
	}

	public ValidationSpellingService() {
		super(preferences);
	}

}