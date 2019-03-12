package com.tztfsoft.tztfDoc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tztfsoft.tztfDoc.entity.FileBean;

/**
 * 文件相关数据库操作
 * @author kuaiDSH
 *
 */
public interface FileDao {
	/**
	 * 文件上传、新增记录表数据
	 * @param bean
	 * @return
	 */
	int insertFileRecord(FileBean bean);
	/**
	 * <pre>
	 * 文件上传记录表 修改
	 * 	一行数据基本都要修改的情况下使用
	 * </pre>
	 * @param bean
	 * @return
	 */
	int updateFileRecord(FileBean bean);
	/**
	 * 根据id 删除记录表数据
	 * @param id
	 * @return
	 */
	int deleteFileRecord(Integer id);
	/**
	 * 根据菜单id  用户id  获取文件记录
	 * @param menuid
	 * @param userid
	 * @return
	 */
	List<Map<String,Object>> getFileRecord(@Param("menuid")Integer menuid,@Param("userid")Integer userid);
	/**
	 * 根据菜单id 获取文件记录
	 * @param menuid
	 * @return
	 */
	List<Map<String,Object>> getFileRecordByMenu(Integer menuid);
	/**
	 * 根据菜单id 文件状态获取相关数据
	 * @param menuid
	 * @return
	 */
	List<Map<String,Object>> getFileRecordByMenuAndState(@Param("menuid")Integer menuid,@Param("status")Integer status);
	/**
	 * 根据id  获取文件记录数据
	 * @return
	 */
	List<Map<String,Object>> getFileRecordByID(Integer id);
	/**
	 * 根据文件id 修改其状态
	 * @param id
	 * @param status
	 * @return
	 */
	int updateFileStatus(@Param("id")Integer id,@Param("status")Integer status);
	/**
	 * 【分析】 传入菜单id 得到最新的一个
	 * @param menuid
	 * @return
	 */
	FileBean fenxi(Integer menuid);
}







