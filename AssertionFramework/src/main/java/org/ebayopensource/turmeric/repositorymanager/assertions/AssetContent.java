/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.repositorymanager.assertions;

/**
 * AssetContent is an AssertionContent with an underlying Asset artifact.
 * 
 * @author pcopeland
 */
public class AssetContent
    implements AssertionContent
{
    private String contentName;
    private String sourceName;
    private AssetReference assetRef;
    private String artifactName;
    private String artifactDisplayName;

    /**
     * Constructs an AssetContent.  The contentName is an artifact name.
     * 
     * @param contentName the artifact name.
     * @param assetRef the AssetReference for this AssetContent.
     */
    public AssetContent(String contentName, AssetReference assetRef)
    {
        this.contentName = contentName.replace(' ','-');
        this.artifactName = contentName;
        this.assetRef = assetRef;
        sourceName = assetRef.getAssetReference()+":"+contentName;
    }
    
    public AssetContent(String contentName, AssetReference assetRef,String artifactName)
    {
        this.contentName = contentName.replace(' ','-');
        this.artifactName = artifactName;
        this.assetRef = assetRef;
        sourceName = assetRef.getAssetReference()+":"+contentName;
    }

    /**
     * Returns the artifact name as a URI.
     * 
     * @return the artifact name as a URI.
     */
    @Override
	public String getName() { return contentName; }

    /**
     * Sets the name of this AssetContent.
     * 
     * @param name the new name of this AssetContent.
     */
    @Override
	public void setName(String contentName) { this.contentName = contentName; }

    /**
     * Returns the source name for this AssetContent. The form of 
     * the source name is Library:AssetName:Version:ArtifactName.
     * 
     * @return the source name for this AssetContent.
     */
    @Override
	public String getSourceName() { return sourceName; }

    /**
     * Returns the Artifact name for this AssetContent.
     * 
     * @return the Artifact name for this AssetContent.
     */

    public String getArtifactName() { return artifactName; }

    /**
     * Returns the AssetReference for this AssetContent.
     * 
     * @return the AssetReference for this AssetContent.
     */
    public AssetReference getAssetReference() { return assetRef; }

    @Override
	public String toString()
    {
        return "AssetContent["+sourceName+"]";
    }

	public String getArtifactDisplayName() {
		return artifactDisplayName;
	}

	public void setArtifactDisplayName(String artifactDisplayName) {
		this.artifactDisplayName = artifactDisplayName;
	}
    
}
