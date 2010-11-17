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
package org.eclipse.tigerstripe.workbench.ui.dependencies.internal.depenedencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyType;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.util.Cache;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.util.Cache.Creator;

public class Registry {

	private Diagram diagram;
	private Map<String, LayerDescriptor> initializedLayers;
	private Map<String, LayerDescriptor> loadedLayers;
	private Map<String, KindDescriptor> kinds;
	private IDependencySubject rootSubject;

	public Registry(IDependencySubject rootSubject, Diagram loadedDiagram) {
		this.rootSubject = rootSubject;
		build(rootSubject, loadedDiagram);
	}

	private void build(IDependencySubject initialSubject, Diagram loadedDiagram) {
		diagram = ModelsFactory.INSTANCE.createDiagram();
		initializedLayers = new HashMap<String, LayerDescriptor>();
		loadedLayers = new HashMap<String, LayerDescriptor>();
		kinds = new HashMap<String, KindDescriptor>();

		for (Layer loadedLayer : loadedDiagram.getLayers()) {
			initLoaded(loadedLayer);
		}

		// Initial logic
		initLayers(initialSubject);
		mergeLayers();
		mergeKinds(loadedDiagram);
		mergeLayersHistory(loadedDiagram);

		// Set current layer
		Layer initLayer;
		Layer loadedCurrentLayer = loadedDiagram.getCurrentLayer();
		if (loadedCurrentLayer != null) {
			LayerDescriptor layerInfo = initializedLayers
					.get(loadedCurrentLayer.getId());

			if (layerInfo != null) {
				initLayer = layerInfo.getLayer();
			} else {
				initLayer = initializedLayers.get(initialSubject.getId())
						.getLayer();
			}
		} else {
			initLayer = initializedLayers.get(initialSubject.getId())
					.getLayer();
		}
		diagram.setCurrentLayer(initLayer);
		diagram.eAdapters().add(contentListener);
	}

	private void mergeLayersHistory(Diagram loadedDiagram) {

		for (Layer l : loadedDiagram.getLayersHistory()) {
			LayerDescriptor ld = initializedLayers.get(l.getId());
			if (ld != null) {
				diagram.getLayersHistory().add(ld.getLayer());
			}
		}
	}

	private void mergeKinds(Diagram loadedDiagram) {

		EList<Kind> loadedKinds = loadedDiagram.getKinds();
		Map<String, Kind> loadedKindsMap = new HashMap<String, Kind>(
				loadedKinds.size());

		for (Kind loadedKind : loadedKinds) {
			loadedKindsMap.put(loadedKind.getId(), loadedKind);
		}

		for (Kind kind : diagram.getKinds()) {
			Kind loadedKind = loadedKindsMap.get(kind.getId());
			if (loadedKind == null) {
				continue;
			}
			kind.setStyle(loadedKind.getStyle());
			kind.setUseCustomStyle(loadedKind.isUseCustomStyle());
		}
	}

	private void mergeLayers() {

		for (Entry<String, LayerDescriptor> entry : initializedLayers
				.entrySet()) {

			LayerDescriptor loadedLayerInfo = loadedLayers.get(entry.getKey());

			if (loadedLayerInfo != null) {
				LayerDescriptor layerInfo = entry.getValue();
				layerInfo.getLayer().setWasLayouting(
						loadedLayerInfo.getLayer().isWasLayouting());
				mergeSubjects(loadedLayerInfo, layerInfo);
				addSimpleShapes(loadedLayerInfo, layerInfo);
			}
		}
	}

	private void mergeSubjects(LayerDescriptor loadedLayerInfo,
			LayerDescriptor layerInfo) {
		LayerData loadedData = loadedLayerInfo.getLayerData();
		for (Shape shape : layerInfo.getLayer().getShapes()) {
			if (shape instanceof Subject) {
				Subject subject = (Subject) shape;

				SubjectDescriptor sd = loadedData.get(subject.getExternalId());

				if (sd == null) {
					continue;
				}

				Subject loadedSubject = sd.getSubject();

				subject.setLocation(loadedSubject.getLocation());
				subject.setSize(loadedSubject.getSize());
				subject.setStyle(loadedSubject.getStyle());
				subject.setUseCustomStyle(loadedSubject.isUseCustomStyle());
			}
		}
	}

