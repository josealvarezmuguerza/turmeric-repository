/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

package org.ebayopensource.turmeric.repository.wso2;

import static org.junit.Assert.*;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.ebayopensource.turmeric.common.v1.types.AckValue;
import org.ebayopensource.turmeric.repository.v2.services.ApprovalInfo;
import org.ebayopensource.turmeric.repository.v2.services.ApproveAssetRequest;
import org.ebayopensource.turmeric.repository.v2.services.ApproveAssetResponse;
import org.ebayopensource.turmeric.repository.v2.services.Artifact;
import org.ebayopensource.turmeric.repository.v2.services.ArtifactInfo;
import org.ebayopensource.turmeric.repository.v2.services.ArtifactValueType;
import org.ebayopensource.turmeric.repository.v2.services.AssetInfo;
import org.ebayopensource.turmeric.repository.v2.services.AssetKey;
import org.ebayopensource.turmeric.repository.v2.services.AssetLifeCycleInfo;
import org.ebayopensource.turmeric.repository.v2.services.AttributeNameValue;
import org.ebayopensource.turmeric.repository.v2.services.BasicAssetInfo;
import org.ebayopensource.turmeric.repository.v2.services.CreateCompleteAssetRequest;
import org.ebayopensource.turmeric.repository.v2.services.CreateCompleteAssetResponse;
import org.ebayopensource.turmeric.repository.v2.services.ExtendedAssetInfo;
import org.ebayopensource.turmeric.repository.v2.services.FlattenedRelationship;
import org.ebayopensource.turmeric.repository.v2.services.RejectAssetRequest;
import org.ebayopensource.turmeric.repository.v2.services.RejectAssetResponse;
import org.ebayopensource.turmeric.repository.v2.services.RejectionInfo;
import org.ebayopensource.turmeric.repository.v2.services.SubmitForPublishingRequest;
import org.ebayopensource.turmeric.repository.v2.services.SubmitForPublishingResponse;
import org.ebayopensource.turmeric.services.repositoryservice.impl.RepositoryServiceProvider;

/**
 * @author mgorovoy
 * 
 */
public class AssetLifeCycleTest extends Wso2Base {
   // First resource path must be the primary resource created by the test
   // in order for the assumption checks to work correctly.
   private static final String[] resources = {
            "/_system/governance/trunk/services/http/www/domain/com/assets/AssetLifeCycleTest",
            "/_system/governance/trunk/endpoints/http/www/domain/com/ep-AssetLifeCycleTest", };

   private static final String assetName = "AssetLifeCycleTest";
   private static final String assetDesc = "AssetLifeCycleTest description";
   private static final String assetComment = "AssetLifeCycleTest comment";
   private static final String libraryName = "http://www.domain.com/assets";
   private static final String baseUrl = "http://www.domain.com/assets/";

   @Before
   @Override
   public void setUp() throws Exception {
      super.setUp();

      boolean exists = false;
      try {
         Registry wso2 = RSProviderUtil.getRegistry();
         exists = wso2.resourceExists("/");

         for (String resource : resources) {
            if (wso2.resourceExists(resource)) {
               wso2.delete(resource);
            }
         }
      } catch (Exception ex) {
      }

      assertTrue(exists);
   }

   private CreateCompleteAssetResponse createAsset() throws Exception {
      AssetKey key = new AssetKey();
      key.setAssetName(assetName);

      BasicAssetInfo basicInfo = new BasicAssetInfo();
      basicInfo.setAssetKey(key);
      basicInfo.setAssetName(assetName);
      basicInfo.setAssetDescription(assetDesc);
      basicInfo.setAssetType("Service");

      ExtendedAssetInfo extendedInfo = new ExtendedAssetInfo();
      List<AttributeNameValue> attrs = extendedInfo.getAttribute();
      attrs.add(RSProviderUtil.newAttribute("namespace", libraryName));

      AssetLifeCycleInfo lifeCycleInfo = new AssetLifeCycleInfo();
      lifeCycleInfo.setDomainOwner("John Doe");
      lifeCycleInfo.setDomainType("Technical Owner");

      AssetInfo assetInfo = new AssetInfo();
      assetInfo.setBasicAssetInfo(basicInfo);
      assetInfo.setExtendedAssetInfo(extendedInfo);
      assetInfo.setAssetLifeCycleInfo(lifeCycleInfo);

      Artifact endpoint = new Artifact();
      endpoint.setArtifactName("ep-" + assetName);
      endpoint.setArtifactCategory("endpoint");
      endpoint.setArtifactValueType(ArtifactValueType.URL);

      String endpointUrl = baseUrl + assetName;
      ArtifactInfo endpointInfo = new ArtifactInfo();
      endpointInfo.setArtifact(endpoint);
      endpointInfo.setArtifactDetail(endpointUrl.getBytes("UTF-8"));
      endpointInfo.setContentType("application/vnd.wso2.endpoint");

      Artifact wsdl = new Artifact();
      wsdl.setArtifactName(assetName + ".wsdl");
      wsdl.setArtifactCategory("wsdl");
      wsdl.setArtifactValueType(ArtifactValueType.FILE);

      ArtifactInfo wsdlInfo = new ArtifactInfo();
      wsdlInfo.setArtifact(wsdl);
      wsdlInfo.setArtifactDetail(loadFile("src/main/resources/META-INF/soa/services/wsdl/CreateServiceTest/CreateServiceTest.wsdl"));
      wsdlInfo.setContentType("application/wsdl+xml");

      List<ArtifactInfo> artifactList = assetInfo.getArtifactInfo();
      artifactList.add(endpointInfo);
      artifactList.add(wsdlInfo);

      FlattenedRelationship rel = new FlattenedRelationship();
      rel.getRelatedAsset();
      // relationList.add(
      // new Relation() {
      // {
      // this.setSourceAsset(RSProviderUtil.completeAssetKey(
      // new AssetKey() {
      // {
      // this.setAssetId(resources[0]);
      // }
      // }, null, null));
      // this.setTargetAsset(RSProviderUtil.completeAssetKey(
      // new AssetKey() {
      // {
      // this.setAssetId(dstAsset);
      // }
      // }, null, null));
      // this.setAssetRelationship("DependsOn");
      // }
      // });
      assetInfo.setFlattenedRelationship(rel);

      CreateCompleteAssetRequest request = new CreateCompleteAssetRequest();
      request.setAssetInfo(assetInfo);

      RepositoryServiceProvider provider = new RepositoryServiceProviderImpl();
      CreateCompleteAssetResponse response = provider.createCompleteAsset(request);

      assertEquals(AckValue.SUCCESS, response.getAck());
      assertEquals(null, response.getErrorMessage());
      return response;
   }

