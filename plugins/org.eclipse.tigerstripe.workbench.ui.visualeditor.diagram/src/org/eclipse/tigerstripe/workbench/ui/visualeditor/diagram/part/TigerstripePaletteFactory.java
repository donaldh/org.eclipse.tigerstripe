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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.gef.Tool;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest.ViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeConnectionTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeCreationTool;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.tigerstripe.metamodel.impl.IFieldImpl;
import org.eclipse.tigerstripe.metamodel.impl.ILiteralImpl;
import org.eclipse.tigerstripe.metamodel.impl.IMethodImpl;
import org.eclipse.tigerstripe.repository.internal.ArtifactMetadataFactory;
import org.eclipse.tigerstripe.repository.internal.IModelComponentMetadata;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.NodePattern;
import org.eclipse.tigerstripe.workbench.internal.api.patterns.PatternFactory;
import org.eclipse.tigerstripe.workbench.internal.core.model.ComponentNameProvider;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPattern;
import org.eclipse.tigerstripe.workbench.patterns.IArtifactPatternResult;
import org.eclipse.tigerstripe.workbench.patterns.IEnumPattern;
import org.eclipse.tigerstripe.workbench.patterns.INodePattern;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.patterns.IPatternBasedCreationValidator;
import org.eclipse.tigerstripe.workbench.patterns.IQueryPattern;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ElementTypeMapper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.providers.TigerstripeElementTypes;

/**
 * @generated
 */
public class TigerstripePaletteFactory {

	/**
	 * 
	 */
	public void fillPalette(PaletteRoot paletteRoot) {
		paletteRoot.add(createArtifacts1Group());
		paletteRoot.add(createFeatures2Group());
		/*
		 * Remove the connection group from the palette (so that users can only
		 * create connections using handles on objects). This will give us more
		 * control over the types of connections that can be made between
		 * artifacts.
		 */
		// paletteRoot.add(createConnections3Group());
	}

	/**
	 * 
	 */
	private PaletteContainer createArtifacts1Group() {
		PaletteContainer paletteContainer = new PaletteDrawer("Artifacts");
		paletteContainer.setDescription("Tigerstripe Artifact Palette");
		/*
		 * based on the profile being used, add the appropriate artifacts to the
		 * palette (only those artifacts that should be in the model for a given
		 * profile)
		 */
		for (String patternName : PatternFactory.getInstance().getRegisteredPatterns().keySet()){
			
			IPattern pattern = PatternFactory.getInstance().getPattern(patternName);
			if (pattern instanceof NodePattern){
				// TODO remove package from the palette as we don't yet support this 
				
				if (! ((IArtifactPattern) pattern).getTargetArtifactType().equals(IPackageArtifact.class.getName())){
					paletteContainer.add(createPatternBasedCreationTool((NodePattern)pattern));
				}
			}

			
		}
		

		return paletteContainer;
	}

	/**
	 * @generated
	 */
	private PaletteContainer createFeatures2Group() {
		PaletteContainer paletteContainer = new PaletteDrawer("Features");
		paletteContainer.add(createAttribute1CreationTool());
		paletteContainer.add(createMethod2CreationTool());
		paletteContainer.add(createLiteral3CreationTool());
		return paletteContainer;
	}



	/**
	 * @generated NOT
	 */
	private ToolEntry createPatternBasedCreationTool(NodePattern pattern) {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = pattern.getImageDescriptor();
		largeImage = pattern.getImageDescriptor();

		final List<IElementType> elementTypes = new ArrayList<IElementType>();
		elementTypes.add(ElementTypeMapper.mapClassStringToElementType(pattern.getTargetArtifactType()));
		ToolEntry result = new PatternBasedNodeToolEntry(pattern.getUILabel(),
				pattern.getDescription(), smallImage,
				largeImage, elementTypes,pattern);

		return result;
	}
	


	/**
	 * @generated NOT
	 */
	private ToolEntry createAttribute1CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = Images.getDescriptor(Images.FIELD_ICON);
		largeImage = Images.getDescriptor(Images.FIELD_ICON);

		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
				.getMetadata(IFieldImpl.class.getName());

		final List<IElementType> elementTypes = new ArrayList<IElementType>();
		elementTypes.add(TigerstripeElementTypes.Attribute_2001);
		elementTypes.add(TigerstripeElementTypes.Attribute_2002);
		elementTypes.add(TigerstripeElementTypes.Attribute_2003);
		elementTypes.add(TigerstripeElementTypes.Attribute_2005);
		elementTypes.add(TigerstripeElementTypes.Attribute_2006);
		elementTypes.add(TigerstripeElementTypes.Attribute_2009);
		elementTypes.add(TigerstripeElementTypes.Attribute_2011);
		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
				"Create new " + metadata.getLabel(null), smallImage,
				largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated NOT
	 */
	private ToolEntry createMethod2CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = Images.getDescriptor(Images.METHOD_ICON);
		largeImage = Images.getDescriptor(Images.METHOD_ICON);

		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
				.getMetadata(IMethodImpl.class.getName());

