package org.eclipse.tigerstripe.workbench.ui.uml2import.internal.ui.wizards;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.uml2.uml.Classifier;

public class UmlClassesTreeContentProvider extends ArrayContentProvider
		implements ITreeContentProvider {

	private ArrayList<Node> nodes = new ArrayList<Node>();

	private Map<Classifier, String> input;

	private boolean showDeltaOnly;

	public abstract class Node {

		private Node parentNode;

		public Node(Node parentNode) {
			this.parentNode = parentNode;
		}

		public boolean hasChildren() {
			return getChildren().length != 0;
		}

		public abstract Node[] getChildren();

		public abstract String getName();

		public Node getParentNode() {
			return this.parentNode;
		}

	};

	public class PackageNode extends Node {

		private ArrayList<Node> children = new ArrayList<Node>();

		private String name;

		public PackageNode(Node parentNode, String name) {
			super(parentNode);
			this.name = name;
		}

		public void addChild(Node child) {
			children.add(child);
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public Node[] getChildren() {
			return children.toArray(new Node[children.size()]);
		}
	};

	/**
	 * Representation of a RawClass in the tree
	 * 
	 * @author Eric Dillon
	 * 
	 */
	public class ClassNode extends Node {

		private Classifier classifier;
	

		public ClassNode(Node parentNode, Classifier annotable) {
			super(parentNode);
			this.classifier = annotable;
		}

		@Override
		public String getName() {
			return classifier.getQualifiedName();
		}

		public Classifier getClassifier() {
			return this.classifier;
		}

		public String getMappingType(){
			return input.get(this.getClassifier());
		}

		public void setMappingType (String type){
			input.put(this.getClassifier(),type);
		}
		
		@Override
		public Node[] getChildren() {
			// We don't care about children
			return new Node[0]; // NO CHILDREN
		}
	}



	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return nodes.toArray(new Node[nodes.size()]);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			this.input = (Map<Classifier, String>) newInput;
			createNodeList((Map<Classifier, String>) newInput);
		}
		super.inputChanged(viewer, oldInput, newInput);
	}

	public Object[] getChildren(Object parentElement) {
		return ((Node) parentElement).getChildren();
	}

	public Object getParent(Object element) {
		return ((Node) element).getParentNode();
	}

	public boolean hasChildren(Object element) {
		return ((Node) element).hasChildren();
	}

	private void createNodeList(Map<Classifier, String> elements) {
		nodes.clear();
		
		// How about some packages ?
		for (Classifier elem : elements.keySet()) {
				ClassNode node = new ClassNode(null, elem);
				nodes.add(node);
				
		}
		
	}

}
