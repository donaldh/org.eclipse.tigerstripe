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
package org.eclipse.tigerstripe.core.model.importing;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.project.IImportCheckpoint;
import org.eclipse.tigerstripe.api.project.IImportCheckpointDetails;
import org.eclipse.tigerstripe.core.model.importing.xmi.XMIAnnotableElement;

public abstract class AbstractImportCheckpoint implements IImportCheckpoint {

	public final static String ROOT_TAG = "importCheckpoint";

	public final static String ROOT_TYPE_TAG = "type";

	public final static String ROOT_VERSION_TAG = "version";

	public final static String ANNOTABLES_TAG = "annotables";

	public final static String ANNOTABLE_ELEMENT_TAG = "annotableElement";

	public final static String FQN_TAG = "fqn";

	public final static String ATTRIBUTE_TAG = "annotableElementAttribute";

	public final static String NAME_TAG = "name";

	public final static String DIMENSION_TAG = "dimension";

	public final static String PARENT_TAG = "parent";

	public final static String ANNOTATION_TAG = "annotation";

	private HashMap<String, AnnotableElement> annotables = null;

	private IImportCheckpointDetails details;

	public AbstractImportCheckpoint() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean isValid() {
		return annotables != null;
	}

	public IImportCheckpointDetails getDetails() {
		return details;
	}

	public void setDetails(IImportCheckpointDetails details) {
		this.details = details;
	}

	/**
	 * Reads in the Checkpoint based on the reader
	 * 
	 */
	public abstract void read(Document document) throws TigerstripeException;

	/**
	 * Sets the annotables to be written in this checkpoint
	 */
	public void setAnnotables(AnnotableElement[] annotables) {
		this.annotables = new HashMap<String, AnnotableElement>();
		for (AnnotableElement anno : annotables) {
			this.annotables.put(anno.getFullyQualifiedName(), anno);
		}
	}

	/**
	 * Parse the Annotables contained in the document and builds the internal
	 * list
	 * 
	 * @param document
	 * @throws TigerstripeException
	 */
	protected void parseAnnotables(Document document)
			throws TigerstripeException {

		annotables = new HashMap<String, AnnotableElement>();
		Element root = document.getRootElement();
		Element elmAnnotables = root.element(ANNOTABLES_TAG);

		if (elmAnnotables == null)
			throw new TigerstripeException("Error parsing checkpoint, no "
					+ ANNOTABLES_TAG);
		List<Element> annotablesTags = elmAnnotables
				.elements(ANNOTABLE_ELEMENT_TAG);

		for (Element annoElem : annotablesTags) {
			AnnotableElement anno = extractAnnotable(annoElem);
			annotables.put(anno.getFullyQualifiedName(), anno);
		}

		// TigerstripeRuntime.logInfoMessage("Read "
		// + annotables.values().size() + " annotables");
	}

	protected abstract AnnotableElement extractAnnotable(Element annoElm)
			throws TigerstripeException;

	protected void extractAnnotableElementCore(AnnotableElement anno,
			Element annoElm) throws TigerstripeException {
		String fqn = annoElm.attributeValue(FQN_TAG);
		String annotationType = annoElm.attributeValue(ANNOTATION_TAG);
		String annoParentStr = annoElm.attributeValue(PARENT_TAG);

		anno.setName(fqn);
		if (annoParentStr != null && !"".equals(annoParentStr.trim())) {
			anno.setParentAnnotableElement(new XMIAnnotableElement(
					annoParentStr));
		}
		anno.setAnnotationType(annotationType);
	}

	/**
	 * Once all Annotables have parsed, we must resolve all references (parents)
	 * 
	 */
	protected void resolveParents() throws TigerstripeException {
		for (AnnotableElement anno : annotables.values()) {
			if (anno.getParentAnnotableElement() != null) {
				String parentFQN = anno.getParentAnnotableElement()
						.getFullyQualifiedName();
				AnnotableElement resolved = annotables.get(parentFQN);

				if (resolved != null) {
					anno.setParentAnnotableElement(resolved);
				} else
					throw new TigerstripeException(
							"Inconsistent checkpoint, cannot resolve "
									+ parentFQN);

			}
		}
	}

	public abstract void write(IImportCheckpointDetails details,
			FileWriter writer) throws TigerstripeException;

