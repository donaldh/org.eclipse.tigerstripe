package org.eclipse.tigerstripe.annotation.internal.core;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.annotation.core.AnnotationFactory;
import org.eclipse.tigerstripe.annotation.core.storage.internal.SimpleCommand;

public class Test {

	
	public void testAnn() {

		final ResourceSetImpl rs = new ResourceSetImpl();
		
		TransactionalEditingDomain domain = TransactionalEditingDomain.Factory.INSTANCE
				.createEditingDomain(rs);

		domain.addResourceSetListener(new ResourceSetListenerImpl() {
			
			@Override
			public void resourceSetChanged(ResourceSetChangeEvent event) {
				System.out.println(event);
			}
		});

		final Resource[] resource = new Resource[1];
		
		SimpleCommand command = new SimpleCommand() {
			
			public void execute() {
				resource[0] = rs.createResource(URI.createFileURI("/home/xibyte/trash/text.xmi"));
			}
		};
		
		
		domain.getCommandStack().execute(command);
		
		command = new SimpleCommand() {
			
			public void execute() {
				resource[0].getContents().add(AnnotationFactory.eINSTANCE.createAnnotation());
			}
		};
		
		domain.getCommandStack().execute(command);
	}
	
}
