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
package org.eclipse.tigerstripe.workbench.ui.gmf.export.wizards;

import java.io.File;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.InternalTigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.rendering.IDiagramRenderer;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.ui.eclipse.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs.XSLFileSelectionDialog;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.ClassDiagramLogicalNode;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.abstraction.InstanceDiagramLogicalNode;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

public class AllDiagramsExportWizardPage extends WizardPage {

	private final String[] diagramFileExtensions = {
			ClassDiagramLogicalNode.DIAG_EXT,
			InstanceDiagramLogicalNode.DIAG_EXT }; // FIXME: use extension

	public final static String PAGE_NAME = "ts.exportAllDiagrams";

	private Text initialContainerText;

	private Button initialContainerButton;

	private Text targetContainerText;

	private IStructuredSelection selection;

	private Button useXSLButton;

	private Text xslText;

	private Button xslBrowseButton;

	private Label xslFileLabel;

	private Button includeInstanceDiags;

	private Button includeClassDiags;

	private Text diNameRegexp;

	private class AllDiagramsExportWizardPageAdapter implements ModifyListener,
			SelectionListener {

		public void modifyText(ModifyEvent e) {
			allDiagramsExportModifyText(e);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			allDiagramsExportWidgetSelected(e);
		}

	}

	protected void allDiagramsExportModifyText(ModifyEvent e) {
		if (e.getSource() == diNameRegexp) {
			String regexpStr = diNameRegexp.getText();
			if (regexpStr != null && regexpStr.length() != 0) {
				updatePageComplete();
			}
		}
	}

	public AllDiagramsExportWizardPage() {
		super(PAGE_NAME);

		setTitle("Export All Diagrams");
		setDescription("Export all diagrams to .gif files and create an index.");
	}

