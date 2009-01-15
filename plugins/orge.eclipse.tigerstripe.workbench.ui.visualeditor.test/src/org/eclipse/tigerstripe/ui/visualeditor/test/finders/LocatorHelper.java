package org.eclipse.tigerstripe.ui.visualeditor.test.finders;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.AnimatableScrollPane;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassClassNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute3EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.Attribute5EditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactAttributeCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationLiteralCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationNamePackageEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ExceptionArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.LiteralEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactAttributeCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactMethodCompartmentEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactNamePackageEditPart;
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
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.WidgetReference;

public class LocatorHelper {


	/**
	 * Find an attribute within a named ManagedEntity.
	 * Just look for the attribute Name - ignore the type
	 * You should be able to derive the type from the returned object if you want 
	 * 
	 */
	public IWidgetLocator getManagedEntityAttributeLocator(IUIContext ui, String artifactName, String attributeName){
		IWidgetLocator comp = getManagedEntityAttributeCompartmentLocator(ui, artifactName);
		
		List figChildren = getUsefulChildren(comp);
		for (Object figChild : figChildren){
			if (figChild instanceof Attribute3EditPart.AttributeLabelFigure ){
				Attribute3EditPart.AttributeLabelFigure attribFigure  = (Attribute3EditPart.AttributeLabelFigure) figChild;												
				String matchPattern = "^[+-]"+attributeName+":.*";
				if (attribFigure.getText().matches(matchPattern)){
					return new WidgetReference(attribFigure);
				}
			}
		}
		return null;
	}
	
	/**
	 * Find an method within a named ManagedEntity.
	 * Just look for the method Name - ignore the type
	 * You should be able to derive the type from the returned object if you want 
	 * 
	 */
	public IWidgetLocator getManagedEntityMethodLocator(IUIContext ui, String artifactName, String methodName){
		IWidgetLocator comp = getManagedEntityMethodCompartmentLocator(ui, artifactName);
		
		List figChildren = getUsefulChildren(comp);
		for (Object figChild : figChildren){
			if (figChild instanceof MethodEditPart.MethodLabelFigure ){
				MethodEditPart.MethodLabelFigure methodFigure  = (MethodEditPart.MethodLabelFigure) figChild;												
				String matchPattern = "^[+-]"+methodName+"\\(.*\\):.*";
				if (methodFigure.getText().matches(matchPattern)){
					return new WidgetReference(methodFigure);
				}
			}
		}
		return null;
	}

