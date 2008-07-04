/**
 * 
 */
package org.eclipse.tigerstripe.annotation.ui.diagrams.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ViewComponentEditPolicy;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationAdapter;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.IAnnotationListener;
import org.eclipse.tigerstripe.annotation.ui.diagrams.model.AnnotationNode;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.parts.AnnotationConnectionEditPart;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.parts.ConnectionCollector;
import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.parts.NonSemanticEditPolicy;

/**
 * @author Yuri Strot
 *
 */
public abstract class AnnotationEditPart extends ShapeNodeEditPart {
    
    private IAnnotationListener annotationListener; 
    private ConnectionCollector collector;

	/**
	 * constructor
	 * @param view the view controlled by this edit part
	 */
	public AnnotationEditPart(View view) {
		super(view);
		collector = new ConnectionCollector(view);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class key) {
		if (Annotation.class.isAssignableFrom(key))
			return getAnnotation();
		return super.getAdapter(key);
	}
	
	private void addChangeListener() {
		if (annotationListener == null) {
			annotationListener = new AnnotationAdapter() {
				
				@Override
				public void annotationsChanged(Annotation[] annotations) {
					if (getNotationView().isVisible() && 
							isAnnotationChanged(annotations)) {
						updatePart();
					}
				}
			
			};
			AnnotationPlugin.getManager().addAnnotationListener(annotationListener);
		}
	}
	
	protected boolean isAnnotationChanged(Annotation[] changedAnnotations) {
		Annotation ann = getAnnotation();
		if (ann != null) {
			for (Annotation annotation : changedAnnotations) {
				if (ann.equals(annotation))
					return true;
			}
		}
		return false;
	}
	
	private void updatePart() {
		try {
			getEditingDomain().runExclusive(new Runnable() {
				public void run() {
					refreshVisuals();
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void removeChangeListener() {
		if (annotationListener != null) {
			AnnotationPlugin.getManager().removeAnnotationListener(annotationListener);
			annotationListener = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		addChangeListener();
		super.activate();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		super.deactivate();
		removeChangeListener();
	}
	
	protected AnnotationNode getAnnotationModel() {
		return (AnnotationNode)getModel();
	}
	
	public Annotation getAnnotation() {
		return getAnnotationModel().getAnnotation();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#refreshSourceConnections()
	 */
	@Override
	protected void refreshSourceConnections() {
		collector.start();
		super.refreshSourceConnections();
		collector.refresh();
		collector.finish();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#refreshTargetConnections()
	 */
	@Override
	protected void refreshTargetConnections() {
		collector.start();
		super.refreshTargetConnections();
		collector.refresh();
		collector.finish();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createChild(java.lang.Object)
	 */
	@Override
	protected EditPart createChild(Object model) {
		EditPart part = super.createChild(model);
		if (part instanceof AnnotationConnectionEditPart) {
			collector.addConnection((AnnotationConnectionEditPart)part);
		}
		return part;
	}

	/** Adds support for diagram links. */
	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();

		// Remove semantic edit policy and install a non-semantic edit policy
		removeEditPolicy(EditPolicyRoles.SEMANTIC_ROLE);
		installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE,
			new NonSemanticEditPolicy());

		// This View doesn't have semantic elements so use a component edit
		// policy that only gets a command to delete the view
		installEditPolicy(
			EditPolicy.COMPONENT_ROLE,
			new ViewComponentEditPolicy());
	}
}
