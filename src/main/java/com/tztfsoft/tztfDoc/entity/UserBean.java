package com.tztfsoft.tztfDoc.entity;

import java.io.Serializable;

/**
 * 用户实体类
 * @author kuaiDSH
 *
 */
public class UserBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6025156126983382305L;
	//用户id
	private Integer id;
	//用户名
	private String username;
	//密码
	private String password;
	//用户部门
	private String dept;
	//用户联系方式
	private String phone;
	//用户姓名
	private String name;
	//用户图片地址
	private String path;
	//反审核权限
	private Integer noaudit;
	public UserBean() {
		super();
	}
	public UserBean(Integer id, String username, String password, String dept,
			String phone, String name, String path, Integer noaudit) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.dept = dept;
		this.phone = phone;
		this.name = name;
		this.path = path;
		this.noaudit = noaudit;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public Integer getNoaudit() {
		return noaudit;
	}
	public void setNoaudit(Integer noaudit) {
		this.noaudit = noaudit;
	}
	@Override
	public String toString() {
		return "UserBean [id=" + id + ", username=" + username + ", password="
				+ password + ", dept=" + dept + ", phone=" + phone + ", name="
				+ name + ", path=" + path + ", noaudit=" + noaudit + "]";
	}
	
	
	
	
}






