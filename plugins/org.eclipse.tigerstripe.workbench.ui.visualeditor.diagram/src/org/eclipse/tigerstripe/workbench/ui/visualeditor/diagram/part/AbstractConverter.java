package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactDeleteRequest;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.commands.AssociationUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.ui.IObjectActionDelegate;

/**
 * See bugzilla 221443: This is the base converter class used as a parent of all other converters
 * 
 * @author Navid Mehregani
 *
 */
public abstract class AbstractConverter extends BaseDiagramPartAction implements IObjectActionDelegate {

	protected void run(IAction action, boolean associationClassToAssociation) {
		
		try {
			IArtifactManagerSession artifactManagerSession = getCorrespondingTigerstripeProject().getArtifactManagerSession();
			IModelUpdater updater = artifactManagerSession.getIModelUpdater();
			
			IAbstractArtifact[] abstractArtifacts = getCorrespondingArtifacts();
			IAbstractArtifact selectedAssociation = abstractArtifacts[0];
			
			IAssociationArtifact association = null;
			if (selectedAssociation instanceof IAssociationArtifact)  
				association = (IAssociationArtifact) selectedAssociation;
			else {
				TigerstripeDiagramEditorPlugin.getInstance().logError("Selected object is not an " + getArtifactName() + " artifact");
				return;
			}
			
			if (associationClassToAssociation) {
				Collection<IMethod> methods = association.getMethods();
				Collection<IField> fields = association.getFields();
				if ((methods!=null && methods.size()>0) || (fields!=null && fields.size()>0)) {
					String message = "Converting to an Association will remove all methods and fields for the selected Association Class.  Are you sure you want to continue?";
					MessageDialog messageDialog = new MessageDialog(TigerstripeDiagramEditorPlugin.getInstance().getWorkbench().getDisplay().getActiveShell(), "Warning", null, message, MessageDialog.WARNING, new String[]{"No", "Yes"}, 1);
					int result = messageDialog.open();
					if (result==0 || result==SWT.DEFAULT)
						return;
				}
			}
			
			// Gather info for selected association
			String associationName = association.getName();
			String associationFQN = association.getFullyQualifiedName();
			String associationPackage = association.getPackage();
			String aEndName = association.getAEnd().getName();
			String zEndName = association.getZEnd().getName();
			String aEndNodeFQN = association.getAEnd().getType().getFullyQualifiedName();
			String zEndNodeFQN = association.getZEnd().getType().getFullyQualifiedName();
			String aEndMultiplicity = association.getAEnd().getMultiplicity().getLabel();
			String zEndMultiplicity = association.getZEnd().getMultiplicity().getLabel();
			String aEndAggregation = association.getAEnd().getAggregation().getLabel();
			String zEndAggregation = association.getZEnd().getAggregation().getLabel();
			String description = association.getComment();
			
			
			// Get EObject for selected association 
			EObject[] correspondingEObjects = getCorrespondingEObjects();
			if (correspondingEObjects==null || correspondingEObjects.length==0) {
				TigerstripeDiagramEditorPlugin.getInstance().logError("Selected object doesn't map to an EObject");
				return;
			}
						
			// Get the request ready for creating an association 
			TransactionalEditingDomain editingDomain = TransactionUtil.getEditingDomain(correspondingEObjects[0]);
			EObject source = getEObjectFromFQN(aEndNodeFQN);
			EObject target = getEObjectFromFQN(zEndNodeFQN);
			IElementType elementType = getElementType();
			CreateRelationshipRequest relationshipRequest = new CreateRelationshipRequest(editingDomain, null, source, target, elementType, null);
			EditCommandRequestWrapper editCommandRequestWrapper = new EditCommandRequestWrapper(relationshipRequest);			
			
			
			// Get the corresponding command for the request
			EditPart artifactEditPart = (EditPart) getEditPart(aEndNodeFQN);
			if (artifactEditPart == null) {
				TigerstripeDiagramEditorPlugin.getInstance().logError("Could not find the list of all edit parts");
				return;
			}
			EditPolicy artifactEditPolicy = artifactEditPart
					.getEditPolicy(EditPolicyRoles.SEMANTIC_ROLE);
			Command command = artifactEditPolicy
					.getCommand(editCommandRequestWrapper);
			
			// Delete the association class (to be replaced by an association) 
			IArtifactDeleteRequest deleteRequest = (IArtifactDeleteRequest) updater.getRequestFactory().makeRequest(IModelChangeRequestFactory.ARTIFACT_DELETE);
			deleteRequest.setArtifactName(associationName);
			deleteRequest.setArtifactPackage(associationPackage);
			updater.handleChangeRequest(deleteRequest);
			
			// Remove the resource corresponding to the association
			IResource resource = (IResource) selectedAssociation.getAdapter(IResource.class);
			if (resource != null) {
				try {
					resource.delete(true, new NullProgressMonitor());
				} catch (CoreException e) {
					EclipsePlugin.log(e);
				}
			}
			
			// Execute the command
			DiagramEditDomain gefEditDomain = null;
			if (myTargetWorkbenchPart instanceof TigerstripeDiagramEditor) {
				DiagramGraphicalViewer viewer = (DiagramGraphicalViewer) ((TigerstripeDiagramEditor)myTargetWorkbenchPart).getDiagramGraphicalViewer();
				gefEditDomain = (DiagramEditDomain) viewer.getEditDomain();
								
				gefEditDomain.getCommandStack().execute(command);	
			}
			
			// Get the newly created association EObject
			Association newAssociation = null;
			if (command instanceof ICommandProxy) {
				ICommand iCommand = ((ICommandProxy)command).getICommand();
				if (iCommand instanceof CompositeTransactionalCommand) {
					CommandResult commandResult = ((CompositeTransactionalCommand)iCommand).getCommandResult();
					Object returnValueArray = commandResult.getReturnValue();
					if ((returnValueArray instanceof ArrayList) && (((ArrayList)returnValueArray).size()>0)) {
						Object returnValue = ((ArrayList)returnValueArray).get(0);
						if (returnValue instanceof Association) {
							newAssociation = (Association)returnValue;
						}
					}
				}
			}
		
			// Set the association properties
			MapEditPart mapEditPart = getMapEditPart();
			if (mapEditPart!=null) {
				Map<String, Object> changedValuesMap = new HashMap<String,Object>();
				changedValuesMap.put("associationName", associationName);
				changedValuesMap.put("aEndName", aEndName);			
				changedValuesMap.put("zEndName", zEndName);		
				changedValuesMap.put("aEndMultiplicity", aEndMultiplicity);
				changedValuesMap.put("zEndMultiplicity", zEndMultiplicity);
				changedValuesMap.put("aEndAggregation", aEndAggregation);
				changedValuesMap.put("zEndAggregation", zEndAggregation);
				AssociationUpdateCommand associationUpdateCommand = new AssociationUpdateCommand(editingDomain, mapEditPart, newAssociation, changedValuesMap);
				ICommandProxy commandProxy = new ICommandProxy(associationUpdateCommand);
				
				gefEditDomain.getCommandStack().execute(commandProxy);
			} else {
				TigerstripeDiagramEditorPlugin.getInstance().logError("Could not get MapEditPart");
			}
			
			if (description!=null && description.length()>0) {
				IAbstractArtifact abstractArtifact = artifactManagerSession.getArtifactByFullyQualifiedName(associationFQN);
				if (abstractArtifact!=null)
					abstractArtifact.setComment(description);
			}
			
			
		} catch (TigerstripeException e) {
			TigerstripeDiagramEditorPlugin.getInstance().logError(e.getMessage(), e);
		}
		
	}
	
