/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.figures.SubjectFigure;
import org.eclipse.tigerstripe.workbench.ui.dependencies.diagram.ui.properties.SubjectPropertySource;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.SubjectStyleService;
import org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies.Utils;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.DependenciesPackage;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.ShapeStyle;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject;
import org.eclipse.ui.views.properties.IPropertySource;

public class SubjectEditPart extends ShapeEditPart {

	private AdapterImpl loadedAdapter;
	private EContentAdapter kindAdapter;
	private AdapterImpl customStyleAdapter;

	@Override
	public void activate() {
		super.activate();
		loadedAdapter = new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				if (msg.getEventType() == Notification.SET) {
					if (DependenciesPackage.Literals.SUBJECT__LOADED.equals(msg
							.getFeature())) {
						updateTitleComment((SubjectFigure) getFigure());
						refreshVisuals();
					}
				}
			}

		};
		getSubject().eAdapters().add(loadedAdapter);

		kindAdapter = new EContentAdapter() {
			@Override
			public void notifyChanged(Notification msg) {
				if (msg.getEventType() == Notification.SET) {
					Object feature = msg.getFeature();
					if (DependenciesPackage.Literals.SHAPE_STYLE__BACKGROUND_COLOR
							.equals(feature)
							|| DependenciesPackage.Literals.SHAPE_STYLE__FOREGROUND_COLOR
									.equals(feature)
							|| DependenciesPackage.Literals.KIND__STYLE
									.equals(feature)
							|| DependenciesPackage.Literals.KIND__USE_CUSTOM_STYLE
									.equals(feature)) {
						updateStyle();
						refreshVisuals();
					}

				}
			}
		};

		Utils.getDiagram(this).eAdapters().add(kindAdapter);

		customStyleAdapter = new AdapterImpl() {
			@Override
			public void notifyChanged(Notification msg) {
				if (msg.getEventType() == Notification.SET) {
					Object feature = msg.getFeature();
					if (DependenciesPackage.Literals.SUBJECT__USE_CUSTOM_STYLE
							.equals(feature)) {

						updateStyle();
						refreshVisuals();
					}
				}

			}
		};

		getSubject().eAdapters().add(customStyleAdapter);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		if (loadedAdapter != null) {
			getSubject().eAdapters().remove(loadedAdapter);
		}
		if (kindAdapter != null) {
			Utils.getDiagram(this).eAdapters().remove(kindAdapter);
		}
		if (customStyleAdapter != null) {
			getSubject().eAdapters().remove(customStyleAdapter);
		}
	}

	public Subject getSubject() {
		return (Subject) getModel();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object getAdapter(Class key) {
		if (key.isAssignableFrom(IDependencySubject.class)) {
			// return getSubject().getSubject();
			throw new UnsupportedOperationException();
		} else if (key.isAssignableFrom(IPropertySource.class)) {
			return new SubjectPropertySource(getSubject(), getStyleService());
		} else {
			return super.getAdapter(key);
		}
	}

	private void updateTitleComment(SubjectFigure sf) {
		Subject subject = getSubject();
		if (!subject.isMaster() && subject.isLoaded()) {
			sf.setTitleComment("loaded");
		} else {
			sf.setTitleComment(null);
		}
	}

	@Override
	protected IFigure doCreateFigure() {
		SubjectFigure subjectFigure = new SubjectFigure(this);
		updateTitleComment(subjectFigure);
		return subjectFigure;
	}

	@Override
	protected ShapeStyle getStyle() {
		return getStyleService().getStyle(getSubject());
	}

	private SubjectStyleService getStyleService() {
		return Utils.getService(getViewer(), SubjectStyleService.class);
	}

	@Override
	public void performRequest(Request req) {
		if (RequestConstants.REQ_OPEN.equals(req.getType())) {

			Subject subject = getSubject();
			if (subject.isMaster()) {
				return;
			}
			if (subject.isLoaded()) {
				Utils.collapseDependencies(this);
			} else {
				Utils.loadDependencies(this);
			}
		}
	}

}
