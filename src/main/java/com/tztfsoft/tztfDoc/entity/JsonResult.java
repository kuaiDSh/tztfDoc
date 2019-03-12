package com.tztfsoft.tztfDoc.entity;

import java.io.Serializable;

public class JsonResult implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final int ERROR = 0;
	//消息类型
	private Object resultTypeID;
	//消息
	private Object head;
	//行数
	private Integer rowcount;
	//数据
	private Object data;
	
	public JsonResult(Object resultTypeID, Object head, Integer rowcount, Object data) {
		super();
		this.resultTypeID = resultTypeID;
		this.head = head;
		this.rowcount = rowcount;
		this.data = data;
	}

	public JsonResult() {
		super();
	}

	public Object getResultTypeID() {
		return resultTypeID;
	}

	public void setResultTypeID(Object resultTypeID) {
		this.resultTypeID = resultTypeID;
	}

	public Object getHead() {
		return head;
	}

	public void setHead(Object head) {
		this.head = head;
	}

	public Integer getRowcount() {
		return rowcount;
	}

	public void setRowcount(Integer rowcount) {
		this.rowcount = rowcount;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "{resultTypeID=" + resultTypeID + ", head=" + head + ", rowcount=" + rowcount + ", data="
				+ data + "}";
	}

	public JsonResult(Object data) {
		super();
		this.data = data;
	}

}