	/**
	 * Returns an EObject given an FQN.
	 * 
	 * @param fqn  Fully qualified name
	 * @return eObject with the given FQN
	 */
	private EObject getEObjectFromFQN(String fqn) {
		List<EditPart> allEditParts = getAllEditParts();
		
		for (int i=0; i < allEditParts.size(); i++) {
			EditPart editPart = allEditParts.get(i);
			if (editPart.getModel() instanceof Node) {
				Node obj = (Node) editPart.getModel();
				EObject eObject = obj.getElement();
				if ((eObject instanceof QualifiedNamedElement) && (((QualifiedNamedElement)eObject).getFullyQualifiedName().equals(fqn))) {
					return eObject;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Get the edit part for the element with the given fully qualified name
	 * 
	 * @param fqn  Fully qualified name
	 * @return     Edit part corresponding to fqn. Null if corresponding edit part cannot be found.
	 */
	private EditPart getEditPart(String fqn) {
		
		
		List<EditPart> allEditParts = getAllEditParts();
		
		for (int i=0; i < allEditParts.size(); i++) {
			EditPart editPart = allEditParts.get(i);
			if (editPart.getModel() instanceof Node) {
				Node obj = (Node) editPart.getModel();
				EObject eObject = obj.getElement();
				if ((eObject instanceof QualifiedNamedElement) && (((QualifiedNamedElement)eObject).getFullyQualifiedName().equals(fqn))) {
					return editPart;
				}
			}
		}
		
		return null;
	}
	
	private MapEditPart getMapEditPart() {
		if (myTargetWorkbenchPart instanceof TigerstripeDiagramEditor) {
			IDiagramGraphicalViewer graphicalViewer = ((TigerstripeDiagramEditor)myTargetWorkbenchPart).getDiagramGraphicalViewer();
			if (graphicalViewer instanceof DiagramGraphicalViewer) {
				RootEditPart rootEditPart = ((DiagramGraphicalViewer)graphicalViewer).getRootEditPart();
				if (rootEditPart != null && rootEditPart.getChildren()!=null && rootEditPart.getChildren().size()>0) {
					Object obj = rootEditPart.getChildren().get(0);
					if (obj instanceof MapEditPart) 
						return (MapEditPart)obj;					
				}
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private List<EditPart> getAllEditParts() {
		MapEditPart mapEditPart = getMapEditPart();
		if (mapEditPart==null) 
			return null;
		
		List<EditPart> allEditParts = mapEditPart.getChildren();		
		if (allEditParts==null || allEditParts.size()==0) 
			return null;
		
		return allEditParts;
	}
	
	/**
	 * Enable the action only when one single association is selected
	 */
	@Override
	protected boolean isEnabled() {
		return mySelectedElements != null && mySelectedElements.length == 1;
	}

	
	/**
	 * @return  The element type we're converting to
	 */
	public abstract  IElementType getElementType();
	
	/**
	 * @return  Artifact name we're dealing with
	 */
	public abstract String getArtifactName();
	

}