	public IRunnableWithProgress getRunnable() {
		IRunnableWithProgress result = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {

				try {
					IContainer root = (IContainer) ResourcesPlugin
							.getWorkspace().getRoot().findMember(
									new Path(initialContainerText.getText()
											.trim()));
					IFolder target = root.getFolder(new Path(
							targetContainerText.getText().trim()));
					if (!target.exists()) {
						target.create(true, true, monitor);
					}
					String targetLocation = target.getProjectRelativePath()
							.toOSString();

					int fileCounter = 0;
					Map<IResource, String> resMap = new HashMap<IResource, String>();
					monitor.beginTask("Setting up rendering...",
							IProgressMonitor.UNKNOWN);
					IDiagramRenderer renderer = InternalTigerstripeCore
							.getIDiagramRenderingSession()
							.getRendererByName(
									"org.eclipse.tigerstripe.workbench.ui.rendererplugin.actions.Renderer");

					Pattern regexp = getRegExp();

					for (String diagExt : diagramFileExtensions) {
						List<IResource> diags = TigerstripeProjectAuditor
								.findAll(root, diagExt);

						// should we include Class Diagrams
						if (ClassDiagramLogicalNode.DIAG_EXT.equals(diagExt)
								&& !shouldIncludeClassDiagrams())
							continue;

						// should we include Instance Diagrams
						if (InstanceDiagramLogicalNode.DIAG_EXT.equals(diagExt)
								&& !shouldIncludeInstanceDiagrams())
							continue;

						String handlingStr = "";
						if (ClassDiagramLogicalNode.DIAG_EXT.equals(diagExt))
							handlingStr = "Class Diagrams";
						if (InstanceDiagramLogicalNode.DIAG_EXT.equals(diagExt))
							handlingStr = "Instance Diagrams";

						monitor.beginTask("Handling: '" + handlingStr + "'",
								diags.size());
						for (IResource res : diags) {

							// Skip diags in /bin
							IPath projRev = res.getProjectRelativePath();
							if (projRev.matchingFirstSegments(new Path("bin")) != 0) {
								continue;
							}

							String diagName = res.getProjectRelativePath()
									.removeFileExtension().lastSegment();

							// Should we exclude based on a regexp
							if (regexp != null) {
								Matcher m = regexp.matcher(diagName);
								if (m.matches())
									continue;
							}

							String projectLabel = res.getProject().getName();
							String diagRelPath = res.getProjectRelativePath()
									.toOSString();
							monitor.subTask(diagRelPath);
							String imageType = IDiagramRenderer.GIF;
							String imagePath = targetLocation + File.separator
									+ fileCounter + "." + IDiagramRenderer.GIF;
							renderer.renderDiagram(projectLabel, diagRelPath,
									imageType, imagePath);
							resMap.put(res, String.valueOf(fileCounter) + "."
									+ IDiagramRenderer.GIF);
							fileCounter++;
							monitor.worked(1);
						}
					}

					monitor.beginTask("Creating index",
							IProgressMonitor.UNKNOWN);
					// then create the xml index
					Document doc = DocumentFactory.getInstance()
							.createDocument();
					Element rootElem = doc.addElement("diagList");
					List<IResource> sortedRes = new ArrayList<IResource>();
					sortedRes.addAll(resMap.keySet());
					Collections.sort(sortedRes, new Comparator<IResource>() {

						public int compare(IResource o1, IResource o2) {
							return o1.getLocation().toOSString().compareTo(
									o2.getLocation().toOSString());
						}

					});
					for (IResource res : sortedRes) {

						Element diagElem = rootElem.addElement("diagram");
						diagElem.addAttribute("name", getName(res));
						Element contElem = diagElem.addElement("container");
						contElem.addText(getPackageFor(res));
						String fileStr = resMap.get(res);
						Element fileElem = diagElem.addElement("file");
						fileElem.addText(fileStr);
					}

					IContainer rootCont = root;
					IFile fileHandle = rootCont.getFile(new Path(targetLocation
							+ File.separator + rootCont.getName()
							+ "-diagrams.xml"));

					StringWriter writer = new StringWriter();
					OutputFormat format = OutputFormat.createPrettyPrint();
					XMLWriter xmlWriter = new XMLWriter(writer, format);
					xmlWriter.write(doc);
					xmlWriter.close();
					writer.close();
					String contents = writer.toString();
					StringBufferInputStream iStream = new StringBufferInputStream(
							contents);
					fileHandle.create(iStream, true, monitor);

					// then optionally run the XSLT
					monitor.beginTask("Running XSLT...",
							IProgressMonitor.UNKNOWN);
					if (useXSLButton.getSelection()) {
						File xslTFile = root.findMember(
								xslText.getText().trim()).getLocation()
								.toFile();
						if (xslTFile.exists()) {
							StringWriter resultWriter = new StringWriter();
							TransformerFactory tFactory = TransformerFactory
									.newInstance();
							Transformer transformer = tFactory
									.newTransformer(new StreamSource(xslTFile));
							StringReader reader = new StringReader(contents);
							transformer.transform(new StreamSource(reader),
									new StreamResult(resultWriter));
							IFile htmlFileHandle = rootCont.getFile(new Path(
									targetLocation + File.separator
											+ rootCont.getName()
											+ "-diagrams.html"));
							StringBufferInputStream htmlStream = new StringBufferInputStream(
									resultWriter.toString());
							htmlFileHandle.create(htmlStream, true, monitor);
						}
					}
				} catch (TigerstripeException e) {
					throw new InvocationTargetException(e);
				} catch (IOException e) {
					throw new InvocationTargetException(e);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} catch (TransformerConfigurationException e) {
					throw new InvocationTargetException(e);
				} catch (TransformerException e) {
					throw new InvocationTargetException(e);
				}
				monitor.done();
			}
		};

