/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.repositorymanager.assertions.exception;

import org.ebayopensource.turmeric.common.v1.types.ErrorMessage;



public class AssertionIllegalArgumentException extends java.lang.IllegalArgumentException 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3466311003091061462L;
	private ErrorMessage errorMessage;
	private Throwable throwable;
	
	
	public AssertionIllegalArgumentException(String message, ErrorMessage errorMessage,
			Throwable throwable) 
	{
		super(message,throwable);
		this.errorMessage = errorMessage;
		this.throwable = throwable;
	}
	
	public AssertionIllegalArgumentException(String message, Throwable throwable) 
	{
		super(message,throwable);
		this.throwable = throwable;
	}
	
	public AssertionIllegalArgumentException(ErrorMessage errorMessage,
			Throwable throwable) 
	{
		super(throwable);
		this.errorMessage = errorMessage;
		this.throwable = throwable;
	}
	
	public AssertionIllegalArgumentException(ErrorMessage errorMessage) 
	{
		this.errorMessage = errorMessage;
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public Throwable getThrowable() {
		return throwable;
	}	
	
}
