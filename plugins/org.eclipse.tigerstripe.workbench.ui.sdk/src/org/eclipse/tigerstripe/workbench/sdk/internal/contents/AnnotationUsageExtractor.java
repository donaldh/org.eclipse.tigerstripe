package org.eclipse.tigerstripe.workbench.sdk.internal.contents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.tigerstripe.workbench.patterns.IPattern;
import org.eclipse.tigerstripe.workbench.sdk.internal.ISDKProvider;



public class AnnotationUsageExtractor {

	private Map<AnnotationTypeContribution, Collection<AnnotationUsage>> annotationMap;
	private Map<AuditContribution, Map<AnnotationTypeContribution,IResource>> auditMap;
	private Map<NamingContribution, Map<AnnotationTypeContribution,IResource>> namingMap;
	private Map<DecoratorContribution, Map<AnnotationTypeContribution,IResource>> decoratorMap;
	private Map<ModelComponentIconProviderContribution, Map<AnnotationTypeContribution,IResource>> iconProviderMap;
	private Map<PatternFileContribution, Map<AnnotationTypeContribution,IResource>> patternMap;
	
	
	
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
	
	
	public Map<AnnotationTypeContribution, Collection<AnnotationUsage>> getAnnotationMap() {
		if (annotationMap == null){
			init();
		}
		return annotationMap;
	}


	public Map<AuditContribution, Map<AnnotationTypeContribution, IResource>> getAuditMap() {
		if (auditMap == null){
			init();
		}
		return auditMap;
	}
	
	public Map<NamingContribution, Map<AnnotationTypeContribution,IResource>> getNamingMap() {
		if (namingMap == null){
			init();
		}
		return namingMap;
	}


	public Map<DecoratorContribution, Map<AnnotationTypeContribution,IResource>> getDecoratorMap() {
		if (decoratorMap == null){
			init();
		}return decoratorMap;
	}


	public Map<ModelComponentIconProviderContribution, Map<AnnotationTypeContribution,IResource>> getIconProviderMap() {
		if (iconProviderMap == null){
			init();
		}return iconProviderMap;
	}

