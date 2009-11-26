package org.eclipse.tigerstripe.workbench.internal.core.util;

import org.osgi.framework.Version;

public class OSGIRef {
	private Version minVersion;
	private Version maxVersion;
	private int minConstraint;
	private int maxConstraint;
	
	public static String INCLUSIVE = "Inclusive";
	public static String EXCLUSIVE = "Exclusive";
	public static String[] constraintNames = new String[]{INCLUSIVE, EXCLUSIVE};
	public static String[] minConstraintValues = new String[]{"(", "["};
	public static String[] maxConstraintValues = new String[]{")", "]"};
	
	public static int INCLUSIVE_VAL = 0;
	public static int EXCLUSIVE_VAL = 1;
	
	
	public OSGIRef(Version minVersion, Version maxVersion,
			int minConstraint, int maxConstraint) {
		super();
		this.minVersion = minVersion;
		this.maxVersion = maxVersion;
		this.minConstraint = minConstraint;
		this.maxConstraint = maxConstraint;
	}
	
	public Version getMinVersion() {
		return minVersion;
	}
	public Version getMaxVersion() {
		return maxVersion;
	}
	public int getMinConstraint() {
		return minConstraint;
	}
	public int getMaxConstraint() {
		return maxConstraint;
	}
	
	public void setMinVersion(Version minVersion) {
		this.minVersion = minVersion;
	}
	public void setMaxVersion(Version maxVersion) {
		this.maxVersion = maxVersion;
	}
	public void setMinConstraint(int minConstraint) {
		this.minConstraint = minConstraint;
	}
	public void setMaxConstraint(int maxConstraint) {
		this.maxConstraint = maxConstraint;
	}
	
	public static OSGIRef parseRef(String string) throws IllegalArgumentException{
		// Look for 
		// [ or (
		// ) or ]
		// And one or more Versions
		 Version minVersion = null;
		 Version maxVersion = null;
		 int minConstraint = 0;
		 int maxConstraint = 1;
		 
		 if (string.startsWith("(")){
			 minConstraint = INCLUSIVE_VAL;
			 string = string.substring(1);
		 } else if (string.startsWith("[")){
			 minConstraint = EXCLUSIVE_VAL;
			 string = string.substring(1);
		 }
		 
		 if (string.endsWith(")")){
			 maxConstraint = INCLUSIVE_VAL;
			 string = string.substring(0,string.length()-1);
		 } else if (string.endsWith("]")){
			 maxConstraint = EXCLUSIVE_VAL;
			 string = string.substring(0,string.length()-1);
		 }
		 
		 if (string.contains(",")){
			 String[] bits = string.split(",");
			 if (bits.length == 2){
				 minVersion = new Version(bits[0]);
				 maxVersion = new Version(bits[1]);
				 
			 } else {
				 throw new IllegalArgumentException("Incorrect number of Version Strings");
			 }
		 } else {
			 minVersion = new Version(string);
			 maxVersion = null;
			 minConstraint = 0;
			 maxConstraint = 1;
			 
		 }
		 
		return new OSGIRef(minVersion, maxVersion, minConstraint, maxConstraint);
		
	}
	
	public String toString(){
		if (maxVersion == null){
			// Simple case
			return minVersion.toString();
		} else {
			
			return minConstraintValues[minConstraint]+
			       minVersion.toString()+
			       ","+
			       maxVersion.toString()+
			       maxConstraintValues[maxConstraint];
			
		}
		
	}
	
	
	public boolean isInScope(Version inVersion){
		
		int a = inVersion.compareTo(minVersion);
		if (minConstraint == INCLUSIVE_VAL) {
			if (a >= 0 ){
				// This is OK
			} else {
				// This version is Less THAN OR EQUAL TO the min.
				return false;
			}

		} else if (minConstraint == EXCLUSIVE_VAL) {
			if (a > 0 ){
				// This is OK
			} else {
				// This version is Less THAN  the min.
				return false;
			}
		}

		if (maxVersion != null){
			int b = inVersion.compareTo(maxVersion);
			if (maxConstraint == INCLUSIVE_VAL) {
				if (b <= 0 ){
					// This is OK
				} else {
					// This version is MORE THAN OR EQUAL TO the max.
					return false;
				}

			} else if (maxConstraint == EXCLUSIVE_VAL) {
				if (b < 0 ){
					// This is OK
				} else {
					// This version is MORE THAN the max.
					return false;
				}
			}
			
			
			
		}
		
		
		

		return true;
	}
	
}
