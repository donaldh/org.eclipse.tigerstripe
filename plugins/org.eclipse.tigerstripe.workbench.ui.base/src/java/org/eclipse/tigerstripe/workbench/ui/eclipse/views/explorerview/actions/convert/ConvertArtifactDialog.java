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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.actions.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IDatatypeArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEnumArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IEventArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IManagedEntityArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.ossj.ISessionArtifact.INamedQuery;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.core.model.EnumArtifact;
import org.eclipse.tigerstripe.core.model.EventArtifact;
import org.eclipse.tigerstripe.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.core.model.QueryArtifact;
import org.eclipse.tigerstripe.core.model.UpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.eclipse.elements.TSMessageDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerUtils;

public class ConvertArtifactDialog extends TSMessageDialog {

	private IStructuredSelection ssel;
	private IAbstractArtifact artifact;

	private final static String ENTITY = "Entity";
	private final static String DATATYPE = "Datatype";
	private final static String ENUMERATION = "Enumeration";
	private final static String NAMEDQUERY = "Named Query";
	private final static String UPDATEPROC = "Update Procedure";
	private final static String NOTIF = "Notification";

	private String[] targetsTypes = { ENTITY, DATATYPE, ENUMERATION,
			NAMEDQUERY, UPDATEPROC, NOTIF };

	private Text artifactFQN;
	private Combo combo;

	public ConvertArtifactDialog(Shell parent, IStructuredSelection ssel) {
		super(parent);
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.ssel = ssel;
	}

	@Override
	public Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new FillLayout());
		GridData gdl = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		area.setLayoutData(gdl);

		initializeDialogUnits(parent);
		Composite composite = new Composite(area, SWT.NONE);
		int nColumns = 2;
		GridLayout layout = new GridLayout();

		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createMessageArea(composite, nColumns);
		createConvertDetails(composite, nColumns);
		initDialog();

		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);
		return area;
	}

	protected void createConvertDetails(Composite composite, int nColumns) {
		Label l = new Label(composite, SWT.NULL);
		l.setText("Artifact to convert:");
		artifactFQN = new Text(composite, SWT.BORDER);
		artifactFQN.setEditable(false);
		artifactFQN
				.setToolTipText("This is the current type of this artifact.");
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL
				| GridData.FILL_HORIZONTAL);
		artifactFQN.setLayoutData(gd);

		l = new Label(composite, SWT.NULL);
		l.setText("Target Artifact Type:");

		combo = new Combo(composite, SWT.READ_ONLY);
		combo
				.setToolTipText("Select the target artifact type that this instance should be converted to.");
	}

	protected void initDialog() {
		getShell().setText("Convert an Artifact...");

		artifact = TSExplorerUtils.getArtifactFor(ssel.getFirstElement());

		if (artifact != null) {
			artifactFQN.setText(artifact.getFullyQualifiedName());
			setMessage("Select a target Artifact type");
			initCombo();
		} else {
			setMessage("Invalid Artifact");
		}

	}

	private void initCombo() {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(targetsTypes));
		if (artifact instanceof IManagedEntityArtifact) {
			list.remove(ENTITY);
		} else if (artifact instanceof IDatatypeArtifact) {
			list.remove(DATATYPE);
		} else if (artifact instanceof IEnumArtifact) {
			list.remove(ENUMERATION);
		} else if (artifact instanceof INamedQuery) {
			list.remove(NAMEDQUERY);
		} else if (artifact instanceof IUpdateProcedureArtifact) {
			list.remove(UPDATEPROC);
		} else if (artifact instanceof IEventArtifact) {
			list.remove(NOTIF);
		}

		for (Iterator iter = list.iterator(); iter.hasNext();) {
			String label = (String) iter.next();
			combo.add(label);
		}

		combo.select(0);
	}

	@Override
	protected void okPressed() {

		try {
			IArtifactManagerSession session = artifact.getIProject()
					.getArtifactManagerSession();
			int index = combo.getSelectionIndex();
			String label = combo.getItem(index);

			IAbstractArtifact model = null;
			if (ENTITY.equals(label)) {
				model = ManagedEntityArtifact.MODEL;
			} else if (DATATYPE.equals(label)) {
				model = DatatypeArtifact.MODEL;
			} else if (ENUMERATION.equals(label)) {
				model = EnumArtifact.MODEL;
			} else if (NAMEDQUERY.equals(label)) {
				model = QueryArtifact.MODEL;
			} else if (UPDATEPROC.equals(label)) {
				model = UpdateProcedureArtifact.MODEL;
			} else if (NOTIF.equals(label)) {
				model = EventArtifact.MODEL;
			}

			IAbstractArtifact newArtifact = session.makeArtifact(model,
					artifact);

			ICompilationUnit unit = (ICompilationUnit) ssel.getFirstElement();
			unit.getBuffer().setContents(newArtifact.asText());
			unit.save(new NullProgressMonitor(), true);

		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		} catch (JavaModelException e) {
			TigerstripeRuntime
					.logErrorMessage("JavaModelException detected", e);
		}
		super.okPressed();
	}

}