	private void addSimpleShapes(LayerDescriptor loadedLayerInfo,
			LayerDescriptor layerInfo) {

		List<Shape> toAdd = new ArrayList<Shape>();

		for (Shape shape : loadedLayerInfo.getLayer().getShapes()) {
			if (!(shape instanceof Subject)) {

				for (Connection con : shape.getTargetConnections()) {
					Shape source = con.getSource();
					if (source instanceof Subject) {
						Subject loadedSourceSubject = (Subject) source;
						SubjectDescriptor sd = layerInfo.getLayerData().get(
								loadedSourceSubject.getExternalId());
						if (sd != null) {
							Subject sourceSubject = sd.getSubject();
							con.setSource(sourceSubject);
							sourceSubject.getSourceConnections().add(con);
							toAdd.add(shape);
						}
					}
				}

				for (Connection con : shape.getSourceConnections()) {
					Shape target = con.getTarget();
					if (target instanceof Subject) {
						Subject loadedTargetSubject = (Subject) target;
						SubjectDescriptor sd = layerInfo.getLayerData().get(
								loadedTargetSubject.getExternalId());
						if (sd != null) {
							Subject targetSubject = sd.getSubject();
							con.setTarget(targetSubject);
							targetSubject.getTargetConnections().add(con);
							toAdd.add(shape);
						}
					}
				}
			}
		}
		layerInfo.getLayer().getShapes().addAll(toAdd);
	}

	private void initLoaded(Layer layer) {
		LayerData loadedSubjects = new LayerData();
		LayerDescriptor layerInfo = new LayerDescriptor(layer, loadedSubjects);
		loadedLayers.put(layer.getId(), layerInfo);

		for (Shape shape : layer.getShapes()) {
			if (shape instanceof Subject) {
				Subject subject = (Subject) shape;
				loadedSubjects.put(subject.getExternalId(),
						new SubjectDescriptor(null, subject));
			}
		}
	}

	public void initLayers(IDependencySubject root) {

		LayerDescriptor layerInfo = initializedLayers.get(root.getId());

		if (layerInfo != null) {
			return;
		}
		Layer layer = ModelsFactory.INSTANCE.createLayer();
		layer.setId(root.getId());
		diagram.getLayers().add(layer);

		LayerData layerData = new LayerData();
		layerInfo = new LayerDescriptor(layer, layerData);
		initializedLayers.put(layer.getId(), layerInfo);

		Subject rootSubject = findOrCreate(layerData, root);
		rootSubject.setMaster(true);
		layer.getShapes().add(rootSubject);

		loadDependenciesRecursive(rootSubject, new HashSet<Subject>());

		// Set<IDependencySubject> dependencies = root.getDependencies();
		// for (IDependencySubject dependency : dependencies) {
		// Utils.link(rootSubject, findOrCreate(layerData, dependency));
		// }
		//
		// for (IDependencySubject dependency : dependencies) {
		// Subject subject = layerData.get(dependency.getId())
		// .getSubject();
		// for (IDependencySubject subDependency : dependency
		// .getDependencies()) {
		// SubjectDescriptor sd = layerData.get(subDependency
		// .getId());
		// if (sd != null) {
		// Utils.link(subject, sd.getSubject());
		// }
		// }
		// }

		for (Entry<String, SubjectDescriptor> e : layerData.entrySet()) {

			// Subject subject = e.getValue().getSubject();
			IDependencySubject externalSubject = e.getValue()
					.getExternalSubject();

			// layer.getShapes().add(subject);
			initLayers(externalSubject);
		}
	}

