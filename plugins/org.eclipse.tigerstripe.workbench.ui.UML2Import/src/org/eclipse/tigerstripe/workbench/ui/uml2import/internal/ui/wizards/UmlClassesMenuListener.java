package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IAssociationClassArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IDatatypeArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IDependencyArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IEnumArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IEventArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IExceptionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IManagedEntityArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IQueryArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.ISessionArtifactImpl;
import org.eclipse.tigerstripe.metamodel.impl.IUpdateProcedureArtifactImpl;
import org.eclipse.tigerstripe.metamodel.internal.ArtifactMetadataFactory;
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
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UmlClassesTreeContentProvider.ClassNode;


public class UmlClassesMenuListener implements IMenuListener {

	private TreeViewer viewer;
	
	public UmlClassesMenuListener(TreeViewer viewer) {
		this.viewer = viewer;
	}
	
	private class DontAnnotateAction extends Action {
		@Override
		public String getText() {
			return "Ignore";
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.setMappingType("");
				} 
			}
			viewer.refresh(true);
		}
	}
	
	private class AsEntityAction extends Action {

		@Override
		public String getText() {
			return "as "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IManagedEntityArtifactImpl.class.getName())
							.getLabel();
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.setMappingType(IManagedEntityArtifact.class.getName());
				}
			}
			viewer.refresh(true);
		}
	}
	
	private class AsDatatypeAction extends Action {

		@Override
		public String getText() {
			return "as "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IDatatypeArtifactImpl.class.getName())
							.getLabel();
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.setMappingType(IDatatypeArtifact.class.getName());
				}
			}
			viewer.refresh(true);
		}
	}

	private class AsEnumerationAction extends Action {

		@Override
		public String getText() {
			return "as "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IEnumArtifactImpl.class.getName())
							.getLabel();
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.setMappingType(IEnumArtifact.class.getName());
				}
			}
			viewer.refresh(true);
		}
	}
	
	private class AsExceptionAction extends Action {

		@Override
		public String getText() {
			return "as "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IExceptionArtifactImpl.class.getName())
							.getLabel();
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.setMappingType(IExceptionArtifact.class.getName());
				}
			}
			viewer.refresh(true);
		}
	}
	
	private class AsQueryAction extends Action {

		@Override
		public String getText() {
			return "as "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IQueryArtifactImpl.class.getName())
							.getLabel();
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.setMappingType(IQueryArtifact.class.getName());
				}
			}
			viewer.refresh(true);
		}
	}
	
	private class AsEventAction extends Action {

		@Override
		public String getText() {
			return "as "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IEventArtifactImpl.class.getName())
							.getLabel();
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.setMappingType(IEventArtifact.class.getName());
				}
			}
			viewer.refresh(true);
		}
	}
	
	private class AsUpdateProcAction extends Action {

		@Override
		public String getText() {
			return "as "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IUpdateProcedureArtifactImpl.class.getName())
							.getLabel();
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.setMappingType(IUpdateProcedureArtifact.class.getName());
				}
			}
			viewer.refresh(true);
		}
	}
	
	private class AsSessionAction extends Action {

		@Override
		public String getText() {
			return "as "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							ISessionArtifactImpl.class.getName())
							.getLabel();
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.setMappingType(ISessionArtifact.class.getName());
				}
			}
			viewer.refresh(true);
		}
	}
	
	private class AsDependencyAction extends Action {

		@Override
		public String getText() {
			return "as "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IDependencyArtifactImpl.class.getName())
							.getLabel();
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.setMappingType(IDependencyArtifact.class.getName());
				}
			}
			viewer.refresh(true);
		}
	}
	
	private class AsAssociationAction extends Action {

		@Override
		public String getText() {
			return "as "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IAssociationArtifactImpl.class.getName())
							.getLabel();
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.setMappingType(IAssociationArtifact.class.getName());
				}
			}
			viewer.refresh(true);
		}
	}
	
	private class AsAssociationClassAction extends Action {

		@Override
		public String getText() {
			return "as "
					+ ArtifactMetadataFactory.INSTANCE.getMetadata(
							IAssociationClassArtifactImpl.class.getName())
							.getLabel();
		}

		@Override
		public void run() {
			IStructuredSelection ssel = (IStructuredSelection) viewer
					.getSelection();
			for (Object selectedObj : ssel.toArray()) {
				if (selectedObj instanceof ClassNode) {
					ClassNode node = (ClassNode) selectedObj;
					node.setMappingType(IAssociationClassArtifact.class.getName());
				}
			}
			viewer.refresh(true);
		}
	}
	
	public void menuAboutToShow(IMenuManager manager) {

		// TODO  Filter for active types in profile.
			manager.add(new DontAnnotateAction());
			manager.add(new AsEntityAction());
			manager.add(new AsDatatypeAction());
			manager.add(new AsEnumerationAction());
			manager.add(new AsExceptionAction());
			manager.add(new AsQueryAction());
			manager.add(new AsEventAction());
			manager.add(new AsUpdateProcAction());
			manager.add(new AsSessionAction());
			manager.add(new AsDependencyAction());
			manager.add(new AsAssociationAction());
			manager.add(new AsAssociationClassAction());

	}

}
