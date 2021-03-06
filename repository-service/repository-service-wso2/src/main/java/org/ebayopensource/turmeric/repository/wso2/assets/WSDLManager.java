/*
 * Copyright (c) 2008, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*******************************************************************************
 * Copyright (c) 2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 *    Original code from WSO2 governance registry WsdlManager class
 *    copyright header left in tact.
 *******************************************************************************/

package org.ebayopensource.turmeric.repository.wso2.assets;

import java.util.*;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jaxen.JaxenException;
import org.wso2.carbon.governance.api.common.dataobjects.GovernanceArtifact;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.api.util.GovernanceConstants;
import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.wso2.carbon.governance.api.wsdls.WsdlFilter;
import org.wso2.carbon.governance.api.wsdls.dataobjects.Wsdl;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.utils.RegistryUtils;

import javax.xml.namespace.QName;

/**
 * This provides the management functionality for WSDL artifacts stored on the registry.
 */
public class WSDLManager {

   private static final Log log = LogFactory.getLog(WSDLManager.class);
   private Registry registry;

   /**
    * Constructor accepting an instance of the registry to use.
    * 
    * @param registry
    *           the instance of the registry.
    */
   public WSDLManager(Registry registry) {
      this.registry = registry;
   }

   /**
    * Adds a new WSDL artifact from the given URL.
    * 
    * @param url
    *           the given URL.
    * 
    * @return the artifact added.
    * @throws GovernanceException
    *            if the operation failed.
    */
   public Wsdl newWsdl(String url) throws GovernanceException {
      String wsdlId = UUID.randomUUID().toString();
      Wsdl wsdl = new Wsdl(wsdlId, url);
      wsdl.associateRegistry(registry);
      return wsdl;
   }

   /**
    * Create a new WSDL based on content either embedded or passed to a service.
    * 
    * @param registry
    *           the associated registry
    * @param content
    *           the wsdl as bytes
    * @return a new Wsdl object
    * @throws GovernanceException
    *            a GovernanceException is thrown in case the wsdl can't be created.
    */
   public Wsdl newWsdl(Registry registry, byte[] content) throws GovernanceException {
      String wsdlId = UUID.randomUUID().toString();
      Wsdl wsdl = new Wsdl(wsdlId, (String) null);
      wsdl.associateRegistry(registry);
      wsdl.setWsdlElement(GovernanceUtils.buildOMElement(content));
      return wsdl;
   }

   /**
    * Adds the given WSDL artifact to the registry.
    * 
    * @param wsdl
    *           the WSDL artifact.
    * 
    * @throws GovernanceException
    *            if the operation failed.
    */
   public void addWsdl(Wsdl wsdl) throws GovernanceException {
      boolean succeeded = false;
      try {
         registry.beginTransaction();
         Resource wsdlResource = registry.newResource();
         wsdlResource.setMediaType(GovernanceConstants.WSDL_MEDIA_TYPE);

         // setting the wsdl content
         setContent(wsdl, wsdlResource);

         String name = wsdl.getAttribute(AssetConstants.TURMERIC_NAME);

         if (!name.endsWith(".wsdl")) {
            name = name + ".wsdl";
         }

         registry.put("/" + name, wsdlResource);

         wsdl.updatePath();
         wsdl.loadWsdlDetails();
         succeeded = true;
      } catch (RegistryException e) {
         String msg = "Error in adding the wsdl. wsdl id: " + wsdl.getId() + ".";
         log.error(msg, e);
         throw new GovernanceException(msg, e);
      } finally {
         if (succeeded) {
            try {
               registry.commitTransaction();
            } catch (RegistryException e) {
               String msg = "Error in committing transactions. Add wsdl failed: wsdl id: " + wsdl.getId() + ", path: "
                        + wsdl.getPath() + ".";
               log.error(msg, e);
            }
         } else {
            try {
               registry.rollbackTransaction();
            } catch (RegistryException e) {
               String msg = "Error in rolling back transactions. Add wsdl failed: wsdl id: " + wsdl.getId()
                        + ", path: " + wsdl.getPath() + ".";
               log.error(msg, e);
            }
         }
      }
   }

