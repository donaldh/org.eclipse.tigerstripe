package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.jface.action.IAction;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ElementTypeMapper;
import org.eclipse.ui.IObjectActionDelegate;

/**
 * Bugzilla 221443: Convenient method to convert an Association Class to an Association
 * 
 * @author Navid Mehregani
 */
public class ConvertAssociationClassToAssociation extends AbstractConverter implements IObjectActionDelegate {

	public void run(IAction action) {		
		super.run(action, true);		
	}
	
	public IElementType getElementType() {
		return ElementTypeMapper.Association_3001;
	}
	
	public String getArtifactName() {
		return "association class";
	}
}
