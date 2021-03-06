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

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserEditStatus;
import org.eclipse.gmf.runtime.emf.type.core.commands.SetValueCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.util.AnnotationUtils;
import org.eclipse.tigerstripe.annotation.ui.util.DisplayAnnotationUtil;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotype;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.NamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditorPlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.DiagramPropertiesHelper;

/**
 * @generated
 */
public abstract class TigerstripeAbstractParser implements IParser {

	protected Map currentMap;

	/**
	 * @generated
	 */
	private String viewPattern;

	/**
	 * @generated
	 */
	private MessageFormat viewProcessor;

	/**
	 * @generated
	 */
	private String editPattern;

	/**
	 * @generated
	 */
	private MessageFormat editProcessor;

	/**
	 * @generated
	 */
	public String getViewPattern() {
		return viewPattern;
	}

	/**
	 * @generated
	 */
	protected MessageFormat getViewProcessor() {
		return viewProcessor;
	}

	/**
	 * @generated
	 */
	public void setViewPattern(String viewPattern) {
		this.viewPattern = viewPattern;
		viewProcessor = createViewProcessor(viewPattern);
	}

	/**
	 * @generated
	 */
	protected MessageFormat createViewProcessor(String viewPattern) {
		return new MessageFormat(viewPattern);
	}

	/**
	 * @generated
	 */
	public String getEditPattern() {
		return editPattern;
	}

	/**
	 * @generated
	 */
	protected MessageFormat getEditProcessor() {
		return editProcessor;
	}

	/**
	 * @generated
	 */
	public void setEditPattern(String editPattern) {
		this.editPattern = editPattern;
		editProcessor = createEditProcessor(editPattern);
	}

	/**
	 * @generated
	 */
	protected MessageFormat createEditProcessor(String editPattern) {
		return new MessageFormat(editPattern);
	}

	/**
	 * @generated
	 */
	public String getPrintString(IAdaptable adapter, int flags) {
		return getStringByPattern(adapter, flags, getViewPattern(),
				getViewProcessor());
	}

	/**
	 * @generated
	 */
	public String getEditString(IAdaptable adapter, int flags) {
		return getStringByPattern(adapter, flags, getEditPattern(),
				getEditProcessor());
	}

	/**
	 * @generated
	 */
	protected abstract String getStringByPattern(IAdaptable adapter, int flags,
			String pattern, MessageFormat processor);

	/**
	 * @generated
	 */
	public IParserEditStatus isValidEditString(IAdaptable element,
			String editString) {
		ParsePosition pos = new ParsePosition(0);
		Object[] values = getEditProcessor().parse(editString, pos);
		if (values == null) {
			return new ParserEditStatus(TigerstripeDiagramEditorPlugin.ID,
					IParserEditStatus.UNEDITABLE, "Invalid input at "
							+ pos.getErrorIndex());
		}
		return validateNewValues(values);
	}

	/**
	 * @generated
	 */
	protected IParserEditStatus validateNewValues(Object[] values) {
		return ParserEditStatus.EDITABLE_STATUS;
	}

	/**
	 * @generated NOT
	 */
	public ICommand getParseCommand(IAdaptable adapter, String newString,
			int flags) {
		MessageFormat messageFormat = getEditProcessor();
		Object[] values = getEditProcessor().parse(newString,
				new ParsePosition(0));
		if (values == null
				|| validateNewValues(values).getCode() != IParserEditStatus.EDITABLE) {
			return UnexecutableCommand.INSTANCE;
		}
		return getParseCommand(adapter, values);
	}

	/**
	 * @generated
	 */
	protected abstract ICommand getParseCommand(IAdaptable adapter,
			Object[] values);

	/**
	 * @generated
	 */
	public IContentAssistProcessor getCompletionProcessor(IAdaptable element) {
		return null;
	}

	/**
	 * @generated
	 */
	protected ICommand getModificationCommand(EObject element,
			EStructuralFeature feature, Object value) {
		value = getValidNewValue(feature, value);
		if (value instanceof InvalidValue) {
			return UnexecutableCommand.INSTANCE;
		}
		SetRequest request = new SetRequest(element, feature, value);
		return new SetValueCommand(request);
	}

