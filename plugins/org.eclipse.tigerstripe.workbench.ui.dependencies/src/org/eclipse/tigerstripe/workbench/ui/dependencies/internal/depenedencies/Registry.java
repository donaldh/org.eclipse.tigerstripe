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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencySubject;
import org.eclipse.tigerstripe.workbench.ui.dependencies.api.IDependencyType;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Connection;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Diagram;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Kind;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Layer;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Shape;
import org.eclipse.tigerstripe.workbench.ui.model.dependencies.Subject;

public class Registry {

	private final Map<String, LayerDescriptor> initializedLayers;
	private final Map<String, LayerDescriptor> loadedLayers;
	private final Map<String, KindDescriptor> kinds;

	private final Diagram diagram;

	public Registry(IDependencySubject initialSubject, Diagram newDiagram,
			Diagram loadedDiagram) {

		diagram = newDiagram;

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
						}
					}
				}
				toAdd.add(shape);
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

		LayerData createdSubjects = new LayerData();
		layerInfo = new LayerDescriptor(layer, createdSubjects);
		initializedLayers.put(layer.getId(), layerInfo);

		Subject rootSubject = findOrCreate(createdSubjects, root);
		rootSubject.setMaster(true);
		for (IDependencySubject dependency : root.getDependencies()) {
			Utils.link(rootSubject, findOrCreate(createdSubjects, dependency));
		}

		for (IDependencySubject dependency : root.getDependencies()) {
			Subject subject = createdSubjects.get(dependency.getId())
					.getSubject();
			for (IDependencySubject subDependency : dependency
					.getDependencies()) {
				SubjectDescriptor sd = createdSubjects.get(subDependency
						.getId());
				if (sd != null) {
					Utils.link(subject, sd.getSubject());
				}
			}
		}

		for (Entry<String, SubjectDescriptor> e : createdSubjects.entrySet()) {

			Subject subject = e.getValue().getSubject();
			IDependencySubject externalSubject = e.getValue()
					.getExternalSubject();

			layer.getShapes().add(subject);
			initLayers(externalSubject);
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
}
