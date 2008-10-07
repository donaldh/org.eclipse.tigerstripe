/**
 * 
 */
package org.eclipse.tigerstripe.workbench.model.annotation;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * A local and very temporary cache of annotations from a single <code>AnnotationCapable</code>
 * instance. References to such a cache should not be held.
 * @author jworrell
 *
 */
public class AnnotationCache
{
	private Map<String,List<Object>> annotationsByName;

	private Map<Class,List<Object>> annotationsByClass;
	
	public AnnotationCache(IAnnotationCapable ac, String prefix)
	{
		initByName(ac, prefix);
	}

	private void initByName(IAnnotationCapable ac, String prefix) {
		annotationsByName = new LinkedHashMap<String,List<Object>>();
		for(Object o : ac.getAnnotations())
		{
			Class<?> c = o.getClass();
			if(c.getInterfaces() != null)
			{
				for(Class<?> i : c.getInterfaces())
				{
					String name = i.getName();
					if(name.startsWith(prefix))
					{
						String key = name.substring(prefix.length()+1);
						List<Object> l = annotationsByName.get(key);
						if(l == null)
						{
							l = new LinkedList<Object>();
							annotationsByName.put(key,l);
						}
						l.add(o);
					}
				}
			}
		}
	}
	
	void initByClass()
	{
		if(annotationsByClass == null)
		{
			annotationsByClass = new LinkedHashMap<Class,List<Object>>();
			for(List<Object> l : annotationsByName.values())
			{
				Object o = l.get(0);
				Class i = o.getClass().getInterfaces()[0];
				do {
					annotationsByClass.put(i, l);
				}
				while((i = i.getSuperclass()) != EObject.class);					
			}
		}
	}
	
	public boolean hasAnnotation(String id)
	{
		return annotationsByName.containsKey(id);
	}
	
	public List<Object> getAnnotations(String id)
	{
		return annotationsByName.get(id);
	}

	Map<Class,List<Object>> getAnnotationsByClass()
	{
		if(annotationsByClass == null)
			initByClass();
		return annotationsByClass;
	}
	
	public boolean hasAnnotation(Class id)
	{
		return getAnnotationsByClass().containsKey(id);
	}
	
	public List<Object> getAnnotations(Class id)
	{
		return getAnnotationsByClass().get(id);
	}
}