package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import static org.eclipse.tigerstripe.workbench.ui.EclipsePlugin.getClipboard;
import static org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions.CopyPasteUtils.copyMemebers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.IAction;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IField;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMember;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Attribute;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Literal;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Method;
import org.eclipse.ui.IObjectActionDelegate;

public class CopyAction extends BaseDiagramPartAction implements
		IObjectActionDelegate {

	public void run(IAction action) {
		List<IMember> members = new ArrayList<IMember>(
				mySelectedElements.length);
		for (EditPart editPart : mySelectedElements) {
			EObject element = ((View) editPart.getModel()).getElement();

			if (element instanceof Attribute) {
				Attribute attribute = (Attribute) element;
				IAbstractArtifact artifact = getArtifact(attribute);
				if (artifact != null) {
					IField field = findMemberByName(artifact.getFields(),
							attribute.getName());
					if (field != null) {
						members.add(field);
					}
				}
			} else if (element instanceof Method) {
				Method method = (Method) element;
				IAbstractArtifact artifact = getArtifact(method);
				if (artifact != null) {
					IMethod iliteral = findMethodByLabel(artifact.getMethods(),
							method.getLabelString());
					if (method != null) {
						members.add(iliteral);
					}
				}
			} else if (element instanceof Literal) {
				Literal literal = (Literal) element;
				IAbstractArtifact artifact = getArtifact(literal);
				if (artifact != null) {
					ILiteral iliteral = findMemberByName(
							artifact.getLiterals(), literal.getName());
					if (literal != null) {
						members.add(iliteral);
					}
				}
			}
		}
		copyMemebers(members.toArray(), getClipboard());
	}

	public IAbstractArtifact getArtifact(EObject member) {
		EObject eContainer = member.eContainer();
		if (eContainer instanceof AbstractArtifact) {
			try {
				return ((AbstractArtifact) eContainer)
						.getCorrespondingIArtifact();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		return null;
	}

	public <T extends IMember> T findMemberByName(Collection<T> members,
			String name) {
		for (T member : members) {
			if (member.getName().equals(name)) {
				return member;
			}
		}
		return null;
	}

	public IMethod findMethodByLabel(Collection<IMethod> methods, String label) {
		for (IMethod method : methods) {
			if (method.getLabelString().equals(label)) {
				return method;
			}
		}
		return null;
	}

	protected boolean isEnabled() {
		if (mySelectedElements == null) {
			return false;
		}
		for (EditPart editPart : mySelectedElements) {
			View view = (View) editPart.getModel();
			EObject element = view.getElement();
			if (element instanceof Attribute || element instanceof Method
					|| element instanceof Literal) {
				return true;
			}
		}
		return false;
	}

}