	private void loadDependenciesRecursive(Subject subject, Set<Subject> seen) {

		if (!seen.add(subject)) {
			return;
		}

		LayerDescriptor loadedLayerDescriptor = loadedLayers.get(subject
				.getParentLayer().getId());
		SubjectDescriptor oldSubject = null;
		if (loadedLayerDescriptor != null) {
			oldSubject = loadedLayerDescriptor.getLayerData().get(
					subject.getExternalId());
		}

		if (subject.isMaster()
				|| (oldSubject != null && oldSubject.getSubject().isLoaded())) {
			loadDependencies(subject);

			for (Connection con : subject.getTargetConnections()) {
				Shape shape = con.getSource();
				if (shape instanceof Subject) {
					loadDependenciesRecursive((Subject) shape, seen);
				}
			}
		}
	}

	private Subject findOrCreate(Map<String, SubjectDescriptor> created,
			IDependencySubject external) {
		SubjectDescriptor sd = created.get(external.getId());
		if (sd == null) {
			Subject subject = Utils.createFromExternalModel(external);
			sd = new SubjectDescriptor(external, subject);

			IDependencyType type = external.getType();
			if (type != null) {
				String typeId = type.getId();
				KindDescriptor kd = kinds.get(typeId);
				if (kd == null) {
					Kind kind = ModelsFactory.INSTANCE.createKind();
					kind.setId(type.getId());
					kd = new KindDescriptor(type, kind);
					kind.setStyle(EcoreUtil.copy(kd.getDefaultStyle()));
					diagram.getKinds().add(kind);
					kinds.put(typeId, kd);
				}
				subject.setKind(kd.getKind());
			}
			created.put(external.getId(), sd);
		}
		return sd.getSubject();
	}

	public LayerDescriptor getLayerInfo(String id) {
		return initializedLayers.get(id);
	}

	public KindDescriptor getKindType(String id) {
		return kinds.get(id);
	}

	public void addDependency(IDependencySubject from, IDependencySubject to) {

		for (Entry<String, LayerDescriptor> e : initializedLayers.entrySet()) {

			LayerData layerData = e.getValue().getLayerData();

			SubjectDescriptor fromDescriptor = layerData.get(from.getId());

			if (fromDescriptor == null) {
				continue;
			}

			Subject fromShape = findOrCreate(layerData, from);
			Subject toShape = findOrCreate(layerData, to);

			EList<Shape> shapes = fromShape.getParentLayer().getShapes();
			if (!shapes.contains(toShape)) {
				shapes.add(toShape);
			}
			Utils.linkWithCheck(fromShape, toShape);
		}
	}

	public void removeDependency(IDependencySubject from, IDependencySubject to) {

		for (Entry<String, LayerDescriptor> e : initializedLayers.entrySet()) {
			LayerData layerData = e.getValue().getLayerData();

			SubjectDescriptor fromDescriptor = layerData.get(from.getId());
			SubjectDescriptor toDescriptor = layerData.get(to.getId());

			if (fromDescriptor == null || toDescriptor == null) {
				continue;
			}
			Subject toShape = toDescriptor.getSubject();
			Subject fromShape = fromDescriptor.getSubject();
			Utils.unLink(fromShape, toShape);
			// Delete orphan
			if (notContaintSubjects(toShape.getTargetConnections())
					&& notContaintSubjects(toShape.getSourceConnections())) {
				fromShape.getParentLayer().getShapes().remove(toShape);
				layerData.remove(toShape.getExternalId());
			}
		}
	}

	private boolean notContaintSubjects(Iterable<Connection> connections) {
		for (Connection connection : connections) {
			if (connection instanceof Subject) {
				return true;
			}
		}
		return false;
	}

	public void update() {
		diagram.eAdapters().remove(contentListener);
		build(rootSubject, diagram);
	}

	public void update(IDependencySubject newRoot) {
		rootSubject = newRoot;
		update();
	}

