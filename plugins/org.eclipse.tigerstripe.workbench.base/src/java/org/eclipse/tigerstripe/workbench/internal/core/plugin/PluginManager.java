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
package org.eclipse.tigerstripe.workbench.internal.core.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IGlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.ContributedPlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggablePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.GlobalSettingsProperty;
import org.eclipse.tigerstripe.workbench.internal.core.util.MatchedConfigHousing;
import org.eclipse.tigerstripe.workbench.internal.core.util.OSGIRef;
import org.eclipse.tigerstripe.workbench.internal.core.util.ZipFileUnzipper;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

/**
 * @author Eric Dillon
 * 
 * The plugin manager (singleton) manages all plugin housings
 * 
 */
public class PluginManager {

	private static boolean osgiVersioning;

	private static PluginManager instance;

	private List<PluginHousing> housings;

	public static PluginManager getManager() {
		if (PluginManager.instance == null) {
			PluginManager.instance = new PluginManager();
			PluginManager.instance.load();
		}

		return PluginManager.instance;
	}

	private PluginManager() {
		this.housings = new ArrayList<PluginHousing>();
	}

	public static boolean isOsgiVersioning() {
		return osgiVersioning;
	}

	/**
	 * Resolves the plugin reference and returns the corresponding plugin
	 * housing.
	 * 
	 * @param ref
	 *            - PluginConfig the plugin reference to resolve
	 * @return PluginHousing - the plugin housing corresponding to the given
	 *         PluginConfig.
	 * @throws UnknownPluginException
	 *             , if the PluginConfig cannot be resolved
	 */
	public PluginHousing resolveReference(PluginConfig ref)
			throws UnknownPluginException {

		for (PluginHousing housing : housings) {
			if (housing.matchRef(ref))
				return housing;
		}
		throw new UnknownPluginException(ref);
	}

	public List<PluggableHousing> getRegisteredPluggableHousings() {
		List<PluggableHousing> result = new ArrayList<PluggableHousing>();

		for (PluginHousing housing : housings) {
			if (housing instanceof PluggableHousing) {
				result.add((PluggableHousing) housing);
			}
		}
		return result;
	}

	/**
	 * Returns the available housings
	 * 
	 */
	public List<PluginHousing> getRegisteredHousings() {
		return this.housings;
	}

	/**
	 * Load up all the plugins
	 * 
	 * @return
	 */
	public void load() {
		housings = new ArrayList<PluginHousing>();

		TigerstripeCore.getWorkbenchProfileSession().getActiveProfile()
				.getProperty(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);
		GlobalSettingsProperty prop = (GlobalSettingsProperty) TigerstripeCore
				.getWorkbenchProfileSession().getActiveProfile()
				.getProperty(IWorkbenchPropertyLabels.GLOBAL_SETTINGS);
		osgiVersioning = prop
				.getPropertyValue(IGlobalSettingsProperty.OSGIVERSIONING);
		// This will load the actual pluggable plugins
		loadPluggableHousings();

	}

	/**
	 * Loads pluggable housings by scanning a known directory
	 * 
	 */
	private void loadPluggableHousings() {

        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                ZipFile zip = null;
                try {
                    zip = new ZipFile(arg0.getAbsolutePath() + File.separator
                            + arg1);
                    return true;
                } catch (IOException e) {
                    return false;
                } finally {
                    if (zip != null) {
                        try {
                            zip.close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                }
            }
        };

        String runtimeRoot = TigerstripeRuntime.getTigerstripeRuntimeRoot();

        String pluginsRoot = runtimeRoot + File.separator
                + TigerstripeRuntime.TIGERSTRIPE_PLUGINS_DIR;
        File pluginDir = new File(pluginsRoot);

        if (pluginDir.exists() && pluginDir.canRead()) {
            String[] files = pluginDir.list(filter);

            for (String file : files) {
                // First un-zip the file

                String unZippedFile = pluginsRoot + File.separator + "." + file;
                String zippedFile = pluginsRoot + File.separator + file;

                if (unZippedFile.endsWith(".zip")) {
                    unZippedFile = unZippedFile.substring(0,
                            unZippedFile.length() - 4);
                }
                try {

                    // Since 2.2.0 we now check on the existence of a tstamp.txt
                    // file
                    // in the unzip dir to avoid un-zipping blindly everytime
                    // all plugins
                    long unzippedTStamp = readTStamp(unZippedFile);
                    File zip = new File(pluginsRoot + File.separator + file);
                    if (zip.exists()) {
                        long zipTStamp = zip.lastModified();
                        if (zipTStamp != unzippedTStamp) {
                            ZipFileUnzipper.unzip(pluginsRoot + File.separator
                                    + file, unZippedFile);
                            writeTStamp(zipTStamp, unZippedFile);
                        }
                    }

                    PluggablePlugin pluginBody = new PluggablePlugin(
                            unZippedFile, zippedFile);
                    if (pluginBody.isValid()) {
                        PluggableHousing housing = new PluggableHousing(
                                pluginBody);

                        registerHousing(housing);
                    }
                } catch (TigerstripeException e) {
                    TigerstripeRuntime.logErrorMessage(
                            "TigerstripeException detected", e);
                }
            }
        }

        // Now see if there any contributed generators.

        try {
            IConfigurationElement[] elements = Platform
                    .getExtensionRegistry()
                    .getConfigurationElementsFor(
                            "org.eclipse.tigerstripe.workbench.base.contributedGenerator");
            // TODO check for only one contrib

            for (IConfigurationElement element : elements) {
                if (element.getName().equals("generator")) {
                    // Need to get the file from the contributing plugin
                    IContributor contributor = ((IExtension) element
                            .getParent()).getContributor();
                    Bundle bundle = org.eclipse.core.runtime.Platform
                            .getBundle(contributor.getName());
                    File f = FileLocator.getBundleFile(bundle);

                    PluggablePlugin pluginBody = new ContributedPlugin(
                            f.getAbsolutePath(), bundle);
                    pluginBody.setCanDelete(false);
                    if (pluginBody.isValid()) {
                        PluggableHousing housing = new PluggableHousing(
                                pluginBody);
                        registerHousing(housing);
                        BasePlugin.log(new Status(IStatus.INFO, BasePlugin.PLUGIN_ID, "Loaded Tigerstripe plugin: " + pluginBody.getPluginName()));
                    } else {
                    	BasePlugin.logErrorMessage("Contributed Tigerstripe plugin is not valid: " + pluginBody.getPluginName());
                    }
                }
            }
        } catch (Exception e) {
            BasePlugin
                    .logErrorMessage("Failed to instantiate generator from Extension Point");
            BasePlugin.log(e);

        }

    }

