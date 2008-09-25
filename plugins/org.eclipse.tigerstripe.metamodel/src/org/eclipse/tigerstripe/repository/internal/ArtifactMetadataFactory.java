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
package org.eclipse.tigerstripe.repository.internal;

import java.net.URL;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.metamodel.IAssociationArtifact;
import org.eclipse.tigerstripe.metamodel.IAssociationClassArtifact;
import org.eclipse.tigerstripe.metamodel.IDatatypeArtifact;
import org.eclipse.tigerstripe.metamodel.IDependencyArtifact;
import org.eclipse.tigerstripe.metamodel.IEnumArtifact;
import org.eclipse.tigerstripe.metamodel.IEventArtifact;
import org.eclipse.tigerstripe.metamodel.IExceptionArtifact;
import org.eclipse.tigerstripe.metamodel.IField;
import org.eclipse.tigerstripe.metamodel.ILiteral;
import org.eclipse.tigerstripe.metamodel.IManagedEntityArtifact;
import org.eclipse.tigerstripe.metamodel.IMethod;
import org.eclipse.tigerstripe.metamodel.IPackage;
import org.eclipse.tigerstripe.metamodel.IPrimitiveType;
import org.eclipse.tigerstripe.metamodel.IQueryArtifact;
import org.eclipse.tigerstripe.metamodel.ISessionArtifact;
import org.eclipse.tigerstripe.metamodel.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.repository.metamodel.providers.IModelComponentIconProvider;
import org.osgi.framework.Bundle;

/**
 * A factory for all Artifact Metadata.
 * 
 * Default implementations for core Tigerstripe artifacts are provided unless
 * overriden from the org.eclipse.tigerstripe.metadata.artifactMetadata
 * extension point.
 * 
 * @author erdillon
 * @since 0.2.3
 */
public class ArtifactMetadataFactory {

	private final static String[] registryKeys = {
			IManagedEntityArtifact.class.getSimpleName(),
			IDatatypeArtifact.class.getSimpleName(),
			IExceptionArtifact.class.getSimpleName(),
			IEnumArtifact.class.getSimpleName(),
			IAssociationArtifact.class.getSimpleName(),
			IAssociationClassArtifact.class.getSimpleName(),
			IDependencyArtifact.class.getSimpleName(),
			ISessionArtifact.class.getSimpleName(),
			IQueryArtifact.class.getSimpleName(),
			IUpdateProcedureArtifact.class.getSimpleName(),
			IEventArtifact.class.getSimpleName(),
			IPrimitiveType.class.getSimpleName(), IField.class.getSimpleName(),
			IMethod.class.getSimpleName(), ILiteral.class.getSimpleName(),
			IPackage.class.getSimpleName() };

	private final static IArtifactMetadata MANAGED_ENTITY_DEFAULT = new ArtifactMetadata(
			IManagedEntityArtifact.class, true, true, true, "entity.gif",
			"entity_gs.gif", "entity_new.gif", "Entity");

	private final static IArtifactMetadata DATATYPE_DEFAULT = new ArtifactMetadata(
			IDatatypeArtifact.class, true, true, true, "datatype.gif",
			"datatype_gs.gif", "datatype_new.gif", "Datatype");

	private final static IArtifactMetadata EXCEPTION_DEFAULT = new ArtifactMetadata(
			IExceptionArtifact.class, true, false, false, "exception.gif",
			"exception_gs.gif", "exception_new.gif", "Exception");

	private final static IArtifactMetadata ENUM_DEFAULT = new ArtifactMetadata(
			IEnumArtifact.class, false, false, true, "enum.gif", "enum_gs.gif",
			"enum_new.gif", "Enumeration");

	private final static IArtifactMetadata ASSOCIATION_DEFAULT = new ArtifactMetadata(
			IAssociationArtifact.class, false, false, false,
			"association.gif", "association_gs.gif",
			"association_new.gif", "Association");

	private final static IArtifactMetadata ASSOCIATIONCLASS_DEFAULT = new ArtifactMetadata(
			IAssociationClassArtifact.class, true, true, false,
			"associationClass.gif", "associationClass_gs.gif",
			"associationClass_new.gif", "Association Class");