	public Set<Subject> loadDependencies(Subject forSubject) {

		if (forSubject.isLoaded()) {
			return Collections.emptySet();
		}

		LayerDescriptor layerDescriptor = initializedLayers.get(forSubject
				.getParentLayer().getId());

		if (layerDescriptor == null) {
			return Collections.emptySet();
		}

		final LayerData layerData = layerDescriptor.getLayerData();
		SubjectDescriptor subjectDescriptor = layerData.get(forSubject
				.getExternalId());

		if (subjectDescriptor == null) {
			return Collections.emptySet();
		}

		Cache<String, Set<IDependencySubject>> dependencyCache = new Cache<String, Set<IDependencySubject>>(
				new Creator<String, Set<IDependencySubject>>() {

					public Set<IDependencySubject> create(String subjectId) {
						return layerData.get(subjectId).getExternalSubject()
								.getDependencies();
					}

				});

		Set<IDependencySubject> dependencies = dependencyCache
				.get(subjectDescriptor.getExternalSubject().getId());

		Set<Subject> toAdd = new HashSet<Subject>();
		for (IDependencySubject dependency : dependencies) {
			boolean needAdd = !layerData.containsKey(dependency.getId());
			Subject subject = findOrCreate(layerData, dependency);
			if (needAdd) {
				toAdd.add(subject);
			}
			// Utils.linkWithCheck(subjectDescriptor.getSubject(), subject);
		}

		Set<Subject> affectedSubjects = new HashSet<Subject>();

		for (IDependencySubject dependency : dependencies) {
			SubjectDescriptor sd1 = layerData.get(dependency.getId());

			if (!toAdd.contains(sd1.getSubject())) {
				continue;
			}

			final Subject subject1 = sd1.getSubject();
			affectedSubjects.add(subject1);

			for (Entry<String, SubjectDescriptor> e : layerData.entrySet()) {
				if (e.getKey().equals(dependency.getId())) {
					continue;
				}
				SubjectDescriptor sd2 = e.getValue();

				final Subject subject2 = sd2.getSubject();
				final IDependencySubject externalSubject1 = sd1
						.getExternalSubject();
				final IDependencySubject externalSubject2 = sd2
						.getExternalSubject();

				if (isDependency(externalSubject1, externalSubject2,
						dependencyCache)) {
					if (!Utils.isLinked(subject1, subject2)) {
						Utils.link(subject1, subject2);
						affectedSubjects.add(subject1);
						affectedSubjects.add(subject2);
					}
				}

				if (isDependency(externalSubject2, externalSubject1,
						dependencyCache)) {
					if (!Utils.isLinked(subject2, subject1)) {
						Utils.link(subject2, subject1);
						affectedSubjects.add(subject2);
						affectedSubjects.add(subject1);
					}
				}

			}
		}
		layerDescriptor.getLayer().getShapes().addAll(toAdd);
		forSubject.setLoaded(true);
		return affectedSubjects;
	}

	public Set<Subject> collapseDependencies(Subject forSubject) {
		return collapseDependenciesInternal(forSubject, new HashSet<Subject>());
	}

