/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request;

import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequest;

public interface IArtifactSetFeatureRequest extends IModelChangeRequest {

	public final static String COMMENT_FEATURE = "comment";
	public final static String EXTENDS_FEATURE = "extends";
	public final static String RETURNED_TYPE = "returnedType";
	public final static String BASE_TYPE = "baseType";
	public final static String ISABSTRACT_FEATURE = "isAbstract";

	public final static String AEND = "aEnd";
	public final static String AENDName = "aEndName";
	public final static String AENDType = "aEndType";
	public final static String AENDISORDERED = "aEndIsOrdered";
	public final static String AENDISUNIQUE = "aEndIsUnique";
	public final static String AENDNAVIGABLE = "aEndIsNavigable";
	public final static String AENDISCHANGEABLE = "aEndisChangeable";
	public final static String AENDAGGREGATION = "aEndAggregation";
	public final static String AENDMULTIPLICITY = "aEndMultiplicity";
	public final static String AENDVISIBILITY = "aEndVisibility";
	public final static String AENDCOMMENT = "aEndComment";
	

	public final static String ZEND = "zEnd";
	public final static String ZENDName = "zEndName";
	public final static String ZENDType = "zEndType";
	public final static String ZENDISORDERED = "zEndIsOrdered";
	public final static String ZENDISUNIQUE = "zEndIsUnique";
	public final static String ZENDNAVIGABLE = "zEndIsNavigable";
	public final static String ZENDISCHANGEABLE = "zEndisChangeable";
	public final static String ZENDAGGREGATION = "zEndAggregation";
	public final static String ZENDMULTIPLICITY = "zEndMultiplicity";
	public final static String ZENDVISIBILITY = "zEndVisibility";
	public final static String ZENDCOMMENT = "zEndComment";

	public void setArtifactFQN(String artifactFQN);

	public void setFeatureId(String featureID);

	public void setFeatureValue(String value);
}
