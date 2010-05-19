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
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.annotation.ui.core.properties.NoteFilter;
import org.eclipse.tigerstripe.annotation.ui.core.view.INote;
import org.eclipse.ui.views.properties.tabbed.ISectionDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITypeMapper;

/**
 * @author Yuri Strot
 * 
 */
public class TabDescriptionManipulator {

	private static final String EXTPT_SECTIONS = "propertySections"; //$NON-NLS-1$

	private static final String ELEMENT_SECTION = "propertySection"; //$NON-NLS-1$

	private static final String ATT_CONTRIBUTOR_ID = "contributorId"; //$NON-NLS-1$

	private static final String CONTRIBUTOR_ID = "org.eclipse.tigerstripe.annotation.ui.properties"; //$NON-NLS-1$

	private static final String SECTION_ID = "property.section.PropertiesPropertySection"; //$NON-NLS-1$

	private static TabDescriptionManipulator manipulator;

	private ITypeMapper typeMapper = new AnnotationElementTypeMapper();

	public static TabDescriptionManipulator getInstance() {
		if (manipulator == null) {
			manipulator = new TabDescriptionManipulator();
		}
		return manipulator;
	}

	private Map<String, List<ISectionDescriptor>> tabToSections;
	private Map<String, Boolean> classToFilter = new HashMap<String, Boolean>();

	public boolean isEnabled(NoteFilter filter) {
		Boolean value = classToFilter.get(filter.getClass().getName());
		if (value != null && value.booleanValue())
			return true;
		return false;
	}

	public void update(INote note) {
		Map<String, List<ISectionDescriptor>> tabToSections = getTabToSections();
		for (List<ISectionDescriptor> list : tabToSections.values()) {
			List<ISectionDescriptor> newList = new ArrayList<ISectionDescriptor>();
			for (ISectionDescriptor descriptor : list) {
				NoteFilter filter = (NoteFilter) descriptor.getFilter();
				if (filter.select(note))
					newList.add(descriptor);
			}
			int size = newList.size();
			ISectionDescriptor enable = size > 0 ? newList.get(0) : null;
			if (size > 1 && enable.getId().equals(SECTION_ID)) {
				enable = list.get(1);
			}
			for (ISectionDescriptor descriptor : list) {
				Boolean value = descriptor == enable ? Boolean.TRUE
						: Boolean.FALSE;
				classToFilter.put(descriptor.getFilter().getClass().getName(),
						value);
			}
		}
	}

	private Map<String, List<ISectionDescriptor>> getTabToSections() {
		if (tabToSections == null) {
			tabToSections = new HashMap<String, List<ISectionDescriptor>>();
			ISectionDescriptor[] descriptors = readSectionDescriptors();
			for (ISectionDescriptor descriptor : descriptors) {
				if (descriptor.getFilter() instanceof NoteFilter) {
					String tab = descriptor.getTargetTab();
					List<ISectionDescriptor> list = tabToSections.get(tab);
					if (list == null) {
						list = new ArrayList<ISectionDescriptor>();
						tabToSections.put(tab, list);
					}
					list.add(descriptor);
				}
			}
		}
		return tabToSections;
	}

	/**
	 * Reads property tab extensions. Returns all tab descriptors for the
	 * current contributor id or an empty list if none is found.
	 */

	/**
	 * Reads property section extensions. Returns all section descriptors for
	 * the current contributor id or an empty array if none is found.
	 */
	protected ISectionDescriptor[] readSectionDescriptors() {
		List<ISectionDescriptor> result = new ArrayList<ISectionDescriptor>();
		IConfigurationElement[] extensions = getConfigurationElements(EXTPT_SECTIONS);
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement extension = extensions[i];
			IConfigurationElement[] sections = extension
					.getChildren(ELEMENT_SECTION);
			for (int j = 0; j < sections.length; j++) {
				IConfigurationElement section = sections[j];
				ISectionDescriptor descriptor = new SectionDescriptor(section,
						typeMapper);
				result.add(descriptor);
			}
		}
		return (ISectionDescriptor[]) result
				.toArray(new ISectionDescriptor[result.size()]);
	}

	/**
	 * Returns the configuration elements targeted for the given extension point
	 * and the current contributor id. The elements are also sorted by plugin
	 * prerequisite order.
	 */
	protected IConfigurationElement[] getConfigurationElements(
			String extensionPointId) {
		if (CONTRIBUTOR_ID == null) {
			return new IConfigurationElement[0];
		}
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint("org.eclipse.ui.views.properties.tabbed",
						extensionPointId);
		IConfigurationElement[] extensions = point.getConfigurationElements();
		List<IConfigurationElement> unordered = new ArrayList<IConfigurationElement>(
				extensions.length);
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement extension = extensions[i];
			if (!extension.getName().equals(extensionPointId)) {
				continue;
			}
			String contributor = extension.getAttribute(ATT_CONTRIBUTOR_ID);
			if (!CONTRIBUTOR_ID.equals(contributor)) {
				continue;
			}
			unordered.add(extension);
		}
		return (IConfigurationElement[]) unordered
				.toArray(new IConfigurationElement[unordered.size()]);
	}

}
