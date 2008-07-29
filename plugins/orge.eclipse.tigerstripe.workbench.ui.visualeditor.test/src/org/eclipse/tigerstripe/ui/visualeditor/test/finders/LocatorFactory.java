package org.eclipse.tigerstripe.ui.visualeditor.test.finders;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimatableScrollPane;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute3EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.LiteralEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MethodEditPart;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.gef.IFigureReference;
import com.windowtester.runtime.gef.locator.FigureClassLocator;
import com.windowtester.runtime.locator.IWidgetLocator;

public class LocatorFactory {

	private static IUIContext ui;
	public static void setUi(IUIContext ui) {
		LocatorFactory.ui = ui;
	}

	private static LocatorFactory instance;
	
	public IUIContext getUi() {
		return ui;
	}

	public LocatorFactory() {
	}
	
	public static LocatorFactory getInstance(){
		if (instance == null){
			instance = new LocatorFactory();
		}
		return instance;
	}

	public String getFieldString(IUIContext ui, String artifactName, String fieldName) {
		
//		IWidgetLocator[] allMatches = ui.findAll(new FigureClassLocator("org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart$1"));
//		for (IWidgetLocator match : allMatches) {
//		  new com.windowtester.runtime.gef.internal.helpers.GEFDebugHelper().printFigures(((IFigureReference)match).getFigure());
//		}
	
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactEditPart$ManagedEntityArtifactFigure"));
		for (IWidgetLocator match : matches) {
			ManagedEntityArtifactEditPart.ManagedEntityArtifactFigure attrib = (ManagedEntityArtifactEditPart.ManagedEntityArtifactFigure) ((IFigureReference) match).getFigure();
			if (attrib.getFigureManagedEntityNameFigure().getText().equals(artifactName)){
				List children = attrib.getChildren();
				for (Object child : children){
//					System.out.println(child.getClass().getName());
					if (child instanceof ResizableCompartmentFigure){
						ResizableCompartmentFigure rcf = (ResizableCompartmentFigure) child;
						List rcfChildren = rcf.getChildren();
						for (Object rcfChild : rcfChildren){
//							System.out.println("    "+rcfChild.getClass().getName());
							if (rcfChild instanceof AnimatableScrollPane){
								AnimatableScrollPane asp = (AnimatableScrollPane) rcfChild;
								List aspChildren = asp.getChildren();
								for (Object aspChild : aspChildren){
//									System.out.println("        "+aspChild.getClass().getName());
									if (aspChild instanceof Viewport){
										Viewport vp = (Viewport) aspChild;
										List vpChildren = vp.getChildren();
										for (Object vpChild : vpChildren){
//											System.out.println("            "+vpChild.getClass().getName());
											if (vpChild instanceof Figure){
												Figure fig = (Figure) vpChild;
												List figChildren = fig.getChildren();
												for (Object figChild : figChildren){
//													System.out.println("                "+figChild.getClass().getName());
													if (figChild instanceof Attribute3EditPart.AttributeLabelFigure ){
														Attribute3EditPart.AttributeLabelFigure attribPart  = (Attribute3EditPart.AttributeLabelFigure) figChild;												
														String matchPattern = "^[+-]"+fieldName+":.*";
//														System.out.println("                "+attribPart.getText()+" "+matchPattern);
														if (attribPart.getText().matches(matchPattern)){
															return attribPart.getText();
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public String getMethodString(IUIContext ui, String artifactName, String methodName) {
		
//		IWidgetLocator[] allMatches = ui.findAll(new FigureClassLocator("org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart$1"));
//		for (IWidgetLocator match : allMatches) {
//		  new com.windowtester.runtime.gef.internal.helpers.GEFDebugHelper().printFigures(((IFigureReference)match).getFigure());
//		}
	
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactEditPart$ManagedEntityArtifactFigure"));
		for (IWidgetLocator match : matches) {
			ManagedEntityArtifactEditPart.ManagedEntityArtifactFigure attrib = (ManagedEntityArtifactEditPart.ManagedEntityArtifactFigure) ((IFigureReference) match).getFigure();
			if (attrib.getFigureManagedEntityNameFigure().getText().equals(artifactName)){
				List children = attrib.getChildren();
				for (Object child : children){
//					System.out.println(child.getClass().getName());
					if (child instanceof ResizableCompartmentFigure){
						ResizableCompartmentFigure rcf = (ResizableCompartmentFigure) child;
						List rcfChildren = rcf.getChildren();
						for (Object rcfChild : rcfChildren){
//							System.out.println("    "+rcfChild.getClass().getName());
							if (rcfChild instanceof AnimatableScrollPane){
								AnimatableScrollPane asp = (AnimatableScrollPane) rcfChild;
								List aspChildren = asp.getChildren();
								for (Object aspChild : aspChildren){
//									System.out.println("        "+aspChild.getClass().getName());
									if (aspChild instanceof Viewport){
										Viewport vp = (Viewport) aspChild;
										List vpChildren = vp.getChildren();
										for (Object vpChild : vpChildren){
//											System.out.println("            "+vpChild.getClass().getName());
											if (vpChild instanceof Figure){
												Figure fig = (Figure) vpChild;
												List figChildren = fig.getChildren();
												for (Object figChild : figChildren){
//													System.out.println("                "+figChild.getClass().getName());
													if (figChild instanceof MethodEditPart.MethodLabelFigure ){
														MethodEditPart.MethodLabelFigure attribPart  = (MethodEditPart.MethodLabelFigure) figChild;												
														String matchPattern = "^[+-]"+methodName+"\\(\\):.*";
//														System.out.println("                "+attribPart.getText()+" "+matchPattern);
														if (attribPart.getText().matches(matchPattern)){
															return attribPart.getText();
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	
public String getLiteralString(IUIContext ui, String enumerationName, String literalName) {
		
//		IWidgetLocator[] allMatches = ui.findAll(new FigureClassLocator("org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart$1"));
//		for (IWidgetLocator match : allMatches) {
//		  new com.windowtester.runtime.gef.internal.helpers.GEFDebugHelper().printFigures(((IFigureReference)match).getFigure());
//		}
	
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationEditPart$EnumerationFigure"));
		for (IWidgetLocator match : matches) {
			EnumerationEditPart.EnumerationFigure attrib = (EnumerationEditPart.EnumerationFigure) ((IFigureReference) match).getFigure();
			if (attrib.getFigureEnumerationNameFigure().getText().equals(enumerationName)){
				List children = attrib.getChildren();
				for (Object child : children){
//					System.out.println(child.getClass().getName());
					if (child instanceof ResizableCompartmentFigure){
						ResizableCompartmentFigure rcf = (ResizableCompartmentFigure) child;
						List rcfChildren = rcf.getChildren();
						for (Object rcfChild : rcfChildren){
//							System.out.println("    "+rcfChild.getClass().getName());
							if (rcfChild instanceof AnimatableScrollPane){
								AnimatableScrollPane asp = (AnimatableScrollPane) rcfChild;
								List aspChildren = asp.getChildren();
								for (Object aspChild : aspChildren){
//									System.out.println("        "+aspChild.getClass().getName());
									if (aspChild instanceof Viewport){
										Viewport vp = (Viewport) aspChild;
										List vpChildren = vp.getChildren();
										for (Object vpChild : vpChildren){
//											System.out.println("            "+vpChild.getClass().getName());
											if (vpChild instanceof Figure){
												Figure fig = (Figure) vpChild;
												List figChildren = fig.getChildren();
												for (Object figChild : figChildren){
//													System.out.println("                "+figChild.getClass().getName());
													if (figChild instanceof LiteralEditPart.LiteralLabelFigure ){
														LiteralEditPart.LiteralLabelFigure attribPart  = (LiteralEditPart.LiteralLabelFigure) figChild;												
														String matchPattern = "^[+-]"+literalName+"=.*";
//														System.out.println("                "+attribPart.getText()+" "+matchPattern);
														if (attribPart.getText().matches(matchPattern)){
															return attribPart.getText();
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	



	public IWidgetLocator getAttributeLocator(IUIContext ui, String attrributeName){
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute3EditPart$AttributeLabelFigure"));
		for (IWidgetLocator match : matches) {
			Attribute3EditPart.AttributeLabelFigure attrib = (Attribute3EditPart.AttributeLabelFigure) ((IFigureReference) match).getFigure();
			if (attrib.getText().equals(attrributeName)){
				return (IWidgetLocator) match;
			}
		}
		return null;
	}

	
	public IWidgetLocator getManagedEntityLocator(IUIContext ui, String nameToFind) {
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactEditPart$ManagedEntityArtifactFigure"));
		for (IWidgetLocator match : matches) {
			ManagedEntityArtifactEditPart.ManagedEntityArtifactFigure attrib = (ManagedEntityArtifactEditPart.ManagedEntityArtifactFigure) ((IFigureReference) match).getFigure();
			if (attrib.getFigureManagedEntityNameFigure().getText().equals(nameToFind)){
				return (IWidgetLocator) match;
			}
		}
		return null;
	}
	
	
	
	public IWidgetLocator getEnumerationLocator(IUIContext ui, String nameToFind) {
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationEditPart$EnumerationFigure"));
		for (IWidgetLocator match : matches) {
			EnumerationEditPart.EnumerationFigure attrib = (EnumerationEditPart.EnumerationFigure) ((IFigureReference) match).getFigure();
			if (attrib.getFigureEnumerationNameFigure().getText().equals(nameToFind)){
				return (IWidgetLocator) match;
			}
		}
		return null;
	}
	
}
