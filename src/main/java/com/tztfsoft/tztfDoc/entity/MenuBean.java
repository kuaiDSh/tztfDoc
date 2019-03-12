package com.tztfsoft.tztfDoc.entity;

import java.io.Serializable;

/**
 * 菜单实体类
 * @author kuaiDSH
 *
 */
public class MenuBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2538280531358895919L;
	//菜单id
	private Integer id;
	//菜单名称
	private String name;
	//菜单父节点id
	private Integer parent;
	//菜单备注
	private String content;
	//完成时间
	private String doneTime;
	//文档负责人
	private Integer fuser;
	public MenuBean() {
		super();
	}
	public MenuBean(Integer id, String name, Integer parent, String content,
			String doneTime, Integer fuser) {
		super();
		this.id = id;
		this.name = name;
		this.parent = parent;
		this.content = content;
		this.doneTime = doneTime;
		this.fuser = fuser;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParent() {
		return parent;
	}
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDoneTime() {
		return doneTime;
	}
	public void setDoneTime(String doneTime) {
		this.doneTime = doneTime;
	}
	public Integer getFuser() {
		return fuser;
	}
	public void setFuser(Integer fuser) {
		this.fuser = fuser;
	}
	@Override
	public String toString() {
		return "MenuBean [id=" + id + ", name=" + name + ", parent=" + parent
				+ ", content=" + content + ", doneTime=" + doneTime
				+ ", fuser=" + fuser + "]";
	}
	
	
}






