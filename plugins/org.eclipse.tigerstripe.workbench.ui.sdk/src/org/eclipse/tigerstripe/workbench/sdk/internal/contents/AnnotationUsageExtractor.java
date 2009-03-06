package org.eclipse.tigerstripe.workbench.sdk.internal.contents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;



public class AnnotationUsageExtractor {

	private Map<AnnotationTypeContribution, Collection<IResource>> annotationMap;
	private Map<AuditContribution, Collection<AnnotationTypeContribution>> auditMap;
	private Map<NamingContribution, Collection<AnnotationTypeContribution>> namingMap;
	private Map<DecoratorContribution, Collection<AnnotationTypeContribution>> decoratorMap;
	private Map<ModelComponentIconProviderContribution, Collection<AnnotationTypeContribution>> iconProviderMap;
	
	public AnnotationUsageExtractor(
			ISDKProvider provider) {
		super();
		this.provider = provider;
		init();
	}

	private ISDKProvider provider;
	
	private class MySearchRequestor extends SearchRequestor {
		
		private IProject ownProject;
		
		public MySearchRequestor(IProject ownProject) {
			super();
			this.ownProject = ownProject;
		}

		private Collection<IResource> usedResources = new ArrayList<IResource>(); 
		
		public Collection<IResource> getUsedResources() {
			return usedResources;
		}

		public void acceptSearchMatch(SearchMatch match) {
			if (! usedResources.contains(match.getResource()))
				if (! match.getResource().getProject().equals(ownProject))
					usedResources.add(match.getResource());
		}
		
	};
	
	
	public Map<AnnotationTypeContribution, Collection<IResource>> getAnnotationMap() {
		if (annotationMap == null){
			init();
		}
		return annotationMap;
	}


	public Map<AuditContribution, Collection<AnnotationTypeContribution>> getAuditMap() {
		if (auditMap == null){
			init();
		}
		return auditMap;
	}
	
	public Map<NamingContribution, Collection<AnnotationTypeContribution>> getNamingMap() {
		if (namingMap == null){
			init();
		}
		return namingMap;
	}


	public Map<DecoratorContribution, Collection<AnnotationTypeContribution>> getDecoratorMap() {
		if (decoratorMap == null){
			init();
		}return decoratorMap;
	}


