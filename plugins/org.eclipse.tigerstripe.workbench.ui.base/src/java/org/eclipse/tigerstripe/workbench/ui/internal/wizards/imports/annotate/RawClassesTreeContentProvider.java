/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.annotate;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.Annotable;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableAssociation;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableAssociationEnd;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableDatatype;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableDependency;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementAttribute;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementConstant;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElementOperation;

public class RawClassesTreeContentProvider extends ArrayContentProvider
		implements ITreeContentProvider {

	private ArrayList<Node> nodes = new ArrayList<Node>();

	private AnnotableElement[] annotableInput;

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

		public abstract int getDelta();
	};

	public class PackageNode extends Node {

		private ArrayList<Node> children = new ArrayList<Node>();

		private String name;

		@Override
		public int getDelta() {
			return Annotable.DELTA_UNKNOWN;
		}

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

		private AnnotableElement annotable;

		private List<AttributeNode> attributes = new ArrayList<AttributeNode>();

		private List<OperationNode> operations = new ArrayList<OperationNode>();

		private List<ConstantNode> constants = new ArrayList<ConstantNode>();

		private Node[] children = null;

		@Override
		public int getDelta() {
			return annotable.getDelta();
		}

		public ClassNode(Node parentNode, AnnotableElement annotable) {
			super(parentNode);
			this.annotable = annotable;
		}

		@Override
		public String getName() {
			return annotable.getFullyQualifiedName();
		}

		public AnnotableElement getAnnotableElement() {
			return this.annotable;
		}

		public void addAttributeNode(AttributeNode attributeNode) {
			attributes.add(attributeNode);
		}

		public void addOperationNode(OperationNode operationNode) {
			operations.add(operationNode);
		}

		public void addConstantNode(ConstantNode constantNode) {
			constants.add(constantNode);
		}

		@Override
		public Node[] getChildren() {
			if (children == null) {
				ArrayList<Node> arr = new ArrayList<Node>();
				arr.addAll(attributes);
				arr.addAll(operations);
				arr.addAll(constants);
				children = arr.toArray(new Node[arr.size()]);
			}

			return children;
		}
	}

	public class DependencyNode extends Node {
		private AnnotableDependency annotable;

		@Override
		public int getDelta() {
			return annotable.getDelta();
		}

		public DependencyNode(Node parentNode, AnnotableDependency annotable) {
			super(parentNode);
			this.annotable = annotable;
		}

		@Override
		public String getName() {
			return annotable.getFullyQualifiedName();
		}

		public AnnotableElement getAnnotableElement() {
			return this.annotable;
		}

		@Override
		public Node[] getChildren() {
			return new Node[] {
					new DependencyEndNode(this, annotable.getAEnd()),
					new DependencyEndNode(this, annotable.getZEnd()) };
		}
	}

	public class DependencyEndNode extends Node {

		private AnnotableDatatype type;

		@Override
		public int getDelta() {
			return Annotable.DELTA_UNCHANGED;
		}

		public DependencyEndNode(Node parentNode, AnnotableDatatype type) {
			super(parentNode);
			this.type = type;
		}

		@Override
		public String getName() {
			return this.type.getName();
		}

		public AnnotableDatatype getAnnotableDatatype() {
			return this.type;
		}

		@Override
		public Node[] getChildren() {
			return new Node[0]; // NO CHILDREN
		}
	};

	/**
	 * Representation of a RawClass in the tree
	 * 
	 * @author Eric Dillon
	 * 
	 */
	public class AssociationNode extends Node {

		private AnnotableAssociation annotable;

		private AssociationEndNode aEnd;
		private AssociationEndNode zEnd;

		@Override
		public int getDelta() {
			return annotable.getDelta();
		}

		public AssociationNode(Node parentNode, AnnotableAssociation annotable) {
			super(parentNode);
			this.annotable = annotable;
		}

		@Override
		public String getName() {
			return annotable.getFullyQualifiedName();
		}

		public AnnotableElement getAnnotableElement() {
			return this.annotable;
		}

		public void setAEnd(AnnotableAssociationEnd aEnd) {
			this.aEnd = new AssociationEndNode(this, aEnd);
		}

		public void setZEnd(AnnotableAssociationEnd aEnd) {
			this.zEnd = new AssociationEndNode(this, aEnd);
		}

		@Override
		public Node[] getChildren() {
			return new Node[] { aEnd, zEnd };
		}
	}

	/**
	 * Representation of an attribute in a Raw Class
	 * 
	 * @author Eric Dillon
	 * 
	 */
	public class AttributeNode extends Node {

		private AnnotableElementAttribute annotableElementAttribute;

		public AttributeNode(Node parentNode,
				AnnotableElementAttribute annotableElementAttribute) {
			super(parentNode);
			this.annotableElementAttribute = annotableElementAttribute;
		}

		@Override
		public int getDelta() {
			return annotableElementAttribute.getDelta();
		}

		@Override
		public String getName() {
			return this.annotableElementAttribute.getName()
					+ ": "
					+ this.annotableElementAttribute.getType()
							.getFullyQualifiedName();
		}

		public AnnotableElementAttribute getAnnotableElementAttribute() {
			return this.annotableElementAttribute;
		}

		@Override
		public Node[] getChildren() {
			return new Node[0]; // NO CHILDREN
		}
	};

	public class OperationNode extends Node {

		private AnnotableElementOperation annotableElementOperation;

		public OperationNode(Node parentNode,
				AnnotableElementOperation annotableElementOperation) {
			super(parentNode);
			this.annotableElementOperation = annotableElementOperation;
		}

		@Override
		public int getDelta() {
			return annotableElementOperation.getDelta();
		}

		@Override
		public String getName() {
			return this.annotableElementOperation.getName();
		}

		public AnnotableElementOperation getAnnotableElementOperation() {
			return this.annotableElementOperation;
		}

		@Override
		public Node[] getChildren() {
			return new Node[0]; // NO CHILDREN
		}
	};

	public class ConstantNode extends Node {

		private AnnotableElementConstant annotableElementConstant;

		@Override
		public int getDelta() {
			return annotableElementConstant.getDelta();
		}

		public ConstantNode(Node parentNode,
				AnnotableElementConstant annotableElementConstant) {
			super(parentNode);
			this.annotableElementConstant = annotableElementConstant;
		}

		@Override
		public String getName() {
			return this.annotableElementConstant.getName();
		}

		public AnnotableElementConstant getAnnotableElementConstant() {
			return this.annotableElementConstant;
		}

		@Override
		public Node[] getChildren() {
			return new Node[0]; // NO CHILDREN
		}
	};

	public class AssociationEndNode extends Node {

		private AnnotableAssociationEnd annotableAssociationEnd;

		@Override
		public int getDelta() {
			return annotableAssociationEnd.getDelta();
		}

		public AssociationEndNode(Node parentNode,
				AnnotableAssociationEnd annotableAssociationEnd) {
			super(parentNode);
			this.annotableAssociationEnd = annotableAssociationEnd;
		}

		@Override
		public String getName() {
			return this.annotableAssociationEnd.getName();
		}

		public AnnotableAssociationEnd getAnnotableAssociationEnd() {
			return this.annotableAssociationEnd;
		}

		@Override
		public Node[] getChildren() {
			return new Node[0]; // NO CHILDREN
		}
	};

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
			this.annotableInput = (AnnotableElement[]) newInput;
			createNodeList((AnnotableElement[]) newInput);
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

	private void createNodeList(AnnotableElement[] elements) {
		nodes.clear();
		for (AnnotableElement elem : elements) {

			if (elem instanceof AnnotableAssociation) {
				AssociationNode node = new AssociationNode(null,
						(AnnotableAssociation) elem);
				node.setAEnd(((AnnotableAssociation) elem).getAEnd());
				node.setZEnd(((AnnotableAssociation) elem).getZEnd());
				if (!showDeltaOnly) {
					nodes.add(node);
				} else {
					if (elem.getDelta() != Annotable.DELTA_UNCHANGED) {
						nodes.add(node);
					}
				}
			} else if (elem instanceof AnnotableDependency) {
				DependencyNode node = new DependencyNode(null,
						(AnnotableDependency) elem);
				if (!showDeltaOnly) {
					nodes.add(node);
				} else {
					if (elem.getDelta() != Annotable.DELTA_UNCHANGED) {
						nodes.add(node);
					}
				}
			} else {
				ClassNode node = new ClassNode(null, elem);
				if (!showDeltaOnly) {
					nodes.add(node);
				} else {
					if (elem.getDelta() != Annotable.DELTA_UNCHANGED) {
						nodes.add(node);
					}
				}
				for (AnnotableElementAttribute attr : elem
						.getAnnotableElementAttributes()) {

					AttributeNode attrNode = new AttributeNode(node, attr);

					if (!showDeltaOnly) {
						node.addAttributeNode(attrNode);
					} else {
						if (attr.getDelta() != Annotable.DELTA_UNCHANGED) {
							node.addAttributeNode(attrNode);
						}
					}
				}

				for (AnnotableElementOperation op : elem
						.getAnnotableElementOperations()) {
					OperationNode opNode = new OperationNode(node, op);
					if (!showDeltaOnly) {
						node.addOperationNode(opNode);
					} else {
						if (op.getDelta() != Annotable.DELTA_UNCHANGED) {
							node.addOperationNode(opNode);
						}
					}
				}

				for (AnnotableElementConstant cst : elem
						.getAnnotableElementConstants()) {
					ConstantNode cstNode = new ConstantNode(node, cst);
					if (!showDeltaOnly) {
						node.addConstantNode(cstNode);
					} else {
						if (cst.getDelta() != Annotable.DELTA_UNCHANGED) {
							node.addConstantNode(cstNode);
						}
					}
				}
			}
		}
	}

	public void setDeltaOnly(boolean deltaOnly) {
		this.showDeltaOnly = deltaOnly;
		createNodeList(annotableInput);
	}
}
