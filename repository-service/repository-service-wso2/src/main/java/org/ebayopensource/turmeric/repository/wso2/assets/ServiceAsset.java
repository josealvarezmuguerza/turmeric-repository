/*******************************************************************************
 * Copyright (c) 2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.repository.wso2.assets;

import javax.xml.namespace.QName;

import org.ebayopensource.turmeric.repository.v2.services.*;
import org.ebayopensource.turmeric.repository.wso2.Asset;
import org.ebayopensource.turmeric.repository.wso2.AssetFactory;
import org.ebayopensource.turmeric.repository.wso2.filters.DuplicateServiceFilter;
import org.wso2.carbon.governance.api.common.dataobjects.GovernanceArtifact;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.services.ServiceManager;
import org.wso2.carbon.governance.api.services.dataobjects.Service;
import org.wso2.carbon.governance.api.wsdls.WsdlManager;
import org.wso2.carbon.governance.api.wsdls.dataobjects.Wsdl;
import org.wso2.carbon.registry.core.Registry;

public class ServiceAsset implements Asset {

	private BasicAssetInfo basicInfo = null;
	private ServiceManager serviceManager = null;
	private Service service = null;
	private Registry registry = null;
	
	public ServiceAsset(BasicAssetInfo bi, Registry registry) {
		this.basicInfo = bi;
		this.registry = registry;
		serviceManager =  new ServiceManager(registry);
	}
	
	@Override
	public boolean isNamespaceRequired() {
		return true;
	}

	@Override
	public boolean hasNamespace() {
		return basicInfo.getNamespace() != null;
	}
	
	@Override
	public String getType() {
		return basicInfo.getAssetType();
	}

	@Override
	public boolean createAsset() {
		try {
			service = serviceManager.newService(new QName(
					basicInfo.getNamespace(), basicInfo.getAssetName()));
			service.setAttribute(ServiceConstants.TURMERIC_SERVICE_NAME, basicInfo.getAssetName());
			service.setAttribute(ServiceConstants.TURMERIC_SERVICE_DESCRIPTION,
					basicInfo.getAssetDescription());
			service.setAttribute(ServiceConstants.TURMERIC_VERSION, basicInfo.getVersion());
			service.setAttribute(ServiceConstants.TURMERIC_NAMESPACE, basicInfo.getNamespace());
			service.setAttribute(ServiceConstants.TURMERIC_OWNER, basicInfo.getGroupName());
		} catch (GovernanceException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean hasName() {
		return basicInfo.getAssetName() != null && basicInfo.getAssetName().length() > 0;
	}
	
	@Override
	public boolean duplicatesAllowed() {
		return false;
	}
	
	@Override
	public boolean hasDuplicates() {
		Service[] services;
		try {
			services = serviceManager
					.findServices(new DuplicateServiceFilter(basicInfo));
		} catch (GovernanceException e) {
			return false;
		}
		return services.length > 0;
	}
	
	@Override
	public boolean addAsset() {
		try {
			serviceManager.addService(service);			
		} catch (GovernanceException e) {
			return false;
		}
		
		if (basicInfo.getAssetKey() == null) {
			basicInfo.setAssetKey(new AssetKey());
		}
		basicInfo.getAssetKey().setAssetId(service.getId());

		return true;
	}

	@Override
	public String getId() {
		return basicInfo.getAssetKey().getAssetId();
	}
	
	@Override
	public GovernanceArtifact addArtifact(ArtifactInfo artifact) {
		AssetFactory factory = new AssetFactory(artifact, registry);
		Asset asset = factory.createArtifactAsset();
		
		asset.createAsset();
		asset.addAsset();		
		if (asset.getGovernanceArtifact() != null) {
			if ("WSDL".equalsIgnoreCase(asset.getType())) {
				try {
					String artifactId = asset.getId();
					WsdlManager manager = new WsdlManager(registry);
					Wsdl wsdl = manager.getWsdl(artifactId);
					service.attachWSDL(wsdl);
					return asset.getGovernanceArtifact();
				} catch (GovernanceException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public GovernanceArtifact getGovernanceArtifact() {
		return service;
	}
	
}