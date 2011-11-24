package org.eclipse.tigerstripe.annotation.core.storage.internal;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
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
							return new XMISaveImpl(createXMLHelper());
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
				documentRoot = null;
				extent.clear();
			} else {
				super.endElement(uri, localName, name);
			}
		}
	}
}
