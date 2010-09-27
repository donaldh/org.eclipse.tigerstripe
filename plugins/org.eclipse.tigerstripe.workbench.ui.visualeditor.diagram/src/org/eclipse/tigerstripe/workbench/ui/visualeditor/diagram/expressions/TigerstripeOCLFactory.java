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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.expressions;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.EvaluationEnvironment;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.ecore.EcoreFactory;
import org.eclipse.ocl.ecore.OCL.Helper;
import org.eclipse.ocl.options.ParsingOptions;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.TigerstripeDiagramEditorPlugin;

/**
 * @generated
 */
public class TigerstripeOCLFactory {
	/**
	 * @generated
	 */
	private final TigerstripeAbstractExpression[] expressions;

	/**
	 * @generated
	 */
	protected TigerstripeOCLFactory() {
		this.expressions = new TigerstripeAbstractExpression[10];
	}

	/**
	 * @generated
	 */
	public static TigerstripeAbstractExpression getExpression(int index,
			EClassifier context,
			Map/*[String, org.eclipse.emf.ecore.EClassifier]*/environment) {
		TigerstripeOCLFactory cached = TigerstripeDiagramEditorPlugin
				.getInstance().getTigerstripeOCLFactory();
		if (cached == null) {
			TigerstripeDiagramEditorPlugin.getInstance()
					.setTigerstripeOCLFactory(
							cached = new TigerstripeOCLFactory());
		}
		if (index < 0 || index >= cached.expressions.length) {
			throw new IllegalArgumentException();
		}
		if (cached.expressions[index] == null) {
			final String[] exprBodies = new String[] { "self <> oppositeEnd", //$NON-NLS-1$
					"self <> oppositeEnd", //$NON-NLS-1$
					"self <> oppositeEnd", //$NON-NLS-1$
					"self <> oppositeEnd", //$NON-NLS-1$
					"self <> oppositeEnd", //$NON-NLS-1$
					"self <> oppositeEnd", //$NON-NLS-1$
					"self <> oppositeEnd", //$NON-NLS-1$
					"self <> oppositeEnd", //$NON-NLS-1$
					"self <> oppositeEnd", //$NON-NLS-1$
					"self <> oppositeEnd", //$NON-NLS-1$
			};
			cached.expressions[index] = getExpression(exprBodies[index],
					context, environment == null ? Collections.EMPTY_MAP
							: environment);
		}
		return cached.expressions[index];
	}

	/**
	 * @generated
	 */
	public static TigerstripeAbstractExpression getExpression(String body,
			EClassifier context,
			Map/*[String, org.eclipse.emf.ecore.EClassifier]*/environment) {
		return new Expression(body, context, environment);
	}

	/**
	 * @generated
	 */
	public static TigerstripeAbstractExpression getExpression(String body,
			EClassifier context) {
		return getExpression(body, context, Collections.EMPTY_MAP);
	}

	/**
	 * @generated
	 */
	private static class Expression extends TigerstripeAbstractExpression {
		/**
		 * @generated
		 */
		private final org.eclipse.ocl.ecore.OCL oclInstance;
		/**
		 * @generated
		 */
		private org.eclipse.ocl.ecore.OCLExpression oclExpression;

		/**
		 * @generated
		 */
		public Expression(String body, EClassifier context,
				Map/*[String, org.eclipse.emf.ecore.EClassifier]*/environment) {
			super(body, context);
			oclInstance = org.eclipse.ocl.ecore.OCL.newInstance();
			initCustomEnv(oclInstance.getEnvironment(), environment);
			Helper oclHelper = oclInstance.createOCLHelper();
			oclHelper.setContext(context());
			try {
				oclExpression = oclHelper.createQuery(body());
				setStatus(IStatus.OK, null, null);
			} catch (ParserException e) {
				setStatus(IStatus.ERROR, e.getMessage(), e);
			}
		}

		/**
		 * @generated
		 */
		@Override
		protected Object doEvaluate(Object context, Map env) {
			if (oclExpression == null) {
				return null;
			}
			// on the first call, both evalEnvironment and extentMap are clear, for later we have finally, below.
			EvaluationEnvironment/*[?,?,?,?,?]*/evalEnv = oclInstance
					.getEvaluationEnvironment();
			// initialize environment
			for (Iterator/*[Object]*/it = env.keySet().iterator(); it
					.hasNext();) {
				Object nextKey = it.next();
				evalEnv.replace((String) nextKey, env.get(nextKey));
			}
			try {
				Object result = oclInstance.evaluate(context, oclExpression);
				return oclInstance.isInvalid(result) ? null : result;
			} finally {
				evalEnv.clear();
				oclInstance.setExtentMap(null); // clear allInstances cache, and get the oclInstance ready for the next call
			}
		}

		/**
		 * @generated
		 */
		private static void initCustomEnv(
				Environment/*[?,org.eclipse.emf.ecore.EClassifier,?,?,?,org.eclipse.emf.ecore.EParameter,?,?,?,?,?,?]*/ecoreEnv,
				Map/*[String, org.eclipse.emf.ecore.EClassifier]*/environment) {
			// Use EObject as implicit root class for any object, to allow eContainer() and other EObject operations from OCL expressions
			ParsingOptions.setOption(ecoreEnv,
					ParsingOptions.implicitRootClass(ecoreEnv),
					EcorePackage.eINSTANCE.getEObject());
			for (Iterator/*[String]*/it = environment.keySet().iterator(); it
					.hasNext();) {
				String varName = (String) it.next();
				EClassifier varType = (EClassifier) environment.get(varName);
				ecoreEnv.addElement(varName,
						createVar(ecoreEnv, varName, varType), false);
			}
		}

		/**
		 * @generated
		 */
		private static org.eclipse.ocl.ecore.Variable createVar(
				Environment/*[?,org.eclipse.emf.ecore.EClassifier,?,?,?,?,?,?,?,?,?,?]*/ecoreEnv,
				String name, EClassifier type) {
			org.eclipse.ocl.ecore.Variable var = EcoreFactory.eINSTANCE
					.createVariable();
			var.setName(name);
			var.setType((EClassifier) ecoreEnv.getUMLReflection().getOCLType(
					type));
			return var;
		}
	}
}
