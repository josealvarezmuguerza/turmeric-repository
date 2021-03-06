/*******************************************************************************
 * Copyright (c) 2006, 2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.services.repositoryservice.operation.consumer;

import org.ebayopensource.turmeric.repository.v2.services.*;
import org.ebayopensource.turmeric.repository.v2.services.impl.AsyncTurmericRSV2;
import org.ebayopensource.turmeric.runtime.common.exceptions.ServiceException;
import org.ebayopensource.turmeric.runtime.sif.service.Service;
import org.ebayopensource.turmeric.runtime.sif.service.ServiceFactory;
import org.ebayopensource.turmeric.services.repositoryservice.intf.gen.BaseRepositoryServiceConsumer;

public class ApproveAssetConsumer extends BaseRepositoryServiceConsumer {
   private AsyncTurmericRSV2 m_proxy = null;

   public static void testGetAssetStatus_validAsset_WithName() {
      ApproveAssetConsumer approveAssetConsumer = new ApproveAssetConsumer();

      AssetDetail assetDetail = new AssetDetail();
      assetDetail.setAssetName(RepositoryServiceClientConstants.ASSET_NAME);
      assetDetail.setAssetType(RepositoryServiceClientConstants.ASSET_TYPE);
      assetDetail.setVersion(RepositoryServiceClientConstants.ASSET_VERSION);
      ApproveAssetRequest approveAssetRequest = new ApproveAssetRequest();
      ApprovalInfo approvalInfo = new ApprovalInfo();
      approvalInfo.setAssetDetail(assetDetail);
      approvalInfo.setApproverRole(RepositoryServiceClientConstants.VALID_APPROVAL_ROLE);
      approveAssetRequest.setApprovalInfo(approvalInfo);
      ApproveAssetResponse approveAssetResponse = null;
      try {
         approveAssetResponse = approveAssetConsumer.getProxy().approveAsset(approveAssetRequest);
         if (approveAssetResponse == null) {
            throw new ServiceException(null, "Response object can not be null", null);
         }
         System.out.println(approveAssetResponse.isApprovalStatus());
         System.out.println(approveAssetResponse.getAck().toString());

      } catch (ServiceException se) {
         se.getMessage();
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static String testGetAssetStatus_validAsset_WithId(AssetInfo assetInfo) {
      ApproveAssetConsumer approveAssetConsumer = new ApproveAssetConsumer();
      assetInfo.getBasicAssetInfo().getAssetKey();
      ApproveAssetRequest approveAssetRequest = new ApproveAssetRequest();
      ApprovalInfo approvalInfo = new ApprovalInfo();
      approvalInfo.setAssetId(assetInfo.getBasicAssetInfo().getAssetKey().getAssetId());
      approvalInfo.setApproverRole(RepositoryServiceClientConstants.VALID_APPROVAL_ROLE);
      approveAssetRequest.setApprovalInfo(approvalInfo);

      try {
         ApproveAssetResponse approveAssetResponse = approveAssetConsumer.getProxy().approveAsset(approveAssetRequest);
         if (approveAssetResponse == null) {
            throw new ServiceException(null, "Response object can not be null", null);
         }
         if (validateApproveAssetResponse(approveAssetResponse, "positive").equalsIgnoreCase("success")) {
            return "PASSED";
         } else {
            return "FAILED";
         }
      } catch (ServiceException se) {
         se.getMessage();
         se.printStackTrace();
         return "FAILED";
      } catch (Exception e) {
         e.printStackTrace();
         return "FAILED";
      }
   }

   public static void testGetAssetStatus_inValidRole() {
      ApproveAssetConsumer approveAssetConsumer = new ApproveAssetConsumer();

      AssetDetail assetDetail = new AssetDetail();
      assetDetail.setAssetName(RepositoryServiceClientConstants.ASSET_NAME);
      assetDetail.setAssetType(RepositoryServiceClientConstants.ASSET_TYPE);
      assetDetail.setVersion(RepositoryServiceClientConstants.ASSET_VERSION);
      ApproveAssetRequest approveAssetRequest = new ApproveAssetRequest();
      ApprovalInfo approvalInfo = new ApprovalInfo();
      approvalInfo.setAssetDetail(assetDetail);
      approvalInfo.setApproverRole(RepositoryServiceClientConstants.INVALID_APPROVAL_ROLE);
      approveAssetRequest.setApprovalInfo(approvalInfo);

      try {

         ApproveAssetResponse approveAssetResponse = approveAssetConsumer.getProxy().approveAsset(approveAssetRequest);
         if (approveAssetResponse == null) {
            throw new ServiceException(null, "Response object can not be null", null);
         }
         System.out.println(approveAssetResponse.isApprovalStatus());
         System.out.println(approveAssetResponse.getAck().toString());

      } catch (ServiceException se) {
         se.getMessage();
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
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

   public static String validateApproveAssetResponse(ApproveAssetResponse ApproveAssetResponse, String criteria) {
      if (criteria.equalsIgnoreCase("positiveCase")) {
         if (ApproveAssetResponse.getAck().value().equalsIgnoreCase(RepositoryServiceClientConstants.SUCCESS)) {
            return RepositoryServiceClientConstants.SUCCESS;
         }
         if (ApproveAssetResponse.getAck().value().equalsIgnoreCase(RepositoryServiceClientConstants.PARTIAL_FAILURE)) {
            return RepositoryServiceClientConstants.FAILURE;
         }
      }
      if (criteria.equalsIgnoreCase("negativeCase")) {
         if (ApproveAssetResponse.getAck().value().equalsIgnoreCase(RepositoryServiceClientConstants.FAILURE)) {
            if (ApproveAssetResponse.getErrorMessage().getError().size() > 0) {
               return RepositoryServiceClientConstants.SUCCESS;
            }
         }
      }

      return RepositoryServiceClientConstants.FAILURE;
   }
}