	protected boolean isEdit() {
		Exception e = new Exception();
		StackTraceElement[] stackTrace = e.getStackTrace();
		for (StackTraceElement stackTraceElement : stackTrace) {
			String className = stackTraceElement.getClassName();
			int lastDotPos = className.lastIndexOf(".");
			if (lastDotPos >= 0
					&& lastDotPos < className.length() - 2
					&& className.substring(lastDotPos + 1).startsWith(
							"TigerstripeAbstractParser")
					&& stackTraceElement.getMethodName()
							.equals("getEditString")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @generated NOT
	 */
	protected Object getValidValue(EStructuralFeature feature, Object value) {
		EClassifier type = feature.getEType();
		if (type instanceof EDataType) {
			Class iClass = type.getInstanceClass();
			if (String.class.equals(iClass)) {
				if (value == null) {
					value = ""; //$NON-NLS-1$
				} else if (!isEdit() && hidePackage()
						&& value instanceof String // when in an
						// edit request
						// don't include
						// the FQN
						&& !value.equals(Util.nameOf((String) value))) {
					value = Util.nameOf((String) value);
				}
			}
		}
		return value;
	}

	/**
	 * @generated
	 */
	protected Object getValidNewValue(EStructuralFeature feature, Object value) {
		EClassifier type = feature.getEType();
		if (type instanceof EDataType) {
			Class iClass = type.getInstanceClass();
			if (Boolean.TYPE.equals(iClass)) {
				if (value instanceof Boolean) {
					// ok
				} else if (value instanceof String) {
					value = Boolean.valueOf((String) value);
				} else {
					value = new InvalidValue(
							"Value of type Boolean is expected");
				}
			} else if (Character.TYPE.equals(iClass)) {
				if (value instanceof Character) {
					// ok
				} else if (value instanceof String) {
					String s = (String) value;
					if (s.length() == 0) {
						value = null;
					} else {
						value = new Character(s.charAt(0));
					}
				} else {
					value = new InvalidValue(
							"Value of type Character is expected");
				}
			} else if (Byte.TYPE.equals(iClass)) {
				if (value instanceof Byte) {
					// ok
				} else if (value instanceof Number) {
					value = new Byte(((Number) value).byteValue());
				} else if (value instanceof String) {
					String s = (String) value;
					if (s.length() == 0) {
						value = null;
					} else {
						try {
							value = Byte.valueOf(s);
						} catch (NumberFormatException nfe) {
							value = new InvalidValue(
									"String value does not convert to Byte value");
						}
					}
				} else {
					value = new InvalidValue("Value of type Byte is expected");
				}
			} else if (Short.TYPE.equals(iClass)) {
				if (value instanceof Short) {
					// ok
				} else if (value instanceof Number) {
					value = new Short(((Number) value).shortValue());
				} else if (value instanceof String) {
					String s = (String) value;
					if (s.length() == 0) {
						value = null;
					} else {
						try {
							value = Short.valueOf(s);
						} catch (NumberFormatException nfe) {
							value = new InvalidValue(
									"String value does not convert to Short value");
						}
					}
				} else {
					value = new InvalidValue("Value of type Short is expected");
				}
			} else if (Integer.TYPE.equals(iClass)) {
				if (value instanceof Integer) {
					// ok
				} else if (value instanceof Number) {
					value = new Integer(((Number) value).intValue());
				} else if (value instanceof String) {
					String s = (String) value;
					if (s.length() == 0) {
						value = null;
					} else {
						try {
							value = Integer.valueOf(s);
						} catch (NumberFormatException nfe) {
							value = new InvalidValue(
									"String value does not convert to Integer value");
						}
					}
				} else {
					value = new InvalidValue(
							"Value of type Integer is expected");
				}
			} else if (Long.TYPE.equals(iClass)) {
				if (value instanceof Long) {
					// ok
				} else if (value instanceof Number) {
					value = new Long(((Number) value).longValue());
				} else if (value instanceof String) {
					String s = (String) value;
					if (s.length() == 0) {
						value = null;
					} else {
						try {
							value = Long.valueOf(s);
						} catch (NumberFormatException nfe) {
							value = new InvalidValue(
									"String value does not convert to Long value");
						}
					}
				} else {
					value = new InvalidValue("Value of type Long is expected");
				}
			} else if (Float.TYPE.equals(iClass)) {
				if (value instanceof Float) {
					// ok
				} else if (value instanceof Number) {
					value = new Float(((Number) value).floatValue());
				} else if (value instanceof String) {
					String s = (String) value;
					if (s.length() == 0) {
						value = null;
					} else {
						try {
							value = Float.valueOf(s);
						} catch (NumberFormatException nfe) {
							value = new InvalidValue(
									"String value does not convert to Float value");
						}
					}
				} else {
					value = new InvalidValue("Value of type Float is expected");
				}
			} else if (Double.TYPE.equals(iClass)) {
				if (value instanceof Double) {
					// ok
				} else if (value instanceof Number) {
					value = new Double(((Number) value).doubleValue());
				} else if (value instanceof String) {
					String s = (String) value;
					if (s.length() == 0) {
						value = null;
					} else {
						try {
							value = Double.valueOf(s);
						} catch (NumberFormatException nfe) {
							value = new InvalidValue(
									"String value does not convert to Double value");
						}
					}
				} else {
					value = new InvalidValue("Value of type Double is expected");
				}
			} else if (type instanceof EEnum) {
				if (value instanceof String) {
					EEnumLiteral literal = ((EEnum) type)
							.getEEnumLiteralByLiteral((String) value);
					if (literal == null) {
						value = new InvalidValue("Unknown literal: " + value);
					} else {
						value = literal.getInstance();
					}
				} else {
					value = new InvalidValue("Value of type String is expected");
				}
			}
		}
		return value;
	}

	/**
	 * @generated
	 */
	protected class InvalidValue {

		private final String description;

		public InvalidValue(String description) {
			this.description = description;
		}

		@Override
		public String toString() {
			return description;
		}
	}

	protected boolean hidePackage() {
		return getBooleanPropertyValue(
				DiagramPropertiesHelper.HIDEPACKAGESINCOMPARTMENTS, true);
	}

	protected boolean hideStereotypes() {
		return getBooleanPropertyValue(DiagramPropertiesHelper.HIDESTEREOTYPES,
				true);
	}

	protected boolean hideDefaultValues() {
		return getBooleanPropertyValue(
				DiagramPropertiesHelper.HIDEDEFAULTVALUES, false);
	}

	private boolean getBooleanPropertyValue(String property,
			boolean defaultValue) {
		if (currentMap != null) {
			DiagramPropertiesHelper helper = new DiagramPropertiesHelper(
					currentMap);
			return Boolean.parseBoolean(helper.getPropertyValue(property));
		}
		return defaultValue;
	}

	protected void setCurrentMap(IAdaptable adapter) {
		EObject con = null;
		EObject obj = (EObject) adapter.getAdapter(EObject.class);
		if (obj != null) {
			con = obj.eContainer();
			while (!(con instanceof Map) && con != null) {
				con = con.eContainer();
			}
			currentMap = (Map) con;
		}
	}

	protected String getAnnotationsAsString(NamedElement namedElement) {
		return getAnnotationsAsString(true, namedElement);
	}

	protected String getAnnotationsAsString(boolean format,
			NamedElement namedElement) {
		StringBuilder builder = new StringBuilder("");
		if (namedElement.getStereotypes().size() > 0) {
			IWorkbenchProfile profile = TigerstripeCore
					.getWorkbenchProfileSession().getActiveProfile();
			for (Object obj : namedElement.getStereotypes()) {
				String value = (String) obj;
				IStereotype stereotype = profile.getStereotypeByName(value);
				if (stereotype != null) {
					appendElement(format, value, builder);
				}
			}
		}

		Set<Annotation> annotations = new LinkedHashSet<Annotation>();
		AnnotationUtils.collectAllAnnotations(namedElement, annotations);
		for (Annotation annotation : annotations) {
			appendElement(format, DisplayAnnotationUtil.getText(annotation),
					builder);
		}

		appendTail(format, builder);

		return builder.toString();
	}

	private void appendElement(boolean format, String element,
			StringBuilder builder) {
		if (builder.length() > 0) {
			builder.append(", ");
		} else if (format) {
			builder.append("<<");
		}
		builder.append(element);
	}

	private void appendTail(boolean format, StringBuilder builder) {
		if (format && builder.length() > 0) {
			builder.append(">>");
		}
	}
}