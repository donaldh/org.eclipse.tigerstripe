/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.components.md;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MasterDetails {

	public final Map<Object, IDetails> details;
	private final Collection<IKindResolver> keyResolvers;
	private final IDetails defaultDetails;

	/**
	 * For lazy initialize
	 */
	private final Set<IDetails> initialized = new HashSet<IDetails>();

	private IDetails currentDetails;

	public MasterDetails(final IDetails defaultDetails,
			Map<Object, IDetails> details,
			Collection<IKindResolver> keyResolvers) {

		this.details = defenseNull(details, "details");
		this.defaultDetails = defenseNull(defaultDetails, "defenseNull");
		this.keyResolvers = new ArrayList<IKindResolver>(defenseNull(
				keyResolvers, "keyResolvers"));
		((List<IKindResolver>) this.keyResolvers)
				.add(new DefaultKindResolver());
		currentDetails = defaultDetails;
	}

	private <T> T defenseNull(T value, String name) {
		if (value == null) {
			throw new IllegalArgumentException(String.format(
					"'%s' parameter was null.", name));
		}
		return value;
	}

	/**
	 * Switches the current object, which entails switching forms for this kind
	 * if necessary and fill in the form attributes of the new object or if
	 * target is null switch to default empty details
	 * 
	 * @param target
	 *            object to switch
	 */
	public void switchTo(Object target) {
		if (target == null) {
			switchToDefault();
			return;
		}

		IDetails details = resolveKind(target);
		switchTo(details);
		details.switchTarget(target);
	}

	private IDetails resolveKind(Object target) {
		for (IKindResolver resolver : keyResolvers) {
			if (resolver.canResolve(target)) {
				for (Map.Entry<Object, IDetails> e : details.entrySet()) {
					if (resolver.equalsKind(target, e.getKey())) {
						return e.getValue();
					}
				}
			}
		}

		throw new IllegalArgumentException(format(
				"Detials for kind of object '%s' is not register", target));
	}

	/**
	 * Switches to default empty details
	 */
	public void switchToDefault() {
		switchTo(defaultDetails);
	}

	private void switchTo(IDetails detail) {
		if (detail.equals(currentDetails)) {
			return;
		}
		if (initialized.add(detail)) {
			detail.init();
		}
		currentDetails.hide();
		currentDetails = detail;
		currentDetails.show();
	}
}
