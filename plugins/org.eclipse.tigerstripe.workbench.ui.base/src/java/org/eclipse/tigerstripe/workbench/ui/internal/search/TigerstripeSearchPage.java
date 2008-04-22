/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.internal.ui.dialogs.TextFieldNavigationHandler;
import org.eclipse.jdt.internal.ui.search.SearchMessages;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tigerstripe.workbench.internal.contract.ContractUtils;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;

public class TigerstripeSearchPage extends DialogPage implements ISearchPage {

	private ISearchPageContainer container;

	private boolean fIsCaseSensitive;

	private static final int HISTORY_SIZE = 12;

	private IDialogSettings fDialogSettings;

	private Combo fPattern;
	private Button fCaseSensitive;
	private boolean fFirstTime = true;

	private Button[] fSearchFor;
	private Button[] fLimitTo;
	private SearchPatternData fInitialData;

	private final static String PAGE_NAME = "TigerstripeSearchPage"; //$NON-NLS-1$
	private final static String STORE_CASE_SENSITIVE = "CASE_SENSITIVE"; //$NON-NLS-1$
	private final static String STORE_HISTORY = "HISTORY"; //$NON-NLS-1$
	private final static String STORE_HISTORY_SIZE = "HISTORY_SIZE"; //$NON-NLS-1$

	private final List<SearchPatternData> fPreviousSearchPatterns;

	public static class SearchPatternData {
		private int searchFor;
		private String searchPattern;
		private boolean isCaseSensitive;
		private int limitTo;
		private ISearchPageContainer container;
		private Pattern pattern;

		public SearchPatternData(int searchFor, String searchPattern,
				int limitTo, boolean isCaseSensitive,
				ISearchPageContainer container) {
			this.searchFor = searchFor;
			this.searchPattern = searchPattern;
			this.isCaseSensitive = isCaseSensitive;
			this.container = container;
			this.limitTo = limitTo;
		}

		public int getLimitTo() {
			return limitTo;
		}

		public int getContainerScope() {
			return this.container.getSelectedScope();
		}

		public IAbstractArtifact[] getArtifactsInScope() {
			if (container == null)
				return new IAbstractArtifact[0];

			List<IAbstractArtifact> artifacts = new ArrayList<IAbstractArtifact>();

			if (container.getSelectedScope() == ISearchPageContainer.SELECTION_SCOPE) {
				if (container.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection ssel = (IStructuredSelection) container
							.getSelection();
					for (Iterator iter = ssel.iterator(); iter.hasNext();) {
						Object obj = iter.next();
						if (obj instanceof ICompilationUnit) {
							IAbstractArtifact art = TSExplorerUtils
									.getArtifactFor(obj);
							if (art != null) {
								artifacts.add(art);
							}
						} else if (obj instanceof IPackageFragment) {
							// add all content
							IPackageFragment frag = (IPackageFragment) obj;
							List<IAbstractArtifact> containedArts = TSExplorerUtils
									.findAllContainedArtifacts(frag);
							artifacts.addAll(containedArts);
						}
					}
				}
			}

			return artifacts.toArray(new IAbstractArtifact[artifacts.size()]);
		}

		public ITigerstripeModelProject[] getProjectsInScope() {
			if (container == null)
				return new ITigerstripeModelProject[0];

			List<ITigerstripeModelProject> prjList = new ArrayList<ITigerstripeModelProject>();

			if (container.getSelectedScope() == ISearchPageContainer.SELECTED_PROJECTS_SCOPE) {
				String[] projectNames = container.getSelectedProjectNames();
				for (String projectName : projectNames) {
					IProject project = ResourcesPlugin.getWorkspace().getRoot()
							.getProject(projectName);
					IAbstractTigerstripeProject atsProject = EclipsePlugin
							.getITigerstripeProjectFor(project);
					if (atsProject instanceof ITigerstripeModelProject
							&& atsProject.exists()) {
						prjList.add((ITigerstripeModelProject) atsProject);
					}
				}
			} else if (container.getSelectedScope() == ISearchPageContainer.WORKSPACE_SCOPE) {
				IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
						.getProjects();
				for (IProject project : projects) {
					IAbstractTigerstripeProject atsProject = EclipsePlugin
							.getITigerstripeProjectFor(project);
					if (atsProject instanceof ITigerstripeModelProject
							&& atsProject.exists()) {
						prjList.add((ITigerstripeModelProject) atsProject);
					}
				}
			}

			return prjList.toArray(new ITigerstripeModelProject[prjList.size()]);
		}

		public int getSearchFor() {
			return this.searchFor;
		}

		public String getSearchPattern() {
			return this.searchPattern;
		}

