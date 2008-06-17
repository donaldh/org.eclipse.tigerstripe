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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.TypeMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorPackage;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.utils.ClassDiagramUtils;

public class MethodUpdateCommand extends AbstractArtifactUpdateCommand {

	public MethodUpdateCommand(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		super(eArtifact, iArtifact);
	}

	private void migrateMultiplicities(AbstractArtifact eArtifact) {
		for (Method eMeth : (List<Method>) eArtifact.getMethods()) {
			if (!eMeth.eIsSet(VisualeditorPackage.eINSTANCE
					.getTypedElement_TypeMultiplicity())) {
				if (eMeth.getMultiplicity() == TypeMultiplicity.NONE_LITERAL
						&& eMeth.getTypeMultiplicity() != AssocMultiplicity.ONE_LITERAL) {
					eMeth.setTypeMultiplicity(AssocMultiplicity.ONE_LITERAL);
					eMeth.eUnset(VisualeditorPackage.eINSTANCE
							.getTypedElement_Multiplicity());
				} else if (eMeth.getMultiplicity() == TypeMultiplicity.ARRAY_LITERAL
						&& eMeth.getTypeMultiplicity() != AssocMultiplicity.ZERO_STAR_LITERAL) {
					eMeth
							.setTypeMultiplicity(AssocMultiplicity.ZERO_STAR_LITERAL);
					eMeth.eUnset(VisualeditorPackage.eINSTANCE
							.getTypedElement_Multiplicity());
				}
			}

			for (Parameter param : (List<Parameter>) eMeth.getParameters()) {
				if (!param.eIsSet(VisualeditorPackage.eINSTANCE
						.getTypedElement_TypeMultiplicity())) {
					if (param.getMultiplicity() == TypeMultiplicity.NONE_LITERAL
							&& param.getTypeMultiplicity() != AssocMultiplicity.ONE_LITERAL) {
						param
								.setTypeMultiplicity(AssocMultiplicity.ONE_LITERAL);
						param.eUnset(VisualeditorPackage.eINSTANCE
								.getTypedElement_Multiplicity());
					} else if (param.getMultiplicity() == TypeMultiplicity.ARRAY_LITERAL
							&& param.getTypeMultiplicity() != AssocMultiplicity.ZERO_STAR_LITERAL) {
						param
								.setTypeMultiplicity(AssocMultiplicity.ZERO_STAR_LITERAL);
						param.eUnset(VisualeditorPackage.eINSTANCE
								.getTypedElement_Multiplicity());
					}
				}
			}
		}
	}

