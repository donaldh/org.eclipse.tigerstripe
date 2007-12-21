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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditorPlugin;

/**
 * @generated
 */
public class TigerstripeStructuralFeaturesParser extends
		TigerstripeAbstractParser {

	/**
	 * @generated
	 */
	private List features;

	protected EObject feature;

	protected EObject container;

	/**
	 * @generated
	 */
	public TigerstripeStructuralFeaturesParser(List features) {
		this.features = features;
	}

	/**
	 * @generated
	 */
	@Override
	protected String getStringByPattern(IAdaptable adapter, int flags,
			String pattern, MessageFormat processor) {
		EObject element = (EObject) adapter.getAdapter(EObject.class);
		List values = new ArrayList(features.size());
		for (Iterator it = features.iterator(); it.hasNext();) {
			EStructuralFeature feature = (EStructuralFeature) it.next();
			Object value = element.eGet(feature);
			value = getValidValue(feature, value);
			values.add(value);
		}
		return processor.format(values.toArray(new Object[values.size()]),
				new StringBuffer(), new FieldPosition(0)).toString();
	}

	@Override
	public IParserEditStatus isValidEditString(IAdaptable element,
			String editString) {
		// In order to test the validity of Attributes/Methods names we need to
		// put the
		// AbstractArtifact being edited in context
		feature = (EObject) element.getAdapter(EObject.class);
		container = feature.eContainer();

		return super.isValidEditString(element, editString);
	}

	@Override
	protected IParserEditStatus validateNewValues(Object[] values) {

		if (feature instanceof Attribute) {
			String name = (String) values[0];
			for (Attribute attribute : (List<Attribute>) ((AbstractArtifact) container)
					.getAttributes()) {
				if (!attribute.equals(feature)
						&& name.equals(attribute.getName()))
					return new ParserEditStatus(
							TigerstripeDiagramEditorPlugin.ID,
							IParserEditStatus.UNEDITABLE,
							"Duplicate attribute name ");
			}
		} else if (feature instanceof Method) {

			// Bug 997: same name methods are now permitted as long as the args
			// are different
			String name = (String) values[0];
			for (Method method : (List<Method>) ((AbstractArtifact) container)
					.getMethods()) {
				if (!method.equals(feature) && name.equals(method.getName())) {
					// check that the args are different
					if (method.sameSignature((Method) feature))
						return new ParserEditStatus(
								TigerstripeDiagramEditorPlugin.ID,
								IParserEditStatus.UNEDITABLE,
								"Duplicate method name ");
				}
			}
		} else if (feature instanceof Literal) {
			String name = (String) values[0];
			for (Literal literal : (List<Literal>) ((AbstractArtifact) container)
					.getLiterals()) {
				if (!literal.equals(feature) && name.equals(literal.getName()))
					return new ParserEditStatus(
							TigerstripeDiagramEditorPlugin.ID,
							IParserEditStatus.UNEDITABLE,
							"Duplicate literal name ");
			}
		} else if (feature instanceof Map) {
			// current handled by NameAwareTextEditManager in commit method
		}
		return super.validateNewValues(values);
	}

	/**
	 * @generated
	 */
	protected IParserEditStatus validateValues(Object[] values) {
		if (values.length != features.size())
			return ParserEditStatus.UNEDITABLE_STATUS;
		for (int i = 0; i < values.length; i++) {
			Object value = getValidNewValue((EStructuralFeature) features
					.get(i), values[i]);
			if (value instanceof InvalidValue)
				return new ParserEditStatus(TigerstripeDiagramEditorPlugin.ID,
						IParserEditStatus.UNEDITABLE, value.toString());
		}
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
		CompositeTransactionalCommand command = new CompositeTransactionalCommand(
				editingDomain, "Set Values"); //$NON-NLS-1$
		for (int i = 0; i < values.length; i++) {
			EStructuralFeature feature = (EStructuralFeature) features.get(i);
			command
					.compose(getModificationCommand(element, feature, values[i]));
		}
		return command;
	}

	/**
	 * @generated
	 */
	public boolean isAffectingEvent(Object event, int flags) {
		if (event instanceof Notification) {
			Object feature = ((Notification) event).getFeature();
			if (features.contains(feature))
				return true;
		}
		return false;
	}
}
