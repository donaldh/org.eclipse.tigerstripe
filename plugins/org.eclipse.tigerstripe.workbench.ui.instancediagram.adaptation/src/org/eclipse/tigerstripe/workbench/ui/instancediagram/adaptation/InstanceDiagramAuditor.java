/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Variable;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.helpers.InstanceDiagramMapHelper;
import org.eclipse.tigerstripe.workbench.ui.internal.builder.IDiagramAuditor;
import org.eclipse.tigerstripe.workbench.ui.internal.gmf.synchronization.DiagramHandle;

public class InstanceDiagramAuditor implements IDiagramAuditor {

	private InstanceDiagramMapHelper helper;
	
	public IStatus auditDiagram(Diagram diagram, IProgressMonitor monitor)
			throws TigerstripeException {
		monitor.subTask(diagram.getName());

		try {
			InstanceMap map = getMap(diagram);
			helper = new InstanceDiagramMapHelper(map);

			MultiStatus status = new MultiStatus(Activator.PLUGIN_ID, 222,
					diagram.getName(), null);

			IStatus diagStatus = internalAuditDiagram(diagram, monitor);
			IStatus modelStatus = auditModel(status, map, monitor);

			if (!diagStatus.isOK())
				status.add(diagStatus);
			if (!modelStatus.isOK())
				status.add(modelStatus);
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
			InstanceMap map = getMap(diagram);
			helper = new InstanceDiagramMapHelper(map);

			MultiStatus status = new MultiStatus(Activator.PLUGIN_ID, 222,
					handle.getDiagramResource().getFullPath().toOSString(),
					null);

			IStatus diagStatus = internalAuditDiagram(diagram, monitor);
			IStatus modelStatus = auditModel(status, map, monitor);

			if (!diagStatus.isOK())
				status.add(diagStatus);
			if (!modelStatus.isOK())
				status.add(modelStatus);
			return status;
		} catch (TigerstripeException e) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, 222, e
					.getMessage(), e);
		}
	}

	private IStatus auditModel(MultiStatus parentStatus, InstanceMap map,
			IProgressMonitor monitor) throws TigerstripeException {

		MultiStatus instanceResult = new MultiStatus(Activator.PLUGIN_ID, 222,
				"Instances", null);

		ITigerstripeModelProject tsProject = map
				.getCorrespondingITigerstripeProject();

		auditBasePackage(instanceResult, map, tsProject.getArtifactManagerSession(), monitor);
		
		auditClassInstances(instanceResult, map, tsProject
				.getArtifactManagerSession(), monitor);
		auditAssociationInstances(instanceResult, map, tsProject
				.getArtifactManagerSession(), monitor);

		return instanceResult;
	}

	/**
	 * 	Check that the base package set in the diagram corresponds to the location of the diagram
	 * 
	 * @param parentStatus
	 * @param map
	 * @param session
	 * @param monitor
	 */
	private void auditBasePackage( MultiStatus parentStatus, InstanceMap map,
			IArtifactManagerSession session, IProgressMonitor monitor ) {
		String basePackage = map.getBasePackage();
		if ( basePackage == null )
			basePackage = "";
		Resource resource = map.eResource(); // this is model resource
		URI resourceURI = resource.getURI();
		
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(resourceURI.toPlatformString(true));
		if ( res != null ) {
			IContainer container = res.getParent();
			Object obj = JavaCore.create(container);
			if ( obj instanceof IPackageFragment ) {
				IPackageFragment pack = (IPackageFragment) obj;
				String locatedPackage = pack.getElementName();
				if ( !basePackage.equals(locatedPackage)) {
					IStatus s = getErrorStatus("Invalid base package '"
							+ basePackage + "' (located in '" + locatedPackage + "').");
					parentStatus.add(s);
				}
			}
		}
	}
	
	private void auditClassInstances(MultiStatus parentStatus, InstanceMap map,
			IArtifactManagerSession session, IProgressMonitor monitor) {
		EList<ClassInstance> instances = map.getClassInstances();

		for (ClassInstance instance : instances) {
			String fqn = instance.getFullyQualifiedName();
			IAbstractArtifact art = session.getArtifactByFullyQualifiedName(
					fqn, true);
			if (art == null) {
				IStatus s = getErrorStatus("Undefined class for Instance '"
						+ instance.getInstanceName() + "' ('" + fqn + "').");
				parentStatus.add(s);
			} else {
				EList<Variable> variables = instance.getVariables();
				boolean attributeFound = false;
				for (Variable variable : variables) {
					String varName = variable.getName();
					List<IField> fields = new ArrayList<IField>();
					fields.addAll(art.getFields());
					fields.addAll(art.getInheritedFields());
					for (IField field : fields) {
						if (varName.equals(field.getName())) {
							attributeFound = true;
							break;
						}
					}

					if (!attributeFound) {
						IStatus s = getErrorStatus("Undefined variable '"
								+ varName + "' in instance '"
								+ instance.getInstanceName() + "' (" + fqn + ")");
						parentStatus.add(s);
					}
				}
			}
		}
	}

	private void auditAssociationInstances(MultiStatus parentStatus,
			InstanceMap map, IArtifactManagerSession session,
			IProgressMonitor monitor) {
		EList<AssociationInstance> instances = map.getAssociationInstances();
		for (AssociationInstance instance : instances) {
			String fqn = instance.getFullyQualifiedName();
			IAbstractArtifact art = session.getArtifactByFullyQualifiedName(
					fqn, true);
			if (art == null) {
				IStatus s = getErrorStatus("Undefined Association for Instance '"
						+ instance.getInstanceName() + "' ('" + fqn + "').");
				parentStatus.add(s);
			}
		}
	}

	public String getSupportedFileExtension() {
		return ClosedInstanceDiagramSynchronizer.DIAGRAM_EXT;
	}

	private IStatus internalAuditDiagram(Diagram diagram,
			IProgressMonitor monitor) throws TigerstripeException {

		return Status.OK_STATUS;
	}

	private InstanceMap getMap(Diagram diagram) throws TigerstripeException {
		EObject element = diagram.getElement();
		if (element instanceof InstanceMap)
			return (InstanceMap) element;
		throw new TigerstripeException("Incompatible diagram type: got "
				+ element.getClass().getCanonicalName() + ", was expecting "
				+ InstanceMap.class.getCanonicalName());
	}

	public IStatus getErrorStatus(String message) {
		return new Status(IStatus.ERROR, Activator.PLUGIN_ID, 222, message,
				null);
	}

}
