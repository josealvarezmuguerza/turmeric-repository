/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.12.17 at 10:07:03 AM PST 
//


package org.ebayopensource.turmeric.assetdata.artifacts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.ebay.assetdata package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AssetData_QNAME = new QName("http://www.ebay.com/AssetData", "assetData");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.ebay.assetdata
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AssetData }
     * 
     */
    public AssetData createAssetData() {
        return new AssetData();
    }

    /**
     * Create an instance of {@link Assets }
     * 
     */
    public Assets createAssets() {
        return new Assets();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssetData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ebay.com/AssetData", name = "assetData")
    public JAXBElement<AssetData> createAssetData(AssetData value) {
        return new JAXBElement<AssetData>(_AssetData_QNAME, AssetData.class, null, value);
    }

}