	private List getUsefulChildren(IWidgetLocator comp){
		ResizableCompartmentFigure rcf = (ResizableCompartmentFigure) ((IFigureReference) comp).getFigure();
		List rcfChildren = rcf.getChildren();
		for (Object rcfChild : rcfChildren){
			if (rcfChild instanceof AnimatableScrollPane){
				AnimatableScrollPane asp = (AnimatableScrollPane) rcfChild;
				List aspChildren = asp.getChildren();
				for (Object aspChild : aspChildren){
					if (aspChild instanceof Viewport){
						Viewport vp = (Viewport) aspChild;
						List vpChildren = vp.getChildren();
						for (Object vpChild : vpChildren){
							if (vpChild instanceof Figure){
								Figure fig = (Figure) vpChild;
								List figChildren = fig.getChildren();
								return figChildren;
							}
						}
					}
				}
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
	
	public IWidgetLocator getDatatypeAttributeCompartmentLocator(IUIContext ui, String nameToFind) {
		
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure"));
		for (IWidgetLocator match : matches) {
			EditPart editPart =  ((IFigureReference) match).getEditPart();
			if (editPart instanceof DatatypeArtifactAttributeCompartmentEditPart){
				DatatypeArtifactAttributeCompartmentEditPart meAttributes = (DatatypeArtifactAttributeCompartmentEditPart) editPart;
				DatatypeArtifactEditPart meA =(DatatypeArtifactEditPart) meAttributes.getParent();
				DatatypeArtifactNamePackageEditPart nameEP = (DatatypeArtifactNamePackageEditPart) meA.getPrimaryChildEditPart();
				if (((WrapLabel) nameEP.getFigure()).getText().equals(nameToFind)){
					return (IWidgetLocator) match;
				}
			}
		}
		System.out.println("Didn't find Attribute Compartment part");
		return null;
	}
	
	
	/**
	 * Find an attribute within a named Datatype.
	 * Just look for the attribute Name - ignore the type
	 * You should be able to derive the type from the returned object if you want 
	 * 
	 */
	public IWidgetLocator getDatatypeAttributeLocator(IUIContext ui, String artifactName, String attributeName){
		IWidgetLocator comp = getDatatypeAttributeCompartmentLocator(ui, artifactName);
		
		List figChildren = getUsefulChildren(comp);
		for (Object figChild : figChildren){
			if (figChild instanceof Attribute5EditPart.AttributeLabelFigure ){
				Attribute5EditPart.AttributeLabelFigure attribFigure  = (Attribute5EditPart.AttributeLabelFigure) figChild;												
				String matchPattern = "^[+-]"+attributeName+":.*";
				if (attribFigure.getText().matches(matchPattern)){
					return new WidgetReference(attribFigure);
				}
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
	
	
	public IWidgetLocator getManagedEntityMethodCompartmentLocator(IUIContext ui, String nameToFind) {
		
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure"));
		for (IWidgetLocator match : matches) {
			EditPart editPart =  ((IFigureReference) match).getEditPart();
			if (editPart instanceof ManagedEntityArtifactMethodCompartmentEditPart){
				ManagedEntityArtifactMethodCompartmentEditPart meMethods = (ManagedEntityArtifactMethodCompartmentEditPart) editPart;
				ManagedEntityArtifactEditPart meA =(ManagedEntityArtifactEditPart) meMethods.getParent();
				ManagedEntityArtifactNamePackageEditPart nameEP = (ManagedEntityArtifactNamePackageEditPart) meA.getPrimaryChildEditPart();
				if (((WrapLabel) nameEP.getFigure()).getText().equals(nameToFind)){
					return (IWidgetLocator) match;
				}
			}
		}
		System.out.println("Didn't find Method Compartment part");
		return null;
	}
	
	public IWidgetLocator getManagedEntityAttributeCompartmentLocator(IUIContext ui, String nameToFind) {
		
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure"));
		for (IWidgetLocator match : matches) {
			EditPart editPart =  ((IFigureReference) match).getEditPart();
			if (editPart instanceof ManagedEntityArtifactAttributeCompartmentEditPart){
				ManagedEntityArtifactAttributeCompartmentEditPart meAttributes = (ManagedEntityArtifactAttributeCompartmentEditPart) editPart;
				ManagedEntityArtifactEditPart meA =(ManagedEntityArtifactEditPart) meAttributes.getParent();
				ManagedEntityArtifactNamePackageEditPart nameEP = (ManagedEntityArtifactNamePackageEditPart) meA.getPrimaryChildEditPart();
				if (((WrapLabel) nameEP.getFigure()).getText().equals(nameToFind)){
					return (IWidgetLocator) match;
				}
			}
		}
		System.out.println("Didn't find Attribute Compartment part");
		return null;
	}
	
	
	
	


	
	public IWidgetLocator getEnumerationLiteralCompartmentLocator(IUIContext ui, String nameToFind) {
		IWidgetLocator[] matches = ui.findAll(new FigureClassLocator("org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure"));
		for (IWidgetLocator match : matches) {
			EditPart editPart =  ((IFigureReference) match).getEditPart();
			if (editPart instanceof EnumerationLiteralCompartmentEditPart){
				EnumerationLiteralCompartmentEditPart enumLiterals = (EnumerationLiteralCompartmentEditPart) editPart;
				EnumerationEditPart enumEP =(EnumerationEditPart) enumLiterals.getParent();
				EnumerationNamePackageEditPart nameEP = (EnumerationNamePackageEditPart) enumEP.getPrimaryChildEditPart();
				if (((WrapLabel) nameEP.getFigure()).getText().equals(nameToFind)){
					return (IWidgetLocator) match;
				}
			}
		}
		System.out.println("Didn't find Literal Compartment part");
		return null;
	}
	
	
	/**
	 * Find an literal within a named Enumeration.
	 * Just look for the method Name - ignore the type
	 * You should be able to derive the type from the returned object if you want 
	 * 
	 */
	public IWidgetLocator getEnumerationLiteralLocator(IUIContext ui, String artifactName, String literalName){
		IWidgetLocator comp = getEnumerationLiteralCompartmentLocator(ui, artifactName);
		
		List figChildren = getUsefulChildren(comp);
		for (Object figChild : figChildren){
			if (figChild instanceof LiteralEditPart.LiteralLabelFigure ){
				LiteralEditPart.LiteralLabelFigure literalFigure  = (LiteralEditPart.LiteralLabelFigure) figChild;												
				String matchPattern = "^[+-]"+literalName+"=.*";
				if (literalFigure.getText().matches(matchPattern)){
					return new WidgetReference(literalFigure);
				}
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