	protected Document createDocument() {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement(ROOT_TAG);
		root.addAttribute(ROOT_TYPE_TAG, details.getType());
		root.addAttribute(ROOT_VERSION_TAG, details.getVersion());

		return document;
	}

	protected void writeCheckpoint(Document document, Writer writer)
			throws TigerstripeException {
		try {
			document.write(writer);
		} catch (IOException e) {
			throw new TigerstripeException("Error while writing checkpoint: "
					+ e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}

	protected void addAnnotables(Document document) {
		Element root = document.getRootElement();

		Element annoElm = root.addElement(ANNOTABLES_TAG);
		for (AnnotableElement anno : annotables.values()) {
			Element result = annoElm.addElement(ANNOTABLE_ELEMENT_TAG);
			result.addAttribute(FQN_TAG, anno.getFullyQualifiedName());

			boolean isMapped = !AnnotableElement.AS_UNKNOWN.equals(anno
					.getAnnotationType());
			result.addAttribute("isMapped", String.valueOf(isMapped));

			if (anno.getParentAnnotableElement() != null) {
				result.addAttribute(PARENT_TAG, anno
						.getParentAnnotableElement().getFullyQualifiedName());
			}

			for (AnnotableElementAttribute attr : anno
					.getAnnotableElementAttributes()) {
				Element attributeElem = result.addElement(ATTRIBUTE_TAG);
				attributeElem.addAttribute(NAME_TAG, attr.getName());
				attributeElem.addAttribute(FQN_TAG, attr.getType()
						.getFullyQualifiedName());
				attributeElem.addAttribute(DIMENSION_TAG, String.valueOf(attr
						.getDimensions()));
			}
		}
	}

	public Element addElementFromAnnotable(Element root,
			AnnotableElement element) {
		Element result = root.addElement(ANNOTABLE_ELEMENT_TAG);
		result.addAttribute(FQN_TAG, element.getFullyQualifiedName());

		for (AnnotableElementAttribute attr : element
				.getAnnotableElementAttributes()) {
			Element attributeElem = result.addElement(ATTRIBUTE_TAG);
			attributeElem.addAttribute(NAME_TAG, attr.getName());
			attributeElem.addAttribute(FQN_TAG, attr.getType()
					.getFullyQualifiedName());
			attributeElem.addAttribute(DIMENSION_TAG, String.valueOf(attr
					.getDimensions()));
		}
		return result;
	}

	public void computeDelta(List<AnnotableElement> newAnnotables)
			throws TigerstripeException {

		// TigerstripeRuntime.logInfoMessage("Computing delta");

		// Go thru the list of newAnnotables and check whether we have
		// an identical copy in the checkpoint
		for (AnnotableElement newAnno : newAnnotables) {
			// TigerstripeRuntime.logInfoMessage("Looking for " +
			// newAnno.getFullyQualifiedName() );
			AnnotableElement matching = annotables.get(newAnno
					.getFullyQualifiedName());
			if (matching == null) {
				// TigerstripeRuntime.logInfoMessage(" Added!");
				newAnno.setDelta(Annotable.DELTA_ADDED);
				for (AnnotableElementAttribute attr : newAnno
						.getAnnotableElementAttributes()) {
					attr.setDelta(Annotable.DELTA_ADDED);
				}
				for (AnnotableElementOperation op : newAnno
						.getAnnotableElementOperations()) {
					op.setDelta(Annotable.DELTA_ADDED);
				}
				for (AnnotableElementConstant cst : newAnno
						.getAnnotableElementConstants()) {
					cst.setDelta(Annotable.DELTA_ADDED);
				}
			} else {
				// check all attributes
				int attrDelta = computeDeltaAttributes(newAnno, matching);
				int opDelta = computeDeltaConstants(newAnno, matching);
				int cstDelta = computeDeltaOperations(newAnno, matching);

				if (attrDelta != Annotable.DELTA_UNCHANGED
						|| opDelta != Annotable.DELTA_UNCHANGED
						|| cstDelta != Annotable.DELTA_UNCHANGED) {
					newAnno.setDelta(Annotable.DELTA_CHANGED);
					// TigerstripeRuntime.logInfoMessage(" CHANGED" );
				} else {
					newAnno.setDelta(Annotable.DELTA_UNCHANGED);
					// TigerstripeRuntime.logInfoMessage(" UNCHANGED");
				}
			}
		}
	}

	private int computeDeltaAttributes(AnnotableElement newAnno,
			AnnotableElement chkAnno) {

		// TigerstripeRuntime.logInfoMessage(" attributes ");
		int deltaResult = Annotable.DELTA_UNCHANGED;

		// look for new and changed attrs
		for (AnnotableElementAttribute attr : newAnno
				.getAnnotableElementAttributes()) {
			attr.setDelta(Annotable.DELTA_UNCHANGED);
			AnnotableElementAttribute matching = chkAnno
					.getAttributeByName(attr.getName());
			if (matching == null) {
				// TigerstripeRuntime.logInfoMessage(" no match: ADDED");
				attr.setDelta(Annotable.DELTA_ADDED);
				deltaResult = Annotable.DELTA_CHANGED;
			} else {
				if (!attr.getType().getFullyQualifiedName().equals(
						matching.getType().getFullyQualifiedName())) {
					attr.setDelta(Annotable.DELTA_CHANGED);
					deltaResult = Annotable.DELTA_CHANGED;
					// TigerstripeRuntime.logInfoMessage(" CHANGED TYPE");
				}
			}
		}

		// look for removed attrs
		for (AnnotableElementAttribute attr : chkAnno
				.getAnnotableElementAttributes()) {
			AnnotableElementAttribute matching = newAnno
					.getAttributeByName(attr.getName());
			if (matching == null) {
				// TigerstripeRuntime.logInfoMessage("Missing " + attr.getName()
				// + " in " + newAnno.getFullyQualifiedName() );
				deltaResult = Annotable.DELTA_CHANGED;
			}
		}
		return deltaResult;
	}

	private int computeDeltaConstants(AnnotableElement newAnno,
			AnnotableElement chkAnno) {

		int deltaResult = Annotable.DELTA_UNCHANGED;

		// look for new and changed attrs
		for (AnnotableElementConstant cst : newAnno
				.getAnnotableElementConstants()) {
			cst.setDelta(Annotable.DELTA_UNCHANGED);
			AnnotableElementConstant matching = chkAnno.getConstantByName(cst
					.getName());
			if (matching == null) {
				cst.setDelta(Annotable.DELTA_ADDED);
				deltaResult = Annotable.DELTA_CHANGED;
			} else {
				if (!cst.getType().getFullyQualifiedName().equals(
						matching.getType().getFullyQualifiedName())) {
					cst.setDelta(Annotable.DELTA_CHANGED);
					deltaResult = Annotable.DELTA_CHANGED;
				}
			}
		}

		// look for removed attrs
		for (AnnotableElementConstant cst : chkAnno
				.getAnnotableElementConstants()) {
			AnnotableElementConstant matching = newAnno.getConstantByName(cst
					.getName());
			if (matching == null) {
				// TigerstripeRuntime.logInfoMessage("Missing " + cst.getName()
				// + " in " + newAnno.getFullyQualifiedName() );
				deltaResult = Annotable.DELTA_CHANGED;
			}
		}
		return deltaResult;
	}

	private int computeDeltaOperations(AnnotableElement newAnno,
			AnnotableElement chkAnno) {
		int deltaResult = Annotable.DELTA_UNCHANGED;

		// look for new and changed attrs
		for (AnnotableElementOperation op : newAnno
				.getAnnotableElementOperations()) {
			op.setDelta(Annotable.DELTA_UNCHANGED);
			AnnotableElementOperation matching = chkAnno
					.getOperationBySignature(op.getSignature());
			if (matching == null) {
				op.setDelta(Annotable.DELTA_ADDED);
				deltaResult = Annotable.DELTA_CHANGED;
			}
		}

		// look for removed attrs
		for (AnnotableElementOperation op : chkAnno
				.getAnnotableElementOperations()) {
			AnnotableElementOperation matching = newAnno
					.getOperationBySignature(op.getSignature());
			if (matching == null) {
				// TigerstripeRuntime.logInfoMessage("Missing " +
				// op.getSignature() + " in " + newAnno.getFullyQualifiedName()
				// );
				deltaResult = Annotable.DELTA_CHANGED;
			}
		}
		return deltaResult;
	}
}
