package com.zjuh.ally.downloads;

import java.util.HashSet;
import java.util.Set;

public class PageInfo {
	
	private Set<String> imgUrls = new HashSet<String>();
	
	private Set<String> visitedLinkUrls = new HashSet<String>();

	public Set<String> getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(Set<String> imgUrls) {
		this.imgUrls = imgUrls;
	}

	public Set<String> getLinkUrls() {
		return visitedLinkUrls;
	}

	public void setLinkUrls(Set<String> linkUrls) {
		this.visitedLinkUrls = linkUrls;
	}

}
