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
package org.eclipse.tigerstripe.workbench.internal.builder;

import java.lang.reflect.Proxy;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.UnresolvedStereotypeInstance;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPrimitiveTypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeCapable;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;

public class CommonArtifactAuditor extends AbstractArtifactAuditor implements
        IArtifactAuditor {

    /**
     * Checks in the current active profile whether References are allowed or
     * not
     * 
     * @return
     */
    /**
     * Check that all attributes are valid, without duplicates.
     * 
     * @param monitor
     */
    private void checkAttributes(IProgressMonitor monitor) {
        IAbstractArtifact artifact = getArtifact();
        String artifactName = "";
        if (artifact != null)
            artifactName = artifact.getFullyQualifiedName();

        if (artifactName == null)
            artifactName = "";
        else
            artifactName = artifactName + ".";

        for (IField attribute : getArtifact().getFields()) {
            checkStereotypes(attribute, "attribute '" + attribute.getName()
                    + "' of artifact '" + getArtifact().getName() + "'",
                    attribute);
            checkAttributeDefaultValue(artifact, attribute);
            checkEnumField(attribute, artifactName);
        }
    }

    private void checkAttributeDefaultValue(IAbstractArtifact artifact,
            IField attribute) {
        IStatus vStatus = isDefaultValueValid(attribute.getType(),
                attribute.getDefaultValue());
        if (!vStatus.isOK()) {
            TigerstripeProjectAuditor.reportProblem(
                    "Default value of '" + artifact.getFullyQualifiedName()
                            + "." + attribute.getName()
                            + "' attribute is incorrect. "
                            + vStatus.getMessage(), (IResource) getArtifact()
                            .getAdapter(IResource.class), 222,
                    IMarker.SEVERITY_ERROR, attribute);
        }
    }

    private void checkMethodDefaultValue(IAbstractArtifact artifact,
            IMethod method) {
        IStatus vStatus = isDefaultValueValid(method.getReturnType(),
                method.getDefaultReturnValue());
        if (!vStatus.isOK()) {
            TigerstripeProjectAuditor.reportProblem("Default value of '"
                    + artifact.getFullyQualifiedName() + "." + method.getName()
                    + "' method is incorrect. " + vStatus.getMessage(),
                    (IResource) getArtifact().getAdapter(IResource.class), 222,
                    IMarker.SEVERITY_ERROR, method);
        }
    }

    private IStatus isDefaultValueValid(IType type, String defaultValue) {
        if (type.isPrimitive()
                || type.getArtifact() instanceof IPrimitiveTypeArtifact) {
            if (defaultValue != null && defaultValue.trim().length() > 0) {
                IWorkbenchProfile profile = TigerstripeCore
                        .getWorkbenchProfileSession().getActiveProfile();
                for (IPrimitiveTypeDef primitiveTypeDef : profile
                        .getPrimitiveTypeDefs(true)) {
                    if (primitiveTypeDef.getName().equals(type.getName())) {
                        if (primitiveTypeDef.getValidationExpression() != null
                                && !defaultValue.matches(primitiveTypeDef
                                        .getValidationExpression())) {
                            return new Status(SWT.ERROR, "pluginId",
                                    "Default value should match following regular expression: "
                                            + primitiveTypeDef
                                                    .getValidationExpression());
                        }
                        break;
                    }
                }
            }
        }
        return Status.OK_STATUS;
    }

    private void checkEnumField(IField field, String artifactName) {
        if (field.getType() != null) {
            IAbstractArtifact typeArtifact = field.getType().getArtifact();
            if (typeArtifact instanceof IEnumArtifact) {
                IEnumArtifact enumArtifact = (IEnumArtifact) typeArtifact;
                if (!isEnumFieldDefaultValueCorrect(field, enumArtifact)) {
                    TigerstripeProjectAuditor
                            .reportProblem(
                                    "Default value of '"
                                            + artifactName
                                            + field.getName()
                                            + "' attribute is incorrect. Referenced enumeration '"
                                            + enumArtifact
                                                    .getFullyQualifiedName()
                                            + "' doesn't contain '"
                                            + field.getDefaultValue()
                                            + "' literal.",
                                    (IResource) getArtifact().getAdapter(
                                            IResource.class), 222,
                                    IMarker.SEVERITY_ERROR, field);
                }
            }
        }
    }

    private boolean isEnumFieldDefaultValueCorrect(IField field,
            IEnumArtifact enumArtifact) {
        if (field.getDefaultValue() == null) {
            return true;
        }
        for (ILiteral literal : enumArtifact.getLiterals()) {
            if (field.getDefaultValue().equals(literal.getName())) {
                return true;
            }
        }
        return false;
    }

    public void run(IProgressMonitor monitor) {

        checkAttributes(monitor);
        checkLabels(monitor);
        checkMethods(monitor);
        // checkInterfacePackage();
        checkSuperArtifact();
        checkImplementedArtifacts();
        checkStereotypes(getArtifact(), "artifact '" + getArtifact().getName()
                + "'", null);

    }

    private void checkStereotypes(IStereotypeCapable capable, String location,
            IModelComponent member) {
        for (IStereotypeInstance instance : capable.getStereotypeInstances()) {
            if (instance instanceof UnresolvedStereotypeInstance) {
                TigerstripeProjectAuditor.reportProblem("Stereotype '"
                        + instance.getName() + "' on " + location
                        + " not defined in the current profile",
                        TigerstripeProjectAuditor.getIResourceForArtifact(
                                getIProject(), getArtifact()), 222,
                        IMarker.SEVERITY_WARNING, member);
            }
        }
    }

    /**
     * Need a separate method to get the return and argument stereos
     * 
     * @param capable
     * @param location
     */
    private void checkMethodStereotypes(IMethod method) {
        for (IStereotypeInstance instance : method
                .getReturnStereotypeInstances()) {
            if (instance instanceof UnresolvedStereotypeInstance) {
                String location = " return of method '" + method.getName()
                        + "' of artifact '" + getArtifact().getName() + "'";
                TigerstripeProjectAuditor.reportProblem("Stereotype "
                        + instance.getName() + " on " + location
                        + " not defined in the current profile",
                        TigerstripeProjectAuditor.getIResourceForArtifact(
                                getIProject(), getArtifact()), 222,
                        IMarker.SEVERITY_WARNING, method);
            }
        }
        for (IArgument argument : method.getArguments()) {
            for (IStereotypeInstance instance : argument
                    .getStereotypeInstances()) {
                if (instance instanceof UnresolvedStereotypeInstance) {
                    String location = " argument '" + argument.getName()
                            + "' of method '" + method.getName()
                            + "' of artifact '" + getArtifact().getName() + "'";
                    TigerstripeProjectAuditor.reportProblem("Stereotype '"
                            + instance.getName() + "' on '" + location
                            + "' not defined in the current profile",
                            TigerstripeProjectAuditor.getIResourceForArtifact(
                                    getIProject(), getArtifact()), 222,
                            IMarker.SEVERITY_WARNING, method);
                }
            }
        }
    }

    private void checkImplementedArtifacts() {
        for (IAbstractArtifact art : getArtifact().getImplementedArtifacts()) {
            if (getTSProject() != null) {
                try {
                    IAbstractArtifact s = getTSProject()
                            .getArtifactManagerSession()
                            .getArtifactByFullyQualifiedName(
                                    art.getFullyQualifiedName());
                    if (!(s instanceof ISessionArtifact)) {
                        TigerstripeProjectAuditor.reportError(
                                "Implemented artifact '"
                                        + art.getFullyQualifiedName()
                                        + "' is not a valid ISessionArtifact.",
                                TigerstripeProjectAuditor
                                        .getIResourceForArtifact(getIProject(),
                                                getArtifact()), 222);
                    }
                } catch (TigerstripeException e) {
                    BasePlugin.log(e);
                }
            }
        }
    }

    private void checkLabels(IProgressMonitor monitor) {
        for (ILiteral literal : getArtifact().getLiterals()) {
            checkStereotypes(literal, "literal '" + literal.getName()
                    + "' of artifact '" + getArtifact().getName() + "'", null);
        }
    }

    private void checkMethods(IProgressMonitor monitor) {
        // Checking that abstract methods only belong to abstract artifacts
        for (IMethod method : getArtifact().getMethods()) {
            if (method.isAbstract() && !getArtifact().isAbstract()) {
                TigerstripeProjectAuditor.reportProblem(
                        "Method " + method.getName()
                                + " is marked Abstract although "
                                + getArtifact().getFullyQualifiedName()
                                + " is not marked as Abstract.",
                        TigerstripeProjectAuditor.getIResourceForArtifact(
                                getIProject(), getArtifact()), 222,
                        IMarker.SEVERITY_ERROR, method);
            }
            checkStereotypes(method, "method '" + method.getName()
                    + "' of artifact '" + getArtifact().getName() + "'", method);
            // Need separate method to check the Return and Argument steros.
            // Possible change with new metamodel
            checkMethodDefaultValue(getArtifact(), method);
            checkMethodStereotypes(method);
            checkMethodArguments(method);
        }
    }

    private void checkMethodArguments(IMethod method) {
        for (IArgument argument : method.getArguments()) {
            checkArgumentDefaultValue(getArtifact(), method, argument);
        }
    }

    private void checkArgumentDefaultValue(IAbstractArtifact artifact,
            IMethod method, IArgument argument) {
        IStatus vStatus = isDefaultValueValid(argument.getType(),
                argument.getDefaultValue());
        if (!vStatus.isOK()) {
            TigerstripeProjectAuditor.reportProblem(
                    "Default value of argument '" + argument.getName()
                            + "' in method '"
                            + artifact.getFullyQualifiedName() + "."
                            + method.getName() + "' is incorrect. "
                            + vStatus.getMessage(), (IResource) getArtifact()
                            .getAdapter(IResource.class), 222,
                    IMarker.SEVERITY_ERROR, method);
        }
    }

    /**
     * Checks that the super artifact has the same artifact type. We don't worry
     * about the super artifact existence as the Java Builder will take care of
     * it.
     * 
     */
    private void checkSuperArtifact() {
        IAbstractArtifact artifact = getArtifact();
        IAbstractArtifact superArtifact = artifact.getExtendedArtifact();

        if (superArtifact != null
                && !((IAbstractArtifactInternal) superArtifact).isProxy()) {

            boolean eligableHierarhy = false;
            if (Proxy.isProxyClass(superArtifact.getClass())) {
                eligableHierarhy = compareClasses(artifact.getClass()
                        .getInterfaces(), superArtifact.getClass()
                        .getInterfaces());
            } else {
                eligableHierarhy = superArtifact.getClass() == artifact
                        .getClass();
            }

            if (artifact instanceof IAssociationClassArtifact) {
                eligableHierarhy |= superArtifact instanceof IManagedEntityArtifact;
            } else if (artifact instanceof IManagedEntityArtifact) {
                eligableHierarhy |= superArtifact instanceof IAssociationClassArtifact;
            }

            if (!eligableHierarhy) {
                TigerstripeProjectAuditor.reportError(
                        "Invalid Type Hierarchy in '"
                                + artifact.getFullyQualifiedName()
                                + "'. Super-artifact should be of same type.",
                        TigerstripeProjectAuditor.getIResourceForArtifact(
                                getIProject(), artifact), 222);
            }
        }
    }

    private boolean compareClasses(Class<?>[] childClasses,
            Class<?>[] parentClasses) {
        for (Class<?> child : childClasses) {
            boolean found = false;
            for (Class<?> parent : parentClasses) {
                if (child.equals(parent)
                    || (child.getName().endsWith("IManagedEntityArtifact") && parent
                            .getName()
                            .endsWith("IAssociationClassArtifact"))
                    || (child.getName().endsWith(
                            "IAssociationClassArtifact") && parent
                            .getName().endsWith("IManagedEntityArtifact"))) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }
}