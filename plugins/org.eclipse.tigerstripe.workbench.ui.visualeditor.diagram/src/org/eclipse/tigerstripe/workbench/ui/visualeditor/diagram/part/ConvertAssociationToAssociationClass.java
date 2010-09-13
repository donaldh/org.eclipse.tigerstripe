package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.jface.action.IAction;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ElementTypeMapper;
import org.eclipse.ui.IObjectActionDelegate;

/**
 * Bugzilla 221443: Provide a convenient method to convert an Association to an Association Class
 * 
 * @author Navid Mehregani
 *
 */
public class ConvertAssociationToAssociationClass  extends AbstractConverter implements IObjectActionDelegate {

	
	public void run(IAction action) {		
		super.run(action, false);		
	}
	
	public IElementType getElementType() {
		return ElementTypeMapper.AssociationClass_3010;
	}
	
	public String getArtifactName() {
		return "association";
	}

}
