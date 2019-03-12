package com.tztfsoft.tztfDoc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tztfsoft.tztfDoc.entity.MenuBean;

/**
 * 数据相关工具类
 * @author Administrator
 *
 */
public class DataUtil {
	/**
	 * 获取菜单目录最终数据【递归】
	 * @param listmap 返回数据
	 * @param list  菜单数据
	 * @return  listmap
	 */
	public static List<Map<String, Object>> getMenuList(
			List<Map<String, Object>> listmap,List<MenuBean> list) {
		System.out.println(1);
		//
		if(listmap == null || list == null ) {
			listmap = new ArrayList<Map<String,Object>>();
			for(MenuBean k: list) {
				//父节点等于0 
				if(k.getParent()== 0 ) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", k.getId());
					map.put("text", k.getName());
					listmap.add(map);
				}
			}
			getMenuList(listmap,list);
		}else if(listmap.size()>0 && list.size()>0) {
			for(Map<String,Object> map:listmap) {
				//子节点
				List<Map<String, Object>> childlist = new ArrayList<Map<String,Object>>();
				for(MenuBean bean1 : list) {
					Integer id =(Integer) map.get("id");
					Integer parent = bean1.getParent();
					if(id == parent) {
						Map<String, Object> m = new HashMap<String, Object>();
						List<String> tags = new ArrayList<String>();
						tags.add(bean1.getContent());
						m.put("id", bean1.getId());
						m.put("text", bean1.getName());
						m.put("tags", tags);
						childlist.add(m);
					}
					if(childlist.size()>0) {
						map.put("nodes", childlist);
						getMenuList(childlist,list);
					}
				}
			}
		}
		return listmap;	
	}
	
	public static List<Map<String, Object>> getMenuList1(List<MenuBean> list) {
		System.out.println(list);
		List<Map<String, Object>> listmap = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		//二级菜单
		List<Map<String, Object>> nodes = new ArrayList<Map<String,Object>>();
		for(MenuBean bean : list) {
			if(bean.getParent() == 0) {
				map.put("id", bean.getId());
				map.put("text", bean.getName());
			}else if(bean.getParent() == 1) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("id", bean.getId());
				m.put("text", bean.getName());
				List<String> tags = new ArrayList<String>();
				tags.add(bean.getContent());
				tags.add(bean.getDoneTime());
				tags.add(String.valueOf(bean.getFuser()));
				m.put("tags", tags);
				nodes.add(m);
			}
		}
		
		for(Map<String,Object> m : nodes) {
			//二级目录id
			Integer mid = (Integer) m.get("id");
			//二级子菜单3级目录
			List<Map<String, Object>> nodes3 = new ArrayList<Map<String,Object>>();
			for(MenuBean bean : list) {
				if(bean.getParent() == mid) {
					//创建三级目录
					Map<String, Object> m3 = new HashMap<String, Object>();
					m3.put("id", bean.getId());
					m3.put("text", bean.getName());
					List<String> tags3 = new ArrayList<String>();
					tags3.add(bean.getContent());
					tags3.add(bean.getDoneTime());
					tags3.add(String.valueOf(bean.getFuser()));
					m3.put("tags", tags3);
					//4级菜单
					List<Map<String, Object>> nodes4 = new ArrayList<Map<String,Object>>();
					for(MenuBean bean1 : list) {
						if(bean.getId() == bean1.getParent()) {
							Map<String, Object> m4 = new HashMap<String, Object>();
							m4.put("id", bean1.getId());
							m4.put("text", bean1.getName());
							List<String> tags4 = new ArrayList<String>();
							tags4.add(bean1.getContent());
							tags4.add(bean1.getDoneTime());
							tags4.add(String.valueOf(bean1.getFuser()));
							m4.put("tags", tags4);
							nodes4.add(m4);
						}
					}
					m3.put("nodes", nodes4);
					nodes3.add(m3);
				}
			}
			m.put("nodes", nodes3);
		}
		map.put("nodes", nodes);
		listmap.add(map);
		return listmap;
	}
}




