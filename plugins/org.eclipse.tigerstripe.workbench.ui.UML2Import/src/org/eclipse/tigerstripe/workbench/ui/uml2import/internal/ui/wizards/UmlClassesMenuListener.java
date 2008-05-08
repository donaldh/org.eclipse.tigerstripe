package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards;

import java.util.Arrays;
import java.util.List;

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
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.profile.IWorkbenchProfileProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.ArtifactSettingDetails;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.CoreArtifactSettingsProperty;
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
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UmlClassesTreeContentProvider.AssociationNode;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UmlClassesTreeContentProvider.ClassNode;
import org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards.UmlClassesTreeContentProvider.DependencyNode;


public class UmlClassesMenuListener implements IMenuListener {

	private TreeViewer viewer;
	
	public UmlClassesMenuListener(TreeViewer viewer) {
		this.viewer = viewer;
	}
	
	private List typeList;
	
	public void setSupportedArtifacts(CoreArtifactSettingsProperty property){
		typeList = Arrays.asList(property.getEnabledArtifactTypes());
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


		IStructuredSelection ssel = (IStructuredSelection) viewer
		.getSelection();
		boolean classNodeSelected = false;
		boolean dependencySelected = false;
		boolean associationSelected = false;


		for (Object obj : ssel.toArray()) {
			classNodeSelected = obj instanceof ClassNode;
			dependencySelected = obj instanceof DependencyNode;
			associationSelected = obj instanceof AssociationNode;
		}
		if (classNodeSelected) {
			manager.add(new DontAnnotateAction());
			if (typeList.contains(IManagedEntityArtifact.class.getName())){
				manager.add(new AsEntityAction());
			}
			if (typeList.contains(IDatatypeArtifact.class.getName())){
				manager.add(new AsDatatypeAction());
			}
			if (typeList.contains(IEnumArtifact.class.getName())){
				manager.add(new AsEnumerationAction());
			}
			if (typeList.contains(IExceptionArtifact.class.getName())){
				manager.add(new AsExceptionAction());
			}
			if (typeList.contains(IQueryArtifact.class.getName())){
				manager.add(new AsQueryAction());
			}
			if (typeList.contains(IEventArtifact.class.getName())){
				manager.add(new AsEventAction());
			}
			if (typeList.contains(IUpdateProcedureArtifact.class.getName())){
				manager.add(new AsUpdateProcAction());
			}
			if (typeList.contains(ISessionArtifact.class.getName())){
				manager.add(new AsSessionAction());
			}
		} else if (dependencySelected) {
			manager.add(new DontAnnotateAction());

			if (typeList.contains(IDependencyArtifact.class.getName())){
				manager.add(new AsDependencyAction());
			}
		} else if (associationSelected) {
			manager.add(new DontAnnotateAction());

			if (typeList.contains(IAssociationArtifact.class.getName())){
				manager.add(new AsAssociationAction());
			}
			if (typeList.contains(IAssociationClassArtifact.class.getName())){
				manager.add(new AsAssociationClassAction());
			}
		}
	}


}