	private Set<Subject> collapseDependenciesInternal(Subject forSubject,
			Set<Subject> seen) {

		if (!seen.add(forSubject)) {
			return Collections.emptySet();
		}

		if (!forSubject.isLoaded()) {
			return Collections.emptySet();
		}

		LayerDescriptor layerDescriptor = initializedLayers.get(forSubject
				.getParentLayer().getId());

		if (layerDescriptor == null) {
			return Collections.emptySet();
		}
		forSubject.setLoaded(false);

		Set<Subject> affectedSubjects = new HashSet<Subject>();
		LayerData layerData = layerDescriptor.getLayerData();
		SubjectDescriptor fromD = layerData.get(forSubject.getExternalId());
		Subject fromShape = fromD.getSubject();

		Set<Subject> toShapes = new HashSet<Subject>();

		for (IDependencySubject to : fromD.getExternalSubject()
				.getDependencies()) {

			SubjectDescriptor toDescriptor = layerData.get(to.getId());

			if (toDescriptor == null) {
				continue;
			}

			toShapes.add(toDescriptor.getSubject());
		}

		Set<Subject> toRemoves = new HashSet<Subject>();
		Set<Subject> linkedWithOthers = new HashSet<Subject>();

		for (Subject toShape : toShapes) {
			for (Connection con : toShape.getSourceConnections()) {
				Shape target = con.getTarget();
				if (target instanceof Subject) {
					Subject targetSubject = (Subject) target;
					if (!toShapes.contains(targetSubject)
							&& targetSubject.isLoaded()) {
						linkedWithOthers.add(toShape);
					}
				}
			}
		}

		for (Subject toShape : toShapes.toArray(new Subject[0])) {

			if (toShape.isMaster()) {
				continue;
			}

			// Delete orphan
			boolean canRemove;
			if (linkedWithOthers.contains(toShape)) {
				canRemove = false;
				continue;
			} else {
				canRemove = true;
			}
			for (Connection con : toShape.getSourceConnections()) {
				Shape target = con.getTarget();
				if (target instanceof Subject) {
					Subject targetSubject = (Subject) target;
					if ((linkedWithOthers.contains(targetSubject) || !toShapes
							.contains(targetSubject))
							&& targetSubject.isLoaded()) {
						canRemove = false;
						break;
					}
				}
			}

			if (canRemove) {

				// Unlink
				for (Connection con : toShape.getSourceConnections().toArray(
						new Connection[0])) {
					Shape target = con.getTarget();
					target.getTargetConnections().remove(con);
					if (target instanceof Subject) {
						affectedSubjects.add((Subject) target);
					}
				}

				toRemoves.add(toShape);
			}
		}

		for (Subject toRemove : toRemoves) {
			if (toRemove.isLoaded()) {
				affectedSubjects.addAll(collapseDependenciesInternal(toRemove,
						seen));
			}
		}

		List<Shape> layerShapes = fromShape.getParentLayer().getShapes();
		for (Subject toRemove : toRemoves) {

			for (Connection con : toRemove.getTargetConnections()) {
				Shape source = con.getSource();
				if (!(source instanceof Subject)) {
					layerShapes.remove(source);
				} else {
					source.getSourceConnections().remove(con);
					affectedSubjects.add((Subject) source);
				}
			}

			layerShapes.remove(toRemove);
			layerData.remove(toRemove.getExternalId());
		}
		return affectedSubjects;
	}

	private boolean isDependency(IDependencySubject subject1,
			IDependencySubject subject2,
			Cache<String, Set<IDependencySubject>> cache) {

		Set<IDependencySubject> dependencies = cache.get(subject1.getId());

		for (IDependencySubject ds : dependencies) {
			if (ds.getId().equals(subject2.getId())) {
				return true;
			}
		}
		return false;
	}

	public Diagram getDiagram() {
		return diagram;
	}

	Set<IModelChangeListener> modelChangeListeners = new LinkedHashSet<IModelChangeListener>();

	public boolean addModelChangeListener(IModelChangeListener listener) {
		return modelChangeListeners.add(listener);
	}

	public boolean removeModelChangeListener(IModelChangeListener listener) {
		return modelChangeListeners.remove(listener);
	}

	private void fireModelChange() {
		for (IModelChangeListener l : modelChangeListeners) {
			l.modelChanged();
		}
	}

	public static interface IModelChangeListener {
		void modelChanged();
	}

	private final EContentAdapter contentListener = new EContentAdapter() {

		@Override
		public void notifyChanged(Notification notification) {
			int eventType = notification.getEventType();
			if (eventType == Notification.RESOLVE
					|| eventType == Notification.REMOVING_ADAPTER) {
				return;
			}
			fireModelChange();
		}

	};
}
