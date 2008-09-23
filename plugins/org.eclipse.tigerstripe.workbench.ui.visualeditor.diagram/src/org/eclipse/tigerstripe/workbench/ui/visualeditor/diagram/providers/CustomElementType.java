/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers;

import java.net.URL;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IRelationPattern;

public class CustomElementType implements IHintedType{

	private IHintedType baseType = null;
	

	private IRelationPattern pattern;
	
	public CustomElementType(IHintedType baseType, IRelationPattern pattern){
		this.baseType = baseType;
		this.pattern = pattern;
	}
	
	public IHintedType getBaseType() {
		return baseType;
	}

	public void setBaseType(IHintedType baseType) {
		this.baseType = baseType;
	}
	
	public boolean canEdit(IEditCommandRequest arg0) {
		return baseType.canEdit(arg0);
	}

	public IElementType[] getAllSuperTypes() {
		return baseType.getAllSuperTypes();
	}

	public String getDisplayName() {
		return pattern.getUILabel();
	}

	public EClass getEClass() {
		return baseType.getEClass();
	}

	public ICommand getEditCommand(IEditCommandRequest arg0) {
		return baseType.getEditCommand(arg0);
	}

	public IEditHelper getEditHelper() {
		return baseType.getEditHelper();
	}

	public URL getIconURL() {
		return pattern.getIconURL();
	}

	public String getId() {
		return baseType.getId();
	}

	public Object getAdapter(Class adapter) {
		return baseType.getAdapter(adapter);
	}

	public String getSemanticHint() {
		return baseType.getSemanticHint();
	}

	public String getTargetArtifactType(){
		return pattern.getTargetArtifactType();
	}
	
	public IRelationPattern getPattern(){
		return this.pattern;
	}
	
}
