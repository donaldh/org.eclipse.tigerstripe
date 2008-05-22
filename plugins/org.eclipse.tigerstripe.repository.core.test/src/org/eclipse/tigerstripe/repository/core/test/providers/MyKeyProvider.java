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
package org.eclipse.tigerstripe.repository.core.test.providers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.tigerstripe.repository.core.test.sample.Car;
import org.eclipse.tigerstripe.repository.core.test.sample.House;
import org.eclipse.tigerstripe.repository.core.test.sample.Region;
import org.eclipse.tigerstripe.repository.core.test.sample.Street;
import org.eclipse.tigerstripe.repository.core.test.sample.Village;
import org.eclipse.tigerstripe.repository.core.test.sample.Window;
import org.eclipse.tigerstripe.repository.manager.IKeyProvider;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;

public class MyKeyProvider implements IKeyProvider {

	public Object getKey(EObject obj) throws ModelCoreException {
		if (obj instanceof Region) {
			return ((Region) obj).getName();
		} else if (obj instanceof Village) {
			return ((Village) obj).getName();
		} else if (obj instanceof Street) {
			Street street = (Street) obj;
			if (!(street.eContainer() instanceof Village)) {
				throw new ModelCoreException("Street " + street
						+ " doesn't belong to a village");
			}
			Village vil = (Village) street.eContainer();
			return vil.getName() + "/" + street.getName();
		} else if (obj instanceof House) {
			House house = (House) obj;
			if (!(house.eContainer() instanceof Street)) {
				throw new ModelCoreException("House " + house
						+ " doesn't belong to a street");
			}
			Street street = (Street) house.eContainer();
			if (street.eContainer() instanceof Village) {
				throw new ModelCoreException("Street " + street
						+ " doesn't belong to a village");
			}
			Village vil = (Village) street.eContainer();
			return vil.getName() + "/" + street.getName() + "#"
					+ house.getNumber();
		} else if (obj instanceof Window) {
			Window win = (Window) obj;
			return win.getDirection();
		}
		throw new ModelCoreException("No key for: " + obj);
	}

	public boolean selects(EObject obj) {
		return obj instanceof Region || obj instanceof Village
				|| obj instanceof Street || obj instanceof Car
				|| obj instanceof House;
	}

}
