package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.common;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.tigerstripe.workbench.sdk.internal.contents.AnnotationTypeContribution;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public abstract class CommonDetailsPage {

	protected Table usageTable;
	protected TableViewer usageViewer;
	
	class UsageContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			Map<AnnotationTypeContribution,IResource> map = (Map<AnnotationTypeContribution,IResource>) inputElement;
			Collection inputColl = map.entrySet();
			return inputColl.toArray();
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}
	
	
	class UsageLabelProvider extends LabelProvider implements
	ITableLabelProvider {

		public String getColumnText(Object obj, int index) {
			AnnotationTypeContribution field = (AnnotationTypeContribution) ((Entry)obj).getKey();
			if (index == 0){
				return field.getName();
			}
			if (index == 1){
				return ((IResource) ((Entry)obj).getValue()).getProjectRelativePath().toString();
			}
			return field.getContributor().toString();

		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}
	}
	
	protected void createUsageTable(Composite parent, FormToolkit toolkit ) {
		toolkit.createLabel(parent, "Annotation Usage");
		Composite composite = toolkit.createComposite(parent);
		GridLayout tw = new GridLayout();
		tw.numColumns = 2;
		composite.setLayout(tw);
		TableWrapData gd = new TableWrapData(TableWrapData.FILL_GRAB);
		//gd.colspan = 2;
		composite.setLayoutData(gd);

		usageTable = toolkit.createTable(composite, SWT.FULL_SELECTION);
		GridData td = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL
				| GridData.HORIZONTAL_ALIGN_BEGINNING);
		td.verticalSpan = 5;
		td.heightHint = 140;
		usageTable.setLayoutData(td);
		usageTable.setHeaderVisible(true);
		usageTable.setLinesVisible(true);

		TableColumn usageTypeColumn = new TableColumn(usageTable, SWT.NULL);
		usageTypeColumn.setWidth(350);
		usageTypeColumn.setText("Annotation Name");
		
		TableColumn usageResourceColumn = new TableColumn(usageTable, SWT.NULL);
		usageResourceColumn.setWidth(350);
		usageResourceColumn.setText("Resource");
		
		TableColumn usageProjectColumn = new TableColumn(usageTable, SWT.NULL);
		usageProjectColumn.setWidth(350);
		usageProjectColumn.setText("Contributer");
		
		
		
		usageViewer = new TableViewer(usageTable);
		usageViewer.setContentProvider(new UsageContentProvider());
		usageViewer.setLabelProvider(new UsageLabelProvider());
		usageViewer.getTable().setSortColumn(usageTypeColumn);
		toolkit.paintBordersFor(composite);
	}
	
	
	
}
