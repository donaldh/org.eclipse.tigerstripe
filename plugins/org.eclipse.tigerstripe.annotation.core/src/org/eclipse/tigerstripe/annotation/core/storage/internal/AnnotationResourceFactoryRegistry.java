package org.eclipse.tigerstripe.annotation.core.storage.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.emf.ecore.xmi.impl.SAXXMIHandler;
import org.eclipse.emf.ecore.xmi.impl.XMILoadImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMISaveImpl;
import org.xml.sax.helpers.DefaultHandler;

public class AnnotationResourceFactoryRegistry implements
		Resource.Factory.Registry {

	private final Resource.Factory.Registry registry;

	public AnnotationResourceFactoryRegistry(Registry registry) {
		this.registry = registry;
	}

	public Map<String, Object> getContentTypeToFactoryMap() {
		return registry.getContentTypeToFactoryMap();
	}

	public Map<String, Object> getExtensionToFactoryMap() {
		return registry.getExtensionToFactoryMap();
	}

	public Factory getFactory(URI uri) {
		return wrap(registry.getFactory(uri));
	}

	public Factory getFactory(URI uri, String contentType) {
		return wrap(registry.getFactory(uri, contentType));
	}

	private Factory wrap(Factory factory) {
		if (factory instanceof XMIResourceFactoryImpl) {
			// To handle legacy annotation format.
			// Old files contains copy annotations definitions as custom content
			return new XMIResourceFactoryImpl() {

				@Override
				public Resource createResource(URI uri) {
					return new XMIResourceImpl(uri) {

						@Override
						protected XMLSave createXMLSave() {
							return new AnnotationXMISave(createXMLHelper());
						}

						@Override
						protected XMLLoad createXMLLoad() {
							return new XMILoadImpl(createXMLHelper()) {

								@Override
								protected DefaultHandler makeDefaultHandler() {
									return new AnnotationsXMLHandler(resource,
											helper, options);
								}
							};
						}
					};
				}
			};
		}
		return factory;
	}

	public Map<String, Object> getProtocolToFactoryMap() {
		return registry.getProtocolToFactoryMap();
	}

	private static final String ECORE_DEFINITION_TAG = "ecoreDefinition";

	public static class AnnotationsXMLHandler extends SAXXMIHandler {

		public AnnotationsXMLHandler(XMLResource xmiResource, XMLHelper helper,
				Map<?, ?> options) {
			super(xmiResource, helper, options);
		}

		@Override
		public void startElement(String uri, String localName, String name) {
			if (ECORE_DEFINITION_TAG.equals(name)) {
				// ignore
			} else {
				super.startElement(uri, localName, name);
			}
		}

		@Override
		public void endElement(String uri, String localName, String name) {
			if (ECORE_DEFINITION_TAG.equals(name)) {
				for (EObject object : extent) {
					if (object instanceof EPackage) {
						EPackage pack = (EPackage) object;
						String nsURI = pack.getNsURI();
						if (packageRegistry.getEPackage(nsURI) == null) {
							packageRegistry.put(nsURI, pack);
						}
					}
				}
				documentRoot = null;
				extent.clear();
			} else {
				super.endElement(uri, localName, name);
			}
		}
	}

	private static class AnnotationXMISave extends XMISaveImpl {

		public AnnotationXMISave(XMLHelper helper) {
			super(helper);
		}

		@Override
		public Object writeTopObjects(List<? extends EObject> contents) {
			doc.startElement(XMI_TAG_NS);
			Object mark = doc.mark();

			saveEcoreDefinition(getPackages(contents));

			for (int i = 0, size = contents.size(); i < size; i++) {
				EObject top = contents.get(i);
				EClass eClass = top.eClass();
				if (extendedMetaData == null
						|| featureTable.getDocumentRoot(eClass.getEPackage()) != eClass) {
					String name = helper.getQName(eClass);
					doc.startElement(name);
					root = top;
					saveElementID(top);
				} else {
					doc.startElement(null);
					root = top;
					saveFeatures(top);
					doc.addLine();
				}
			}

			doc.endElement();
			return mark;
		}

		protected List<EPackage> getPackages(List<? extends EObject> contents) {
			List<EPackage> packages = new ArrayList<EPackage>();
			for (EObject object : contents) {
				List<EPackage> newPackages = PackageFinder.getPackages(object);
				for (EPackage newPackage : newPackages) {
					if (!packages.contains(newPackage))
						packages.add(newPackage);
				}
			}
			return packages;
		}

		protected void saveEcoreDefinition(List<EPackage> packages) {
			doc.startElement(ECORE_DEFINITION_TAG);
			for (int i = 0, size = packages.size(); i < size; i++) {
				EObject top = packages.get(i);
				EClass eClass = top.eClass();
				if (extendedMetaData == null
						|| featureTable.getDocumentRoot(eClass.getEPackage()) != eClass) {
					String name = helper.getQName(eClass);
					doc.startElement(name);
					root = top;
					saveElementID(top);
				} else {
					doc.startElement(null);
					root = top;
					saveFeatures(top);
					doc.addLine();
				}
			}
			doc.endElement();
		}

		@Override
		protected Object writeTopObject(EObject top) {
			List<EObject> objects = new ArrayList<EObject>();
			objects.add(top);
			return writeTopObjects(objects);
		}
	}
}
