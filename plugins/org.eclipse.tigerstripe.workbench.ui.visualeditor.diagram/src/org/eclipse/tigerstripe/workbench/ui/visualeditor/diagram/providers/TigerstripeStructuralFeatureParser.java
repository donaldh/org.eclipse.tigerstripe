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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers;

import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.Collections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserEditStatus;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditorPlugin;

/**
 * @generated
 */
public class TigerstripeStructuralFeatureParser extends
		TigerstripeAbstractParser {

	/**
	 * @generated
	 */
	private static final MessageFormat DEFAULT_PROCESSOR = new MessageFormat(
			"{0}"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	private EStructuralFeature feature;

	/**
	 * @generated
	 */
	public TigerstripeStructuralFeatureParser(EStructuralFeature feature) {
		this.feature = feature;
	}

	/**
	 * @generated
	 */
	@Override
	protected MessageFormat getViewProcessor() {
		MessageFormat processor = super.getViewProcessor();
		return processor == null ? DEFAULT_PROCESSOR : processor;
	}

	/**
	 * @generated
	 */
	@Override
	protected MessageFormat getEditProcessor() {
		MessageFormat processor = super.getEditProcessor();
		return processor == null ? DEFAULT_PROCESSOR : processor;
	}

	/**
	 * @generated
	 */
	@Override
	protected String getStringByPattern(IAdaptable adapter, int flags,
			String pattern, MessageFormat processor) {
		EObject element = (EObject) adapter.getAdapter(EObject.class);
		Object value = element.eGet(feature);
		value = getValidValue(feature, value);
		return processor.format(new Object[] { value }, new StringBuffer(),
				new FieldPosition(0)).toString();
	}

	/**
	 * @generated
	 */
	protected IParserEditStatus validateValues(Object[] values) {
		if (values.length > 1)
			return ParserEditStatus.UNEDITABLE_STATUS;
		Object value = values.length == 1 ? values[0] : null;
		value = getValidNewValue(feature, value);
		if (value instanceof InvalidValue)
			return new ParserEditStatus(TigerstripeDiagramEditorPlugin.ID,
					IParserEditStatus.UNEDITABLE, value.toString());
		return ParserEditStatus.EDITABLE_STATUS;
	}

	/**
	 * @generated
	 */
	@Override
	public ICommand getParseCommand(IAdaptable adapter, Object[] values) {
		EObject element = (EObject) adapter.getAdapter(EObject.class);
		if (element == null)
			return UnexecutableCommand.INSTANCE;
		TransactionalEditingDomain editingDomain = TransactionUtil
				.getEditingDomain(element);
		if (editingDomain == null)
			return UnexecutableCommand.INSTANCE;
		Object value = values.length == 1 ? values[0] : null;
		ICommand command = getModificationCommand(element, feature, value);
		return new CompositeTransactionalCommand(editingDomain, command
				.getLabel(), Collections.singletonList(command));
	}

	/**
	 * @generated
	 */
	public boolean isAffectingEvent(Object event, int flags) {
		if (event instanceof Notification) {
			if (feature == ((Notification) event).getFeature())
				return true;
		}
		return false;
	}
}