		private Pattern getPattern() {
			if (pattern == null) {
				// Note the pattern is not given as a true regexp we need to
				// adjust it

				// For convenience we add a * before and after the pattern
				String enhancedPattern = "*" + getSearchPattern() + "*";
				String regExpPattern = ContractUtils
						.mapFromUserPattern(enhancedPattern);
				if (isCaseSensitive())
					pattern = Pattern.compile(regExpPattern);
				else
					pattern = Pattern.compile(regExpPattern,
							Pattern.CASE_INSENSITIVE);
			}
			return pattern;
		}

		/**
		 * Returns true if the given string match the pattern for this Note:
		 * this takes into account the case sensitivity
		 * 
		 * @param str
		 * @return
		 */
		public boolean matchPattern(String str) {
			Pattern pat = getPattern();
			Matcher matcher = pat.matcher(str);
			return matcher.matches();
		}

		public boolean isCaseSensitive() {
			return this.isCaseSensitive;
		}

		public static SearchPatternData create(IDialogSettings settings) {
			String pattern = settings.get("pattern"); //$NON-NLS-1$
			if (pattern.length() == 0)
				return null;

			try {
				int searchFor = settings.getInt("searchFor"); //$NON-NLS-1$
				boolean isCaseSensitive = settings
						.getBoolean("isCaseSensitive"); //$NON-NLS-1$
				int limitTo = settings.getInt("limitTo");

				return new SearchPatternData(searchFor, pattern, limitTo,
						isCaseSensitive, null);
			} catch (NumberFormatException e) {
				return null;
			}
		}

		public void store(IDialogSettings settings) {
			settings.put("searchFor", searchFor); //$NON-NLS-1$
			settings.put("pattern", searchPattern); //$NON-NLS-1$
			settings.put("isCaseSensitive", isCaseSensitive); //$NON-NLS-1$
			settings.put("limitTo", limitTo);
		}

	}

	public TigerstripeSearchPage() {
		fPreviousSearchPatterns = new ArrayList<SearchPatternData>();
	}

	public boolean performAction() {

		if (container != null
				&& container.getSelectedScope() == ISearchPageContainer.WORKING_SET_SCOPE) {
			MessageDialog
					.openError(getShell(),
							"Tigerstripe Search: Unsupported scope",
							"'Working Set' is not supported for Tigerstripe Model Searches.");
			return false;
		}
		return performNewSearch();
	}

	private SearchPatternData findInPrevious(String pattern) {
		for (Iterator<SearchPatternData> iter = fPreviousSearchPatterns
				.iterator(); iter.hasNext();) {
			SearchPatternData element = iter.next();
			if (pattern.equals(element.getSearchPattern()))
				return element;
		}
		return null;
	}

	/**
	 * Return search pattern data and update previous searches. An existing
	 * entry will be updated.
	 * 
	 * @return the pattern data
	 */
	private SearchPatternData getPatternData() {
		String pattern = getPattern();
		SearchPatternData match = findInPrevious(pattern);
		if (match != null) {
			fPreviousSearchPatterns.remove(match);
		}
		match = new SearchPatternData(getSearchFor(), pattern, getLimitTo(),
				fCaseSensitive.getSelection(), getContainer());

		fPreviousSearchPatterns.add(0, match); // insert on top
		return match;
	}

	private boolean performNewSearch() {
		SearchPatternData data = getPatternData();

		TigerstripeSearchQuery query = new TigerstripeSearchQuery(data);
		NewSearchUI.runQueryInBackground(query);
		return true;

	}

