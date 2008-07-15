package org.eclipse.tigerstripe.ui.visualeditor.test.finders;

import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactEditPart;

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