	@Override
	public void updateEArtifact(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		migrateMultiplicities(eArtifact);

		if (!ClassDiagramUtils.checkMethodOrder(eArtifact, iArtifact
				.getMethods())) {
			eArtifact.getMethods().clear();
		}

		// go thru methods in the EMF domain
		List<Method> eMethods = eArtifact.getMethods();
		List<Method> toDelete = new ArrayList<Method>();
		for (Method eMethod : eMethods) {
			IMethod targetIMethod = null;
			for (IMethod iMethod : iArtifact.getMethods()) {
				if (iMethod.getLabelString().equals(eMethod.getLabelString())) {
					targetIMethod = iMethod;
					break;
				}
			}

			if (targetIMethod == null) {
				// not found, delete
				toDelete.add(eMethod);
			}

		}

		if (toDelete.size() != 0)
			eMethods.removeAll(toDelete);

		// then make sure all methods in the TS domain are present
		// in the EMF domain
		eMethods = eArtifact.getMethods();
		for (IMethod iMethod : iArtifact.getMethods()) {
			boolean found = false;
			Method theMethod = null;
			for (Method eMethod : eMethods) {
				if (eMethod.getLabelString().equals(iMethod.getLabelString())) {
					found = true;
					theMethod = eMethod;
					// continue;
				}
			}

			if (!found) {
				// need to create a new method in the EMF domain
				IType iMethodType = iMethod.getReturnType();
				if (iMethodType != null
						&& iMethodType.getFullyQualifiedName() != null
						&& iMethodType.getFullyQualifiedName().length() != 0) {
					String typeStr = getFqnForTypeAndIArtifact(iMethodType,
							iArtifact);

					Method meth = VisualeditorFactory.eINSTANCE.createMethod();
					eArtifact.getMethods().add(meth);
					meth.setName(iMethod.getName());
					meth.setVisibility(ClassDiagramUtils.toVisibility(iMethod
							.getVisibility()));
					meth.setIsAbstract(iMethod.isAbstract());
					meth.setDefaultValue(iMethod.getDefaultReturnValue());
					meth.setIsOrdered(iMethod.isOrdered());
					meth.setIsUnique(iMethod.isUnique());

					meth.setMethod(iMethod);

					if (iMethod.isVoid()) {
						meth.setType("void");
					} else {
						meth.setType(Misc.removeJavaLangString(typeStr));
					}
					meth.setTypeMultiplicity(ClassDiagramUtils
							.mapTypeMultiplicity(iMethodType
									.getTypeMultiplicity()));

					for (IArgument arg : iMethod.getArguments()) {
						Parameter param = VisualeditorFactory.eINSTANCE
								.createParameter();
						param.setName(arg.getName());
						String lclTypeStr = getFqnForTypeAndIArtifact(arg
								.getType(), iArtifact);
						param.setType(Misc.removeJavaLangString(lclTypeStr));
						param.setIsOrdered(arg.isOrdered());
						param.setIsUnique(arg.isUnique());
						param.setDefaultValue(arg.getDefaultValue());
						param.setTypeMultiplicity(ClassDiagramUtils
								.mapTypeMultiplicity(arg.getType()
										.getTypeMultiplicity()));
						meth.getParameters().add(param);
						for (IStereotypeInstance st : arg
								.getStereotypeInstances()) {
							param.getStereotypes().add(st.getName());
						}
					}

					for (IStereotypeInstance instance : iMethod
							.getStereotypeInstances()) {
						meth.getStereotypes().add(instance.getName());
						meth.setName(iMethod.getName());// Bug 219454: this is a
						// hack to
						// force the diagram to go dirty as the stereotype add
						// doesn't??????
					}

				}

			} else {
				// The method with that signature exists but we need to check
				// that all attributes are correct

				if (theMethod.getMethod() == null
						|| !theMethod.getMethod().equals(iMethod)) {
					theMethod.setMethod(iMethod);
				}
				if (theMethod.getVisibility() != ClassDiagramUtils
						.toVisibility(iMethod.getVisibility()))
					theMethod.setVisibility(ClassDiagramUtils
							.toVisibility(iMethod.getVisibility()));

				if (theMethod.isIsAbstract() != iMethod.isAbstract())
					theMethod.setIsAbstract(iMethod.isAbstract());

				if (theMethod.getDefaultValue() != iMethod
						.getDefaultReturnValue())
					theMethod.setDefaultValue(iMethod.getDefaultReturnValue());

				if (theMethod.isIsOrdered() != iMethod.isOrdered())
					theMethod.setIsOrdered(iMethod.isOrdered());

				if (theMethod.isIsUnique() != iMethod.isUnique())
					theMethod.setIsUnique(iMethod.isUnique());

				IType iMethodType = iMethod.getReturnType();
				if (theMethod.getTypeMultiplicity() != ClassDiagramUtils
						.mapTypeMultiplicity(iMethodType.getTypeMultiplicity()))
					theMethod.setTypeMultiplicity(ClassDiagramUtils
							.mapTypeMultiplicity(iMethodType
									.getTypeMultiplicity()));

				// Need to check that all params have the same Stereotypes
				List<Parameter> theParams = new ArrayList<Parameter>();
				Iterator<Parameter> paramIter = theMethod.getParameters()
						.iterator();
				boolean needLabelRefresh = false;
				for (Iterator<IArgument> iterArg = iMethod.getArguments()
						.iterator(); iterArg.hasNext();) {
					boolean needUpdate = false;
					Parameter param = paramIter.next();
					IArgument arg = iterArg.next();
					if (param.getStereotypes().size() == arg
							.getStereotypeInstances().size()) {
						// check them one by one
						int index = 0;
						for (IStereotypeInstance inst : arg
								.getStereotypeInstances()) {
							if (!param.getStereotypes().get(index).equals(
									inst.getName())) {
								needUpdate = true;
								break;
							}
							index++;
						}
					} else {
						needUpdate = true;
					}
					
					if (needUpdate) {
						// refresh
						param.getStereotypes().clear();
						for (IStereotypeInstance inst : arg
								.getStereotypeInstances()) {
							param.getStereotypes().add(inst.getName());
						}
						needLabelRefresh = true;
					}
					
					theParams.add(param);
				}

				if (needLabelRefresh) {
					theMethod.getParameters().clear();
					theMethod.getParameters().addAll(theParams);
					theMethod.setName(iMethod.getName());// Bug 219454: this
					// is a hack to
					// force the diagram to go dirty as the stereotype add
					// doesn't??????
				}

				if (theMethod.getStereotypes().size() != iMethod
						.getStereotypeInstances().size()) {
					// not even the same number of stereotypes, let's redo the
					// list
					theMethod.getStereotypes().clear();
					theMethod.setName(iMethod.getName());// Bug 219454: this
					// is a hack to
					// force the diagram to go dirty as the stereotype add
					// doesn't??????

					for (IStereotypeInstance stereo : iMethod
							.getStereotypeInstances()) {
						theMethod.getStereotypes().add(stereo.getName());
					}
				} else {
					// same number of stereotypes let's see if they all match
					List<String> eStereotypes = theMethod.getStereotypes();
					Iterator<String> eStereo = eStereotypes.iterator();
					Collection<IStereotypeInstance> iStereotypes = iMethod
							.getStereotypeInstances();
					boolean updateNeeded = false;
					for (IStereotypeInstance iStereo : iStereotypes) {
						String eStereotypeName = eStereo.next();
						String iStereotypeName = iStereo.getName();

						if (!eStereotypeName.equals(iStereotypeName)) {
							updateNeeded = true;
							break;
						}

					}
					if (updateNeeded) {
						// Bug 215646 - Just redo the whole list as the order is
						// relevant -
						// You can confuse the diagram
						theMethod.getStereotypes().clear();
						for (IStereotypeInstance stereo : iMethod
								.getStereotypeInstances()) {
							theMethod.getStereotypes().add(stereo.getName());
						}

						theMethod.setName(iMethod.getName());// Bug 219454:
						// this is a
						// hack to
						// force the diagram to go dirty as the stereotype add
						// doesn't??????

					}

				}
			}
		}

	}

	private String getFqnForTypeAndIArtifact(IType type,
			IAbstractArtifact iArtifact) {
		String typeStr = type.getFullyQualifiedName();
		if (!type.isPrimitive() && !typeStr.equals("String")) {
			String packageStr = getPackagePart(typeStr);
			if (packageStr.length() == 0) {
				packageStr = iArtifact.getPackage();
				typeStr = packageStr + "." + typeStr;
			}
		}
		return typeStr;
	}

	private String getPackagePart(String fullyQualifiedName) {
		int idx = fullyQualifiedName.lastIndexOf(".");
		if (idx > 0)
			return fullyQualifiedName.substring(0, idx);
		return "";
	}

	public void redo() {
		// TODO Auto-generated method stub
	}

}
