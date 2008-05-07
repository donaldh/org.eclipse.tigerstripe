package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards;

import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDatatypeArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEventArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IExceptionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IManagedEntityArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IQueryArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ISessionArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IUpdateProcedureArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UmlClassesTreeContentProvider.AssociationNode;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UmlClassesTreeContentProvider.ClassNode;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UmlClassesTreeContentProvider.DependencyNode;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UmlClassesTreeContentProvider.Node;


public class UmlClassesTreeLabelProvider extends LabelProvider {

	public String getText(Object element) {
		String text = ((Node) element).getName();
		return text;
	}

	public Image getImage(Object element) {
		Node node = (Node) element;

		if (node instanceof ClassNode) {
			if (((ClassNode) node).getMappingType().equals(IManagedEntityArtifact.class.getName())){
				return Images.get(Images.ENTITY_ICON);
			} 
			
			if (((ClassNode) node).getMappingType().equals(IDatatypeArtifact.class.getName())){
				return Images.get(Images.DATATYPE_ICON);
			}
			if (((ClassNode) node).getMappingType().equals(IEnumArtifact.class.getName())){
				return Images.get(Images.ENUM_ICON);
			}
			if (((ClassNode) node).getMappingType().equals(ISessionArtifact.class.getName())){
				return Images.get(Images.SESSION_ICON);
			}
			if (((ClassNode) node).getMappingType().equals(IUpdateProcedureArtifact.class.getName())){
				return Images.get(Images.UPDATEPROC_ICON);
			}
			
			if (((ClassNode) node).getMappingType().equals(IQueryArtifact.class.getName())){
				return Images.get(Images.QUERY_ICON);
			}
			if (((ClassNode) node).getMappingType().equals(IExceptionArtifact.class.getName())){
				return Images.get(Images.EXCEPTION_ICON);
			}
			if (((ClassNode) node).getMappingType().equals(IEventArtifact.class.getName())){
				return Images.get(Images.NOTIFICATION_ICON);
			}
		} else if (node instanceof AssociationNode){
			if (((AssociationNode) node).getMappingType().equals(IAssociationClassArtifact.class.getName())){
				return Images.get(Images.ASSOCIATIONCLASS_ICON);
			} 
			if (((AssociationNode) node).getMappingType().equals(IAssociationArtifact.class.getName())){
				return Images.get(Images.ASSOCIATION_ICON);
			} 
			
		} else if (node instanceof DependencyNode){
			if (((DependencyNode) node).getMappingType().equals(IDependencyArtifact.class.getName())){
				return Images.get(Images.DEPENDENCY_ICON);
			}
		}
		return JavaPluginImages.get(JavaPluginImages.IMG_OBJS_HELP);
	}
}
