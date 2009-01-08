package org.eclipse.tigerstripe.ui.visualeditor.test.finders;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimatableScrollPane;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassClassNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute3EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ExceptionArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.LiteralEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MethodEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamedQueryArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NotificationArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassEditPart.AssociationClassFigure;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationEditPart.AssociationFigure;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationNamePackageEditPart.AssociationNameFigure;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyEditPart.DependencyConnectionFigure;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyNamePackageEditPart.DependencyNameFigure;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.gef.IFigureReference;
import com.windowtester.runtime.gef.locator.FigureClassLocator;
import com.windowtester.runtime.gef.locator.NamedEditPartFigureLocator;
import com.windowtester.runtime.locator.IWidgetLocator;

public class LocatorHelper {

	private static IUIContext ui;

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
	
	public MethodEditPart.MethodLabelFigure getMethodLocator(ManagedEntityArtifactEditPart.ManagedEntityArtifactFigure managedEntityFigure){
		List children = managedEntityFigure.getChildren();
		for (Object child : children){
					System.out.println(child.getClass().getName());
			if (child instanceof ResizableCompartmentFigure){
				ResizableCompartmentFigure rcf = (ResizableCompartmentFigure) child;
				List rcfChildren = rcf.getChildren();
				for (Object rcfChild : rcfChildren){
									System.out.println("RCF    "+rcfChild.getClass().getName());
					if (rcfChild instanceof AnimatableScrollPane){
						AnimatableScrollPane asp = (AnimatableScrollPane) rcfChild;
						List aspChildren = asp.getChildren();
						for (Object aspChild : aspChildren){
													System.out.println("ASP        "+aspChild.getClass().getName());
							if (aspChild instanceof Viewport){
								Viewport vp = (Viewport) aspChild;
								List vpChildren = vp.getChildren();
								for (Object vpChild : vpChildren){
																	System.out.println("VP            "+vpChild.getClass().getName());
									if (vpChild instanceof Figure){
										Figure fig = (Figure) vpChild;
										List figChildren = fig.getChildren();
										for (Object figChild : figChildren){
																					System.out.println("FIG                "+figChild.getClass().getName());
											if (figChild instanceof MethodEditPart.MethodLabelFigure ){
												System.out.println("GOT one");
												MethodEditPart.MethodLabelFigure methodPart  = (MethodEditPart.MethodLabelFigure) figChild;														
													return methodPart;
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
			System.out.println(match);
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
	
	public IWidgetLocator getDatatypeLocator(IUIContext ui, String nameToFind) {
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactEditPart$DatatypeArtifactFigure"));
		for (IWidgetLocator match : matches) {
			DatatypeArtifactEditPart.DatatypeArtifactFigure attrib = (DatatypeArtifactEditPart.DatatypeArtifactFigure) ((IFigureReference) match).getFigure();
			if (attrib.getFigureDatatypeNameFigure().getText().equals(nameToFind)){
				return (IWidgetLocator) match;
			}
		}
		return null;
	}
	
	public IWidgetLocator getQueryLocator(IUIContext ui, String nameToFind) {
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamedQueryArtifactEditPart$NamedQueryArtifactFigure"));
		for (IWidgetLocator match : matches) {
			NamedQueryArtifactEditPart.NamedQueryArtifactFigure attrib = (NamedQueryArtifactEditPart.NamedQueryArtifactFigure) ((IFigureReference) match).getFigure();
			if (attrib.getFigureNamedQueryNameFigure().getText().equals(nameToFind)){
				return (IWidgetLocator) match;
			}
		}
		return null;
	}
	
	public IWidgetLocator getUpdateProcedureLocator(IUIContext ui, String nameToFind) {
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactEditPart$UpdateProcedureArtifactFigure"));
		for (IWidgetLocator match : matches) {
			UpdateProcedureArtifactEditPart.UpdateProcedureArtifactFigure attrib = (UpdateProcedureArtifactEditPart.UpdateProcedureArtifactFigure) ((IFigureReference) match).getFigure();
			if (attrib.getFigureUpdateProcedureNameFigure().getText().equals(nameToFind)){
				return (IWidgetLocator) match;
			}
		}
		return null;
	}
	
	public IWidgetLocator getExceptionLocator(IUIContext ui, String nameToFind) {
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ExceptionArtifactEditPart$ExceptionArtifactFigure"));
		for (IWidgetLocator match : matches) {
			ExceptionArtifactEditPart.ExceptionArtifactFigure attrib = (ExceptionArtifactEditPart.ExceptionArtifactFigure) ((IFigureReference) match).getFigure();
			if (attrib.getFigureExceptionNameFigure().getText().equals(nameToFind)){
				return (IWidgetLocator) match;
			}
		}
		return null;
	}
	
	public IWidgetLocator getNotificationLocator(IUIContext ui, String nameToFind) {
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NotificationArtifactEditPart$NotificationArtifactFigure"));
		for (IWidgetLocator match : matches) {
			NotificationArtifactEditPart.NotificationArtifactFigure attrib = (NotificationArtifactEditPart.NotificationArtifactFigure) ((IFigureReference) match).getFigure();
			if (attrib.getFigureNotificationNameFigure().getText().equals(nameToFind)){
				return (IWidgetLocator) match;
			}
		}
		return null;
	}
	
	public IWidgetLocator getSessionFacadeLocator(IUIContext ui, String nameToFind) {
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactEditPart$SessionFacadeArtifactFigure"));
		for (IWidgetLocator match : matches) {
			SessionFacadeArtifactEditPart.SessionFacadeArtifactFigure attrib = (SessionFacadeArtifactEditPart.SessionFacadeArtifactFigure) ((IFigureReference) match).getFigure();
			if (attrib.getFigureSessionFacadeNameFigure().getText().equals(nameToFind)){
				return (IWidgetLocator) match;
			}
		}
		return null;
	}
	
	public IWidgetLocator getManagedEntityMethodCompartmentLocator(IUIContext ui, String nameToFind) {
		
		IWidgetLocator[] matches = ui.findAll(new NamedEditPartFigureLocator("MethodCompartment"));
		for (IWidgetLocator match : matches) {
			org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactMethodCompartmentEditPart artifactFigure = (org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactMethodCompartmentEditPart) ((IFigureReference) match).getEditPart();
			System.out.println("hello");
		}
		System.out.println("Didn't find Method Compartment part");
		return null;
	}
	
	public IWidgetLocator getManagedEntityAttributeCompartmentLocator(IUIContext ui, String nameToFind) {
		
		IWidgetLocator[] matches = ui.findAll(new NamedEditPartFigureLocator("AttributeCompartment"));
		for (IWidgetLocator match : matches) {
			org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactMethodCompartmentEditPart artifactFigure = (org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactMethodCompartmentEditPart) ((IFigureReference) match).getEditPart();
			System.out.println("hello");
		}
		System.out.println("Didn't find Method Compartment part");
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

	public IWidgetLocator getAssociationFigureLocator(IUIContext ui, String name) {
			IWidgetLocator[] allAssocs = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationEditPart$AssociationFigure"));
			for (IWidgetLocator assoc : allAssocs){
				AssociationEditPart assocEP = (AssociationEditPart) ((AssociationFigure) ((IFigureReference) assoc).getFigure()).getParentEditPart();
				if (assocEP != null){
					AssociationNamePackageEditPart ep = (AssociationNamePackageEditPart) assocEP.getPrimaryChildEditPart();
					AssociationNameFigure anFig = (AssociationNameFigure) ep.getFigure();
					if (anFig.getText().equals(name)){
						return assoc;
					}
				}
			}
			return null;
	}
	
	public IWidgetLocator getDependencyFigureLocator(IUIContext ui, String name) {
		IWidgetLocator[] allAssocs = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyEditPart$DependencyConnectionFigure"));
		for (IWidgetLocator assoc : allAssocs){
			DependencyEditPart assocEP = (DependencyEditPart) ((DependencyConnectionFigure) ((IFigureReference) assoc).getFigure()).getParentEditPart();
			if (assocEP != null){
				DependencyNamePackageEditPart ep = (DependencyNamePackageEditPart) assocEP.getPrimaryChildEditPart();
				DependencyNameFigure anFig = (DependencyNameFigure) ep.getFigure();
				if (anFig.getText().equals(name)){
					return assoc;
				}
			}
		}
		return null;
	}
	
	public IWidgetLocator getAssociationClassFigureLocator(IUIContext ui, String name) {
		IWidgetLocator[] allAssocs = ui.findAll(new FigureClassLocator("org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassEditPart$AssociationClassFigure"));
		for (IWidgetLocator assoc : allAssocs){
			AssociationClassEditPart assocEP = (AssociationClassEditPart) ((AssociationClassFigure) ((IFigureReference) assoc).getFigure()).getParentEditPart();
			if (assocEP != null){
				AssociationClassClassNamePackageEditPart ep = (AssociationClassClassNamePackageEditPart) assocEP.getPrimaryChildEditPart();
				
				WrapLabel anFig =  (WrapLabel) ep.getFigure();
				if (anFig.getText().equals(name)){
					return assoc;
				}
			}
		}
		return null;
	}
	
	
}