		final List<IElementType> elementTypes = new ArrayList<IElementType>();
		elementTypes.add(TigerstripeElementTypes.Method_2004);
		elementTypes.add(TigerstripeElementTypes.Method_2007);
		elementTypes.add(TigerstripeElementTypes.Method_2010);
		elementTypes.add(TigerstripeElementTypes.Method_2012);
		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
				"Create new " + metadata.getLabel(null), smallImage,
				largeImage, elementTypes);

		return result;
	}

	/**
	 * @generated NOT
	 */
	private ToolEntry createLiteral3CreationTool() {
		ImageDescriptor smallImage;
		ImageDescriptor largeImage;

		smallImage = Images.getDescriptor(Images.LITERAL_ICON);
		largeImage = Images.getDescriptor(Images.LITERAL_ICON);

		IModelComponentMetadata metadata = ArtifactMetadataFactory.INSTANCE
				.getMetadata(ILiteralImpl.class.getName());

		final List<IElementType> elementTypes = new ArrayList<IElementType>();
		elementTypes.add(TigerstripeElementTypes.Literal_2008);
		ToolEntry result = new NodeToolEntry(metadata.getLabel(null),
				"Create new " + metadata.getLabel(null), smallImage,
				largeImage, elementTypes);

		return result;
	}



	/**
	 * @generated
	 */
	private static class NodeToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List elementTypes;

		/**
		 * @generated
		 */
		private NodeToolEntry(String title, String description,
				ImageDescriptor smallIcon, ImageDescriptor largeIcon,
				List elementTypes) {
			super(title, description, smallIcon, largeIcon);
			this.elementTypes = elementTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeCreationTool(elementTypes) {

				@Override
				protected void performCreation(int button) {
					// TODO Auto-generated method stub
					super.performCreation(button);
				}

			};
			tool.setProperties(getToolProperties());
			return tool;
		}
	}
	
	/**
	 * @generated NOT
	 */
	private static class PatternBasedNodeToolEntry extends ToolEntry {

		/**
		 * @generated NOT
		 */
		private final List elementTypes;
		private final IElementType elementType;
		private final IPattern pattern;

		/**
		 * @generated NOT
		 */
		private PatternBasedNodeToolEntry(String title, String description,
				ImageDescriptor smallIcon, ImageDescriptor largeIcon,
				List elementTypes, IPattern pattern) {
			super(title, description, smallIcon, largeIcon);
			this.elementTypes = elementTypes;
			this.pattern = pattern;
			this.elementType = (IElementType) elementTypes.get(0);
		}

		
		private boolean ok = true;
		private String failureString = "Unspecified Error";
		
		/**
		 * @generated NOT
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeCreationTool(elementTypes) {

				@Override
				protected void performCreation(int button) {

					final IPatternBasedCreationValidator validator = pattern.getValidator();
					if (validator != null){
						// TODO These bits are worked out from the created EObject.
						CreateUnspecifiedTypeRequest request = (CreateUnspecifiedTypeRequest) getCreateRequest();
						CreateViewAndElementRequest cver = (CreateViewAndElementRequest) request.getRequestForType(elementType);
						ViewAndElementDescriptor desc = cver.getViewAndElementDescriptor();
						CreateElementRequestAdapter adapter = (CreateElementRequestAdapter) desc.getElementAdapter();
						CreateElementRequest req = (CreateElementRequest) adapter.getAdapter(CreateElementRequest.class);

						Map map = (Map) req.getContainer();
						final ITigerstripeModelProject modelproject = map.getCorrespondingITigerstripeProject();
						final String packageName = map.getBasePackage();
						final String artifactName = getArtifactName((IArtifactPattern) pattern,((IArtifactPattern) pattern).getTargetArtifactType(),modelproject,packageName);
						
						final String extendedArtifactFQN = ((IArtifactPattern) pattern).getExtendedArtifactName();
						SafeRunner.run(new ISafeRunnable() {
							public void handleException(Throwable exception) {
								BasePlugin.log(exception);
							}

							public void run() throws Exception {
								ok = validator.okToCreate(modelproject,(INodePattern) pattern,
										packageName,artifactName,extendedArtifactFQN);
								failureString = validator.getCreateFailureMessage();
							}
						});
					}
					if (!ok){
						MessageDialog.openError(null, "Unable to Create Artifact", failureString);
					} else {
						super.performCreation(button);


						Object o = getCreateRequest().getNewObject();
						if (o instanceof Collection<?>) {
							Collection<?> c = (Collection<?>) o;
							Object e = c.iterator().next();
							if (e instanceof ViewAndElementDescriptor) {
								ViewAndElementDescriptor desc = (ViewAndElementDescriptor) e;
								Node node = (Node) desc.getAdapter(View.class);
								AbstractArtifact createdEArtifact = (AbstractArtifact) node
								.getElement();

								Map map = (Map) createdEArtifact.eContainer();
								ITigerstripeModelProject project = map.getCorrespondingITigerstripeProject();

								try {
									//IAbstractArtifact createdIArtifact = createdEArtifact
									//		.getCorrespondingIArtifact();
									//System.out.println("Just created: "
									//		+ createdIArtifact);


									if (pattern instanceof IEnumPattern){
										IEnumPattern enumPattern =(IEnumPattern) pattern;
										IArtifactPatternResult artifact = enumPattern.createArtifact(
												project, 
												createdEArtifact.getPackage(), 
												createdEArtifact.getName(), enumPattern.getExtendedArtifactName(),
												enumPattern.getBaseType());
										enumPattern.addToManager(project, artifact.getArtifact());
										enumPattern.annotateArtifact(project, artifact);
									} else if (pattern instanceof IQueryPattern){
										IQueryPattern queryPattern =(IQueryPattern) pattern;
										IArtifactPatternResult artifact = queryPattern.createArtifact(
												project, 
												createdEArtifact.getPackage(), 
												createdEArtifact.getName(), queryPattern.getExtendedArtifactName(),
												queryPattern.getReturnType());
										queryPattern.addToManager(project, artifact.getArtifact());
										queryPattern.annotateArtifact(project, artifact);
									} else {
										IArtifactPattern artifactPattern =(IArtifactPattern) pattern;
										IArtifactPatternResult artifact = artifactPattern.createArtifact(
												project, 
												createdEArtifact.getPackage(), 
												createdEArtifact.getName(), artifactPattern.getExtendedArtifactName());
										artifactPattern.addToManager(project, artifact.getArtifact());
										artifactPattern.annotateArtifact(project, artifact);
									}

								} catch (TigerstripeException ex) {
									ex.printStackTrace();
								}
							}
						}
						//System.out.println("Created Object="
						//		+ getCreateRequest().getNewObject());
					}
				}

			};
			tool.setProperties(getToolProperties());
			return tool;
		}
	}

	/**
	 * @generated
	 */
	private static class LinkToolEntry extends ToolEntry {

		/**
		 * @generated
		 */
		private final List relationshipTypes;

		/**
		 * @generated
		 */
		private LinkToolEntry(String title, String description,
				ImageDescriptor smallIcon, ImageDescriptor largeIcon,
				List relationshipTypes) {
			super(title, description, smallIcon, largeIcon);
			this.relationshipTypes = relationshipTypes;
		}

		/**
		 * @generated
		 */
		public Tool createTool() {
			Tool tool = new UnspecifiedTypeConnectionTool(relationshipTypes);
			tool.setProperties(getToolProperties());
			return tool;
		}
	}
	
	public static String getArtifactName(IArtifactPattern pattern, Class clazz, ITigerstripeModelProject project,
			String packageName){
		String name = "";
		// If we move the name provider to the pattern then we only need change this line!
		
		ComponentNameProvider nameFactory = ComponentNameProvider.getInstance();	
		return nameFactory.getNewArtifactName(pattern,
				clazz, project, packageName);
	}
	
	public static String getArtifactName(IArtifactPattern pattern, String  className, ITigerstripeModelProject project,
			String packageName){
		String artifactName = "";
		if (className.equals(IManagedEntityArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IManagedEntityArtifact.class, project,packageName); 
		} else if (className.equals(IDatatypeArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IDatatypeArtifact.class,project,packageName);
		} else if (className.equals(IEnumArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IEnumArtifact.class,project,packageName);

		} else if (className.equals(IQueryArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IQueryArtifact.class,project,packageName);
		} else if (className.equals(IUpdateProcedureArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IUpdateProcedureArtifact.class, project,packageName);
		} else if (className.equals(ISessionArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					ISessionArtifact.class,	project,packageName);
		} else if (className.equals(IExceptionArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IExceptionArtifact.class, project,packageName);
		} else if (className.equals(IEventArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IEventArtifact.class, project,packageName);

		} else if (className.equals(IDependencyArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IDependencyArtifact.class, project,packageName);
		} else if (className.equals(IAssociationClassArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IAssociationClassArtifact.class, project,packageName);
		} else if (className.equals(IAssociationArtifact.class.getName())){
			artifactName = getArtifactName(pattern,
					IAssociationArtifact.class, project,packageName);
		}
			
		return artifactName;
		
	}
}
