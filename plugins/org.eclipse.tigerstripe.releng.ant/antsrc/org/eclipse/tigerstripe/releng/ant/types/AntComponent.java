/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.releng.ant.types;

import org.eclipse.tigerstripe.releng.ant.builds.schema.BuildSchemaFactory;
import org.eclipse.tigerstripe.releng.ant.builds.schema.Bundle;
import org.eclipse.tigerstripe.releng.ant.builds.schema.Component;
import org.eclipse.tigerstripe.releng.ant.builds.schema.impl.ComponentImpl;

public class AntComponent extends ComponentImpl implements Component {

	public Bundle createBundle() {
		Bundle result = BuildSchemaFactory.eINSTANCE.createBundle();
		getBundle().add(result);
		return result;
	}
}
