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
package org.eclipse.tigerstripe.workbench.internal.core.model.ossj.specifics;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifactTag;
import org.eclipse.tigerstripe.workbench.internal.core.model.IAbstractArtifactInternal;
import org.eclipse.tigerstripe.workbench.internal.core.model.ManagedEntityDetails;
import org.eclipse.tigerstripe.workbench.internal.core.model.SessionFacadeArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.Tag;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IMethod.OssjEntityMethodFlavor;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ossj.IOssjSessionSpecifics;
@Deprecated
public class OssjSessionFacadeSpecifics extends OssjArtifactSpecifics implements
		IOssjSessionSpecifics {

	public final static String MANAGED_ENTITY_OVERRIDE_TAG = AbstractArtifactTag.PREFIX
			+ "managed-entity-overide";

	public OssjSessionFacadeSpecifics(IAbstractArtifactInternal artifact) {
		super(artifact);
	}

	@Override
	public void build() {
		super.build();
	}

	/**
	 * Reads all the overide definitions allowing to override method-level
	 * definitions for any managed entity
	 * 
	 * Something like:
	 * 
	 * @tigerstripe.managed-entity-overide entity="entity-fqn" methodId="xxxx"
	 *                                     <flavor>="true|optional|false:<optional-exceptions>"
	 */
	public void buildOverides() {
		Collection<Tag> tags = getArtifact().getTagsByName(
				MANAGED_ENTITY_OVERRIDE_TAG);
		for (Tag tag : tags) {
			Properties props = tag.getProperties();
			String managedEntityFqn = props.getProperty("entity");
			String methodId = props.getProperty("methodId");

			ManagedEntityDetails mEnt = (ManagedEntityDetails) ((SessionFacadeArtifact) getArtifact())
					.getManagedEntityByName(managedEntityFqn);
			if (mEnt != null) {
				for (Iterator iter = props.keySet().iterator(); iter.hasNext();) {
					String key = (String) iter.next();
					if (!"entity".equals(key) && !"methodId".equals(key)) {
						mEnt.getOveride().overideMethod(
								methodId,
								OssjEntityMethodFlavor.valueFromPojoLabel(key),
								new EntityMethodFlavorDetails(getArtifact(),
										props.getProperty(key)));
					}
				}
			} else {
				// TigerstripeRuntime.logInfoMessage("skipping " +
				// managedEntityFqn );
			}
		}
	}

}
