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
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.uml2;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationClassArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IEventArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IUpdateProcedureArtifactImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.project.IImportCheckpointDetails;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.model.DatatypeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.EnumArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AbstractImportCheckpointHelper;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.Annotable;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementAttribute;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementConstant;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementOperation;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementOperationParameter;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.UML2ImportCheckpoint;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.uml2.UML2ImportCheckpointHelper;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.xmi.XmiImportCheckpoint;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.xmi.XmiImportCheckpointHelper;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.IArgument;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent.EVisibility;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjArtifactSpecifics;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEntitySpecifics;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjEnumSpecifics;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IStandardSpecifics;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.queries.IQueryAllArtifacts;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.ArtifactDefinitionGenerator;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.artifacts.NewArtifactWizardPage;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.ImportWithCheckpointWizardPage;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.annotate.AnnotateWizardPage;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.xmi.AnnotatedElement;
import org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.xmi.AnnotatedElementGenerator;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the
 * provided container. If the container resource (a folder or a project) is
 * selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "mpe". If a
 * sample multi-page editor (also available as a template) is registered for the
 * same extension, it will be able to open it.
 */

public class ImportFromUML2Wizard extends Wizard implements INewWizard {

	private final static String ENTITY_TEMPLATE = ArtifactDefinitionGenerator.VM_TEMPLATE_BASE
			+ "entity.vm";

	private final static String DATATYPE_TEMPLATE = ArtifactDefinitionGenerator.VM_TEMPLATE_BASE
			+ "datatype.vm";

	private final static String ENUM_TEMPLATE = ArtifactDefinitionGenerator.VM_TEMPLATE_BASE
			+ "enum.vm";

	private final static String EXCEPTION_TEMPLATE = ArtifactDefinitionGenerator.VM_TEMPLATE_BASE
			+ "exception.vm";

	private final static String NOTIFICATION_TEMPLATE = ArtifactDefinitionGenerator.VM_TEMPLATE_BASE
			+ "notification.vm";

	private final static String NAMEDQUERY_TEMPLATE = ArtifactDefinitionGenerator.VM_TEMPLATE_BASE
			+ "query.vm";

	private final static String UPDATEPROC_TEMPLATE = ArtifactDefinitionGenerator.VM_TEMPLATE_BASE
			+ "updateProcedure.vm";

	private final static String SESSION_TEMPLATE = ArtifactDefinitionGenerator.VM_TEMPLATE_BASE
			+ "session.vm";

	private final static String ASSOCIATION_TEMPLATE = ArtifactDefinitionGenerator.VM_TEMPLATE_BASE
			+ "association.vm";

	private final static String ASSOCIATIONCLASS_TEMPLATE = ArtifactDefinitionGenerator.VM_TEMPLATE_BASE
			+ "associationClass.vm";

	private final static String DEPENDENCY_TEMPLATE = ArtifactDefinitionGenerator.VM_TEMPLATE_BASE
			+ "dependency.vm";

	private ImportWithCheckpointWizardPage initialPage;

	private ImportFromUML2WizardPage firstPage;

	private AnnotateWizardPage secondPage;

	private IStructuredSelection fSelection;

	private AbstractImportCheckpointHelper checkpointHelper;

	private ImageDescriptor image;

	private ByteArrayOutputStream buffer;

	public IStructuredSelection getSelection() {
		return this.fSelection;
	}

	public ImageDescriptor getDefaultImageDescriptor() {
		return this.image;
	}