	private final static IArtifactMetadata DEPENDENCY_DEFAULT = new ArtifactMetadata(
			IDependencyArtifact.class, false, false, false,
			"dependency.gif", "dependency_gs.gif",
			"dependency_new.gif", "Dependency");

	private final static IArtifactMetadata SESSION_DEFAULT = new ArtifactMetadata(
			ISessionArtifact.class, false, true, true, "session.gif",
			"session_gs.gif", "session_new.gif", "Session Facade");

	private final static IArtifactMetadata QUERY_DEFAULT = new ArtifactMetadata(
			IQueryArtifact.class, true, false, true, "query.gif",
			"query_gs.gif", "query_new.gif", "Query");

	private final static IArtifactMetadata UPDATEPROC_DEFAULT = new ArtifactMetadata(
			IUpdateProcedureArtifact.class, true, false, true,
			"updateProc.gif", "updateProc_gs.gif", "updateProc_new.gif",
			"Update Procedure");

	private final static IArtifactMetadata PACKAGE_DEFAULT = new ArtifactMetadata(
			IPackage.class, true, false, true, "package.gif", "package_gs.gif",
			"package_new.gif", "Package");

	private final static IArtifactMetadata EVENT_DEFAULT = new ArtifactMetadata(
			IEventArtifact.class, true, false, true, "event.gif",
			"event_gs.gif", "event_new.gif", "Event");

	private final static IArtifactMetadata PRIMITIVE_DEFAULT = new ArtifactMetadata(
			IPrimitiveType.class, false, false, false, "primitive.gif",
			"primitive.gif", "primitive.gif", "Primitive Type");

	private final static IModelComponentMetadata FIELD_DEFAULT = new ModelComponentMetadata(
			IField.class, "field.gif", "field_gs.gif", "field_new.gif", "Field");

	private final static IModelComponentMetadata METHOD_DEFAULT = new ModelComponentMetadata(
			IMethod.class, "method.gif", "method_gs.gif", "method_new.gif",
			"Method");

	private final static IModelComponentMetadata LITERAL_DEFAULT = new ModelComponentMetadata(
			ILiteral.class, "literal.gif", "literal_gs.gif", "literal_new.gif",
			"Literal");

	private final static IModelComponentMetadata[] registryDefaultRegistryEntries = {
			MANAGED_ENTITY_DEFAULT, DATATYPE_DEFAULT, EXCEPTION_DEFAULT,
			ENUM_DEFAULT, ASSOCIATION_DEFAULT, ASSOCIATIONCLASS_DEFAULT,
			DEPENDENCY_DEFAULT, SESSION_DEFAULT, QUERY_DEFAULT,
			UPDATEPROC_DEFAULT, EVENT_DEFAULT, PRIMITIVE_DEFAULT,
			FIELD_DEFAULT, METHOD_DEFAULT, LITERAL_DEFAULT, PACKAGE_DEFAULT };

	private HashMap<String, IModelComponentMetadata> metadataRegistry = null;

	public static ArtifactMetadataFactory INSTANCE = new ArtifactMetadataFactory();

	private ArtifactMetadataFactory() {
		populateRegistry();
	}

