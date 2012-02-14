package org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.scope;

import java.util.Comparator;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.Kind;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope.ScopePattern;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.SegmentEditor;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.TigerstripeSegmentSectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class ScopeSection extends TigerstripeSegmentSectionPart {

	public ScopeSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit, int style) {
		super(page, parent, toolkit, style);
	}

	protected void sortButtonSelected(int type, Kind kind, Viewer viewer) {
		try {
			getScope().sort(type, PatternComparator.INSTANCE, kind);
			viewer.refresh();
			markPageModified();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	protected void sort(int type, String pattern) {
		
	}
	
	protected ISegmentScope getScope() throws TigerstripeException {
		return ((ScopePage) getPage()).getScope();
	}
	
	protected void markPageModified() {
		SegmentEditor editor = (SegmentEditor) getPage().getEditor();
		editor.pageModified();
	}
	
	static enum PatternComparator implements Comparator<Object> {
		
		INSTANCE;
		
		private final LabelProvider labelProvider = new PatternLabelProvider();

		public int compare(Object o1, Object o2) {
			return labelProvider.getText(o1).compareTo(
					labelProvider.getText(o2));
		}
	}
}
