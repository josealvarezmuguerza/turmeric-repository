/*******************************************************************************
 * Copyright (c) 2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.repository.wso2.assets;

import org.ebayopensource.turmeric.repository.v2.services.ArtifactInfo;
import org.ebayopensource.turmeric.repository.v2.services.BasicAssetInfo;
import org.ebayopensource.turmeric.repository.wso2.Asset;
import org.wso2.carbon.governance.api.common.dataobjects.GovernanceArtifact;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.wsdls.dataobjects.Wsdl;
import org.wso2.carbon.registry.core.Registry;

/**
 * Represents a WSDL asset.
 * 
 * @author dcarver
 * 
 */
public class WSDLAsset implements Asset {

   private ArtifactInfo artifactInfo = null;
   private WSDLManager wsdlManager = null;
   private Wsdl wsdl;
   private Registry registry = null;

   /**
    * Constructor for a WSDL created from an ArtifactInfo object.
    * 
    * @param ai
    *           the artifactInfo object
    * @param registry
    *           the associated registry
    * @throws Exception
    *            any exception that could be thrown
    */
   public WSDLAsset(ArtifactInfo ai, Registry registry) throws Exception {
      artifactInfo = ai;
      this.registry = registry;
      wsdlManager = new WSDLManager(registry);
   }

   public WSDLAsset(BasicAssetInfo bi, Registry registry) throws Exception {
      this.registry = registry;
      wsdlManager = new WSDLManager(registry);
   }

   @Override
   public boolean isNamespaceRequired() {
      return true;
   }

   @Override
   public boolean hasNamespace() {
      return true;
   }

   @Override
   public String getType() {
      return "WSDL";
   }

   @Override
   public boolean hasName() {
      return artifactInfo.getArtifact().getArtifactName() != null;
   }

   @Override
   public boolean duplicatesAllowed() {
      return false;
   }

   @Override
   public boolean hasDuplicates() {
      return false;
   }

   @Override
   public boolean createAsset() {
      try {
         wsdl = wsdlManager.newWsdl(registry, artifactInfo.getArtifactDetail());
         wsdl.setAttribute(AssetConstants.TURMERIC_NAME, artifactInfo.getArtifact().getArtifactName());
         wsdl.setAttribute(AssetConstants.TURMERIC_DISPLAY_NAME, artifactInfo.getArtifact().getArtifactDisplayName());
         wsdl.setAttribute(AssetConstants.TURMERIC_ARTIFACT_CATEGORY, artifactInfo.getArtifact().getArtifactCategory());

      } catch (Exception ex) {
         return false;
      }

      return true;
   }

   /**
    * {@inheritDoc}
    * 
    * Only type of assets that can be added to a WSDL are Schemas and Endpoints.
    */
   @Override
   public boolean addAsset() {
      try {
         wsdlManager.addWsdl(wsdl);
         wsdl = wsdlManager.getWsdl(wsdl.getId());
      } catch (GovernanceException e) {
         return false;
      }
      return true;
   }

   @Override
   public String getId() {
      return wsdl.getId();
   }

   @Override
   public GovernanceArtifact addArtifact(ArtifactInfo artifact) {
      return null;
   }

   @Override
   public GovernanceArtifact getGovernanceArtifact() {
      return wsdl;
   }

   @Override
   public boolean exists() {
      return hasDuplicates();
   }

   @Override
   public void findAsset() {

   }

   @Override
   public void lockAsset() {
      try {
         String lock = wsdl.getAttribute(AssetConstants.TURMERIC_LOCK);
         if (lock == null) {
            wsdl.addAttribute(AssetConstants.TURMERIC_LOCK, "true");
            return;
         }
         wsdl.setAttribute(AssetConstants.TURMERIC_LOCK, "true");
      } catch (GovernanceException e) {
      }

   }

   @Override
   public void unlock() {
      try {
         String lock = wsdl.getAttribute(AssetConstants.TURMERIC_LOCK);
         if (lock == null) {
            wsdl.addAttribute(AssetConstants.TURMERIC_LOCK, "false");
            return;
         }
         wsdl.setAttribute(AssetConstants.TURMERIC_LOCK, "false");
      } catch (GovernanceException e) {
      }

   }

   @Override
   public boolean isLocked() {
      String lock = null;
      try {
         lock = wsdl.getAttribute(AssetConstants.TURMERIC_LOCK);
         if (lock == null) {
            return false;
         }
      } catch (GovernanceException ex) {
         return false;
      }
      return lock.equals("true");
   }

   @Override
   public boolean save() {
      try {
         wsdlManager.updateWsdl(wsdl);
      } catch (GovernanceException ex) {
         return false;
      }
      return true;
   }

}