	/**
	 * Constructor for NewProjectWizard.
	 */
	public ImportFromUML2Wizard() {
		super();
		setNeedsProgressMonitor(true);
		image = Images.getDescriptor(Images.WIZARD_IMPORT_LOGO);
		setDefaultPageImageDescriptor(image);

		setWindowTitle("Import from UML2");
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {
		super.addPages();
		this.initialPage = new ImportWithCheckpointWizardPage(
				"UML2 Model Import",
				"Import a UML2 Model into a Tigerstripe project.",
				UML2ImportCheckpoint.class.getCanonicalName());

		this.firstPage = new ImportFromUML2WizardPage();
		// this.secondPage = new AnnotateFromXMIWizardPage();
		this.secondPage = new AnnotateWizardPage();

		addPage(this.initialPage);
		addPage(this.firstPage);
		addPage(this.secondPage);

		this.initialPage.init(getSelection());
		this.firstPage.init(getSelection());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		fSelection = currentSelection;
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {

		try {
			// make sure no build will happen during import or
			// else if becomes catastrophic performance-wise (try to import SID
			// :-))
			TigerstripeProjectAuditor.setTurnedOffForImport(true);

			if (initialPage.getReferenceTSProject() == null)
				return finishNewImport();
			else
				return finishCheckpointedImport();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			return false;
		} finally {
			TigerstripeProjectAuditor.setTurnedOffForImport(false);
			Utilities.tearDown();
		}
	}

	/**
	 * When dealing with a checkpointed import, the reference project needs to
	 * be copied over into the target project. IN the process, each delta
	 * (that's not marked as ignored) should be applied on the copied artifact.
	 * 
	 * @return
	 */
	protected boolean finishCheckpointedImport() {
		final AnnotableElement[] annotables = secondPage.getRawClasses();
		final ITigerstripeModelProject targetProject = (ITigerstripeModelProject) initialPage
				.getIProject().getAdapter(ITigerstripeModelProject.class);
		checkpointHelper = new XmiImportCheckpointHelper(targetProject);

		try {
			finishCheckpointedImportAsync(new NullProgressMonitor());
			IImportCheckpointDetails ckDetails = checkpointHelper
					.makeImportCheckpointDetails(XmiImportCheckpoint.class
							.getCanonicalName());
			checkpointHelper.createCheckpoint(ckDetails, annotables);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		return true;
	}

	protected boolean finishCheckpointedImportAsync(IProgressMonitor monitor) {

		// let's copy the reference project first
		// we'll apply the deltas afterward.
		try {
			ITigerstripeModelProject refProject = initialPage
					.getReferenceTSProject();
			ITigerstripeModelProject targetProject = initialPage.getTSProject();
			AnnotableElement[] annotables = secondPage.getRawClasses();

			IArtifactManagerSession refSession = refProject
					.getArtifactManagerSession();
			IArtifactManagerSession targetSession = targetProject
					.getArtifactManagerSession();

			IQueryAllArtifacts query = (IQueryAllArtifacts) refSession
					.makeQuery(IQueryAllArtifacts.class.getCanonicalName());
			query.setIncludeDependencies(false);

			Collection<IAbstractArtifact> refArtifacts = refSession
					.queryArtifact(query);

			monitor.beginTask("Cloning reference project", refArtifacts.size());
			for (IAbstractArtifact refArtifact : refArtifacts) {
				IAbstractArtifact artifactCopy = targetSession.makeArtifact(
						refArtifact, refArtifact);

				// lookForDeltas( artifactCopy, annotables );

				targetSession.addArtifact(artifactCopy);
				// artifactCopy.doSave();
				monitor.worked(1);
			}
			monitor.done();

			// Apply deltas now
			monitor.beginTask("Applying selected import deltas",
					annotables.length);
			for (AnnotableElement anno : annotables) {
				if (anno.getDelta() != Annotable.DELTA_UNCHANGED
						&& anno.getDelta() != Annotable.DELTA_UNKNOWN
						&& !anno.shouldIgnore()) {
					// is it a new annotable?
					switch (anno.getDelta()) {
					case Annotable.DELTA_ADDED:
						addedAnnotable(targetSession, anno);
						break;
					case Annotable.DELTA_CHANGED:
						changedAnnotable(targetSession, anno);
						break;
					}
				}
			}

			// Now save everything
			Collection<IAbstractArtifact> targetArtifacts = targetSession
					.queryArtifact(query);
			for (IAbstractArtifact targetArtifact : targetArtifacts) {
				targetArtifact.doSave(new NullProgressMonitor());
				monitor.worked(1);
			}

			monitor.done();
			return true;
		} catch (TigerstripeException e) {
			TigerstripeRuntime.logErrorMessage("TigerstripeException detected",
					e);
		}

		return false;
	}

	protected void addedAnnotable(IArtifactManagerSession session,
			AnnotableElement anno) throws TigerstripeException {

		IAbstractArtifact newArtifact = null;
		IStandardSpecifics specifics = null;
		if (AnnotableElement.AS_DATATYPE.equals(anno.getAnnotationType())) {
			newArtifact = session.makeArtifact(DatatypeArtifact.MODEL);
		} else if (AnnotableElement.AS_ENTITY.equals(anno.getAnnotationType())) {
			newArtifact = session.makeArtifact(ManagedEntityArtifact.MODEL);
		} else if (AnnotableElement.AS_ENUMERATION.equals(anno
				.getAnnotationType())) {
			newArtifact = session.makeArtifact(EnumArtifact.MODEL);
		}

		if (newArtifact == null)
			// this annotable is not to be mapped
			// just ignore;
			return;

		newArtifact.setFullyQualifiedName(anno.getFullyQualifiedName());
		newArtifact.setComment(anno.getDescription());
		specifics = newArtifact.getIStandardSpecifics();

		IOssjArtifactSpecifics spec = (IOssjArtifactSpecifics) specifics;
		spec.getInterfaceProperties().setProperty("package",
				newArtifact.getPackage());

		if (specifics instanceof IOssjEntitySpecifics) {
			IOssjEntitySpecifics specs = (IOssjEntitySpecifics) specifics;
			specs.setPrimaryKey("String");
		} else if (specifics instanceof IOssjEnumSpecifics) {
			IOssjEnumSpecifics specs = (IOssjEnumSpecifics) specifics;
			IType type = newArtifact.makeField().makeType();
			type.setFullyQualifiedName("String");
			specs.setBaseIType(type);
		}

		IAbstractArtifact extArt = session.makeArtifact(newArtifact);
		extArt.setFullyQualifiedName(anno.getParentAnnotableElement()
				.getFullyQualifiedName());
		newArtifact.setExtendedArtifact(extArt);

		// go thru all the attr
		for (AnnotableElementAttribute attr : anno
				.getAnnotableElementAttributes()) {
			if (!attr.shouldIgnore()) {
				IField field = newArtifact.makeField();
				field.setName(attr.getName());
				field.setComment(attr.getDescription());
				IType type = field.makeType();
				type.setFullyQualifiedName(attr.getType()
						.getFullyQualifiedName());
				type.setMultiplicity(attr.getDimensions());
				field.setType(type);
				newArtifact.addField(field);
			}
		}

		// go thru all the constants
		for (AnnotableElementConstant cst : anno.getAnnotableElementConstants()) {
			if (!cst.shouldIgnore()) {
				ILiteral literal = newArtifact.makeLiteral();
				literal.setName(cst.getName());
				literal.setComment(cst.getDescription());
				IType type = literal.makeType();
				type.setFullyQualifiedName("String");
				literal.setType(type);
				literal.setValue(cst.getValue());
				literal.setVisibility(EVisibility.at(cst.getVisibility()));
				newArtifact.addLiteral(literal);
			}
		}

		for (AnnotableElementOperation op : anno
				.getAnnotableElementOperations()) {
			if (!op.shouldIgnore()) {
				IMethod method = newArtifact.makeMethod();
				method.setName(op.getName());
				method.setComment(op.getDescription());
				IType returnType = method.makeType();
				returnType.setFullyQualifiedName(op.getReturnType()
						.getFullyQualifiedName());
				for (AnnotableElementOperationParameter param : op
						.getAnnotableElementOperationParameters()) {
					IArgument arg = method.makeArgument();
					arg.setName(param.getName());
					IType argType = method.makeType();
					argType.setFullyQualifiedName(param.getType()
							.getFullyQualifiedName());
					arg.setType(argType);
					method.addArgument(arg);
				}

				// FIXME: we're not importing the exceptions !
				newArtifact.addMethod(method);
			}
		}
		session.addArtifact(newArtifact);
	}

	protected void changedAnnotable(IArtifactManagerSession session,
			AnnotableElement anno) {
		// At this point we can only detect adds...

		// Looks for the corresponding IArtifact
		IAbstractArtifact artifact = session.getArtifactByFullyQualifiedName(
				anno.getFullyQualifiedName(), false);

		if (artifact != null) {
			// look in the attributes first
			for (AnnotableElementAttribute attr : anno
					.getAnnotableElementAttributes()) {
				if (attr.getDelta() == Annotable.DELTA_ADDED
						&& !attr.shouldIgnore()) {
					IField field = artifact.makeField();
					field.setName(attr.getName());
					field.setComment(attr.getDescription());
					IType type = field.makeType();
					type.setFullyQualifiedName(attr.getType()
							.getFullyQualifiedName());
					type.setMultiplicity(attr.getDimensions());
					field.setType(type);

					List<IField> tmp = new ArrayList<IField>();
					tmp.add(field);
					for (IField existing : artifact.getFields()) {
						tmp.add(existing);
					}
					artifact.setFields(tmp);
				}
			}

			for (AnnotableElementConstant cst : anno
					.getAnnotableElementConstants()) {
				if (cst.getDelta() == Annotable.DELTA_ADDED
						&& !cst.shouldIgnore()) {
					ILiteral literal = artifact.makeLiteral();
					literal.setName(cst.getName());
					literal.setComment(cst.getDescription());
					IType type = literal.makeType();
					type.setFullyQualifiedName("String");
					literal.setType(type);
					literal.setValue(cst.getValue());

					List<ILiteral> tmp = new ArrayList<ILiteral>();
					tmp.add(literal);
					for (ILiteral existing : artifact.getLiterals()) {
						tmp.add(existing);
					}
					artifact.setLiterals(tmp);
				}
			}

			for (AnnotableElementOperation op : anno
					.getAnnotableElementOperations()) {
				if (op.getDelta() == Annotable.DELTA_ADDED
						&& !op.shouldIgnore()) {
					IMethod method = artifact.makeMethod();
					method.setName(op.getName());
					method.setComment(op.getDescription());
					IType returnType = method.makeType();
					returnType.setFullyQualifiedName(op.getReturnType()
							.getFullyQualifiedName());
					method.setReturnType(returnType);
					for (AnnotableElementOperationParameter param : op
							.getAnnotableElementOperationParameters()) {
						IArgument arg = method.makeArgument();
						arg.setName(param.getName());
						IType argType = method.makeType();
						argType.setFullyQualifiedName(param.getType()
								.getFullyQualifiedName());
						// FIXME: can't set the argType dimension
						method.addArgument(arg);
					}
					// FIXME: can't set the exceptions

					List<IMethod> tmp = new ArrayList<IMethod>();
					tmp.add(method);
					for (IMethod existing : artifact.getMethods()) {
						tmp.add(existing);
					}
					artifact.setMethods(tmp);

				}
			}
		}
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean finishNewImport() {

		final ITigerstripeModelProject targetProject = (ITigerstripeModelProject) initialPage
				.getIProject().getAdapter(ITigerstripeModelProject.class);

		checkpointHelper = new UML2ImportCheckpointHelper(targetProject);

		final Properties prop = firstPage.getProperties();
		final List entities = secondPage.getEntities();
		final List datatypes = secondPage.getDatatypes();
		final List exceptions = secondPage.getExceptions();
		final List notifications = secondPage.getNotifications();
		final List namedQueries = secondPage.getNamedQueries();
		final List updateProcedures = secondPage.getUpdateProcedures();
		final List sessionFacades = secondPage.getSessionFacades();
		final List enumerations = secondPage.getEnumerations();
		final List associations = secondPage.getAssociations();
		final List associationClasses = secondPage.getAssociationClasses();
		final List dependencies = secondPage.getDependencies();
		final AnnotableElement[] annotables = secondPage.getRawClasses();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					createEntities(prop, entities, monitor);
					createDatatypes(prop, datatypes, monitor);
					createEnumerations(prop, enumerations, monitor);
					createExceptions(prop, exceptions, monitor);
					createNotifications(prop, notifications, monitor);
					createNamedQueries(prop, namedQueries, monitor);
					createUpdateProcedures(prop, updateProcedures, monitor);
					createSessionFacades(prop, sessionFacades, monitor);
					createAssociations(prop, associations, monitor);
					createAssociationClasses(prop, associationClasses, monitor);
					createDependencies(prop, dependencies, monitor);

					IImportCheckpointDetails ckDetails = checkpointHelper
							.makeImportCheckpointDetails(UML2ImportCheckpoint.class
									.getCanonicalName());
					checkpointHelper.createCheckpoint(ckDetails, annotables);

				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} catch (TigerstripeException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};

		try {
			getContainer().run(true, false, op);
		} catch (InvocationTargetException e) {
			TigerstripeRuntime.logErrorMessage(
					"InvocationTargetException detected", e);
			handleFinishException(getShell(), e);
			return false;
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	protected void handleFinishException(Shell shell,
			InvocationTargetException e) {
		String title = NewWizardMessages.NewElementWizard_op_error_title;
		String message = NewWizardMessages.NewElementWizard_op_error_message;
		ExceptionHandler.handle(e, shell, title, message);
	}

	private void createEntities(Properties pageProperties, List entities,
			IProgressMonitor monitor) throws CoreException {
		monitor.setTaskName("Creating "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IManagedEntityArtifactImpl.class.getName()).getLabel()
				+ "(s).");
		runGenerator(ENTITY_TEMPLATE, pageProperties, entities, monitor);
	}

	private void createDatatypes(Properties pageProperties, List datatypes,
			IProgressMonitor monitor) throws CoreException {
		monitor.setTaskName("Creating "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IDatatypeArtifactImpl.class.getName()).getLabel()
				+ "(s).");
		runGenerator(DATATYPE_TEMPLATE, pageProperties, datatypes, monitor);
	}

	private void createEnumerations(Properties pageProperties, List enums,
			IProgressMonitor monitor) throws CoreException {
		monitor.setTaskName("Creating "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IEnumArtifactImpl.class.getName()).getLabel() + "(s).");
		runGenerator(ENUM_TEMPLATE, pageProperties, enums, monitor);
	}

	private void createExceptions(Properties pageProperties, List exceptions,
			IProgressMonitor monitor) throws CoreException {
		monitor.setTaskName("Creating "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IExceptionArtifactImpl.class.getName()).getLabel()
				+ "(s).");
		runGenerator(EXCEPTION_TEMPLATE, pageProperties, exceptions, monitor);
	}

	private void createNotifications(Properties pageProperties,
			List notifications, IProgressMonitor monitor) throws CoreException {
		monitor
				.setTaskName("Creating "
						+ ArtifactMetadataFactory.INSTANCE.getMetadata(
								IEventArtifactImpl.class.getName()).getLabel()
						+ "(s).");
		runGenerator(NOTIFICATION_TEMPLATE, pageProperties, notifications,
				monitor);
	}

	private void createNamedQueries(Properties pageProperties,
			List namedQueries, IProgressMonitor monitor) throws CoreException {
		monitor
				.setTaskName("Creating "
						+ ArtifactMetadataFactory.INSTANCE.getMetadata(
								IQueryArtifactImpl.class.getName()).getLabel()
						+ "(s).");
		runGenerator(NAMEDQUERY_TEMPLATE, pageProperties, namedQueries, monitor);
	}

	private void createUpdateProcedures(Properties pageProperties,
			List updateProcedures, IProgressMonitor monitor)
			throws CoreException {
		monitor.setTaskName("Creating "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IUpdateProcedureArtifactImpl.class.getName())
						.getLabel() + "(s).");
		runGenerator(UPDATEPROC_TEMPLATE, pageProperties, updateProcedures,
				monitor);
	}

