package com.tztfsoft.tztfDoc.dao;

import java.util.List;
import java.util.Map;

import com.tztfsoft.tztfDoc.entity.MenuBean;


/**
 * 菜单相关数据库操作
 * @author kuaiDSH
 *
 */
public interface MenuDao {
	/**
	 * 新增菜单dao
	 * @param bean
	 * @return
	 */
	int insertMenu(MenuBean bean);
	/**
	 * 获取菜单
	 * @return
	 */
	List<MenuBean> getMenu();
	/**
	 * 修改菜单
	 * @param bean
	 * @return
	 */
	int updateMenu(MenuBean bean);
	/**
	 * 根据id 删除菜单
	 * @param id
	 * @return
	 */
	int deleteMenu(Integer id);
	/**
	 * 根据id 获取菜单名称
	 * @param id
	 * @return
	 */
	Map<String,String> getMenuNameByID(Integer id);
	/**
	 * 分析文档完成状态（获取的是菜单中完成时间的值存在）
	 * @return
	 */
	List<MenuBean> fenxi();
	/**
	 * 获取菜单中有完成时间，以及有负责人的菜单数据
	 * @return
	 */
	List<MenuBean> getMenuDoneUser();
	
}







