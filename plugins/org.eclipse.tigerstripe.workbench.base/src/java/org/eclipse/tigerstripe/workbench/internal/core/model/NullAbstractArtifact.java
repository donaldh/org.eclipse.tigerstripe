/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.core.model;

import java.io.Writer;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.tigerstripe.repository.internal.IModelComponentMetadata;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.modules.IModuleHeader;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IStandardSpecifics;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeInstance;
import org.eclipse.tigerstripe.workbench.profile.stereotype.IStereotypeListener;
import org.eclipse.tigerstripe.workbench.project.IProjectDescriptor;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public enum NullAbstractArtifact implements IAbstractArtifact {

	INSATNCE;

	public String getName() {
		return null;
	}

	public void setName(String name) {

	}

	public String getComment() {
		return null;
	}

	public void setComment(String comment) {

	}

	public EVisibility getVisibility() {
		return null;
	}

	public void setVisibility(EVisibility visibility) {

	}

	public boolean isInActiveFacet() throws TigerstripeException {
		return false;
	}

	public IStatus validate() {
		return null;
	}

	public IModelComponentMetadata getMetadata() {
		return null;
	}

	public ITigerstripeModelProject getProject() throws TigerstripeException {
		return null;
	}

	public IModelComponent getContainingModelComponent() {
		return null;
	}

	public Collection<IModelComponent> getContainedModelComponents() {
		return null;
	}

	public String getLabel() {
		return null;
	}

	public Collection<IStereotypeInstance> getStereotypeInstances() {
		return null;
	}

	public IStereotypeInstance getStereotypeInstanceByName(String name) {
		return null;
	}

	public boolean hasStereotypeInstance(String name) {
		return false;
	}

	public void addStereotypeInstance(IStereotypeInstance instance) {

	}

	public void removeStereotypeInstance(IStereotypeInstance instance) {

	}

	public void removeStereotypeInstances(
			Collection<IStereotypeInstance> instances) {

	}

	public void addStereotypeListener(IStereotypeListener listener) {

	}

	public void removeStereotypeListener(IStereotypeListener listener) {

	}

	public String getStereotypeString() {
		return null;
	}

	public List<Object> getAnnotations() {
		return null;
	}

	public Object getAnnotation(String annotationType) {
		return null;
	}

	public List<Object> getAnnotations(String annotationType) {
		return null;
	}

	public List<Object> getAnnotations(Class<?> annotationType) {
		return null;
	}

	public boolean hasAnnotations() {
		return false;
	}

	public boolean hasAnnotations(String annotationType) {
		return false;
	}

	public boolean hasAnnotations(Class<?> annotationType) {
		return false;
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return null;
	}

	public String getArtifactType() {
		return null;
	}

	public boolean isAbstract() {
		return false;
	}

	public void setAbstract(boolean isAbstract) {

	}

	public String getFullyQualifiedName() {
		return null;
	}

	public void setFullyQualifiedName(String fqn) {

	}

	public String getPackage() {
		return null;
	}

	public void setPackage(String packageName) {

	}

	public Collection<Object> getChildren() {
		return null;
	}

	public Collection<IField> getFields() {
		return null;
	}

	public Collection<IField> getFields(boolean filterFacetExcludedFields) {
		return null;
	}

	public Collection<IField> getInheritedFields() {
		return null;
	}

	public Collection<IField> getInheritedFields(
			boolean filterFacetExcludedFields) {
		return null;
	}

	public IField makeField() {
		return null;
	}

	public void setFields(Collection<IField> fields) {

	}

	public void addField(IField field) {

	}

	public void removeFields(Collection<IField> fields) {

	}

	public Collection<ILiteral> getLiterals() {
		return null;
	}

	public Collection<ILiteral> getLiterals(boolean filterFacetExcludedLiterals) {
		return null;
	}

	public Collection<ILiteral> getInheritedLiterals() {
		return null;
	}

	public Collection<ILiteral> getInheritedLiterals(
			boolean filterFacetExcludedLiterals) {
		return null;
	}

	public ILiteral makeLiteral() {
		return null;
	}

	public void setLiterals(Collection<ILiteral> literals) {

	}

	public void addLiteral(ILiteral literal) {

	}

	public void removeLiterals(Collection<ILiteral> literals) {

	}

	public Collection<IMethod> getMethods() {
		return null;
	}

	public Collection<IMethod> getMethods(boolean filterFacetExcludedMethods) {
		return null;
	}

	public Collection<IMethod> getInheritedMethods() {
		return null;
	}

	public Collection<IMethod> getInheritedMethods(
			boolean filterFacetExcludedMethods) {
		return null;
	}

	public IMethod makeMethod() {
		return null;
	}

	public void setMethods(Collection<IMethod> methods) {

	}

	public void addMethod(IMethod method) {

	}

	public void removeMethods(Collection<IMethod> methods) {

	}

	public IAbstractArtifact getExtendedArtifact() {
		return null;
	}

	public boolean hasExtends() {
		return false;
	}

	public void setExtendedArtifact(IAbstractArtifact artifact) {

	}

	public void setExtendedArtifact(String fqn) {

	}

	public Collection<IAbstractArtifact> getAncestors() {
		return null;
	}

	public Collection<IAbstractArtifact> getExtendingArtifacts() {
		return null;
	}

	public Collection<IAbstractArtifact> getImplementedArtifacts() {
		return null;
	}

	public String getImplementedArtifactsAsStr() {
		return null;
	}

	public Collection<IAbstractArtifact> getImplementingArtifacts() {
		return null;
	}

	public void setImplementedArtifacts(Collection<IAbstractArtifact> artifacts) {

	}

	public Collection<IAbstractArtifact> getReferencedArtifacts() {
		return null;
	}

	public Collection<IAbstractArtifact> getReferencingArtifacts() {
		return null;
	}

	public IProjectDescriptor getProjectDescriptor() {
		return null;
	}

	public ITigerstripeModelProject getTigerstripeProject() {
		return null;
	}

	public boolean isReadonly() {
		return false;
	}

	public String getArtifactPath() throws TigerstripeException {
		return null;
	}

	public void doSave(IProgressMonitor monitor) throws TigerstripeException {

	}

	public void doSilentSave(IProgressMonitor monitor)
			throws TigerstripeException {

	}

	public void write(Writer writer) throws TigerstripeException {

	}

	public String asText() throws TigerstripeException {
		return null;
	}

	public IModelUpdater getUpdater() throws TigerstripeException {
		return null;
	}

	public IFieldTypeRef[] getFieldTypes() {
		return null;
	}

	public IStandardSpecifics getIStandardSpecifics() {
		return null;
	}

	public IModuleHeader getParentModuleHeader() {
		return null;
	}

	public IAbstractArtifact makeWorkingCopy(IProgressMonitor monitor)
			throws TigerstripeException {
		return null;
	}

}
