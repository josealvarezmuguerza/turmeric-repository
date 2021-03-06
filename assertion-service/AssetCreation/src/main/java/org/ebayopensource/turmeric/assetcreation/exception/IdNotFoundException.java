/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.assetcreation.exception;

/**
 * The Class IdNotFoundException.
 */
public class IdNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3433761101491317349L;

	/**
	 * Instantiates a new id not found exception.
	 */
	public IdNotFoundException() {
		
	}
	
	/**
	 * Instantiates a new id not found exception.
	 *
	 * @param message the message
	 */
	public IdNotFoundException(String message) {
		super(message);
	}

}
