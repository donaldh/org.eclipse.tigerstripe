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
package org.eclipse.tigerstripe.api.artifacts.model.ossj;

import org.eclipse.tigerstripe.api.external.model.IextMethod.OssjEntityMethodFlavor;

public interface IOssjFlavorDefaults {
	public final static OssjEntityMethodFlavor[] getMethodFlavors = {
			OssjEntityMethodFlavor.SIMPLEBYKEY,
			OssjEntityMethodFlavor.BULKATOMICBYKEYS,
			OssjEntityMethodFlavor.BYTEMPLATE,
			OssjEntityMethodFlavor.BYTEMPLATES };
	public final static String[] getMethodFlavorDefaults = {
			"true:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.ObjectNotFoundException", // OssjEntityMethodFlavor.SIMPLEBYKEY
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException", // OssjEntityMethodFlavor.BULKATOMICBYKEYS
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException", // OssjEntityMethodFlavor.BYTEMPLATE
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException" // OssjEntityMethodFlavor.BYTEMPLATES
	};
	public final static OssjEntityMethodFlavor[] setMethodFlavors = {
			OssjEntityMethodFlavor.SIMPLE, OssjEntityMethodFlavor.BULKATOMIC,
			OssjEntityMethodFlavor.BULKATOMICBYKEYS,
			OssjEntityMethodFlavor.BYTEMPLATE,
			OssjEntityMethodFlavor.BYTEMPLATES,
			OssjEntityMethodFlavor.BULKBESTEFFORT,
			OssjEntityMethodFlavor.BULKBESTEFFORTBYKEYS,
			OssjEntityMethodFlavor.BYTEMPLATEBESTEFFORT,
			OssjEntityMethodFlavor.BYTEMPLATESBESTEFFORT };
	public final static String[] setMethodFlavorDefaults = {
			"true:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.ObjectNotFoundException,javax.oss.OssSetException,javax.oss.OssResyncRequiredException", // OssjEntityMethodFlavor.SIMPLE
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException,javax.oss.OssSetException,javax.oss.OssResyncRequiredException,javax.ejb.DuplicateKeyException", // OssjEntityMethodFlavor.BULKATOMIC
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException,javax.oss.OssSetException", // OssjEntityMethodFlavor.BULKATOMICBYKEYS
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException,javax.oss.OssSetException", // OssjEntityMethodFlavor.BYTEMPLATE
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException,javax.oss.OssSetException,javax.ejb.DuplicateKeyException", // OssjEntityMethodFlavor.BYTEMPLATES
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException", // OssjEntityMethodFlavor.BULKBESTEFFORT
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException", // OssjEntityMethodFlavor.BULKBESTEFFORTBYKEYS
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException", // OssjEntityMethodFlavor.BYTEMPLATEBESTEFFORT
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException" // OssjEntityMethodFlavor.BYTEMPLATESBESTEFFORT
	};
	public final static OssjEntityMethodFlavor[] createMethodFlavors = {
			OssjEntityMethodFlavor.SIMPLE, OssjEntityMethodFlavor.BULKATOMIC,
			OssjEntityMethodFlavor.BULKATOMICBYKEYS,
			OssjEntityMethodFlavor.BULKBESTEFFORT,
			OssjEntityMethodFlavor.BULKBESTEFFORTBYKEYS,
			OssjEntityMethodFlavor.BYAUTONAMING };
	public final static String[] createMethodFlavorDefaults = {
			"true:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.DuplicateKeyException,javax.ejb.CreateException", // OssjEntityMethodFlavor.SIMPLE
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.DuplicateKeyException,javax.ejb.CreateException", // OssjEntityMethodFlavor.BULKATOMIC
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.DuplicateKeyException,javax.ejb.CreateException", // OssjEntityMethodFlavor.BULKATOMICBYKEYS
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException", // OssjEntityMethodFlavor.BULKBESTEFFORT
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException", // OssjEntityMethodFlavor.BULKBESTEFFORTBYKEYS
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.CreateException" // OssjEntityMethodFlavor.BYAUTONAMING
	};
	public final static OssjEntityMethodFlavor[] removeMethodFlavors = {
			OssjEntityMethodFlavor.SIMPLEBYKEY,
			OssjEntityMethodFlavor.BULKATOMICBYKEYS,
			OssjEntityMethodFlavor.BYTEMPLATE,
			OssjEntityMethodFlavor.BYTEMPLATES,
			OssjEntityMethodFlavor.BULKBESTEFFORTBYKEYS,
			OssjEntityMethodFlavor.BYTEMPLATEBESTEFFORT,
			OssjEntityMethodFlavor.BYTEMPLATESBESTEFFORT };
	public final static String[] removeMethodFlavorDefaults = {
			"true:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.ObjectNotFoundException,javax.ejb.RemoveException", // OssjEntityMethodFlavor.SIMPLEBYKEY
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.RemoveException", // OssjEntityMethodFlavor.BULKATOMICBYKEYS
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.RemoveException", // OssjEntityMethodFlavor.BYTEMPLATE
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.RemoveException", // OssjEntityMethodFlavor.BYTEMPLATES
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException", // OssjEntityMethodFlavor.BULKBESTEFFORTBYKEYS
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException", // OssjEntityMethodFlavor.BYTEMPLATEBESTEFFORT
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException" // OssjEntityMethodFlavor.BYTEMPLATESBESTEFFORT
	};
	public final static OssjEntityMethodFlavor[] customMethodFlavors = {
			OssjEntityMethodFlavor.SIMPLE, OssjEntityMethodFlavor.SIMPLEBYKEY,
			OssjEntityMethodFlavor.BULKATOMIC,
			OssjEntityMethodFlavor.BULKATOMICBYKEYS,
			OssjEntityMethodFlavor.BYTEMPLATE,
			OssjEntityMethodFlavor.BYTEMPLATES,
			OssjEntityMethodFlavor.BULKBESTEFFORT,
			OssjEntityMethodFlavor.BULKBESTEFFORTBYKEYS,
			OssjEntityMethodFlavor.BYTEMPLATEBESTEFFORT,
			OssjEntityMethodFlavor.BYTEMPLATESBESTEFFORT };
	public final static String[] customMethodFlavorDefaults = {
			"true:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.ObjectNotFoundException", // OssjEntityMethodFlavor.SIMPLE
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.ObjectNotFoundException", // OssjEntityMethodFlavor.SIMPLEBYKEY
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException", // OssjEntityMethodFlavor.BULKATOMIC
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException", // OssjEntityMethodFlavor.BULKATOMICBYKEYS
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException", // OssjEntityMethodFlavor.BYTEMPLATE
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.DuplicateKeyException", // OssjEntityMethodFlavor.BYTEMPLATES
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException", // OssjEntityMethodFlavor.BULKBESTEFFORT
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException", // OssjEntityMethodFlavor.BULKBESTEFFORTBYKEYS
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException", // OssjEntityMethodFlavor.BYTEMPLATEBESTEFFORT
			"false:javax.oss.OssIllegalArgumentException,java.rmi.RemoteException" // OssjEntityMethodFlavor.BYTEMPLATESBESTEFFORT
	};
}
