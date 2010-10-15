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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.dialogs;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.ui.dialogs.ListDialog;

public class AddRelatedInstancesDialog extends ListDialog {

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		public Object[] getElements(Object inputElement) {
			return (Object[]) inputElement;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	};

	private ILabelProvider labelProvider = new LabelProvider() {

		@Override
		public String getText(Object element) {
			IAbstractArtifact art = (IAbstractArtifact) element;
			return String.format("%s - %s", art.getName(),
					join(categories.get(art)));
		}
	};

	private final Map<IAbstractArtifact, Category> categories;

	private Label messageLabel;

	public AddRelatedInstancesDialog(Shell parentShell,
			Map<String, Set<IAbstractArtifact>> relations) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		setLabelProvider(labelProvider);
		setContentProvider(contentProvider);
		categories = new TreeMap<IAbstractArtifact, Category>(
				new Comparator<IAbstractArtifact>() {

					public int compare(IAbstractArtifact o1,
							IAbstractArtifact o2) {
						return o1.getName().compareTo(o2.getName());
					}

				});
		setInput(createInput(relations));
		setTitle("Add Related Instances");
		setMessage("Select related artifact");
	}

	private Object[] createInput(Map<String, Set<IAbstractArtifact>> relations) {
		for (Entry<String, Set<IAbstractArtifact>> entry : relations.entrySet()) {
			for (IAbstractArtifact a : entry.getValue()) {
				Assert.isNotNull(a.getFullyQualifiedName());
				Category category = categories.get(a);
				if (category == null) {
					categories.put(a, category = new Category());
				}
				category.add(entry.getKey());
			}
		}
		return categories.keySet().toArray(new Object[0]);
	}

	@Override
	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		getTableViewer().addSelectionChangedListener(
				new ISelectionChangedListener() {

					public void selectionChanged(SelectionChangedEvent event) {

						IStructuredSelection ss = (IStructuredSelection) event
								.getSelection();
						Object firstElement = ss.getFirstElement();
						if (firstElement != null) {
							IAbstractArtifact item = (IAbstractArtifact) firstElement;
							messageLabel.setText(item.getFullyQualifiedName());
							messageLabel.getParent().layout();
						}
					}
				});
		return control;
	}

	@Override
	protected Label createMessageArea(Composite composite) {
		messageLabel = super.createMessageArea(composite);
		return messageLabel;
	}

	@Override
	protected int getTableStyle() {
		return super.getTableStyle() ^ SWT.SINGLE | SWT.MULTI;
	}

	private String join(Category category) {
		if (category == null) {
			return "";
		}
		Iterator<String> it = category.iterator();
		if (!it.hasNext()) {
			return "";
		}
		StringBuilder builder = new StringBuilder(it.next());
		while (it.hasNext()) {
			builder.append(", ").append(it.next());
		}
		return builder.toString();
	};

	class Category extends TreeSet<String> {
		private static final long serialVersionUID = 1L;
	}
}
