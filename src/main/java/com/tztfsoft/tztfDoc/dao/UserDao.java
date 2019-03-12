package com.tztfsoft.tztfDoc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.tztfsoft.tztfDoc.entity.UserBean;


/**
 * 用户相关数据库操作
 * @author kuaiDSH
 *
 */
public interface UserDao {
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @return id name
	 */
	List<Map<String,Object>> login(@Param("username")String username,@Param("password")String password);
	/**
	 * 获取所有的用id，户名，姓名
	 * @return
	 */
	List<Map<String,Object>> getUser(); 
	/**
	 * 新增用户
	 * @param bean
	 * @return
	 */
	int insertUser(UserBean bean);
	/**
	 * 获取所有用户信息
	 * @return
	 */
	List<UserBean> getAllUser();
	/**
	 * 根据用户名获取相关数据 判断传入的用户名是否存在
	 * @param username
	 * @return
	 */
	List<Map<String,Object>> getIDByUsername(String username);
	/**
	 * 修改用户数据
	 * @param bean
	 */
	void updateUser(UserBean bean);
	/**
	 * 根据用户id删除用户
	 * @param id
	 */
	void deleteUser(Integer id);
	/**
	 * 根据用户id 获取用户名、姓名
	 * @param id
	 * @return
	 */
	Map<String,Object> getuserByID(Integer id);
	/**
	 * 根据id 密码 获取数据
	 * @param id
	 * @param password
	 * @return
	 */
	Map<String,Object> getByPWAndID(@Param("id") Integer id,@Param("password") String password);
	/**
	 * 根据id 密码 修改密码
	 * @param id
	 * @param password
	 */
	void updatepassword(@Param("id") Integer id,@Param("password") String password);
}