	public Map<PatternFileContribution, Map<AnnotationTypeContribution,IResource>> getPatternMap() {
		if (patternMap == null){
			init();
		}return patternMap;
	}

	
	private void init(){
		annotationMap = new HashMap<AnnotationTypeContribution, Collection<AnnotationUsage>>();
		
		auditMap = new HashMap<AuditContribution, Map<AnnotationTypeContribution,IResource>>();
		namingMap = new HashMap<NamingContribution, Map<AnnotationTypeContribution,IResource>>();
		iconProviderMap = new HashMap<ModelComponentIconProviderContribution, Map<AnnotationTypeContribution,IResource>>();
		decoratorMap = new HashMap<DecoratorContribution, Map<AnnotationTypeContribution,IResource>>();
		patternMap = new HashMap<PatternFileContribution, Map<AnnotationTypeContribution,IResource>>();
		
		for (AnnotationTypeContribution annotation : provider.getAnnotationTypeContributions()){
			Collection<IResource> uses = readAnnotationUsage(annotation);
			Collection<AnnotationUsage> annotationUses = new ArrayList<AnnotationUsage>();
			
			// Now see here those things are (in other contribution terms)
			for (IResource use : uses){
				boolean foundUsage = false;
				String containedClassName = "";
				try{
					IJavaElement element = (IJavaElement) use.getAdapter(IJavaElement.class);
					if (element.getElementType() == IJavaElement.COMPILATION_UNIT){
						containedClassName = ((ICompilationUnit)element).getTypes()[0].getFullyQualifiedName();
					}
					
					//System.out.println("Reading stuff for "+annotation.getName()+ " in Audit");
					for (AuditContribution audit : provider.getAuditContributions()){
						if (audit.getAuditorClass().equals(containedClassName)){
							Map<AnnotationTypeContribution,IResource> map = auditMap.get(audit);
							if ( map == null){
								map = new HashMap<AnnotationTypeContribution,IResource>();
							}
							map.put(annotation,use);
							auditMap.put(audit, map);
							annotationUses.add(new AnnotationUsage(use,audit));
							foundUsage = true;
						}
					}
					

					//System.out.println("Reading stuff for "+annotation.getName()+ " in Naming");
					for (NamingContribution naming : provider.getNamingContributions()){
						if (naming.getNamingClass().equals(containedClassName)){
							Map<AnnotationTypeContribution,IResource> map = namingMap.get(naming);
							if ( map == null){
								map = new HashMap<AnnotationTypeContribution,IResource>();
							}
							map.put(annotation,use);
							namingMap.put(naming, map);
							annotationUses.add(new AnnotationUsage(use,naming));
							foundUsage = true;
						}
					}
					
					//System.out.println("Reading stuff for "+annotation.getName()+ " in Decorators");
					for (DecoratorContribution decorator : provider.getDecoratorContributions()){
						if (decorator.getDecoratorClass().equals(containedClassName)){
							Map<AnnotationTypeContribution,IResource> map = decoratorMap.get(decorator);
							if ( map == null){
								map = new HashMap<AnnotationTypeContribution,IResource>();
							}
							map.put(annotation,use);
							decoratorMap.put(decorator, map);
							annotationUses.add(new AnnotationUsage(use,decorator));
							foundUsage = true;
						}
					}
					
					//System.out.println("Reading stuff for "+annotation.getName()+ " in Icon Provider");
					for (ModelComponentIconProviderContribution iconProvider : provider.getModelComponentIconProviderContributions()){
						if (iconProvider.getProvider().equals(containedClassName)){
							Map<AnnotationTypeContribution,IResource> map = iconProviderMap.get(iconProvider);
							if ( map == null){
								map = new HashMap<AnnotationTypeContribution,IResource>();
							}
							map.put(annotation,use);
							iconProviderMap.put(iconProvider, map);
							annotationUses.add(new AnnotationUsage(use,iconProvider));
							foundUsage = true;
						}
					}
					
					
					//System.out.println("Reading stuff for "+annotation.getName()+ " in Pattern Validator");
					for (PatternFileContribution patternContribution : provider.getPatternFileContributions()){
						if (patternContribution.getValidatorClass().equals(containedClassName)){
							Map<AnnotationTypeContribution,IResource> map = patternMap.get(patternContribution);
							if ( map == null){
								map = new HashMap<AnnotationTypeContribution,IResource>();
							}
							map.put(annotation,use);
							patternMap.put(patternContribution, map);
							annotationUses.add(new AnnotationUsage(use,patternContribution));
							foundUsage = true;
						}
					}
					
					
					if (!foundUsage ){
						annotationUses.add(new AnnotationUsage(use,null));
					}
					
					
				} catch (Exception e){
					e.printStackTrace();
				}
			}
			annotationMap.put(annotation, annotationUses);
			
			// Patterns are done the other way round!
			//System.out.println("Reading stuff for "+annotation.getName()+ " in Patterns "+provider.getPatternFileContributions().size());
			for (PatternFileContribution patternContribution : provider.getPatternFileContributions()){
				IPattern pattern = provider.getPattern(patternContribution.getContributor(), patternContribution.getFileName());
				if ( pattern != null){
					Collection<Class<?>> patternUsedAnns = pattern.getUsedAnnotations();
					for (Class<?> ann : patternUsedAnns){
						if (ann.getName().equals(provider.getPackageForAnnotation(annotation)+annotation.getName())){
							Map<AnnotationTypeContribution,IResource> map = patternMap.get(pattern);
							if ( map == null){
								map = new HashMap<AnnotationTypeContribution,IResource>();
							}
							
							
							IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
							IResource pfile = root.findMember(patternContribution.getContributor().toString());
							if (pfile != null && pfile instanceof IContainer){
								IResource patternResource = ((IContainer) pfile).findMember(patternContribution.getFileName());
								
								map.put(annotation,patternResource);
								patternMap.put(patternContribution, map);
								
								Collection<AnnotationUsage> existing = annotationMap.get(annotation);
								existing.add(new AnnotationUsage(patternResource,patternContribution));
								annotationMap.put(annotation,existing);
							}
						}
					}
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
	
	
}
