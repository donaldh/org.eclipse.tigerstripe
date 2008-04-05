/**
 * <copyright>
 * </copyright>
 *
 * $Id: DownloadSiteXMLProcessor.java,v 1.2 2008/04/08 22:22:16 edillon Exp $
 */
package org.eclipse.tigerstripe.releng.downloadsite.schema.util;

import java.util.Map;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

import org.eclipse.tigerstripe.releng.downloadsite.schema.DownloadSitePackage;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class DownloadSiteXMLProcessor extends XMLProcessor {

	/**
	 * Public constructor to instantiate the helper.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DownloadSiteXMLProcessor() {
		super((EPackage.Registry.INSTANCE));
		DownloadSitePackage.eINSTANCE.eClass();
	}
	
	/**
	 * Register for "*" and "xml" file extensions the DownloadSiteResourceFactoryImpl factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected Map<String, Resource.Factory> getRegistrations() {
		if (registrations == null) {
			super.getRegistrations();
			registrations.put(XML_EXTENSION, new DownloadSiteResourceFactoryImpl());
			registrations.put(STAR_EXTENSION, new DownloadSiteResourceFactoryImpl());
		}
		return registrations;
	}

} //DownloadSiteXMLProcessor