	private static String readFileAsString(String filePath)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = null;
		FileReader fReader = null;
		try {
			fReader = new FileReader(filePath);
			reader = new BufferedReader(fReader);
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
		} finally {
			if (fReader != null) {
				try {
					fReader.close();
				} catch (IOException ee) {
					// ignore
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ee) {
					// ignore
				}
			}
		}
		return fileData.toString();
	}

	private long readTStamp(String zipDir) {
		String tStampFile = zipDir + File.separator + "tstamp.txt";
		try {
			String tsStr = readFileAsString(tStampFile);
			if (tsStr != null && tsStr.length() != 0)
				return Long.parseLong(tsStr);
			else
				return -1;
		} catch (IOException e) {
			return -1;
		}
	}

	private void writeTStamp(long tStamp, String zipDir) {
		String tStampFile = zipDir + File.separator + "tstamp.txt";
		FileWriter writer = null;
		try {
			writer = new FileWriter(tStampFile);
			writer.append(String.valueOf(tStamp));
		} catch (IOException e) {
			TigerstripeRuntime.logErrorMessage("IOException detected", e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	private void registerHousing(PluginHousing housing)
			throws TigerstripeException {
		if (!this.housings.contains(housing))
			this.housings.add(housing);
	}

	public void unRegisterHousing(PluginHousing housing)
			throws TigerstripeException {
		if (this.housings.contains(housing))
			this.housings.remove(housing);
	}

	/*
	 * This should only ever be called if the OSGI versioning is being used. The
	 * housings should all share the name that you are searching for.
	 */
	public MatchedConfigHousing resolve(Collection<PluggableHousing> housings,
			IPluginConfig[] plugins) {

		PluggableHousing potentialHousing = null;
		IPluginConfig usedPluginConfig = null;
		Version currentVersion = null;

		for (PluggableHousing candidateHousing : housings) {

			// System.out.println("Housing "+candidateHousing.getPluginName());
			// pluginRef is the "determinant" - is the one that says what we
			// ant.
			OSGIRef pluginRef = null;
			if (candidateHousing.getPluginName() == null) {
				BasePlugin.logErrorMessage("Plugin name is null:"
						+ candidateHousing.toString());
				// We didn't fnd any - so this is not yet used!
				return new MatchedConfigHousing(null, candidateHousing);
			}

			for (int i = 0; i < plugins.length; i++) {
				// System.out.println("    Plugin "+plugins[i].getPluginName());
				if (candidateHousing.getPluginName().equals(
						plugins[i].getPluginName())) {
					try {
						pluginRef = OSGIRef.parseRef(plugins[i].getVersion());
						usedPluginConfig = plugins[i];
					} catch (IllegalArgumentException e) {
						// This ref has a bad version string.. we need to ignore
						// it.
						BasePlugin
								.logErrorMessage("Illegal Version format for '"
										+ plugins[i].getPluginName() + "'");
						BasePlugin.log(e);
						continue;
					}
					break;
				}
			}
			if (pluginRef == null) {
				// We didn't fnd any - so this is not yet used!
				return new MatchedConfigHousing(null, candidateHousing);

			}

			Version v = null;
			try {
				v = new Version(candidateHousing.getVersion());
			} catch (IllegalArgumentException e) {
				// This housing has a bad version string.. we need to ignore it.
				BasePlugin.logErrorMessage("Illegal Version format for '"
						+ candidateHousing.getPluginName() + "'");
				BasePlugin.log(e);
				continue;
			}
			// System.out.println("    Housing Ver "+v.toString());
			if (v != null && pluginRef != null) {
				if (pluginRef.isInScope(v)) {
					// System.out.println("    In scope!");
					if (potentialHousing == null) {
						potentialHousing = candidateHousing;
						currentVersion = v;
					} else {
						// newer than the previous selection
						if (v.compareTo(currentVersion) > 0) {
							// this is newer
							// System.out.println("    Newer!");
							potentialHousing = candidateHousing;
							currentVersion = v;
						}

					}
				}
			} else {
				// This is all a bit of a disaster - nothing more we can do here
				return null;
			}
		}
		return new MatchedConfigHousing(usedPluginConfig, potentialHousing);
	}

}