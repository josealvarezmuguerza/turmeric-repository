/*******************************************************************************
 * Copyright (c) 2006, 2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.services.repositoryservice.operation.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.ebayopensource.turmeric.repository.v2.services.*;
import org.ebayopensource.turmeric.repository.v2.services.impl.AsyncTurmericRSV2;
import org.ebayopensource.turmeric.services.repository.listener.util.CommonUtil;
import org.ebayopensource.turmeric.services.repositoryservice.intf.gen.BaseRepositoryServiceConsumer;

import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.sif.service.Service;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceFactory;

public class CreateCompleteAssetConsumer extends BaseRepositoryServiceConsumer {
   private AsyncTurmericRSV2 m_proxy = null;
   private static String assetName = null;

   public static String testCreateCompleteAsset_validInput(String variant, AssetInfo targetAssetInfo) {
      CreateCompleteAssetConsumer createCompleteAssetConsumer = new CreateCompleteAssetConsumer();

      Properties props = CommonUtil.loadPropertyFile("properties/common.properties");
      String groupName = props.getProperty("groupName", "GroupName");
      java.util.Date date = new java.util.Date();
      assetName = RepositoryServiceClientConstants.ASSET_NAME + date.getTime();
      AssetKey assetKey = new AssetKey();
      assetKey.setAssetName(assetName);

      BasicAssetInfo basicAssetInfo = new BasicAssetInfo();
      basicAssetInfo.setAssetDescription(RepositoryServiceClientConstants.ASSET_DESCRIPTION);
      basicAssetInfo.setAssetName(assetName);
      basicAssetInfo.setAssetType(RepositoryServiceClientConstants.ASSET_TYPE);
      basicAssetInfo.setVersion(RepositoryServiceClientConstants.ASSET_VERSION);
      basicAssetInfo.setAssetLongDescription(RepositoryServiceClientConstants.ASSET_LONG_DESCRIPTION);
      basicAssetInfo.setAssetKey(assetKey);
      basicAssetInfo.setGroupName(groupName);
      AssetInfo assetInfo = new AssetInfo();
      assetInfo.setBasicAssetInfo(basicAssetInfo);

      if (variant.equalsIgnoreCase("noAssetLongDescription")) {
         basicAssetInfo.setAssetLongDescription(null);
      }

      ArtifactInfo artifactInfo = new ArtifactInfo();
      Artifact artifact = new Artifact();
      // artifact.setArtifactIdentifier(RepositoryServiceClientConstants.ARTIFACT_ID);
      artifact.setArtifactName("test.wsdl");
      artifact.setArtifactCategory(RepositoryServiceClientConstants.ARTIFACT_CATEGORY);
      artifact.setArtifactValueType(ArtifactValueType
               .valueOf(RepositoryServiceClientConstants.ARTIFACT_VALUE_TYPE_FILE));
      artifactInfo.setArtifact(artifact);
      String temp = "Hello world";
      artifactInfo.setArtifactDetail(temp.getBytes());
      artifactInfo.setContentType("application/xml");
      assetInfo.getArtifactInfo().add(artifactInfo);

      List<AttributeNameValue> attributeNameValues = new ArrayList<AttributeNameValue>();
      AttributeNameValue attributeNameValue = new AttributeNameValue();
      attributeNameValue.setAttributeName(RepositoryServiceClientConstants.ATTRIBUTE1_NAME);
      attributeNameValue.setAttributeValueString(RepositoryServiceClientConstants.ATTRIBUTE1_VALUE);
      attributeNameValues.add(attributeNameValue);

      ExtendedAssetInfo extendedAssetInfo = new ExtendedAssetInfo();
      extendedAssetInfo.getAttribute().addAll(attributeNameValues);
      assetInfo.setExtendedAssetInfo(extendedAssetInfo);

      FlattenedRelationship flattenedRelationship = new FlattenedRelationship();
      flattenedRelationship.setDepth(1);
      flattenedRelationship.setPartial(false);
      flattenedRelationship.setSourceAsset(assetKey);
      Relation relation = new Relation();
      relation.setSourceAsset(assetKey);

      AssetKey targetAssetKey = new AssetKey();
      targetAssetKey.setAssetId(targetAssetInfo.getBasicAssetInfo().getAssetKey().getAssetId());
      targetAssetKey.setAssetName(targetAssetInfo.getBasicAssetInfo().getAssetKey().getAssetName());

      relation.setTargetAsset(targetAssetKey);
      relation.setAssetRelationship("Predecessor");
      flattenedRelationship.getRelatedAsset().add(relation);
      assetInfo.setFlattenedRelationship(flattenedRelationship);

      AssetLifeCycleInfo assetLifeCycleInfo = new AssetLifeCycleInfo();
      assetLifeCycleInfo.setApprover("jpnadar");
      assetLifeCycleInfo.setLifeCycleState("Approved");
      assetLifeCycleInfo.setProjectManager("arajmony");
      assetInfo.setAssetLifeCycleInfo(assetLifeCycleInfo);

      CreateCompleteAssetRequest createCompleteAssetRequest = new CreateCompleteAssetRequest();
      createCompleteAssetRequest.setAssetInfo(assetInfo);

      try {
         CreateCompleteAssetResponse createCompleteAssetResponse = createCompleteAssetConsumer.getProxy()
                  .createCompleteAsset(createCompleteAssetRequest);

         if (createCompleteAssetResponse == null) {
            throw new ServiceException(null, "Response object can not be null", null);
         }
         if (validateCreateCompleteAssetResponse(createCompleteAssetResponse, "positiveCase").equalsIgnoreCase(
                  RepositoryServiceClientConstants.SUCCESS)) {
            return RepositoryServiceClientConstants.PASS;
         } else {
            return RepositoryServiceClientConstants.FAIL;
         }
      } catch (ServiceException se) {
         se.getMessage();
         se.printStackTrace();
         return RepositoryServiceClientConstants.FAIL;
      } catch (Exception e) {
         e.printStackTrace();
         return RepositoryServiceClientConstants.FAIL;
      }
   }

   public static String testCreateCompleteAsset_invalidInput(String variant, AssetInfo targetAssetInfo) {
      CreateCompleteAssetConsumer createCompleteAssetConsumer = new CreateCompleteAssetConsumer();

      Properties props = CommonUtil.loadPropertyFile("properties/common.properties");
      props.getProperty("libraryId");
      props.getProperty("libraryName", "GovernedAssets");

      java.util.Date date = new java.util.Date();
      assetName = RepositoryServiceClientConstants.ASSET_NAME + date.getTime();
      AssetKey assetKey = new AssetKey();
      assetKey.setAssetName(assetName);

      BasicAssetInfo basicAssetInfo = new BasicAssetInfo();
      basicAssetInfo.setAssetDescription(RepositoryServiceClientConstants.ASSET_DESCRIPTION);
      basicAssetInfo.setAssetName(assetName);
      basicAssetInfo.setAssetType(RepositoryServiceClientConstants.ASSET_TYPE);
      basicAssetInfo.setVersion(RepositoryServiceClientConstants.ASSET_VERSION);
      basicAssetInfo.setAssetLongDescription(RepositoryServiceClientConstants.ASSET_LONG_DESCRIPTION);
      basicAssetInfo.setAssetKey(assetKey);

      AssetInfo assetInfo = new AssetInfo();
      assetInfo.setBasicAssetInfo(basicAssetInfo);

      if (variant.equalsIgnoreCase("noAssetLongDescription")) {
         basicAssetInfo.setAssetLongDescription(null);
      }

      ArtifactInfo artifactInfo = new ArtifactInfo();
      Artifact artifact = new Artifact();
      artifact.setArtifactName("test.wsdl");
      artifact.setArtifactCategory(RepositoryServiceClientConstants.ARTIFACT_CATEGORY);
      artifact.setArtifactValueType(ArtifactValueType
               .valueOf(RepositoryServiceClientConstants.ARTIFACT_VALUE_TYPE_FILE));
      artifactInfo.setArtifact(artifact);
      String temp = "Hello world";
      artifactInfo.setArtifactDetail(temp.getBytes());
      artifactInfo.setContentType("application/xml");
      assetInfo.getArtifactInfo().add(artifactInfo);

      List<AttributeNameValue> attributeNameValues = new ArrayList<AttributeNameValue>();
      AttributeNameValue attributeNameValue = new AttributeNameValue();
      attributeNameValue.setAttributeName(RepositoryServiceClientConstants.ATTRIBUTE1_NAME);
      attributeNameValue.setAttributeValueString(RepositoryServiceClientConstants.ATTRIBUTE1_VALUE);
      attributeNameValues.add(attributeNameValue);

      ExtendedAssetInfo extendedAssetInfo = new ExtendedAssetInfo();
      extendedAssetInfo.getAttribute().addAll(attributeNameValues);
      assetInfo.setExtendedAssetInfo(extendedAssetInfo);

      FlattenedRelationship flattenedRelationship = new FlattenedRelationship();
      flattenedRelationship.setDepth(1);
      flattenedRelationship.setPartial(false);
      flattenedRelationship.setSourceAsset(assetKey);
      Relation relation = new Relation();
      relation.setSourceAsset(assetKey);

      AssetKey targetAssetKey = new AssetKey();
      targetAssetKey.setAssetId(targetAssetInfo.getBasicAssetInfo().getAssetKey().getAssetId());
      targetAssetKey.setAssetName(targetAssetInfo.getBasicAssetInfo().getAssetKey().getAssetName());

      relation.setTargetAsset(targetAssetKey);
      relation.setAssetRelationship("Predecessor");
      flattenedRelationship.getRelatedAsset().add(relation);
      assetInfo.setFlattenedRelationship(flattenedRelationship);

      AssetLifeCycleInfo assetLifeCycleInfo = new AssetLifeCycleInfo();
      assetLifeCycleInfo.setApprover("jpnadar");
      assetLifeCycleInfo.setLifeCycleState("Approved");
      assetLifeCycleInfo.setProjectManager("arajmony");
      assetInfo.setAssetLifeCycleInfo(assetLifeCycleInfo);

      CreateCompleteAssetRequest createCompleteAssetRequest = new CreateCompleteAssetRequest();
      createCompleteAssetRequest.setAssetInfo(assetInfo);

      if (variant.equals("noRequest")) {
         createCompleteAssetRequest = null;
      }

      try {
         CreateCompleteAssetResponse createCompleteAssetResponse = createCompleteAssetConsumer.getProxy()
                  .createCompleteAsset(createCompleteAssetRequest);

         if (createCompleteAssetResponse == null) {
            throw new ServiceException(null, "Response object can not be null", null);
         }
         if (validateCreateCompleteAssetResponse(createCompleteAssetResponse, "negativeCase").equalsIgnoreCase(
                  RepositoryServiceClientConstants.SUCCESS)) {
            return RepositoryServiceClientConstants.PASS;
         } else {
            return RepositoryServiceClientConstants.FAIL;
         }
      } catch (ServiceException se) {
         se.getMessage();
         se.printStackTrace();
         return RepositoryServiceClientConstants.FAIL;
      } catch (Exception e) {
         e.printStackTrace();
         return RepositoryServiceClientConstants.FAIL;
      }
   }

   private static String validateCreateCompleteAssetResponse(CreateCompleteAssetResponse createCompleteAssetResponse,
            String criteria) {
      if (criteria.equalsIgnoreCase("positiveCase")) {
         if (createCompleteAssetResponse.getAck().value().equalsIgnoreCase(RepositoryServiceClientConstants.SUCCESS)) {
            return RepositoryServiceConsumerUtil.validateAssetKey(createCompleteAssetResponse.getAssetKey());
         }
         return RepositoryServiceClientConstants.FAILURE;
      }
      if (criteria.equalsIgnoreCase("negativeCase")) {
         if (createCompleteAssetResponse.getAck().value().equalsIgnoreCase(RepositoryServiceClientConstants.FAILURE)) {
            if (createCompleteAssetResponse.getErrorMessage().getError().size() > 0) {
               return RepositoryServiceClientConstants.SUCCESS;
            }
         }
      }

      return RepositoryServiceClientConstants.FAILURE;
   }

   @Override
   protected AsyncTurmericRSV2 getProxy() throws ServiceException {
      if (m_proxy == null) {
         String svcAdminName = RepositoryServiceClientConstants.SERVICE_NAME;
         Service service = ServiceFactory.create(svcAdminName, RepositoryServiceClientConstants.SERVICE_NAME);
         service.setSessionTransportHeader("X-TURMERIC-SECURITY-USERID", RepositoryServiceClientConstants.USER_ID);
         service.setSessionTransportHeader("X-TURMERIC-SECURITY-PASSWORD",
                  RepositoryServiceClientConstants.USER_PASSWORD);

         m_proxy = service.getProxy();
      }

      return m_proxy;
   }
}
