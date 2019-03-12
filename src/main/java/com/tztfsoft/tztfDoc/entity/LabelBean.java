package com.tztfsoft.tztfDoc.entity;

import java.io.Serializable;

/**
 * pdf文件标签页实体类
 * @author kuaiDSH
 *
 */
public class LabelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4659550697925899577L;
	//日期
	private String date;
	//审批
	private String audit;
	//审核人 图片地址
	private String auditor;
	//意见
	private String content;
	
	public LabelBean() {
		super();
	}

	public LabelBean(String date, String audit, String auditor, String content) {
		super();
		this.date = date;
		this.audit = audit;
		this.auditor = auditor;
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAudit() {
		return audit;
	}

	public void setAudit(String audit) {
		this.audit = audit;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "LabelBean [date=" + date + ", audit=" + audit + ", auditor="
				+ auditor + ", content=" + content + "]";
	}
	
	
}






