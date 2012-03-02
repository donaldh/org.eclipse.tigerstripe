package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.wizards;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;

public class AddRelatedArtifactsWizard extends Wizard {

	private final BitSet creationMask;
	private final IAbstractArtifact[] artifacts;
	private AddRelatedArtifactsPage addRelatedArtifactsPage;
	private final RelatedCollector collector;
	private FilterArtifactsPage filterArtifactsPage;

	public AddRelatedArtifactsWizard(BitSet creationMask,
			IAbstractArtifact[] artifacts, RelatedCollector collector) {
		this.creationMask = creationMask;
		this.artifacts = artifacts;
		this.collector = collector;
		setWindowTitle("Add Related Artifacts");
	}

	@Override
	public void addPages() {
		addRelatedArtifactsPage = new AddRelatedArtifactsPage(
				artifacts, creationMask, collector);
		addPage(addRelatedArtifactsPage);
		filterArtifactsPage = new FilterArtifactsPage(addRelatedArtifactsPage);
		addPage(filterArtifactsPage);
	}

	Collection<IAbstractArtifact> result;
	
	@Override
	public boolean performFinish() {
		CheckboxTableViewer viewer = filterArtifactsPage.getViewer();
		if (viewer != null && viewer.getInput() != null) {
			result = new ArrayList<IAbstractArtifact>();
			Object[] checkedElements = viewer.getCheckedElements();
			for (Object obj : checkedElements) {
				if (obj instanceof IAbstractArtifact) {
					result.add((IAbstractArtifact) obj);
				}
			}
		} else {
			//it does mean that we haven't gone to filter page
			result = collector.collect(addRelatedArtifactsPage);
		}
		return true;
	}

	public AddRelatedArtifactsPage getAddRelatedArtifactsPage() {
		return addRelatedArtifactsPage;
	}

	public Collection<IAbstractArtifact> getResult() {
		return result;
	}
}
