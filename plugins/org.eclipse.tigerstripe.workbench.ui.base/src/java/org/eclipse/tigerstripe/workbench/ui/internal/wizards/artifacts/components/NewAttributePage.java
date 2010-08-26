/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.components;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.internal.core.model.ComponentNameProvider;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactAttributesSection;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ModelComponentSectionPart;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.StereotypeSectionManager;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.AttributeInfoEditComponent;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.IModifyCallback;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class NewAttributePage extends NewModelComponentPage {

	private IField newField;
	private AttributeInfoEditComponent attributeInfoEditComponent;
	private StereotypeSectionManager stereotypeMgr;

	public NewAttributePage(IAbstractArtifact initArtifact) {
		super("NewAttribute", initArtifact);
		setTitle("Creating new attribute for artifact");
		setDescription("Specify attribute parameters");

		if (getArtifact() != null) {
			newField = createField(getArtifact());
		}
	}

	private IField createField(IAbstractArtifact artifact) {
		IField newField = artifact.makeField();
		String newFieldName = ComponentNameProvider.getInstance()
				.getNewFieldName(artifact);
		newField.setName(newFieldName);
		IType defaultType = newField.makeType();
		defaultType.setFullyQualifiedName("String");
		defaultType.setTypeMultiplicity(IModelComponent.EMultiplicity.ONE);
		newField.setType(defaultType);
		newField.setRefBy(IField.REFBY_VALUE);
		newField.setVisibility(EVisibility.PUBLIC);
		return newField;
	}

	@Override
	protected IModelComponent getModelComponent() {
		return newField;
	}

	@Override
	protected void fill(FormToolkit toolkit, Composite parent) {

		AttributeInfoEditComponent.Handler handler = new AttributeInfoEditComponent.Handler() {

			public void stateModified() {
				revalidate();
			}

			public void refresh() {
			}

			public void navigateToKeyPressed(KeyEvent e) {
			}

			public void afterUpdate() {
			}
		};

		attributeInfoEditComponent = new AttributeInfoEditComponent(false,
				toolkit, getShell(), handler);
		attributeInfoEditComponent.createContents(parent);
		attributeInfoEditComponent.setIField(newField);
		attributeInfoEditComponent.update();
		attributeInfoEditComponent.getNameText().setFocus();
		attributeInfoEditComponent.getNameText().selectAll();
		
		stereotypeMgr = new StereotypeSectionManager(attributeInfoEditComponent
				.getAddAnno(), attributeInfoEditComponent.getEditAnno(),
				attributeInfoEditComponent.getRemoveAnno(),
				attributeInfoEditComponent.getAnnTable(),
				(IStereotypeCapable) newField, getShell(), IModifyCallback.EMPTY);
		stereotypeMgr.delegate();
	}

	@Override
	public void onArtifactChange() {
		if (getArtifact() != null) {
			newField = createField(getArtifact());
		} else {
			newField = null;
		}
		if (attributeInfoEditComponent != null) {
			attributeInfoEditComponent.setIField(newField);
			stereotypeMgr.setArtifactComponent((IStereotypeCapable) newField);
			attributeInfoEditComponent.update();
		}
	}

	@Override
	public void beforeSave() {
		getArtifact().addField(newField);
	}

	@Override
	public Class<? extends ModelComponentSectionPart> getSectionPartClass() {
		return ArtifactAttributesSection.class;
	}
}
