package org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.scope;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.ISegmentScope;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;

public class PatternLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		if (element instanceof ISegmentScope.ScopeAnnotationPattern) {
			ISegmentScope.ScopeAnnotationPattern pattern = (ISegmentScope.ScopeAnnotationPattern) element;

			AnnotationType type = AnnotationPlugin.getManager().getType(
					Util.packageOf(pattern.annotationID),
					Util.nameOf(pattern.annotationID));
			return type.getName();
		} else if (element instanceof ISegmentScope.ScopePattern) {
			ISegmentScope.ScopePattern pattern = (ISegmentScope.ScopePattern) element;
			return pattern.pattern;
		} else if (element instanceof ISegmentScope.ScopeStereotypePattern) {
			ISegmentScope.ScopeStereotypePattern pattern = (ISegmentScope.ScopeStereotypePattern) element;
			return pattern.stereotypeName;
		}
		return "<unknown>";
	}
}