package com.tztfsoft.tztfDoc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tztfsoft.tztfDoc.entity.MenuBean;
import com.tztfsoft.tztfDoc.entity.NoticeBean;


/**
 * 通知相关数据库操作
 * @author kuaiDSH
 *
 */
public interface NoticeDao {
	/**
	 * 根据用户id 获取相关通知信息
	 * @param uerid
	 * @return
	 */
	List<NoticeBean> getNotice(@Param("userid")Integer userid,@Param("state")Integer state);
	/**
	 * 新增通知信息
	 * @param bean
	 * @return
	 */
	int insertNotices(NoticeBean bean);
	/**
	 * 根据id 删除通知
	 * @param id
	 */
	void deleteNotice(Integer id);
	/**
	 * 根据id，修改其状态
	 * @param id
	 * @param state
	 */
	void updateNotice(@Param("id")Integer id,@Param("state")Integer state);
	/**
	 * <pre>
	 * 获取有完成时间的文档记录
	 * where：
	 *  1.必须有完成时间以及文档负责人
	 *  2.文件上传时间年份等于系统时间年份  
	 * </pre>
	 * @return  返回的数据中 文件上传时间包含null
	 */
	List<Map<String,Object>> getMenuDone();
}













