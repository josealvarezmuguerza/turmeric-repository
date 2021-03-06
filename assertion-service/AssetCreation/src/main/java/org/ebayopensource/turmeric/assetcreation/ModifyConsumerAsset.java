/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.assetcreation;

import java.util.List;

import org.ebayopensource.turmeric.assetcreation.artifacts.AssetInput;
import org.ebayopensource.turmeric.assetcreation.exception.ProcessingException;
import org.ebayopensource.turmeric.repository.v2.services.*;

/**
 * The Class ModifyConsumerAsset.
 */
public class ModifyConsumerAsset extends ModifyAsset {

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.ebayopensource.turmeric.assetcreation.ModifyAsset#modify(org.ebayopensource.turmeric.repository.v1.services
    * .AssetInfo, org.ebayopensource.turmeric.assetcreation.artifacts.AssetInput)
    */
   @Override
   public void modify(AssetInfo assetInfo, AssetInput assetInput) throws ProcessingException {

      modifyAssetName(assetInfo, assetInput);
      modifyAssetId(assetInfo);
      modifyArtifact(assetInfo);
      modifySourceRelationship(assetInfo);
      setClassifier(assetInfo);
   }

   /**
    * Modify source relationship.
    * 
    * @param assetInfo
    *           the asset info
    */
   public void modifySourceRelationship(AssetInfo assetInfo) {

      if (assetInfo.getFlattenedRelationship() != null) {
         assetInfo.getFlattenedRelationship().setSourceAsset(null);
      }
   }

   /**
    * Modify artifact.
    * 
    * @param assetInfo
    *           the asset info
    */
   public void modifyArtifact(AssetInfo assetInfo) {
      List<ArtifactInfo> artifactInfos = assetInfo.getArtifactInfo();

      for (ArtifactInfo artifactInfo : artifactInfos) {
         artifactInfo.getArtifact().setArtifactIdentifier(null);
      }
   }

}
