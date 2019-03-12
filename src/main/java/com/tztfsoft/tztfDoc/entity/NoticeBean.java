package com.tztfsoft.tztfDoc.entity;

import java.io.Serializable;

/**
 * 通知实体类
 * @author Administrator
 *
 */
public class NoticeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6374219848363299313L;
	//id
	private Integer id;
	//接收用户
	private Integer fuser;
	//内容
	private String fcount;
	//地址
	private String url;
	//状态  0已完成  1未完成
	private Integer state;
	
	public NoticeBean() {
		super();
	}
	
	public NoticeBean(Integer id, Integer fuser, String fcount, String url,
			Integer state) {
		super();
		this.id = id;
		this.fuser = fuser;
		this.fcount = fcount;
		this.url = url;
		this.state = state;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUser() {
		return fuser;
	}

	public void setUser(Integer fuser) {
		this.fuser = fuser;
	}

	public String getCount() {
		return fcount;
	}

	public void setCount(String fcount) {
		this.fcount = fcount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "NoticeBean [id=" + id + ", fuser=" + fuser + ", fcount=" + fcount
				+ ", url=" + url + ", state=" + state + "]";
	}
	
	
}