	/**
	 * Returns the IArtifactMetadata for the given classname.
	 * 
	 * @param classname
	 * @return
	 */
	public IModelComponentMetadata getMetadata(String classname) {
		
	/* TODO a more explicit mechanism should be used here but there are 300+ uses of this method so will hack in for now */
		if (classname.contains("ManagedEntityArtifact")) {
			return metadataRegistry.get(IManagedEntityArtifact.class.getSimpleName());
		} else if (classname.contains("DatatypeArtifact")) {
			return metadataRegistry.get(IDatatypeArtifact.class.getSimpleName());
		} else if (classname.contains("ExceptionArtifact")) {
			return metadataRegistry.get(IExceptionArtifact.class.getSimpleName());
		} else if (classname.contains("EnumArtifact")) {
			return metadataRegistry.get(IEnumArtifact.class.getSimpleName());
		} else if (classname.contains("AssociationArtifact")) {
			return metadataRegistry.get(IAssociationArtifact.class.getSimpleName());
		} else if (classname.contains(
				"AssociationClassArtifact")) {
			return metadataRegistry.get(IAssociationClassArtifact.class.getSimpleName());
		} else if (classname.contains("DependencyArtifact")) {
			return metadataRegistry.get(IDependencyArtifact.class.getSimpleName());
		} else if (classname.contains("SessionArtifact")) {
			return metadataRegistry.get(ISessionArtifact.class.getSimpleName());
		} else if (classname.contains("QueryArtifact")) {
			return metadataRegistry.get(IQueryArtifact.class.getSimpleName());
		} else if (classname.contains(
				"UpdateProcedureArtifact")) {
			return metadataRegistry.get(IUpdateProcedureArtifact.class.getSimpleName());
		} else if (classname.contains("Package")) {
			return metadataRegistry.get(IPackage.class.getSimpleName());
		} else if (classname.contains("EventArtifact")) {
			return metadataRegistry.get(IEventArtifact.class.getSimpleName());
		} else if (classname.contains("PrimitiveType")) {
			return metadataRegistry.get(IPrimitiveType.class.getSimpleName());
		} else if (classname.contains("Field")) {
			return metadataRegistry.get(IField.class.getSimpleName());
		} else if (classname.contains("Method")) {
			return metadataRegistry.get(IMethod.class.getSimpleName());
		} else if (classname.contains("Literal")) {
			return metadataRegistry.get(ILiteral.class.getSimpleName());
		}
		return null;
	}

