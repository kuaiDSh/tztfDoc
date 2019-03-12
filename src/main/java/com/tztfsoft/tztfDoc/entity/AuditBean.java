package com.tztfsoft.tztfDoc.entity;

import java.io.Serializable;

/**
 * 审核实体类
 * @author kuaiDSH
 *
 */
public class AuditBean implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 56285564421391515L;
	//审核内码
	private Integer id;
	//关联菜单id
	private Integer menuID;
	//审核名称
	private String name;
	public AuditBean() {
		super();
	}
	
	
	public AuditBean(Integer id, Integer menuID, String name) {
		super();
		this.id = id;
		this.menuID = menuID;
		this.name = name;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMenuID() {
		return menuID;
	}
	public void setMenuID(Integer menuID) {
		this.menuID = menuID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "AuditBean [id=" + id + ", menuID=" + menuID + ", name=" + name
				+ "]";
	}

	

	
	
}






