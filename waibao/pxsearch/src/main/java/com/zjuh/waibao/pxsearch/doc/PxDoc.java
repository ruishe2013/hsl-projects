package com.zjuh.waibao.pxsearch.doc;

import java.io.Serializable;

public class PxDoc implements Serializable {

	private static final long serialVersionUID = 1189297701985520107L;
	
	private String id;
	
	private int docId;
	
	private int type;
	
	private String title;
	
	private String tags;
	
	private String content;

	public String getId() {
		return type + "_" + id;
	}

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
