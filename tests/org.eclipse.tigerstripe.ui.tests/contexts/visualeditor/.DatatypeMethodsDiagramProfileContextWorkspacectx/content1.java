/*******************************************************************************
 * Created with Tigerstripe(tm) Workbench v.0.7.0.201107012345
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * DO NOT EDIT THIS FILE - Created with Tigerstripe(tm) Workbench
 *
 *******************************************************************************/
package com.mycompany;


/**
 * 
 *
 * @tigerstripe.datatype
 *		isAbstract = "false"
 *      isSingleExtensionType="false"
 *      isSessionFactoryMethods="false"
 *		implements = ""
 * @tigerstripe.interface package = "$artifact.IStandardSpecifics.InterfaceProperties.get("
 *      generate = "true"
 * * 
 */
public abstract class Datatype1
 {


    /**
     * 
     * @tigerstripe.label
     *  value = '"0"'
     *  
     *
     * 
     */
    public final static String literal0 = "0";



    /**
     * 
     * @tigerstripe.field 
     *  isOptional = "false"
     *  isReadOnly = "false"
     *  isOrdered = "false"
     *  isUnique = "true"
     *  typeMultiplicity = "1"
     *   ref-by = "value"
     *
     *
     * 
     */
	public String attribute0;



    /**
     * 
     * @tigerstripe.method 
     *  iteratorReturn = "false"
     *  isOptional = "false"
	 *  isAbstract = "false"
     *  isOrdered = "false"
     *  isUnique = "false"
     *  typeMultiplicity = "0..1"
     *  returnName = ""
     *  visibility = "public"
     *
     * 
     * @tigerstripe.property ts.id = "ossj.method"
     *   simple = "true"
     *   byTemplates = "false"
     *   byTemplatesBestEffort = "false"
     *   byTemplate = "false"
     *   byTemplateBestEffort = "false"
     *   simpleByKey = "false"
     *   bulkAtomic = "false"
     *   bulkBestEffort = "false"
     *   instanceMethod = "false"
     *   bulkAtomicByKeys = "false"
     *   bulkBestEffortByKeys = "false"
     *
     */
	public abstract void method0(
			)
		;
}