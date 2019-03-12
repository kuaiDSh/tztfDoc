package com.tztfsoft.tztfDoc.entity;

import java.io.Serializable;

/**
 * 审核记录实体类
 * @author kuaiDSH
 *
 */
public class AuditRecordBean implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -714705270642855276L;
	//审核id
	private Integer auditID;
	//关联菜单id
	private Integer menuID;
	//文件id
	private Integer fileID;
	//审核人id
	private Integer auditor;
	//审核顺序
	private Integer fIndex;
	//审核日期
	private String auditDate;
	//审核意见
	private String aditContent;
	//审核逻辑  0 同意 1不同意 2驳回
	private Integer auditState;
	public AuditRecordBean() {
		super();
	}
	public AuditRecordBean(Integer auditID, Integer menuID, Integer fileID,
			Integer auditor, Integer fIndex, String auditDate,
			String aditContent, Integer auditState) {
		super();
		this.auditID = auditID;
		this.menuID = menuID;
		this.fileID = fileID;
		this.auditor = auditor;
		this.fIndex = fIndex;
		this.auditDate = auditDate;
		this.aditContent = aditContent;
		this.auditState = auditState;
	}
	public Integer getAuditID() {
		return auditID;
	}
	public void setAuditID(Integer auditID) {
		this.auditID = auditID;
	}
	public Integer getMenuID() {
		return menuID;
	}
	public void setMenuID(Integer menuID) {
		this.menuID = menuID;
	}
	public Integer getFileID() {
		return fileID;
	}
	public void setFileID(Integer fileID) {
		this.fileID = fileID;
	}
	public Integer getAuditor() {
		return auditor;
	}
	public void setAuditor(Integer auditor) {
		this.auditor = auditor;
	}
	public Integer getfIndex() {
		return fIndex;
	}
	public void setfIndex(Integer fIndex) {
		this.fIndex = fIndex;
	}
	public String getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
	public String getAditContent() {
		return aditContent;
	}
	public void setAditContent(String aditContent) {
		this.aditContent = aditContent;
	}
	public Integer getAuditState() {
		return auditState;
	}
	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}
	@Override
	public String toString() {
		return "AuditRecordBean [auditID=" + auditID + ", menuID=" + menuID
				+ ", fileID=" + fileID + ", auditor=" + auditor + ", fIndex="
				+ fIndex + ", auditDate=" + auditDate + ", aditContent="
				+ aditContent + ", auditState=" + auditState + "]";
	}
	
	
	

	
	
}