	public void setContainer(ISearchPageContainer container) {
		this.container = container;
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		readConfiguration();

		Composite result = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 10;
		result.setLayout(layout);

		Control expressionComposite = createExpression(result);
		expressionComposite.setLayoutData(new GridData(GridData.FILL,
				GridData.CENTER, true, false, 2, 1));

		Label separator = new Label(result, SWT.NONE);
		separator.setVisible(false);
		GridData data = new GridData(GridData.FILL, GridData.FILL, false,
				false, 2, 1);
		data.heightHint = convertHeightInCharsToPixels(1) / 3;
		separator.setLayoutData(data);

		Control searchFor = createSearchFor(result);
		searchFor.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, false, 1, 1));

		Control limitTo = createLimitTo(result);
		limitTo.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true,
				false, 1, 1));

		SelectionAdapter javaElementInitializer = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				doPatternModified();
			}
		};

		for (int i = 0; i < fSearchFor.length; i++) {
			fSearchFor[i].addSelectionListener(javaElementInitializer);
		}

		setControl(result);

		Dialog.applyDialogFont(result);
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(result,
		// IJavaHelpContextIds.JAVA_SEARCH_PAGE);
	}

	private Button createButton(Composite parent, int style, String text,
			int data, boolean isSelected) {
		Button button = new Button(parent, style);
		button.setText(text);
		button.setData(new Integer(data));
		button.setLayoutData(new GridData());
		button.setSelection(isSelected);
		return button;
	}

	private Control createSearchFor(Composite parent) {
		Group result = new Group(parent, SWT.NONE);
		result.setText(SearchMessages.SearchPage_searchFor_label);
		result.setLayout(new GridLayout(2, true));

		fSearchFor = new Button[] {
				createButton(result, SWT.RADIO, "Type",
						ITigerstripeSearchConstants.TYPE, true),
				createButton(result, SWT.RADIO, "Method",
						ITigerstripeSearchConstants.METHOD, false),
				createButton(result, SWT.RADIO, "Attribute",
						ITigerstripeSearchConstants.FIELD, false),
				createButton(result, SWT.RADIO, "Literal",
						ITigerstripeSearchConstants.LITERAL, false) };

		// Fill with dummy radio buttons
		Label filler = new Label(result, SWT.NONE);
		filler.setVisible(false);
		filler.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1,
				1));

		for (Button button : fSearchFor) {
			button.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					Button selectedButton = (Button) e.getSource();
					if ("Type".equals(selectedButton.getText())) {
						setLimitToEnabled(true);
					} else {
						setLimitToEnabled(false);
					}
				}

			});
		}
		return result;
	}

	private void setLimitToEnabled(boolean enabled) {
		for (Button button : fLimitTo) {
			button.setEnabled(enabled);
		}
	}

	private void doPatternModified() {
		if (fInitialData != null
				&& getPattern()
						.equals(
								fInitialData.getSearchPattern() != null
										&& fInitialData.getSearchFor() == getSearchFor())) {
			fCaseSensitive.setEnabled(false);
			fCaseSensitive.setSelection(true);
		} else {
			fCaseSensitive.setEnabled(true);
			fCaseSensitive.setSelection(fIsCaseSensitive);
		}
	}

	private Control createExpression(Composite parent) {
		Composite result = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		result.setLayout(layout);

		// Pattern text + info
		Label label = new Label(result, SWT.LEFT);
		label.setText(SearchMessages.SearchPage_expression_label);
		label.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false,
				false, 2, 1));

		// Pattern combo
		fPattern = new Combo(result, SWT.SINGLE | SWT.BORDER);
		fPattern.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handlePatternSelected();
				updateOKStatus();
			}
		});
		fPattern.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				doPatternModified();
				updateOKStatus();

			}
		});
		TextFieldNavigationHandler.install(fPattern);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, false,
				1, 1);
		data.widthHint = convertWidthInCharsToPixels(50);
		fPattern.setLayoutData(data);

		// Ignore case checkbox
		fCaseSensitive = new Button(result, SWT.CHECK);
		fCaseSensitive
				.setText(SearchMessages.SearchPage_expression_caseSensitive);
		fCaseSensitive.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fIsCaseSensitive = fCaseSensitive.getSelection();
			}
		});
		fCaseSensitive.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				false, false, 1, 1));

		return result;
	}

	/**
	 * Returns the search page's container.
	 * 
	 * @return the search page container
	 */
	private ISearchPageContainer getContainer() {
		return container;
	}

	private String getPattern() {
		return fPattern.getText();
	}

	private void setSearchFor(int searchFor) {
		for (int i = 0; i < fSearchFor.length; i++) {
			Button button = fSearchFor[i];
			button.setSelection(searchFor == getIntData(button));
		}
	}

	private void setLimitTo(int limitTo) {
		for (int i = 0; i < fLimitTo.length; i++) {
			Button button = fLimitTo[i];
			button.setSelection(limitTo == getIntData(button));
		}
	}

	private int getIntData(Button button) {
		return ((Integer) button.getData()).intValue();
	}

	final void updateOKStatus() {
		boolean isValid = isValidSearchPattern();
		getContainer().setPerformActionEnabled(isValid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#dispose()
	 */
	@Override
	public void dispose() {
		writeConfiguration();
		super.dispose();
	}

	/*
	 * Implements method from IDialogPage
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible && fPattern != null) {
			if (fFirstTime) {
				fFirstTime = false;
				// Set item and text here to prevent page from resizing
				fPattern.setItems(getPreviousSearchPatterns());
				initSelections();
			}
			fPattern.setFocus();
		}
		updateOKStatus();
		super.setVisible(visible);
	}

	private SearchPatternData getDefaultInitValues() {
		if (!fPreviousSearchPatterns.isEmpty())
			return fPreviousSearchPatterns.get(0);

		return new SearchPatternData(
				ITigerstripeSearchConstants.TYPE,
				"", ITigerstripeSearchConstants.ALL_OCCURRENCES, fIsCaseSensitive, getContainer()); //$NON-NLS-1$
	}

	private void initSelections() {
		ISelection sel = getContainer().getSelection();
		SearchPatternData initData = null;

		// if (sel instanceof IStructuredSelection) {
		// initData = tryStructuredSelection((IStructuredSelection) sel);
		// } else if (sel instanceof ITextSelection) {
		// initData = trySimpleTextSelection((ITextSelection) sel);
		// }
		//		
		if (initData == null) {
			initData = getDefaultInitValues();
		}

		fInitialData = initData;
		fCaseSensitive.setSelection(initData.isCaseSensitive());

		setSearchFor(initData.getSearchFor());
		setLimitTo(initData.getLimitTo());

		fPattern.setText(initData.getSearchPattern());
	}

	private String[] getPreviousSearchPatterns() {
		// Search results are not persistent
		int patternCount = fPreviousSearchPatterns.size();
		String[] patterns = new String[patternCount];
		for (int i = 0; i < patternCount; i++)
			patterns[i] = (fPreviousSearchPatterns.get(i)).getSearchPattern();
		return patterns;
	}

	private int getSearchFor() {
		for (int i = 0; i < fSearchFor.length; i++) {
			Button button = fSearchFor[i];
			if (button.getSelection())
				return getIntData(button);
		}
		Assert.isTrue(false, "shouldNeverHappen"); //$NON-NLS-1$
		return -1;
	}

	private int getLimitTo() {
		for (int i = 0; i < fLimitTo.length; i++) {
			Button button = fLimitTo[i];
			if (button.getSelection())
				return getIntData(button);
		}
		Assert.isTrue(false, "shouldNeverHappen"); //$NON-NLS-1$
		return -1;
	}

	private boolean isValidSearchPattern() {
		if (getPattern().length() == 0)
			return false;
		return true;
	}

	private void handlePatternSelected() {
		int selectionIndex = fPattern.getSelectionIndex();
		if (selectionIndex < 0
				|| selectionIndex >= fPreviousSearchPatterns.size())
			return;

		SearchPatternData initialData = fPreviousSearchPatterns
				.get(selectionIndex);

		setSearchFor(initialData.getSearchFor());

		fPattern.setText(initialData.getSearchPattern());
		fIsCaseSensitive = initialData.isCaseSensitive();
		fCaseSensitive.setSelection(initialData.isCaseSensitive());

		fInitialData = initialData;
	}

	// --------------- Configuration handling --------------

	/**
	 * Returns the page settings for this Java search page.
	 * 
	 * @return the page settings to be used
	 */
	private IDialogSettings getDialogSettings() {
		if (fDialogSettings == null) {
			fDialogSettings = EclipsePlugin.getDefault()
					.getDialogSettingsSection(PAGE_NAME);
		}
		return fDialogSettings;
	}

	private Control createLimitTo(Composite parent) {
		Group result = new Group(parent, SWT.NONE);
		result.setText("Limit to");
		result.setLayout(new GridLayout(2, true));

		fLimitTo = new Button[] {
				createButton(result, SWT.RADIO, "Declarations",
						ITigerstripeSearchConstants.DECLARATIONS, false),
				createButton(result, SWT.RADIO, "References",
						ITigerstripeSearchConstants.REFERENCES, true),
				createButton(result, SWT.RADIO, "All Occurences",
						ITigerstripeSearchConstants.ALL_OCCURRENCES, false), };

		return result;
	}

	/**
	 * Initializes itself from the stored page settings.
	 */
	private void readConfiguration() {
		IDialogSettings s = getDialogSettings();
		fIsCaseSensitive = s.getBoolean(STORE_CASE_SENSITIVE);

		try {
			int historySize = s.getInt(STORE_HISTORY_SIZE);
			for (int i = 0; i < historySize; i++) {
				IDialogSettings histSettings = s.getSection(STORE_HISTORY + i);
				if (histSettings != null) {
					SearchPatternData data = SearchPatternData
							.create(histSettings);
					if (data != null) {
						fPreviousSearchPatterns.add(data);
					}
				}
			}
		} catch (NumberFormatException e) {
			// ignore
		}
	}

	/**
	 * Stores the current configuration in the dialog store.
	 */
	private void writeConfiguration() {
		IDialogSettings s = getDialogSettings();
		s.put(STORE_CASE_SENSITIVE, fIsCaseSensitive);
		int historySize = Math
				.min(fPreviousSearchPatterns.size(), HISTORY_SIZE);
		s.put(STORE_HISTORY_SIZE, historySize);
		for (int i = 0; i < historySize; i++) {
			IDialogSettings histSettings = s.addNewSection(STORE_HISTORY + i);
			SearchPatternData data = (fPreviousSearchPatterns.get(i));
			data.store(histSettings);
		}
	}
}
