/**
 * 
 */
package org.eclipse.tigerstripe.workbench.model.annotation;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A local and very temporary cache of annotations from a single <code>AnnotationCapable</code>
 * instance. References to such a cache should not be held.
 * @author jworrell
 *
 */
public class AnnotationCache
{
	private Map<String,List<Object>> annotations;

	public AnnotationCache(IAnnotationCapable ac, String prefix)
	{
		annotations = new LinkedHashMap<String,List<Object>>();
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
						List<Object> l = annotations.get(key);
						if(l == null)
						{
							l = new LinkedList<Object>();
							annotations.put(key,l);
						}
						l.add(o);
					}
				}
			}
		}
	}
	
	public boolean hasAnnotation(String id)
	{
		return annotations.containsKey(id);
	}
	
	public List<Object> getAnnotations(String id)
	{
		return annotations.get(id);
	}
}