	/**
	 * Returns the metadata relevant to the given element
	 * 
	 * @param element
	 * @return
	 */
	public IModelComponentMetadata getMetadata(Object element) {
		if (element.getClass().getSimpleName().contains("ManagedEntityArtifact")) {
			return metadataRegistry.get(IManagedEntityArtifact.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains("DatatypeArtifact")) {
			return metadataRegistry.get(IDatatypeArtifact.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains("ExceptionArtifact")) {
			return metadataRegistry.get(IExceptionArtifact.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains("EnumArtifact")) {
			return metadataRegistry.get(IEnumArtifact.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains("AssociationArtifact")) {
			return metadataRegistry.get(IAssociationArtifact.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains(
				"AssociationClassArtifact")) {
			return metadataRegistry.get(IAssociationClassArtifact.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains("DependencyArtifact")) {
			return metadataRegistry.get(IDependencyArtifact.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains("SessionFacadeArtifact")) {
			return metadataRegistry.get(ISessionArtifact.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains("QueryArtifact")) {
			return metadataRegistry.get(IQueryArtifact.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains(
				"UpdateProcedureArtifact")) {
			return metadataRegistry.get(IUpdateProcedureArtifact.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains("Package")) {
			return metadataRegistry.get(IPackage.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains("EventArtifact")) {
			return metadataRegistry.get(IEventArtifact.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains("PrimitiveType")) {
			return metadataRegistry.get(IPrimitiveType.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains("Field")) {
			return metadataRegistry.get(IField.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains("Method")) {
			return metadataRegistry.get(IMethod.class.getSimpleName());
		} else if (element.getClass().getSimpleName().contains("Literal")) {
			return metadataRegistry.get(ILiteral.class.getSimpleName());
		}

		return null;
	}

	private void populateRegistry() {
		metadataRegistry = new HashMap<String, IModelComponentMetadata>();

		populateFromExtensionPoint();

		// Complete the registry with the default values for those
		// keys not redefined in an extension point.
		for (int index = 0; index < registryKeys.length; index++) {
			if (!metadataRegistry.containsKey(registryKeys[index]))
				metadataRegistry.put(registryKeys[index],
						registryDefaultRegistryEntries[index]);
		}
		updateIconsFromExtensionPoint();
		
		registerIconProviders();
	}

	private void updateIconsFromExtensionPoint() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();

		IConfigurationElement[] elements = registry
		.getConfigurationElementsFor("org.eclipse.tigerstripe.metamodel.customArtifactMetadata");
		for (IConfigurationElement element : elements) {
			if ("artifactIcon".equals(element.getName())) {
				String name = element.getAttribute("artifactName");
				IModelComponentMetadata md = metadataRegistry.get(name);
				if (md != null){
					String icon = element.getAttribute("icon");
					if (icon != null && icon.length() != 0) {
						IContributor contributor = element.getContributor();
						String bundleName = contributor.getName();
						Bundle bundle = Platform.getBundle(bundleName);
						md.setIconURL(bundle.getEntry(icon));
					}

					String icon_new = element.getAttribute("icon_new");
					if (icon_new != null && icon_new.length() != 0) {
						IContributor contributor = element.getContributor();
						String bundleName = contributor.getName();
						Bundle bundle = Platform.getBundle(bundleName);
						md.setNewIconURL(bundle.getEntry(icon_new));
					}

					String icon_gs = element.getAttribute("icon_gs");
					if (icon_gs != null && icon_gs.length() != 0) {
						IContributor contributor = element.getContributor();
						String bundleName = contributor.getName();
						Bundle bundle = Platform.getBundle(bundleName);
						md.setGreyedoutIconURL(bundle.getEntry(icon_gs));
					}
				}
			}
		}
	}

	private void registerIconProviders() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();

		IConfigurationElement[] elements = registry
				.getConfigurationElementsFor("org.eclipse.tigerstripe.metamodel.customArtifactMetadata");
		for (IConfigurationElement element : elements) {
			if ("modelComponentIconProvider".equals(element.getName())) {
				String classname = element.getAttribute("artifactType");
				try {
					IModelComponentIconProvider provider = (IModelComponentIconProvider) element
							.createExecutableExtension("provider");
					IModelComponentMetadata metadata = metadataRegistry
							.get(classname);
					if (metadata != null) {
						metadata.setIconProvider(provider);
					}
				} catch (CoreException e) {
					Activator.log(e);
				}
			}
		}
	}

	private void populateFromExtensionPoint() {

		IExtensionRegistry registry = Platform.getExtensionRegistry();

		IConfigurationElement[] elements = registry
				.getConfigurationElementsFor("org.eclipse.tigerstripe.metamodel.customArtifactMetadata");
		for (IConfigurationElement element : elements) {
			if ("artifactMetadata".equals(element.getName())) {
				String classname = element.getAttribute("artifactType");
				String userLabel = element.getAttribute("userLabel");
				boolean hasFields = Boolean.parseBoolean(element
						.getAttribute("hasFields"));
				boolean hasMethods = Boolean.parseBoolean(element
						.getAttribute("hasMethods"));
				boolean hasLiterals = Boolean.parseBoolean(element
						.getAttribute("hasLiterals"));
				String icon = element.getAttribute("icon");
				URL iconURL = null;
				if (icon != null && icon.length() != 0) {
					IContributor contributor = element.getContributor();
					String bundleName = contributor.getName();
					Bundle bundle = Platform.getBundle(bundleName);
					iconURL = bundle.getEntry(icon);
				}

				String icon_new = element.getAttribute("icon_new");
				URL icon_newURL = null;
				if (icon_new != null && icon_new.length() != 0) {
					IContributor contributor = element.getContributor();
					String bundleName = contributor.getName();
					Bundle bundle = Platform.getBundle(bundleName);
					icon_newURL = bundle.getEntry(icon_new);
				}

				String icon_gs = element.getAttribute("icon_gs");
				URL icon_gsURL = null;
				if (icon_gs != null && icon_gs.length() != 0) {
					IContributor contributor = element.getContributor();
					String bundleName = contributor.getName();
					Bundle bundle = Platform.getBundle(bundleName);
					icon_gsURL = bundle.getEntry(icon_gs);
				}

				metadataRegistry.put(classname, new ArtifactMetadata(null,
						hasFields, hasMethods, hasLiterals, iconURL,
						icon_gsURL, icon_newURL, userLabel));
			}
		}
	}
}
