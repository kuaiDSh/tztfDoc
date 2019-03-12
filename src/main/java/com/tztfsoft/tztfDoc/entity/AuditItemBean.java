package com.tztfsoft.tztfDoc.entity;

import java.io.Serializable;

/**
 * 审核节点实体类
 * @author kuaiDSH
 *
 */
public class AuditItemBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6599726729277236459L;

	//关联审核id 
	private Integer id;
	//审核节点名称
	private String name;
	//审核顺序
	private Integer findex;
	//审核人数组
	private String auditor;
	//图形信息
	private String chart;
	public AuditItemBean() {
		super();
	}
	public AuditItemBean(Integer id, String name, Integer index,
			String auditor, String chart) {
		super();
		this.id = id;
		this.name = name;
		this.findex = index;
		this.auditor = auditor;
		this.chart = chart;
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
	public Integer getIndex() {
		return findex;
	}
	public void setIndex(Integer index) {
		this.findex = index;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getChart() {
		return chart;
	}
	public void setChart(String chart) {
		this.chart = chart;
	}
	@Override
	public String toString() {
		return "AuditItemBean [id=" + id + ", name=" + name + ", index="
				+ findex + ", auditor=" + auditor + ", chart=" + chart + "]";
	}
	
	
}






