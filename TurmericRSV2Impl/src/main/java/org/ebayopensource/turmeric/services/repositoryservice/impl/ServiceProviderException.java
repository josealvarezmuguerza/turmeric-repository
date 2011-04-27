/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.services.repositoryservice.impl;

public class ServiceProviderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2623106841603558354L;

	public ServiceProviderException() {
		
	}

	public ServiceProviderException(String message) {
		super(message);
	}

	public ServiceProviderException(Throwable cause) {
		super(cause);
	}

	public ServiceProviderException(String message, Throwable cause) {
		super(message, cause);
	}

}