	private void createSessionFacades(Properties pageProperties,
			List sessionFacades, IProgressMonitor monitor) throws CoreException {
		monitor.setTaskName("Creating "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						ISessionArtifactImpl.class.getName()).getLabel()
				+ "(s).");
		runGenerator(SESSION_TEMPLATE, pageProperties, sessionFacades, monitor);
	}

	private void createAssociations(Properties pageProperties,
			List associations, IProgressMonitor monitor) throws CoreException {
		monitor.setTaskName("Creating "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IAssociationArtifactImpl.class.getName()).getLabel()
				+ "(s).");
		runGenerator(ASSOCIATION_TEMPLATE, pageProperties, associations,
				monitor);
	}

	private void createAssociationClasses(Properties pageProperties,
			List associationClasses, IProgressMonitor monitor)
			throws CoreException {
		monitor.setTaskName("Creating "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IAssociationClassArtifactImpl.class.getName())
						.getLabel() + "(s).");
		runGenerator(ASSOCIATIONCLASS_TEMPLATE, pageProperties,
				associationClasses, monitor);
	}

	private void createDependencies(Properties pageProperties,
			List dependencies, IProgressMonitor monitor) throws CoreException {
		monitor.setTaskName("Creating "
				+ ArtifactMetadataFactory.INSTANCE.getMetadata(
						IDependencyArtifactImpl.class.getName()).getLabel()
				+ "(s).");
		runGenerator(DEPENDENCY_TEMPLATE, pageProperties, dependencies, monitor);
	}

	private void runGenerator(String template, Properties pageProperties,
			List elements, IProgressMonitor monitor) throws CoreException {

		// TODO: the parameter passing through Properties is super-messy
		// needs to be re-done!
		String containerLocation = pageProperties
				.getProperty(NewArtifactWizardPage.CONTAINER_NAME);
		String srcDirectory = pageProperties
				.getProperty(NewArtifactWizardPage.SRCDIRECTORY_NAME);
		String projectName = pageProperties
				.getProperty(NewArtifactWizardPage.CONTAINER_PATH);

		for (Iterator iter = elements.iterator(); iter.hasNext();) {
			AnnotatedElement elem = (AnnotatedElement) iter.next();

			String packageName = elem.getPackageName();
			String elementName = elem.getName();

			final Properties mergedProperties = elem
					.mergeProperties(pageProperties);

			monitor.beginTask("Creating " + elem.getFullyQualifiedName(), 2);
			String dir = containerLocation + File.separator + projectName
					+ File.separator + srcDirectory + File.separator
					+ packageName.replace('.', File.separatorChar);
			IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace()
					.getRoot();
			IProject project = workspaceRoot.getProject(projectName);

			IFolder srcFolder = project.getFolder(srcDirectory);
			// + File.separator
			// + packageName.replace('.', File.separatorChar));

			createFolder(project, srcFolder, packageName);

			final IFile file = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(
							new Path(projectName
									+ File.separator
									+ srcDirectory
									+ File.separator
									+ packageName.replace('.',
											File.separatorChar)
									+ File.separator + elementName + ".java"));
			try {
				InputStream stream = openContentStream(template,
						mergedProperties);
				if (file.exists()) {
					file.setContents(stream, true, true, monitor);
				} else {
					file.create(stream, true, monitor);
				}
				stream.close();
			} catch (IOException e) {
				TigerstripeRuntime.logErrorMessage("IOException detected", e);
			} catch (CoreException e) {
				TigerstripeRuntime.logErrorMessage("CoreException detected", e);
			}
		}
		monitor.worked(1);
	}

	private void createFolder(IProject project, IFolder folder, String pack) {
		if ("".equals(pack))
			return;
		if (pack.indexOf(".") == -1) {
			IFolder packFolder = folder.getFolder(pack);
			try {
				if (!packFolder.exists()) {
					packFolder.create(true, false, null);
				}
			} catch (CoreException e) {
				TigerstripeRuntime.logErrorMessage("CoreException detected", e);
			}
			return;
		}

		String[] parts = pack.split("\\.");
		IFolder fol = folder;
		for (int i = 0; i < parts.length; i++) {
			IFolder currentFolder = fol.getFolder(parts[i]);
			if (!currentFolder.exists()) {
				try {
					if (!currentFolder.exists()) {
						currentFolder.create(IResource.NONE, true, null);
					}
				} catch (CoreException e) {
					TigerstripeRuntime.logErrorMessage(
							"CoreException detected", e);
					return;
				}
			}
			fol = currentFolder;
		}
	}

	/**
	 * We will initialize file contents with a sample text.
	 * 
	 * @param pageProperties -
	 *            the properties gathered through the wizard
	 */
	protected InputStream openContentStream(String template,
			Properties pageProperties) {

		byte[] bytes = null;
		buffer = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				buffer));

		ArtifactDefinitionGenerator generator = new AnnotatedElementGenerator(
				template, pageProperties, writer);
		generator.applyTemplate();
		bytes = buffer.toByteArray();
		return new ByteArrayInputStream(bytes);
	}

}