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

import org.eclipse.tigerstripe.releng.ant.builds.schema.Build;
import org.eclipse.tigerstripe.releng.ant.builds.schema.Component;
import org.eclipse.tigerstripe.releng.ant.builds.schema.impl.BuildImpl;

public class AntBuild extends BuildImpl implements Build {
	
	public Component createComponent() {
		Component result = new AntComponent();
		getComponent().add(result);
		return result;
	}
}
