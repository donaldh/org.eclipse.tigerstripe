package org.eclipse.tigerstripe.workbench.spellcheck;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.jface.text.Document;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.texteditor.spelling.ISpellingProblemCollector;
import org.eclipse.ui.texteditor.spelling.SpellingContext;
import org.eclipse.ui.texteditor.spelling.SpellingProblem;
import org.eclipse.ui.texteditor.spelling.SpellingService;

public class SpellingCore {

	private static final SpellingService spellingService;
	private static final SpellingContext textContext;

	static {
		textContext = new SpellingContext();
		textContext.setContentType(Platform.getContentTypeManager()
				.getContentType(IContentTypeManager.CT_TEXT));
		spellingService = isHeadlessMode() ? new ValidationSpellingService()
				: EditorsUI.getSpellingService();
	}

	private static boolean isHeadlessMode() {
		try {
			PlatformUI.getWorkbench();
			return true;
		} catch (IllegalStateException ex) {
			IStatus status = new Status(IStatus.INFO, BasePlugin.PLUGIN_ID,
					"Start on Headless mode");
			BasePlugin.getDefault().getLog().log(status);
			return false;
		}
	}

	public static List<String> checkText(String text) {
		ProblemCollector collector = new ProblemCollector();
		spellingService.check(new Document(text), textContext, collector, null);
		return collector.getErrors();
	}

	public static class ProblemCollector implements ISpellingProblemCollector {

		private final List<String> errors = new ArrayList<String>();

		public ProblemCollector() {
			super();
		}

		public void accept(SpellingProblem problem) {
			errors.add(problem.getMessage());
		}

		public void beginCollecting() {
			// Nothing
		}

		public void endCollecting() {
			// Nothing
		}

		public List<String> getErrors() {
			return errors;
		}

	}

}
