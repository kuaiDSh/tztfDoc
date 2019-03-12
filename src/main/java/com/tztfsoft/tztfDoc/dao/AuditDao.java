package com.tztfsoft.tztfDoc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tztfsoft.tztfDoc.entity.AuditBean;
import com.tztfsoft.tztfDoc.entity.AuditItemBean;
import com.tztfsoft.tztfDoc.entity.AuditRecordBean;

/**
 * 审核相关dao数据库操作
 * @author kuaiDSH
 *
 */
public interface AuditDao {
	/**
	 * 审核新增
	 * @param bean
	 * @return
	 */
	int insertAudit(AuditBean bean);
	/**
	 * 审核节点新增
	 * @param list
	 * @return
	 */
	int insertAuditItem(List<AuditItemBean> list);
	/**
	 * 根据菜单id 获取审核数据
	 * @param menuid
	 * @return
	 */
	AuditBean getAudit(Integer menuid);
	/**
	 * 根据审核id 获取审核节点数据
	 * @param id
	 * @return
	 */
	List<AuditItemBean> getAuditItem(Integer id);
	/**
	 * 新增审核记录数据
	 * @param bean
	 * @return
	 */
	int insertAuditRecord(AuditRecordBean bean);
	/**
	 * 根据菜单id  文件id  获取审核记录
	 * @param menuid
	 * @param fileid
	 * @return
	 */
	List<Map<String,Object>> getRecordBymf(@Param("menuid")Integer menuid,@Param("fileid")Integer fileid);
	/**
	 * 根据菜单id  文件id 获取审核记录【签核页专用】
	 * @param menuid
	 * @param fileid
	 * @return
	 */
	List<Map<String,Object>> getRecordBymff(@Param("menuid")Integer menuid,@Param("fileid")Integer fileid);
	/**
	 * 根据审核id 删除审核节点数据
	 * @param id
	 */
	void deleteAuditItem(Integer id);
	/**
	 * 修改审核表内容
	 * @param bean
	 */
	void updateAudit(AuditBean bean);
}







