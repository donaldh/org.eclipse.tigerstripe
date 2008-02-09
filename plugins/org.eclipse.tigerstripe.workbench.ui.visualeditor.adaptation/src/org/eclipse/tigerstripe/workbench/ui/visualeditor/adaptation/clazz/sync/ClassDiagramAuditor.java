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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.tigerstripe.workbench.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.model.IField;
import org.eclipse.tigerstripe.workbench.model.ILiteral;
import org.eclipse.tigerstripe.workbench.model.IMethod;
import org.eclipse.tigerstripe.workbench.model.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EAggregationEnum;
import org.eclipse.tigerstripe.workbench.model.IAssociationEnd.EChangeableEnum;
import org.eclipse.tigerstripe.workbench.model.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.eclipse.builder.IDiagramAuditor;
import org.eclipse.tigerstripe.workbench.ui.gmf.synchronization.DiagramHandle;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AggregationEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssocMultiplicity;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.ChangeableEnum;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Parameter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Visibility;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.Activator;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.utils.ClassDiagramUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;

public class ClassDiagramAuditor implements IDiagramAuditor {

	private MapHelper helper;

	public IStatus auditDiagram(Diagram diagram, IProgressMonitor monitor) {
		monitor.subTask(diagram.getName());

		try {
			Map map = getMap(diagram);
			helper = new MapHelper(map);

			MultiStatus status = new MultiStatus(Activator.PLUGIN_ID, 222,
					diagram.getName(), null);

			IStatus diagStatus = internalAuditDiagram(diagram, monitor);
			IStatus modelStatus = auditModel(status, map, monitor);

			status.add(diagStatus);
			return status;
		} catch (TigerstripeException e) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, 222, e
					.getMessage(), e);
		}
	}

	public IStatus auditDiagram(DiagramHandle handle, IProgressMonitor monitor)
			throws TigerstripeException {
		monitor.subTask(handle.getDiagramResource().getName());

		try {
			Diagram diagram = handle.getDiagram();
			Map map = getMap(diagram);
			helper = new MapHelper(map);

			MultiStatus status = new MultiStatus(Activator.PLUGIN_ID, 222,
					handle.getDiagramResource().getFullPath().toOSString(),
					null);

			IStatus diagStatus = internalAuditDiagram(diagram, monitor);
			IStatus modelStatus = auditModel(status, map, monitor);

			status.add(diagStatus);
			return status;
		} catch (TigerstripeException e) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, 222, e
					.getMessage(), e);
		}
	}

	protected boolean hideExtends(AbstractArtifact eArtifact) {
		NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
				eArtifact);
		return Boolean
				.parseBoolean(helper
						.getProperty(NamedElementPropertiesHelper.ARTIFACT_HIDE_EXTENDS));
	}

	private IStatus internalAuditDiagram(Diagram diagram,
			IProgressMonitor monitor) throws TigerstripeException {

		return Status.OK_STATUS;
	}

	private IStatus auditModel(MultiStatus parentStatus, Map map,
			IProgressMonitor monitor) throws TigerstripeException {

		MultiStatus artResult = new MultiStatus(Activator.PLUGIN_ID, 222,
				"Artifacts", null);
		List<AbstractArtifact> artifacts = map.getArtifacts();
		ITigerstripeModelProject tsProject = map
				.getCorrespondingITigerstripeProject();
		for (AbstractArtifact artifact : artifacts) {
			IStatus status = auditArtifact(artifact, tsProject);
			artResult.add(status);
		}
		parentStatus.add(artResult);

		MultiStatus assocResult = new MultiStatus(Activator.PLUGIN_ID, 222,
				"Associations", null);
		List<Association> associations = map.getAssociations();
		for (Association association : associations) {
			IStatus status = auditAssociation(association, tsProject);
			assocResult.add(status);
		}
		parentStatus.add(assocResult);

		MultiStatus depResult = new MultiStatus(Activator.PLUGIN_ID, 222,
				"Dependencies", null);
		List<Dependency> dependencies = map.getDependencies();
		for (Dependency dependency : dependencies) {
			IStatus status = auditDependency(dependency, tsProject);
			depResult.add(status);
		}
		parentStatus.add(depResult);

		return parentStatus;
	}

	private IStatus auditDependency(Dependency eDependency,
			ITigerstripeModelProject project) throws TigerstripeException {
		IArtifactManagerSession session = project.getArtifactManagerSession();

		if (eDependency.getFullyQualifiedName() == null
				|| eDependency.getFullyQualifiedName().length() == 0) {
			IStatus status = getErrorStatus("Dependency has no Fully Qualified Name");
			return status;
		}
		IAbstractArtifact iArtifact = session
				.getArtifactByFullyQualifiedName(eDependency
						.getFullyQualifiedName());

		if (iArtifact == null) {
			IStatus status = getErrorStatus("Dependency '"
					+ eDependency.getFullyQualifiedName()
					+ "' in diagram doesn't exist in model.");
			return status;
		}

		IDependencyArtifact iDep = (IDependencyArtifact) iArtifact;

		MultiStatus result = new MultiStatus(Activator.PLUGIN_ID, 222,
				eDependency.getFullyQualifiedName(), null);

		// Check both ends
		String iAEnd = null;
		String iZEnd = null;
		if (iDep.getAEndType() != null) {
			iAEnd = iDep.getAEndType().getFullyQualifiedName();
		}
		if (iDep.getZEndType() != null) {
			iZEnd = iDep.getZEndType().getFullyQualifiedName();
		}

		String eAEnd = null;
		String eZEnd = null;
		if (eDependency.getAEnd() != null) {
			eAEnd = eDependency.getAEnd().getFullyQualifiedName();
		}
		if (eDependency.getZEnd() != null) {
			eZEnd = eDependency.getZEnd().getFullyQualifiedName();
		}

		if ((eAEnd == null && iAEnd != null)
				|| (eAEnd != null && !eAEnd.equals(iAEnd))) {
			IStatus status = getErrorStatus("Dependency ends (A) don't match");
			result.add(status);
		}

		if ((eZEnd == null && iZEnd != null)
				|| (eZEnd != null && !eZEnd.equals(iZEnd))) {
			IStatus status = getErrorStatus("Dependency ends (Z) don't match");
			result.add(status);
		}

		return result;
	}

	private IStatus auditAssociation(Association eAssociation,
			ITigerstripeModelProject project) throws TigerstripeException {
		IArtifactManagerSession session = project.getArtifactManagerSession();

		if (eAssociation.getFullyQualifiedName() == null
				|| eAssociation.getFullyQualifiedName().length() == 0) {
			IStatus status = getErrorStatus("Association has no Fully Qualified Name");
			return status;
		}
		IAbstractArtifact iArtifact = session
				.getArtifactByFullyQualifiedName(eAssociation
						.getFullyQualifiedName());

		if (iArtifact == null) {
			IStatus status = getErrorStatus("Association '"
					+ eAssociation.getFullyQualifiedName()
					+ "' in diagram doesn't exist in model.");
			return status;
		}

		IAssociationArtifact iAssoc = (IAssociationArtifact) iArtifact;

		MultiStatus result = new MultiStatus(Activator.PLUGIN_ID, 222,
				eAssociation.getFullyQualifiedName(), null);

		// Check both ends
		String iAEnd = null;
		String iZEnd = null;
		if (iAssoc.getAEnd() != null) {
			iAEnd = iAssoc.getAEnd().getType().getFullyQualifiedName();
		}
		if (iAssoc.getZEnd() != null) {
			iZEnd = iAssoc.getZEnd().getType().getFullyQualifiedName();
		}

		String eAEnd = null;
		String eZEnd = null;
		if (eAssociation.getAEnd() != null) {
			eAEnd = eAssociation.getAEnd().getFullyQualifiedName();
		}
		if (eAssociation.getZEnd() != null) {
			eZEnd = eAssociation.getZEnd().getFullyQualifiedName();
		}

		if ((eAEnd == null && iAEnd != null)
				|| (eAEnd != null && !eAEnd.equals(iAEnd))) {
			IStatus status = getErrorStatus("Association (A) ends don't match");
			result.add(status);
		}

		if ((eZEnd == null && iZEnd != null)
				|| (eZEnd != null && !eZEnd.equals(iZEnd))) {
			IStatus status = getErrorStatus("Association (Z) ends don't match");
			result.add(status);
		}

		// Check navigabilities
		boolean iAEndIsNavigable = iAssoc.getAEnd().isNavigable();
		boolean eAEndIsNavigable = eAssociation.isAEndIsNavigable();
		if (iAEndIsNavigable != eAEndIsNavigable) {
			IStatus s = getErrorStatus("'isNavigable' doesn't match on AEnd ("
					+ eAssociation.getAEndName() + ").");
			result.add(s);
		}

		boolean iZEndIsNavigable = iAssoc.getZEnd().isNavigable();
		boolean eZEndIsNavigable = eAssociation.isZEndIsNavigable();
		if (iZEndIsNavigable != eZEndIsNavigable) {
			IStatus s = getErrorStatus("'isNavigable' doesn't match on ZEnd ("
					+ eAssociation.getZEndName() + ").");
			result.add(s);
		}

		// Check Ordering
		boolean iAEndIsOrdered = iAssoc.getAEnd().isOrdered();
		boolean iZEndIsOrdered = iAssoc.getZEnd().isOrdered();
		boolean eAEndIsOrdered = eAssociation.isAEndIsOrdered();
		boolean eZEndIsOrdered = eAssociation.isZEndIsOrdered();
		if (iAEndIsOrdered != eAEndIsOrdered) {
			IStatus s = getErrorStatus("'isOrdered' doesn't match on AEnd ("
					+ eAssociation.getAEndName() + ").");
			result.add(s);
		}
		if (iZEndIsOrdered != eZEndIsOrdered) {
			IStatus s = getErrorStatus("'isOrdered' doesn't match on ZEnd ("
					+ eAssociation.getZEndName() + ").");
			result.add(s);
		}

		// Check Uniqueness
		boolean iAEndIsUnique = iAssoc.getAEnd().isUnique();
		boolean iZEndIsUnique = iAssoc.getZEnd().isUnique();
		boolean eAEndIsUnique = eAssociation.isAEndIsUnique();
		boolean eZEndIsUnique = eAssociation.isZEndIsUnique();
		if (iAEndIsUnique != eAEndIsUnique) {
			IStatus s = getErrorStatus("'isUnique' doesn't match on AEnd ("
					+ eAssociation.getAEndName() + ").");
			result.add(s);
		}
		if (iZEndIsUnique != eZEndIsUnique) {
			IStatus s = getErrorStatus("'isUnique' doesn't match on ZEnd ("
					+ eAssociation.getZEndName() + ").");
			result.add(s);
		}

		// check end names
		String iAEndName = iAssoc.getAEnd().getName();
		String iZEndName = iAssoc.getZEnd().getName();
		String eAEndName = eAssociation.getAEndName();
		String eZEndName = eAssociation.getZEndName();

		if ((iAEndName == null && eAEndName != null)
				|| (iAEndName != null && !iAEndName.equals(eAEndName))) {
			IStatus s = getErrorStatus("AEnd name doesn't match");
			result.add(s);
		}

		if ((iZEndName == null && eZEndName != null)
				|| (iZEndName != null && !iZEndName.equals(eZEndName))) {
			IStatus s = getErrorStatus("ZEnd name doesn't match");
			result.add(s);
		}

		// check multiplicities
		IModelComponent.EMultiplicity iAEndMultiplicity = iAssoc.getAEnd().getMultiplicity();
		IModelComponent.EMultiplicity iZEndMultiplicity = iAssoc.getZEnd().getMultiplicity();
		AssocMultiplicity eAEndMultiplicity = eAssociation
				.getAEndMultiplicity();
		AssocMultiplicity eZEndMultiplicity = eAssociation
				.getZEndMultiplicity();

		if (ClassDiagramUtils.mapTypeMultiplicity(eAEndMultiplicity) != iAEndMultiplicity) {
			IStatus s = getErrorStatus("AEnd (" + eAssociation.getAEndName()
					+ ") multiplicity doesn't match");
			result.add(s);
		}

		if (ClassDiagramUtils.mapTypeMultiplicity(eZEndMultiplicity) != iZEndMultiplicity) {
			IStatus s = getErrorStatus("ZEnd (" + eAssociation.getZEndName()
					+ ") multiplicity doesn't match");
			result.add(s);
		}

		// check annotations
		if (eAssociation.getStereotypes().size() != iAssoc
				.getStereotypeInstances().size()) {
			IStatus status = getErrorStatus("Annotations don't match");
			result.add(status);
		} else {
			// same number of stereotypes let's see if they all
			// match
			List<String> eStereotypes = eAssociation.getStereotypes();
			Iterator<String> eStereo = eStereotypes.iterator();
			Collection<IStereotypeInstance> iStereotypes = iAssoc
					.getStereotypeInstances();
			for (IStereotypeInstance iStereo : iStereotypes) {
				String eStereotypeName = eStereo.next();
				String iStereotypeName = iStereo.getName();
				if (!eStereotypeName.equals(iStereotypeName)) {
					IStatus status = getErrorStatus("Annotations don't match");
					result.add(status);
					break;
				}
			}
		}

		// End Aggregation
		EAggregationEnum iAEndAggr = iAssoc.getAEnd().getAggregation();
		EAggregationEnum iZEndAggr = iAssoc.getZEnd().getAggregation();
		AggregationEnum eAEndAggr = eAssociation.getAEndAggregation();
		AggregationEnum eZEndAggr = eAssociation.getZEndAggregation();

		if (!ClassDiagramUtils.aggregationEquals(iAEndAggr, eAEndAggr)) {
			IStatus s = getErrorStatus("AEnd (" + eAssociation.getAEndName()
					+ ") aggregation doesn't match");
			result.add(s);
		}

		if (!ClassDiagramUtils.aggregationEquals(iZEndAggr, eZEndAggr)) {
			IStatus s = getErrorStatus("ZEnd (" + eAssociation.getZEndName()
					+ ") aggregation doesn't match");
			result.add(s);
		}

		// End Changeable
		EChangeableEnum iAEndChange = iAssoc.getAEnd().getChangeable();
		EChangeableEnum iZEndChange = iAssoc.getZEnd().getChangeable();
		ChangeableEnum eAEndChange = eAssociation.getAEndIsChangeable();
		ChangeableEnum eZEndChange = eAssociation.getZEndIsChangeable();

		if (!ClassDiagramUtils.changeableEquals(iAEndChange, eAEndChange)) {
			IStatus s = getErrorStatus("AEnd (" + eAssociation.getAEndName()
					+ ") changeable doesn't match");
			result.add(s);
		}

		if (!ClassDiagramUtils.changeableEquals(iAEndChange, eAEndChange)) {
			IStatus s = getErrorStatus("AEnd (" + eAssociation.getAEndName()
					+ ") changeable doesn't match");
			result.add(s);
		}

		// End Visibility
		EVisibility iAEndVisibility = iAssoc.getAEnd().getVisibility();
		EVisibility iZEndVisibility = iAssoc.getZEnd().getVisibility();
		Visibility eAEndVisibility = eAssociation.getAEndVisibility();
		Visibility eZEndVisibility = eAssociation.getZEndVisibility();

		if (ClassDiagramUtils.fromVisibility(eAEndVisibility) != iAEndVisibility) {
			IStatus s = getErrorStatus("AEnd (" + eAssociation.getAEndName()
					+ ") visibility doesn't match");
			result.add(s);
		}

		if (ClassDiagramUtils.fromVisibility(eZEndVisibility) != iZEndVisibility) {
			IStatus s = getErrorStatus("ZEnd (" + eAssociation.getZEndName()
					+ ") visibility doesn't match");
			result.add(s);
		}

		return result;
	}

	private IStatus auditArtifact(AbstractArtifact eArtifact,
			ITigerstripeModelProject project) throws TigerstripeException {
		IArtifactManagerSession session = project.getArtifactManagerSession();

		if (eArtifact.getFullyQualifiedName() == null
				|| eArtifact.getFullyQualifiedName().length() == 0) {
			IStatus status = getErrorStatus("Artifact has no Fully Qualified Name");
			return status;
		}
		IAbstractArtifact iArtifact = session
				.getArtifactByFullyQualifiedName(eArtifact
						.getFullyQualifiedName());

		if (iArtifact == null) {
			IStatus status = getErrorStatus("Artifact '"
					+ eArtifact.getFullyQualifiedName()
					+ "' in diagram doesn't exist in model.");
			return status;
		}

		MultiStatus result = new MultiStatus(Activator.PLUGIN_ID, 222,
				eArtifact.getFullyQualifiedName(), null);

		// check extends
		String eExtends = null;
		if (eArtifact.getExtends() != null)
			eExtends = eArtifact.getExtends().getFullyQualifiedName();
		String iExtends = null;
		if (iArtifact.getExtendedArtifact() != null) {
			iExtends = iArtifact.getExtendedArtifact().getFullyQualifiedName();
			boolean extendsIsOnDiagram = helper
					.findAbstractArtifactFor(iExtends) != null;
			if (!extendsIsOnDiagram) {
				iExtends = null;
				// if the extended artifact is not on the diagram, then ignore
				// the extends in the model
			}

			// A special case for Exceptions that need to extend
			// java.lang.Exception in the
			// model
			if (iArtifact instanceof IExceptionArtifact) {
				iExtends = null;
			}
		}

		if ((eExtends == null && !hideExtends(eArtifact) && iExtends != null)
				|| (eExtends != null && !eExtends.equals(iExtends))) {
			IStatus status = getErrorStatus(" In model extends " + iExtends
					+ " but not in diagram (" + eExtends + ").");
			result.add(status);
		}

		// check attributes
		IStatus status = auditAttributes(eArtifact, iArtifact);
		result.add(status);

		// check methods
		status = auditMethods(eArtifact, iArtifact);
		result.add(status);

		// check methods
		status = auditLiterals(eArtifact, iArtifact);
		result.add(status);

		return result;
	}

	private IStatus auditAttributes(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) throws TigerstripeException {
		MultiStatus result = new MultiStatus(Activator.PLUGIN_ID, 222,
				"Attributes", null);

		List<IField> fields = new ArrayList<IField>();
		fields.addAll(iArtifact.getFields());
		List<Attribute> attributes = eArtifact.getAttributes();

		for (Attribute attribute : attributes) {
			String attrName = attribute.getName();
			if (attrName == null || attrName.length() == 0) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						222, "Invalid attribute: no name", null);
				result.add(status);
				continue;
			}

			// remove it from the list of fields
			MultiStatus attrStatus = new MultiStatus(Activator.PLUGIN_ID, 222,
					attrName, null);
			IField iField = null;
			for (Iterator<IField> iter = fields.iterator(); iter.hasNext();) {
				IField field = iter.next();
				if (attrName.equals(field.getName())) {
					iField = field;
					iter.remove();
					break;
				}
			}

			if (iField == null) {
				IStatus status = getErrorStatus("Attribute doesn't exist in model.");
				attrStatus.add(status);
			} else {

				// check type
				if (!Misc.removeJavaLangString(
						iField.getType().getFullyQualifiedName()).equals(
						attribute.getType())) {
					IStatus status = getErrorStatus("Attribute type in model ("
							+ iField.getType().getFullyQualifiedName()
							+ ") is different on diagram: "
							+ attribute.getType());
					attrStatus.add(status);
				}

				// check the details of the attribute now
				if (attribute.getVisibility() != ClassDiagramUtils
						.toVisibility(iField.getVisibility())) {
					IStatus status = getErrorStatus("Visibility doesn't match");
					attrStatus.add(status);
				}

				if ((attribute.getDefaultValue() == null && iField
						.getDefaultValue() != null)
						|| (attribute.getDefaultValue() != null && !attribute
								.getDefaultValue().equals(
										iField.getDefaultValue()))) {
					IStatus status = getErrorStatus("Default values don't match");
					attrStatus.add(status);
				}

				if (attribute.isIsOrdered() != iField.isOrdered()) {
					IStatus status = getErrorStatus("'Ordered' doesn't match");
					attrStatus.add(status);
				}

				if (attribute.isIsUnique() != iField.isUnique()) {
					IStatus status = getErrorStatus("'isUnique' doesn't match");
					attrStatus.add(status);
				}

				if (attribute.getStereotypes().size() != iField
						.getStereotypeInstances().size()) {
					IStatus status = getErrorStatus("Annotations don't match");
					attrStatus.add(status);
				} else {
					// same number of stereotypes let's see if they all
					// match
					List<String> eStereotypes = attribute.getStereotypes();
					Iterator<String> eStereo = eStereotypes.iterator();
					Collection<IStereotypeInstance> iStereotypes = iField
							.getStereotypeInstances();
					for (IStereotypeInstance iStereo: iStereotypes) {
						String eStereotypeName = eStereo.next();
						String iStereotypeName = iStereo.getName();
						if (!eStereotypeName.equals(iStereotypeName)) {
							IStatus status = getErrorStatus("Annotations don't match");
							attrStatus.add(status);
							break;
						}
					}
				}

			}
		}

		if (fields.size() != 0) {
			// There are fields that are not in the diagram
			String fieldList = "";
			for (IField field : fields) {
				if (fieldList.length() != 0)
					fieldList += ", ";
				fieldList += (field.getName());
			}
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					222, "Attributes are missing in the diagram: " + fieldList,
					null);
			result.add(status);
		}

		return result;
	}

	private IStatus auditMethods(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) throws TigerstripeException {
		MultiStatus result = new MultiStatus(Activator.PLUGIN_ID, 222,
				"Methods", null);

		List<IMethod> methods = new ArrayList<IMethod>();
		methods.addAll(iArtifact.getMethods());
		List<Method> eMethods = eArtifact.getMethods();

		for (Method eMethod : eMethods) {
			String methodName = eMethod.getName();
			if (methodName == null || methodName.length() == 0) {
				IStatus status = getErrorStatus("Invalid method: no name");
				result.add(status);
				continue;
			}

			// remove it from the list of fields
			IMethod iMethod = null;
			MultiStatus methResult = new MultiStatus(Activator.PLUGIN_ID, 222,
					methodName, null);
			for (Iterator<IMethod> iter = methods.iterator(); iter.hasNext();) {
				IMethod method = iter.next();
				if (methodName.equals(method.getName())) {
					iMethod = method;
					iter.remove();
					break;
				}
			}

			if (iMethod == null) {
				IStatus status = getErrorStatus("Method '" + methodName
						+ "' in diagram doesn't exist in model.");
				methResult.add(status);
			} else {
				String typeName = iMethod.getReturnType()
						.getFullyQualifiedName();
				String targetReturnedType = Misc.removeJavaLangString(typeName);
				if (iMethod.isVoid()) {
					targetReturnedType = "void";
				}

				if (eMethod.getType() != null
						&& !eMethod.getType().equals(targetReturnedType)) {
					IStatus status = getErrorStatus("Return type in Diagram ("
							+ eMethod.getType()
							+ ") doesn't match model: "
							+ targetReturnedType);
					methResult.add(status);
				}

				if ((eMethod.getDefaultValue() == null && iMethod
						.getDefaultReturnValue() != null)
						|| (eMethod.getDefaultValue() != null && !eMethod
								.getDefaultValue().equals(
										iMethod.getDefaultReturnValue()))) {
					methResult
							.add(getErrorStatus("Default value doesn't match"));
				}

				if (eMethod.isIsAbstract() != iMethod.isAbstract()) {
					methResult
							.add(getErrorStatus("'isAbstract' doesn't match"));
				}

				if (eMethod.getVisibility() != ClassDiagramUtils
						.toVisibility(iMethod.getVisibility())) {
					methResult.add(getErrorStatus("Visibility doesn't match"));
				}

				if (eMethod.isIsOrdered() != iMethod.isOrdered()) {
					methResult.add(getErrorStatus("'isOrdered' doesn't match"));
				}

				if (eMethod.isIsUnique() != iMethod.isUnique()) {
					methResult.add(getErrorStatus("'isUnique' doesn't match"));
				}

				// review multiplicity, swap if necessary
				// First check if the method has a void return type - in which case, the multiplicity is moot
				if (!iMethod.isVoid()){
					AssocMultiplicity eMethodMultiplicy = eMethod
					.getTypeMultiplicity();

					if (iMethod.getReturnType() != null) {
						IModelComponent.EMultiplicity iMethodMultiplicity = iMethod
						.getReturnType().getTypeMultiplicity();
						if (eMethodMultiplicy != ClassDiagramUtils
								.mapTypeMultiplicity(iMethodMultiplicity)) {
							methResult
							.add(getErrorStatus("return multiplicity doesn't match "
									+iMethodMultiplicity.getLabel()+ " "+eMethodMultiplicy.getLiteral()));
						}
					}
				}

				// review arguments
				if (eMethod.getParameters().size() != iMethod.getArguments().size()) {
					methResult
							.add(getErrorStatus("Number of arguments doesn't match"));
				} else {
					// same number of args let's see if they all match
					List<Parameter> parameters = eMethod.getParameters();
					Iterator<IArgument> argumentIterator = iMethod.getArguments().iterator();
					for (int index = 0; index < parameters.size(); index++) {
						boolean changed = false;
						Parameter theParam = parameters.get(index);
						IArgument theArg = argumentIterator.next();
						if (!theArg.getName().equals(theParam.getName())) {
							theParam.setName(theArg.getName());
							changed = true;
						}
						String lclTypeName = theArg.getType()
								.getFullyQualifiedName();
						if (!Misc.removeJavaLangString(lclTypeName).equals(
								theParam.getType())) {
							methResult.add(getErrorStatus("Type of '"
									+ theArg.getName() + "' doesn't match"));
						}

						IModelComponent.EMultiplicity iMultiplicity = theArg.getType()
								.getTypeMultiplicity();
						AssocMultiplicity eMultiplicity = theParam
								.getTypeMultiplicity();
						if (iMultiplicity != ClassDiagramUtils
								.mapTypeMultiplicity(eMultiplicity)) {
							methResult.add(getErrorStatus("Multiplicity of '"
									+ theArg.getName() + "' doesn't match"));
						}

						if (!theArg.isOrdered() == theParam.isIsOrdered()) {
							methResult.add(getErrorStatus("'isOrdered' on '"
									+ theArg.getName() + "' doesn't match"));
						}

						if (!theArg.isUnique() == theParam.isIsUnique()) {
							methResult.add(getErrorStatus("'isUnique' on '"
									+ theArg.getName() + "' doesn't match"));
						}

						if ((theParam.getDefaultValue() == null && theArg
								.getDefaultValue() != null)
								|| (theParam.getDefaultValue() != null && !theParam
										.getDefaultValue().equals(
												theArg.getDefaultValue()))) {
							if (theArg.getDefaultValue() == null) {
								methResult
										.add(getErrorStatus("'defaultValue' for '"
												+ theArg.getName()
												+ "' doesn't match"));
							} else {
								methResult
										.add(getErrorStatus("'defaultValue' for '"
												+ theArg.getName()
												+ "' doesn't match"));
							}
						}

						// review stereotypes
						if (theParam.getStereotypes().size() != theArg
								.getStereotypeInstances().size()) {
							methResult
									.add(getErrorStatus("Number of Annotations for '"
											+ theArg.getName()
											+ "' doesn't match"));
						} else {
							// same number of stereotypes let's see if they all
							// match
							List<String> eStereotypes = theParam
									.getStereotypes();
							Iterator<String> eStereo = eStereotypes.iterator();
							Collection<IStereotypeInstance> iStereotypes = theArg.getStereotypeInstances();
							for (IStereotypeInstance iStereo : iStereotypes) {
								String eStereotypeName = eStereo.next();
								String iStereotypeName = iStereo.getName();
								if (!eStereotypeName.equals(iStereotypeName)) {
									methResult
											.add(getErrorStatus("Annotations for '"
													+ theArg.getName()
													+ "' doesn't match"));
								}
							}
						}

					}
				}

				// review stereotypes
				if (eMethod.getStereotypes().size() != iMethod
						.getStereotypeInstances().size()) {
					// not even the same number of args, let's redo the list
					methResult
							.add(getErrorStatus("Number of Annotations doesn't match"));
				} else {
					// same number of stereotypes let's see if they all match
					List<String> eStereotypes = eMethod.getStereotypes();
					Iterator<String> eStereo = eStereotypes.iterator();
					Collection<IStereotypeInstance> iStereotypes = iMethod.getStereotypeInstances();
					for (IStereotypeInstance iStereo : iStereotypes) {
						String eStereotypeName = eStereo.next();
						String iStereotypeName = iStereo.getName();
						if (!eStereotypeName.equals(iStereotypeName)) {
							methResult
									.add(getErrorStatus("Annotations don't match"));
						}
					}
				}
				result.add(methResult);
			}
		}

		return result;
	}

	private IStatus auditLiterals(AbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) throws TigerstripeException {
		MultiStatus result = new MultiStatus(Activator.PLUGIN_ID, 222,
				"Literals", null);

		List<ILiteral> literals = new ArrayList<ILiteral>();
		literals.addAll(iArtifact.getLiterals());
		List<Literal> eLiterals = eArtifact.getLiterals();

		for (Literal eLiteral : eLiterals) {
			String literalName = eLiteral.getName();
			if (literalName == null || literalName.length() == 0) {
				IStatus status = getErrorStatus("Invalid literal: no name");
				result.add(status);
				continue;
			}

			// remove it from the list of fields
			ILiteral iLiteral = null;
			MultiStatus litResult = new MultiStatus(Activator.PLUGIN_ID, 222,
					literalName, null);
			for (Iterator<ILiteral> iter = literals.iterator(); iter.hasNext();) {
				ILiteral literal = iter.next();
				if (literalName.equals(literal.getName())) {
					iLiteral = literal;
					iter.remove();
					break;
				}
			}

			if (iLiteral == null) {
				IStatus status = getErrorStatus("Literal '" + literalName
						+ "' in diagram doesn't exist in model.");
				litResult.add(status);
			} else {
				String labelValue = iLiteral.getValue();
				if (!labelValue.equals(eLiteral.getValue())) {
					IStatus status = getErrorStatus("Values don't match");
					litResult.add(status);
				}
			}
			result.add(litResult);
		}
		return result;
	}

	public String getSupportedFileExtension() {
		return ClosedClassDiagramSynchronizer.DIAGRAM_EXT;
	}

	private Map getMap(Diagram diagram) throws TigerstripeException {
		EObject element = diagram.getElement();
		if (element instanceof Map)
			return (Map) element;
		throw new TigerstripeException("Incompatible diagram type: got "
				+ element.getClass().getCanonicalName() + ", was expecting "
				+ Map.class.getCanonicalName());
	}

	public IStatus getErrorStatus(String message) {
		return new Status(IStatus.ERROR, Activator.PLUGIN_ID, 222, message,
				null);
	}
}