		return result;
	}

	/**
	 * Return the actual name of the resource, without the file extension.
	 * 
	 * @param res
	 * @return
	 */
	private String getName(IResource res) {
		IPath path = new Path(res.getName());
		return path.removeFileExtension().toOSString();
	}

	private String getPackageFor(IResource res) {
		IContainer cont = res.getParent();
		IJavaElement jElm = JavaCore.create(cont);
		if (jElm instanceof IPackageFragment) {
			IPackageFragment frag = (IPackageFragment) jElm;
			return frag.getElementName();
		}
		return cont.getProjectRelativePath().toOSString();
	}

	protected void allDiagramsExportWidgetSelected(SelectionEvent e) {
		if (e.getSource() == initialContainerButton) {
			ContainerSelectionDialog dialog = new ContainerSelectionDialog(
					getShell(), ResourcesPlugin.getWorkspace().getRoot(),
					false, "Select tigerstripe project");
			if (dialog.open() == Window.OK) {
				Object[] result = dialog.getResult();
				if (result[0] instanceof IPath) {
					IPath path = (IPath) result[0];
					initialContainerText.setText(path.toString());
				}
			}
		} else if (e.getSource() == xslBrowseButton) {
			XSLFileSelectionDialog diag = new XSLFileSelectionDialog(
					getShell(), false, false);
			diag.setInput(ResourcesPlugin.getWorkspace().getRoot().findMember(
					new Path(initialContainerText.getText().trim()))
					.getLocation().toFile());
			if (diag.open() == Window.OK) {
				Object[] res = diag.getResult();
				File file = (File) res[0];
				try {
					String relPath = Util.getRelativePath(file, ResourcesPlugin
							.getWorkspace().getRoot().findMember(
									new Path(initialContainerText.getText()
											.trim())).getLocation().toFile());
					xslText.setText(relPath);
				} catch (IOException ee) {
					EclipsePlugin.log(ee);
				}
			}
		}

		updatePageComplete();
	}

	public AllDiagramsExportWizardPage(String title, ImageDescriptor titleImage) {
		super(PAGE_NAME, title, titleImage);
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);

		int nColumns = 3;

		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createBlueprintControls(composite, nColumns);
		createInclExclControls(composite, nColumns);
		createUseXSLControls(composite, nColumns);

		setControl(composite);
		Dialog.applyDialogFont(composite);
		// WorkbenchHelp.setHelp(composite,
		// IJavaHelpContextIds.NEW_INTERFACE_WIZARD_PAGE);

		updatePageComplete();
	}

	protected void createInclExclControls(Composite composite, int nColumns) {
		AllDiagramsExportWizardPageAdapter adapter = new AllDiagramsExportWizardPageAdapter();
		Group inclExclGroup = new Group(composite, SWT.TITLE);
		inclExclGroup.setText("Diagrams");

		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 3;
		inclExclGroup.setLayoutData(gd);
		GridLayout layout = new GridLayout(3, false);
		inclExclGroup.setLayout(layout);

		Label diagsToIncl = new Label(inclExclGroup, SWT.NULL);
		diagsToIncl.setText("Include:");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 3;
		diagsToIncl.setLayoutData(gd);

		includeClassDiags = new Button(inclExclGroup, SWT.CHECK);
		includeClassDiags.setText("Class Diagrams");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent = 20;
		gd.horizontalSpan = 3;
		includeClassDiags.setLayoutData(gd);

		includeInstanceDiags = new Button(inclExclGroup, SWT.CHECK);
		includeInstanceDiags.setText("Instance Diagrams");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent = 20;
		gd.horizontalSpan = 3;
		includeInstanceDiags.setLayoutData(gd);

		Label diagsToExcl = new Label(inclExclGroup, SWT.NULL);
		diagsToExcl.setText("Exclude:");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 3;
		diagsToExcl.setLayoutData(gd);

		Label diName = new Label(inclExclGroup, SWT.NULL);
		diName.setText("Name-matching RegExp:");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent = 20;
		diName.setLayoutData(gd);

		diNameRegexp = new Text(inclExclGroup, SWT.BORDER);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		diNameRegexp.setLayoutData(gd);
		diNameRegexp.addModifyListener(adapter);

	}

	protected void createUseXSLControls(Composite composite, int nColumns) {
		AllDiagramsExportWizardPageAdapter adapter = new AllDiagramsExportWizardPageAdapter();

		Group useXslGroup = new Group(composite, SWT.TITLE);
		useXslGroup.setText("Post-processing");
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 3;
		useXslGroup.setLayoutData(gd);
		GridLayout layout = new GridLayout(3, false);
		useXslGroup.setLayout(layout);

		useXSLButton = new Button(useXslGroup, SWT.CHECK);
		useXSLButton.setText("Use XSLT");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 3;
		useXSLButton.setLayoutData(gd);
		useXSLButton.addSelectionListener(adapter);

		xslFileLabel = new Label(useXslGroup, SWT.NULL);
		xslFileLabel.setText("XSL File");
		xslText = new Text(useXslGroup, SWT.BORDER);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		xslText.setLayoutData(gd);
		xslText.addModifyListener(adapter);

		xslBrowseButton = new Button(useXslGroup, SWT.PUSH);
		xslBrowseButton.setText("Browse");
		xslBrowseButton.addSelectionListener(adapter);
	}

	protected void createBlueprintControls(Composite composite, int nColumns) {

		AllDiagramsExportWizardPageAdapter adapter = new AllDiagramsExportWizardPageAdapter();

		Label initialContainerLabel = new Label(composite, SWT.NONE);
		initialContainerLabel.setText("Project");
		initialContainerText = new Text(composite, SWT.BORDER);
		initialContainerText.addModifyListener(adapter);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		initialContainerText.setLayoutData(gd);
		initialContainerText.setText(getInitialContainer());

		initialContainerButton = new Button(composite, SWT.PUSH);
		initialContainerButton.setText("Browse");
		initialContainerButton.addSelectionListener(adapter);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		initialContainerButton.setLayoutData(gd);

		Label targetContainerLabel = new Label(composite, SWT.NONE);
		targetContainerLabel.setText("Destination");
		targetContainerText = new Text(composite, SWT.BORDER);
		targetContainerText.addModifyListener(adapter);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
		targetContainerText.setLayoutData(gd);
		targetContainerText.setText(getDefaultTarget());

		new Label(composite, SWT.NULL);
	}

	protected IStructuredSelection getSelection() {
		return this.selection;
	}

	// this is called when the page is "added" to the wizard
	public void init(IStructuredSelection selection) {
		this.selection = selection;
	}

	protected void updatePageComplete() {

		if ("".equals(initialContainerText.getText().trim())) {
			setErrorMessage("Please select source project.");
			setPageComplete(false);
			return;
		} else {
			IResource target = ResourcesPlugin
					.getWorkspace()
					.getRoot()
					.findMember(new Path(initialContainerText.getText().trim()));
			if (target == null) {
				setErrorMessage("Invalid source project.");
				setPageComplete(false);
				return;
			} else if (target instanceof IFile) {
				setErrorMessage("Invalid source project.");
				setPageComplete(false);
				return;
			}
		}

		if ("".equals(targetContainerText.getText().trim())) {
			setErrorMessage("Please select target directory.");
			setPageComplete(false);
			return;
		}

		xslFileLabel.setEnabled(useXSLButton.getSelection());
		xslText.setEnabled(useXSLButton.getSelection());
		xslBrowseButton.setEnabled(useXSLButton.getSelection());
		if (useXSLButton.getSelection()) {
			String xslTextString = xslText.getText().trim();
			if (xslTextString.length() == 0) {
				setErrorMessage("Select XSL File.");
				setPageComplete(false);
				return;
			}
		}

		setErrorMessage(null);

		setMessage("Export project diagrams...");
		setPageComplete(true);
	}

	private String getDefaultTarget() {
		return "diagrams";
	}

	private String getInitialContainer() {
		if (selection != null) {
			if (selection.getFirstElement() instanceof IFolder) {
				IPath path = ((IFolder) selection.getFirstElement())
						.getFullPath();
				return path.toString();
			} else if (selection.getFirstElement() instanceof IJavaElement) {
				IPath path = ((IJavaElement) selection.getFirstElement())
						.getPath();
				return path.toString();
			}
		}
		return "";
	}

	public Pattern getRegExp() {
		if (diNameRegexp != null && diNameRegexp.getText() != null
				&& diNameRegexp.getText().trim().length() != 0)
			return Pattern.compile(diNameRegexp.getText().trim());
		return null;
	}

	public boolean shouldIncludeClassDiagrams() {
		return (includeClassDiags != null && includeClassDiags.getSelection());
	}

	public boolean shouldIncludeInstanceDiagrams() {
		return (includeInstanceDiags != null && includeInstanceDiags
				.getSelection());
	}
}