	public Map<ModelComponentIconProviderContribution, Collection<AnnotationTypeContribution>> getIconProviderMap() {
		if (iconProviderMap == null){
			init();
		}return iconProviderMap;
	}


	
	private void init(){
		annotationMap = new HashMap<AnnotationTypeContribution, Collection<IResource>>();
		auditMap = new HashMap<AuditContribution, Collection<AnnotationTypeContribution>>();
		namingMap = new HashMap<NamingContribution, Collection<AnnotationTypeContribution>>();
		iconProviderMap = new HashMap<ModelComponentIconProviderContribution, Collection<AnnotationTypeContribution>>();
		decoratorMap = new HashMap<DecoratorContribution, Collection<AnnotationTypeContribution>>();
		for (AnnotationTypeContribution annotation : provider.getAnnotationTypeContributions()){
			System.out.println("Reading stuff for "+annotation.getName());
			Collection<IResource> uses = readAnnotationUsage(annotation);
			annotationMap.put(annotation, uses);
			// Now see here those things are (in other contribution terms)
			for (IResource use : uses){

				String containedClassName = "";
				try{
					IJavaElement element = (IJavaElement) use.getAdapter(IJavaElement.class);
					if (element.getElementType() == IJavaElement.COMPILATION_UNIT){
						containedClassName = ((ICompilationUnit)element).getTypes()[0].getFullyQualifiedName();
					}
					
					//System.out.println("Reading stuff for "+annotation.getName()+ " in Audit");
					for (AuditContribution audit : provider.getAuditContributions()){
						if (audit.getAuditorClass().equals(containedClassName)){
							Collection<AnnotationTypeContribution> map = auditMap.get(audit);
							if ( map == null){
								map = new ArrayList<AnnotationTypeContribution>();
							}
							map.add(annotation);
							auditMap.put(audit, map);
						}
					}
					

					//System.out.println("Reading stuff for "+annotation.getName()+ " in Naming");
					for (NamingContribution naming : provider.getNamingContributions()){
						if (naming.getNamingClass().equals(containedClassName)){
							Collection<AnnotationTypeContribution> map = namingMap.get(naming);
							if ( map == null){
								map = new ArrayList<AnnotationTypeContribution>();
							}
							map.add(annotation);
							namingMap.put(naming, map);
						}
					}
					
					//System.out.println("Reading stuff for "+annotation.getName()+ " in Decorators");
					for (DecoratorContribution decorator : provider.getDecoratorContributions()){
						if (decorator.getDecoratorClass().equals(containedClassName)){
							Collection<AnnotationTypeContribution> map = decoratorMap.get(decorator);
							if ( map == null){
								map = new ArrayList<AnnotationTypeContribution>();
							}
							map.add(annotation);
							decoratorMap.put(decorator, map);
						}
					}
					
					//System.out.println("Reading stuff for "+annotation.getName()+ " in Icon Provider");
					for (ModelComponentIconProviderContribution decorator : provider.getModelComponentIconProviderContributions()){
						if (decorator.getProvider().equals(containedClassName)){
							Collection<AnnotationTypeContribution> map = iconProviderMap.get(decorator);
							if ( map == null){
								map = new ArrayList<AnnotationTypeContribution>();
							}
							map.add(annotation);
							iconProviderMap.put(decorator, map);
						}
					}
					
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	private Collection<IResource> readAnnotationUsage(AnnotationTypeContribution contribution){
		IResource res = (IResource) contribution.getContributor().getAdapter(IResource.class);
		IProject project =null;
		if (res != null)
			project = res.getProject();
		
		IJavaSearchScope scope= null;
		
		scope = SearchEngine.createWorkspaceScope();
		
		SearchPattern pattern = SearchPattern.createPattern(provider.getPackageForAnnotation(contribution)+contribution.getName(),
				IJavaSearchConstants.CLASS, 
				IJavaSearchConstants.REFERENCES, 
				SearchPattern.R_EXACT_MATCH);

		SearchEngine engine = new SearchEngine();
		MySearchRequestor requestor = new MySearchRequestor(project);		

		try {
			engine.search(pattern, 
					new SearchParticipant[]{SearchEngine.getDefaultSearchParticipant()}, 
					scope, requestor, null);
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestor.getUsedResources();
	}
	
	
	public void getAuditorAnnotations(AuditContribution contrib){
		System.out.println("Inpu "+contrib.getAuditorClass());

		IResource res = (IResource) contrib.getContributor().getAdapter(IResource.class);
		
		IJavaSearchScope scope= null;

		if (res != null){
			IProject project = res.getProject();
			scope = SearchEngine.createJavaSearchScope(new IJavaProject[] { JavaCore.create(project) }, false);
		} else 
			scope = SearchEngine.createWorkspaceScope();


			SearchPattern pattern = SearchPattern.createPattern("com.cisco.nm.vne.annotation.ird.MappedDC",
					IJavaSearchConstants.CLASS, 
					IJavaSearchConstants.REFERENCES, 
					SearchPattern.R_EXACT_MATCH);

			

			SearchEngine engine = new SearchEngine();
			SearchRequestor requestor = new SearchRequestor() {
				public void acceptSearchMatch(SearchMatch match) {
					System.out.println(match);

					try {
						System.out.println(((IMethod) match.getElement()).getElementName());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println(match.getElement().getClass());
				}
			};


			try {
				engine.search(pattern, 
						new SearchParticipant[]{SearchEngine.getDefaultSearchParticipant()}, 
						scope, requestor, null);


			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	
}
