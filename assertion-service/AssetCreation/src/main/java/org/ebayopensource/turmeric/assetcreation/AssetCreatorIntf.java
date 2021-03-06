/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.assetcreation;

/**
 * @author sanarayanan
 */

import org.ebayopensource.turmeric.assetcreation.exception.*;
import org.ebayopensource.turmeric.repository.v2.services.*;

/**
 * Implementation class of AssetCreatorIntf can be obtained using AssetCreatorFactory
 * AssetCreatorFactory.getAssetCreator(String InputXmlFilePath).
 */

public interface AssetCreatorIntf {

   /**
    * Populates AssetInfo and assetId depending upon the input XML configuration file.
    * 
    * @throws AssetCreationException
    *            thrown if any exception occurred during the process of Asset Creation, exception will be thrown with
    *            the relevant message
    */
   void createAsset() throws AssetCreationException;

   /**
    * Gets the asset as asset info.
    * 
    * @param id
    *           Unique Id configured in the input XML file
    * @return AssetInfo
    * @throws AssetInfoNotFoundException
    *            thrown when all the populated AssetInfos got used
    * @throws IdNotFoundException
    *            thrown when ID which is not present in XML Configuration is passed
    */
   AssetInfo getAssetAsAssetInfo(String id) throws AssetInfoNotFoundException, IdNotFoundException;

   /**
    * Gets the asset as asset id.
    * 
    * @param id
    *           Unique Id configured in the input XML file
    * @return assetId
    * @throws AssetIdNotFoundException
    *            thrown when all the populated AssetIds got used
    * @throws IdNotFoundException
    *            thrown when ID which is not present in XML Configuration is passed
    */
   String getAssetAsAssetId(String id) throws AssetIdNotFoundException, IdNotFoundException;

   /**
    * Deletes the consumed assets from repository.
    * 
    * @throws ProcessingException
    *            the processing exception
    */
   void deleteConsumedAssets() throws ProcessingException;

   /**
    * Does the clean up work and persist unused assetInfos and assetIds depending upon the configuration configured in
    * the input XML file.
    * 
    * @throws AssetPersistException
    *            thrown if any exception occurred during the process of Asset persistence, exception will be thrown with
    *            the relevant message.
    */

   void persist() throws AssetPersistException;

   /**
    * Retrieves BasicAssetInfo and AssetLifeCycleInfo from AsssetInfo and populates AssetInfoForUpdate and returns it.
    * 
    * @param assetInfo
    *           the asset info
    * @return AssetInfoForUpdate
    */
   AssetInfoForUpdate getAssetInfoForUpdate(AssetInfo assetInfo);

   /**
    * Updates the asset.
    * 
    * @param assetInfoForUpdate
    *           the asset info for update
    * @return UpdateCompleteAssetResponse
    * @throws UpdateCompleteAssetException
    *            the update complete asset exception
    */

   UpdateCompleteAssetResponse updateCompleteAsset(AssetInfoForUpdate assetInfoForUpdate)
            throws UpdateCompleteAssetException;

   /**
    * Locks the assetId.
    * 
    * @param assetKey
    *           the asset key
    * @return LockAssetResponse
    * @throws ProcessingException
    *            the processing exception
    */
   LockAssetResponse lockAsset(AssetKey assetKey) throws ProcessingException;

   /**
    * Unlocks the assetId.
    * 
    * @param assetKey
    *           the asset key
    * @return UnlockAssetResponse
    * @throws ProcessingException
    *            the processing exception
    */

   UnlockAssetResponse unlockAsset(AssetKey assetKey) throws ProcessingException;

   /**
    * Removes the asset from the Asset Repository.
    * 
    * @param assetKey
    *           the asset key
    * @return RemoveAssetResponse
    * @throws ProcessingException
    *            the processing exception
    */
   public RemoveAssetResponse removeAsset(AssetKey assetKey) throws ProcessingException;

}
