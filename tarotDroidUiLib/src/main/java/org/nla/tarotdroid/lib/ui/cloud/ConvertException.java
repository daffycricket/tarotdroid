package org.nla.tarotdroid.lib.ui.cloud;

import static com.google.common.base.Preconditions.checkArgument;

@SuppressWarnings("serial")
public class ConvertException extends RuntimeException {

	@SuppressWarnings("rawtypes")
	private Class fromClass;
	
	@SuppressWarnings("rawtypes")
	private Class toClass;
	
	@SuppressWarnings("rawtypes")
	public ConvertException(Class fromClass, Class toClass, Throwable throwable) {
		super(throwable);
		checkArgument(fromClass != null, "fromClass is null");
		checkArgument(toClass != null, "toClass is null");
		checkArgument(throwable != null, "throwable is null");
		
		this.fromClass = fromClass;
		this.toClass = toClass;
	}
	
	@Override
	public String toString() {
		return "From [" + fromClass.getSimpleName() + "] to [" + toClass.getSimpleName() + "] => " + getMessage(); 
	}
}