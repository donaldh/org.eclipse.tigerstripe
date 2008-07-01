/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.core.test;

import org.eclipse.emf.common.util.URI;
import org.eclipse.tigerstripe.annotation.internal.core.InvalidReplaceException;
import org.eclipse.tigerstripe.annotation.internal.core.URIUtil;

import junit.framework.TestCase;

/**
 * @author Yuri Strot
 *
 */
public class URIPrefixReplacingTest extends TestCase {
	
	public void test1() throws Exception {
		URI oldPrefix = URI.createURI("scheme:/seg1/seg2");
		URI newPrefix = URI.createURI("scheme:/seg1/seg2/seg3");
		URI uri = URI.createURI("scheme:/seg1/seg2/seg4");
		URI result = URI.createURI("scheme:/seg1/seg2/seg3/seg4");
		URI newUri = URIUtil.replacePrefix(uri, oldPrefix, newPrefix);
		assertEquals(result, newUri);
	}
	
	public void test2() throws Exception {
		URI oldPrefix = URI.createURI("scheme:/seg1/seg2");
		URI newPrefix = URI.createURI("scheme:/seg1/seg2/seg3");
		URI uri = URI.createURI("scheme:/seg1/seg2/seg4#fragment");
		URI result = URI.createURI("scheme:/seg1/seg2/seg3/seg4#fragment");
		URI newUri = URIUtil.replacePrefix(uri, oldPrefix, newPrefix);
		assertEquals(result, newUri);
	}
	
	public void test3() throws Exception {
		URI oldPrefix = URI.createURI("scheme:/seg1/seg2");
		URI newPrefix = URI.createURI("scheme:/seg1/seg2#fragment");
		URI uri = URI.createURI("scheme:/seg1/seg2");
		URI result = URI.createURI("scheme:/seg1/seg2#fragment");
		URI newUri = URIUtil.replacePrefix(uri, oldPrefix, newPrefix);
		assertEquals(result, newUri);
	}
	
	public void test4() throws Exception {
		URI oldPrefix = URI.createURI("scheme:/seg1/seg2#fragment1");
		URI newPrefix = URI.createURI("scheme:/seg1/seg2#fragment2");
		URI uri = URI.createURI("scheme:/seg1/seg2#fragment1");
		URI result = URI.createURI("scheme:/seg1/seg2#fragment2");
		URI newUri = URIUtil.replacePrefix(uri, oldPrefix, newPrefix);
		assertEquals(result, newUri);
	}
	
	public void test5() throws Exception {
		URI oldPrefix = URI.createURI("scheme://authority/seg1/seg2");
		URI newPrefix = URI.createURI("scheme://authority/seg3/seg4");
		URI uri = URI.createURI("scheme://authority/seg1/seg2/seg5/seg6?query#fragment");
		URI result = URI.createURI("scheme://authority/seg3/seg4/seg5/seg6?query#fragment");
		URI newUri = URIUtil.replacePrefix(uri, oldPrefix, newPrefix);
		assertEquals(result, newUri);
	}
	
	public void test6() throws Exception {
		URI oldPrefix = URI.createURI("scheme://authority/seg1/seg2?query");
		URI newPrefix = URI.createURI("scheme://authority/seg3/seg4");
		URI uri = URI.createURI("scheme://authority/seg1/seg2?query#fragment");
		URI result = URI.createURI("scheme://authority/seg3/seg4#fragment");
		URI newUri = URIUtil.replacePrefix(uri, oldPrefix, newPrefix);
		assertEquals(result, newUri);
	}
	
	public void test7() throws Exception {
		URI oldPrefix = URI.createURI("scheme://authority/seg1/seg2");
		URI newPrefix = URI.createURI("scheme://authority/seg1/seg2#fragment1");
		URI uri = URI.createURI("scheme://authority/seg1/seg2#fragment2");
		boolean gotException = false;
		try {
			URIUtil.replacePrefix(uri, oldPrefix, newPrefix);
		}
		catch (InvalidReplaceException e) {
			gotException = true;
		}
		assertTrue(gotException);
	}

}
