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

		// go thru attributes in the EMF domain
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
			// else {
			// // review that everything matches
			// String typeName =
			// getFqnForTypeAndIArtifact(targetIMethod.getReturnIType(),
			// iArtifact);
			// String targetReturnedType = Misc
			// .removeJavaLangString(typeName);
			// if (targetIMethod.isVoid()) {
			// targetReturnedType = "void";
			// }
			//
			// if (eMethod.getType() != null
			// && !eMethod.getType().equals(targetReturnedType)) {
			// eMethod.setType(targetReturnedType);
			// }
			//
			// if ((eMethod.getDefaultValue() == null && targetIMethod
			// .getDefaultReturnValue() != null)
			// || (eMethod.getDefaultValue() != null && !eMethod
			// .getDefaultValue().equals(
			// targetIMethod.getDefaultReturnValue()))) {
			// if (targetIMethod.getDefaultReturnValue() == null) {
			// eMethod.eUnset(VisualeditorPackage.eINSTANCE
			// .getTypedElement_DefaultValue());
			// } else {
			// eMethod.setDefaultValue(targetIMethod
			// .getDefaultReturnValue());
			// }
			// }
			//
			// if (eMethod.isIsAbstract() != targetIMethod.isAbstract()) {
			// eMethod.setIsAbstract(targetIMethod.isAbstract());
			// }
			//
			// if (eMethod.getVisibility() != ClassDiagramUtils
			// .toVisibility(targetIMethod.getVisibility())) {
			// eMethod.setVisibility(ClassDiagramUtils
			// .toVisibility(targetIMethod.getVisibility()));
			// }
			//
			// if (eMethod.isIsOrdered() != targetIMethod.isOrdered()) {
			// eMethod.setIsOrdered(targetIMethod.isOrdered());
			// }
			//
			// if (eMethod.isIsUnique() != targetIMethod.isUnique()) {
			// eMethod.setIsUnique(targetIMethod.isUnique());
			// }
			//
			// // review multiplicity, swap if necessary
			// AssocMultiplicity eMethodMultiplicy = eMethod
			// .getTypeMultiplicity();
			//
			// if (targetIMethod.getReturnIType() != null) {
			// EMultiplicity iMethodMultiplicity = targetIMethod
			// .getReturnIType().getTypeMultiplicity();
			// if (eMethodMultiplicy != ClassDiagramUtils
			// .mapTypeMultiplicity(iMethodMultiplicity)) {
			// eMethod.setTypeMultiplicity(ClassDiagramUtils
			// .mapTypeMultiplicity(iMethodMultiplicity));
			// }
			// }
			//
			// // review arguments
			// if (eMethod.getParameters().size() != targetIMethod
			// .getIArguments().length) {
			// // not even the same number of args, let's redo the list
			// eMethod.getParameters().clear();
			// for (IArgument arg : targetIMethod.getIArguments()) {
			// Parameter param = VisualeditorFactory.eINSTANCE
			// .createParameter();
			// param.setName(arg.getName());
			// String lclTypeName = getFqnForTypeAndIArtifact(arg.getIType(),
			// iArtifact);
			// param.setType(Misc.removeJavaLangString(lclTypeName));
			// param.setIsOrdered(arg.isOrdered());
			// param.setIsUnique(arg.isUnique());
			// param.setDefaultValue(arg.getDefaultValue());
			//
			// if (arg.getIType().getTypeMultiplicity() != ClassDiagramUtils
			// .mapTypeMultiplicity(param
			// .getTypeMultiplicity())) {
			// param.setTypeMultiplicity(ClassDiagramUtils
			// .mapTypeMultiplicity(arg.getIType()
			// .getTypeMultiplicity()));
			//
			// }
			// eMethod.getParameters().add(param);
			// }
			// } else {
			// // same number of args let's see if they all match
			// List<Parameter> parameters = eMethod.getParameters();
			// IArgument[] arguments = targetIMethod.getIArguments();
			// for (int index = 0; index < arguments.length; index++) {
			// boolean changed = false;
			// Parameter theParam = parameters.get(index);
			// IArgument theArg = arguments[index];
			// if (!theArg.getName().equals(theParam.getName())) {
			// theParam.setName(theArg.getName());
			// changed = true;
			// }
			// String lclTypeName = getFqnForTypeAndIArtifact(theArg.getIType(),
			// iArtifact);
			// if (!Misc.removeJavaLangString(lclTypeName)
			// .equals(theParam.getType())) {
			// theParam.setType(Misc.removeJavaLangString(lclTypeName));
			// changed = true;
			// }
			//
			// EMultiplicity iMultiplicity = theArg.getIType()
			// .getTypeMultiplicity();
			// AssocMultiplicity eMultiplicity = theParam
			// .getTypeMultiplicity();
			// if (iMultiplicity != ClassDiagramUtils
			// .mapTypeMultiplicity(eMultiplicity)) {
			// theParam.setTypeMultiplicity(ClassDiagramUtils
			// .mapTypeMultiplicity(iMultiplicity));
			// changed = true;
			// }
			//
			// if (!theArg.isOrdered() == theParam.isIsOrdered()) {
			// theParam.setIsOrdered(theArg.isOrdered());
			// changed = true;
			// }
			//
			// if (!theArg.isUnique() == theParam.isIsUnique()) {
			// theParam.setIsUnique(theArg.isUnique());
			// changed = true;
			// }
			//
			// if ((theParam.getDefaultValue() == null && theArg
			// .getDefaultValue() != null)
			// || (theParam.getDefaultValue() != null && !theParam
			// .getDefaultValue().equals(
			// theArg.getDefaultValue()))) {
			// if (theArg.getDefaultValue() == null) {
			// theParam.eUnset(VisualeditorPackage.eINSTANCE
			// .getTypedElement_DefaultValue());
			// } else {
			// theParam.setDefaultValue(theArg
			// .getDefaultValue());
			// }
			// changed = true;
			// }
			//						
			// // review stereotypes
			// if (theParam.getStereotypes().size() != theArg
			// .getStereotypeInstances().length) {
			// // not even the same number of args, let's redo the list
			// theParam.getStereotypes().clear();
			// for (IextStereotypeInstance stereo : theArg
			// .getStereotypeInstances()) {
			// theParam.getStereotypes().add(stereo.getName());
			// changed = true;
			// }
			// } else {
			// // same number of stereotypes let's see if they all match
			// List<String> eStereotypes = theParam.getStereotypes();
			// IextStereotypeInstance[] iStereotypes = theArg
			// .getStereotypeInstances();
			// for (int aindex = 0; aindex < iStereotypes.length; aindex++) {
			// String eStereotypeName = eStereotypes.get(aindex);
			// String iStereotypeName = iStereotypes[aindex].getName();
			// if (!eStereotypeName.equals(iStereotypeName)) {
			// theParam.getStereotypes().remove(aindex);
			// theParam.getStereotypes()
			// .add(aindex, iStereotypeName);
			// changed = true;
			// }
			// }
			// }
			//
			//
			// if (changed) {
			// // since we can't listen on Parameter sets at the
			// // graph
			// // level, doing remove/add instead
			// parameters.remove(index);
			// parameters.add(index, theParam);
			// // parameters.set(index, theParam);
			// }
			// }
			// }
			// // review stereotypes
			// if (eMethod.getStereotypes().size() != targetIMethod
			// .getStereotypeInstances().length) {
			// // not even the same number of args, let's redo the list
			// eMethod.getStereotypes().clear();
			// for (IextStereotypeInstance stereo : targetIMethod
			// .getStereotypeInstances()) {
			// eMethod.getStereotypes().add(stereo.getName());
			// }
			// } else {
			// // same number of stereotypes let's see if they all match
			// List<String> eStereotypes = eMethod.getStereotypes();
			// IextStereotypeInstance[] iStereotypes = targetIMethod
			// .getStereotypeInstances();
			// for (int index = 0; index < iStereotypes.length; index++) {
			// String eStereotypeName = eStereotypes.get(index);
			// String iStereotypeName = iStereotypes[index].getName();
			// if (!eStereotypeName.equals(iStereotypeName)) {
			// eMethod.getStereotypes().remove(index);
			// eMethod.getStereotypes()
			// .add(index, iStereotypeName);
			// }
			// }
			// }
			// }
		}

		if (toDelete.size() != 0)
			eMethods.removeAll(toDelete);

		// then make sure all methods in the TS domain are present
		// in the EMF domain
		eMethods = eArtifact.getMethods();
		for (IMethod iMethod : iArtifact.getMethods()) {
			boolean found = false;
			for (Method eMethod : eMethods) {
				if (eMethod.getLabelString().equals(iMethod.getLabelString())) {
					found = true;
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
					}

					for (IStereotypeInstance instance : iMethod
							.getStereotypeInstances()) {
						meth.getStereotypes().add(instance.getName());
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
