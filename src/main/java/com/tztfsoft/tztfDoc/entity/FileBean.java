package com.tztfsoft.tztfDoc.entity;

import java.io.Serializable;

/**
 * 文件实体类
 * @author kuaiDSH
 *
 */
public class FileBean implements Serializable {
	
	private static final long serialVersionUID = 7575186364374741595L;
	//内码
	private Integer id;
	//文件新名称
	private String newName;
	//文件原始名称
	private String oldName;
	//文件被下载次数
	private Integer downNum;
	//文件地址
	private String path;
	//文件状态  0 已上传  1审核中 2已审核 3被驳回  4审核未通过  5 已删除
	private Integer status;
	//文件删除用户
	private Integer userid;
	//文件对应目录菜单id
	private Integer menuid;
	//文件版本号
	private String version;
	//上传时间
	private String fdate;
	public FileBean() {
		super();
	}
	
	
	public FileBean(Integer id, String newName, String oldName,
			Integer downNum, String path, Integer status, Integer userid,
			Integer menuid, String version, String fdate) {
		super();
		this.id = id;
		this.newName = newName;
		this.oldName = oldName;
		this.downNum = downNum;
		this.path = path;
		this.status = status;
		this.userid = userid;
		this.menuid = menuid;
		this.version = version;
		this.fdate = fdate;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	public Integer getDownNum() {
		return downNum;
	}
	public void setDownNum(Integer downNum) {
		this.downNum = downNum;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Integer getMenuid() {
		return menuid;
	}
	public void setMenuid(Integer menuid) {
		this.menuid = menuid;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFdate() {
		return fdate;
	}
	public void setFdate(String fdate) {
		this.fdate = fdate;
	}


	@Override
	public String toString() {
		return "FileBean [id=" + id + ", newName=" + newName + ", oldName="
				+ oldName + ", downNum=" + downNum + ", path=" + path
				+ ", status=" + status + ", userid=" + userid + ", menuid="
				+ menuid + ", version=" + version + ", fdate=" + fdate + "]";
	}
	
	
}