   /**
    * Updates the given WSDL artifact on the registry.
    * 
    * @param wsdl
    *           the WSDL artifact.
    * 
    * @throws GovernanceException
    *            if the operation failed.
    */
   public void updateWsdl(Wsdl wsdl) throws GovernanceException {
      if (wsdl.getWsdlElement() == null) {
         // there won't be any updates
         String msg = "Updates are only accepted if the wsdlElement available. " + "So no updates will be done. "
                  + "wsdl id: " + wsdl.getId() + ", wsdl path: " + wsdl.getPath() + ".";
         log.error(msg);
         throw new GovernanceException(msg);
      }
      boolean succeeded = false;
      try {
         registry.beginTransaction();

         // getting the old wsdl.
         Wsdl oldWsdl = getWsdl(wsdl.getId());
         if (oldWsdl == null) {
            addWsdl(wsdl);
            return;
         }
         // we are expecting only the OMElement to be different.
         Resource wsdlResource = registry.newResource();
         wsdlResource.setMediaType(GovernanceConstants.WSDL_MEDIA_TYPE);

         // setting the wsdl content
         setContent(wsdl, wsdlResource);

         // remove the old wsdl resource.
         String tmpPath = oldWsdl.getPath();

         registry.put(tmpPath, wsdlResource);
         wsdl.updatePath();
         wsdl.loadWsdlDetails();

         succeeded = true;
      } catch (RegistryException e) {
         String msg = "Error in updating the wsdl, wsdl id: " + wsdl.getId() + ", wsdl path: " + wsdl.getPath() + ".";
         log.error(msg, e);
         throw new GovernanceException(msg, e);
      } finally {
         if (succeeded) {
            try {
               registry.commitTransaction();
            } catch (RegistryException e) {
               String msg = "Error in committing transactions. Update wsdl failed: wsdl id: " + wsdl.getId()
                        + ", path: " + wsdl.getPath() + ".";
               log.error(msg, e);
            }
         } else {
            try {
               registry.rollbackTransaction();
            } catch (RegistryException e) {
               String msg = "Error in rolling back transactions. Update wsdl failed: wsdl id: " + wsdl.getId()
                        + ", path: " + wsdl.getPath() + ".";
               log.error(msg, e);
            }
         }
      }
   }

   /**
    * Fetches the given WSDL artifact on the registry.
    * 
    * @param wsdlId
    *           the identifier of the WSDL artifact.
    * 
    * @return the WSDL artifact.
    * @throws GovernanceException
    *            if the operation failed.
    */
   public Wsdl getWsdl(String wsdlId) throws GovernanceException {
      GovernanceArtifact artifact = GovernanceUtils.retrieveGovernanceArtifactById(registry, wsdlId);
      if (artifact != null && !(artifact instanceof Wsdl)) {
         String msg = "The artifact request is not a Wsdl. id: " + wsdlId + ".";
         log.error(msg);
         throw new GovernanceException(msg);
      }
      return (Wsdl) artifact;
   }

   /**
    * Removes the given WSDL artifact from the registry.
    * 
    * @param wsdlId
    *           the identifier of the WSDL artifact.
    * 
    * @throws GovernanceException
    *            if the operation failed.
    */
   public void removeWsdl(String wsdlId) throws GovernanceException {
      GovernanceUtils.removeArtifact(registry, wsdlId);
   }