   @Test
   @Ignore
   public void submitApproveTest() throws Exception {
      Registry wso2 = RSProviderUtil.getRegistry();
      RepositoryServiceProvider provider = new RepositoryServiceProviderImpl();

      CreateCompleteAssetResponse responseCreate = createAsset();

      SubmitForPublishingRequest requestSubmit = new SubmitForPublishingRequest();
      requestSubmit.setAssetKey(responseCreate.getAssetKey());
      requestSubmit.setComment(assetComment);

      SubmitForPublishingResponse responseSubmit = provider.submitForPublishing(requestSubmit);

      assertEquals(AckValue.SUCCESS, responseSubmit.getAck());
      assertEquals(null, responseSubmit.getErrorMessage());

      Resource asset = wso2.get(resources[0]);

      RSLifeCycle lc = RSLifeCycle.get(asset);
      assertEquals("New", lc.getState());
      assertEquals(true, lc.getItem(0).isValue());

      ApprovalInfo approvalInfo = new ApprovalInfo();
      approvalInfo.setAssetId(responseCreate.getAssetKey().getAssetId());
      approvalInfo.setComments(assetComment + " approved");

      ApproveAssetRequest requestApprove = new ApproveAssetRequest();
      requestApprove.setApprovalInfo(approvalInfo);

      ApproveAssetResponse responseApprove = provider.approveAsset(requestApprove);
      assertEquals(AckValue.SUCCESS, responseApprove.getAck());
      assertEquals(null, responseApprove.getErrorMessage());

      asset = wso2.get(resources[0]);

      lc = RSLifeCycle.get(asset);
      assertEquals("Proposed", lc.getState());
      assertEquals(true, lc.getItem(0).isValue());
      assertEquals(false, lc.getItem(1).isValue());
   }

   @Test
   @Ignore
   public void submitRejectTest() throws Exception {
      Registry wso2 = RSProviderUtil.getRegistry();

      RepositoryServiceProvider provider = new RepositoryServiceProviderImpl();

      CreateCompleteAssetResponse responseCreate = createAsset();

      SubmitForPublishingRequest requestSubmit = new SubmitForPublishingRequest();
      requestSubmit.setAssetKey(responseCreate.getAssetKey());
      requestSubmit.setComment(assetComment);

      SubmitForPublishingResponse responseSubmit = provider.submitForPublishing(requestSubmit);

      assertEquals("Unable to submit for publishing. " + getErrorMessage(responseSubmit), AckValue.SUCCESS,
               responseSubmit.getAck());
      assertEquals(null, responseSubmit.getErrorMessage());

      Resource asset = wso2.get(resources[0]);

      RSLifeCycle lc = RSLifeCycle.get(asset);
      assertEquals("New", lc.getState());
      assertEquals(true, lc.getItem(0).isValue());

      RejectionInfo rejectionInfo = new RejectionInfo();
      rejectionInfo.setAssetId(responseCreate.getAssetKey().getAssetId());
      rejectionInfo.setComments(assetComment + " rejected");

      RejectAssetRequest requestReject = new RejectAssetRequest();
      requestReject.setRejectionInfo(rejectionInfo);

      RejectAssetResponse responseReject = provider.rejectAsset(requestReject);
      assertEquals(getErrorMessage(responseReject), AckValue.SUCCESS, responseReject.getAck());
      assertEquals(null, responseReject.getErrorMessage());

      asset = wso2.get(resources[0]);

      lc = RSLifeCycle.get(asset);
      assertEquals("New", lc.getState());
      assertEquals(false, lc.getItem(0).isValue());
   }
}
