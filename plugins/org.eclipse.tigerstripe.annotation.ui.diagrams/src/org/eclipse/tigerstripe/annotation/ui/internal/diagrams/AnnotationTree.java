/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.diagrams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.EditPart;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;

/**
 * @author Yuri Strot
 *
 */
public class AnnotationTree {
	
	private Map<AnnotationType, AnnotationTypeNode> types;
	
	public static AnnotationTree buildTree(EditPart[] parts) {
		AnnotationTree tree = new AnnotationTree();
		for (EditPart part : parts) {
			AnnotationStatus[] statuses = DiagramRebuildUtils.getPartAnnotations(part);
			for (int i = 0; i < statuses.length; i++) {
				AnnotationStatus ann = statuses[i];
				AnnotationType type = AnnotationPlugin.getManager().getType(ann.getAnnotation());
				AnnotationTypeNode node = tree.getNode(type);
				if (node == null) {
					node = new AnnotationTypeNode();
					tree.addAnnotationType(type, node);
				}
				EditPartNode eNode = node.getNode(part);
				if (eNode == null) {
					eNode = new EditPartNode(part);
					node.addPart(part, eNode);
				}
				eNode.addAnnotation(ann);
			}
		}
		return tree;
	}
	
	public AnnotationTree() {
		types = new HashMap<AnnotationType, AnnotationTypeNode>();
	}
	
	public void addAnnotationType(AnnotationType type, AnnotationTypeNode node) {
		types.put(type, node);
	}
	
	public void removeAnnotationType(AnnotationType type) {
		types.remove(type);
	}
	
	public boolean canBeShown() {
		for (AnnotationTypeNode node : getNodes()) {
			if (node.canBeShown())
				return true;
		}
		return false;
	}
	
	public boolean canBeHidden() {
		for (AnnotationTypeNode node : getNodes()) {
			if (node.canBeHidden())
				return true;
		}
		return false;
	}
	
	public int size() {
		return types.size();
	}
	
	public AnnotationTypeNode getNode(AnnotationType type) {
		return types.get(type);
	}
	
	public AnnotationType[] getTypes() {
		return types.keySet().toArray(new AnnotationType[types.size()]);
	}
	
	public AnnotationTypeNode[] getNodes() {
		return types.values().toArray(new AnnotationTypeNode[types.size()]);
	}
	
	public static class AnnotationTypeNode {
		
		private Map<EditPart, EditPartNode> parts;

		/**
		 * @param type
		 * @param parts
		 */
		public AnnotationTypeNode() {
			this.parts = new HashMap<EditPart, EditPartNode>();
		}
		
		public void addPart(EditPart part, EditPartNode node) {
			parts.put(part, node);
		}
		
		public void removePart(EditPart part) {
			parts.remove(part);
		}
		
		public EditPartNode getNode(EditPart part) {
			return parts.get(part);
		}
		
		public EditPartNode[] getNodes() {
			return parts.values().toArray(new EditPartNode[parts.size()]);
		}
		
		public boolean canBeShown() {
			for (EditPartNode node : getNodes()) {
				if (node.canBeShown())
					return true;
			}
			return false;
		}
		
		public boolean canBeHidden() {
			for (EditPartNode node : getNodes()) {
				if (node.canBeHidden())
					return true;
			}
			return false;
		}
		
	}
	
	public static class EditPartNode {
		
		private EditPart part;
		private List<AnnotationStatus> annotations;

		/**
		 * @param part
		 * @param annotations
		 */
		public EditPartNode(EditPart part) {
			this.part = part;
			this.annotations = new ArrayList<AnnotationStatus>();
		}

		/**
		 * @param part
		 * @param annotations
		 */
		public EditPartNode(EditPartNode node) {
			this.part = node.getPart();
			this.annotations = new ArrayList<AnnotationStatus>(
					Arrays.asList(node.getAnnotations()));
		}
		
		public void addAnnotation(AnnotationStatus annotation) {
			annotations.add(annotation);
		}
		
		public void addAnnotations(AnnotationStatus[] annotations) {
			this.annotations.addAll(Arrays.asList(annotations));
		}
		
		public void removeAnnotation(AnnotationStatus annotation) {
			annotations.remove(annotation);
		}
		
		public AnnotationStatus[] getAnnotations() {
			return annotations.toArray(new AnnotationStatus[annotations.size()]);
		}
		
		public boolean canBeShown() {
			for (AnnotationStatus status : annotations) {
				if (status.getStatus() != AnnotationStatus.STATUS_VISIBLE)
					return true;
			}
			return false;
		}
		
		public boolean canBeHidden() {
			for (AnnotationStatus status : annotations) {
				if (status.getStatus() == AnnotationStatus.STATUS_VISIBLE)
					return true;
			}
			return false;
		}
		
		/**
		 * @return the part
		 */
		public EditPart getPart() {
			return part;
		}
		
	}

}