   /**
    * Sets content of the given WSDL artifact to the given resource on the registry.
    * 
    * @param wsdl
    *           the WSDL artifact.
    * @param wsdlResource
    *           the content resource.
    * 
    * @throws GovernanceException
    *            if the operation failed.
    */
   protected void setContent(Wsdl wsdl, Resource wsdlResource) throws GovernanceException {
      if (wsdl.getWsdlElement() != null) {
         OMElement contentElement = wsdl.getWsdlElement().cloneOMElement();
         try {
            for (String importType : new String[] { "import", "include" }) {
               List<OMElement> wsdlImports = GovernanceUtils.evaluateXPathToElements("//wsdl:" + importType,
                        contentElement);
               for (OMElement wsdlImport : wsdlImports) {
                  OMAttribute location = wsdlImport.getAttribute(new QName("location"));
                  if (location != null) {
                     String path = location.getAttributeValue();
                     if (path.indexOf(";version:") > 0) {
                        location.setAttributeValue(path.substring(0, path.lastIndexOf(";version:")));
                     }
                  }
               }
            }
            for (String importType : new String[] { "import", "include", "redefine" }) {
               List<OMElement> schemaImports = GovernanceUtils.evaluateXPathToElements("//xsd:" + importType,
                        contentElement);
               for (OMElement schemaImport : schemaImports) {
                  OMAttribute location = schemaImport.getAttribute(new QName("schemaLocation"));
                  if (location != null) {
                     String path = location.getAttributeValue();
                     if (path.indexOf(";version:") > 0) {
                        location.setAttributeValue(path.substring(0, path.lastIndexOf(";version:")));
                     }
                  }
               }
            }
         } catch (JaxenException ignore) {
         }
         String wsdlContent = contentElement.toString();
         try {
            wsdlResource.setContent(wsdlContent);
         } catch (RegistryException e) {
            String msg = "Error in setting the content from wsdl, wsdl id: " + wsdl.getId() + ", wsdl path: "
                     + wsdl.getPath() + ".";
            log.error(msg, e);
            throw new GovernanceException(msg, e);
         }
      }
      // and set all the attributes as properties.
      String[] attributeKeys = wsdl.getAttributeKeys();
      if (attributeKeys != null) {
         Properties properties = new Properties();
         for (String attributeKey : attributeKeys) {
            String[] attributeValues = wsdl.getAttributes(attributeKey);
            if (attributeValues != null) {
               // The list obtained from the Arrays#asList method is
               // immutable. Therefore,
               // we create a mutable object out of it before adding it as
               // a property.
               properties.put(attributeKey, new ArrayList<String>(Arrays.asList(attributeValues)));
            }
         }
         wsdlResource.setProperties(properties);
      }
      wsdlResource.addProperty(GovernanceConstants.ARTIFACT_ID_PROP_KEY, wsdl.getId());
   }

   /**
    * Finds all WSDL artifacts matching the given filter criteria.
    * 
    * @param criteria
    *           the filter criteria to be matched.
    * 
    * @return the WSDL artifacts that match.
    * @throws GovernanceException
    *            if the operation failed.
    */
   public Wsdl[] findWsdls(WsdlFilter criteria) throws GovernanceException {
      List<Wsdl> wsdls = new ArrayList<Wsdl>();
      for (Wsdl wsdl : getAllWsdls()) {
         if (wsdl != null) {
            if (criteria.matches(wsdl)) {
               wsdls.add(wsdl);
            }
         }
      }
      return wsdls.toArray(new Wsdl[wsdls.size()]);
   }

   /**
    * Finds all WSDL artifacts on the registry.
    * 
    * @return all WSDL artifacts on the registry.
    * @throws GovernanceException
    *            if the operation failed.
    */
   public Wsdl[] getAllWsdls() throws GovernanceException {
      List<String> wsdlPaths = Arrays.asList(GovernanceUtils.getResultPaths(registry,
               GovernanceConstants.WSDL_MEDIA_TYPE));
      Collections.sort(wsdlPaths, new Comparator<String>() {
         @Override
         public int compare(String o1, String o2) {
            // First order by name
            int result = RegistryUtils.getResourceName(o1).compareToIgnoreCase(RegistryUtils.getResourceName(o2));
            if (result != 0) {
               return result;
            }
            // Then order by namespace
            return o1.compareToIgnoreCase(o2);
         }
      });
      List<Wsdl> wsdls = new ArrayList<Wsdl>();
      for (String wsdlPath : wsdlPaths) {
         GovernanceArtifact artifact = GovernanceUtils.retrieveGovernanceArtifactByPath(registry, wsdlPath);
         wsdls.add((Wsdl) artifact);
      }
      return wsdls.toArray(new Wsdl[wsdls.size()]);
   }
}