
package com.tztfsoft.tztfDoc.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;






import com.tztfsoft.tztfDoc.dao.MenuDao;
import com.tztfsoft.tztfDoc.dao.NoticeDao;
import com.tztfsoft.tztfDoc.entity.MenuBean;
import com.tztfsoft.tztfDoc.entity.NoticeBean;
/**
 * 定时任务类
 * @author kuaiD
 *
 */
@Component
public class MyQuartz {
	@Resource
	NoticeDao noticeDao;
	@Resource
	MenuDao menuDao;
	/**
	 * 文档每月1号分析文档是否按时完成、发送通知
	 */
	@Scheduled(cron="0 0 1 1 * ? ")
	public void getNotice() {
		
		List<MenuBean> listBean = menuDao.getMenuDoneUser();
		List<Map<String,Object>> list = noticeDao.getMenuDone();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		//当前系统时间年份
		String year = sdf.format(date).substring(0, 4);
		//当前系统时间月份
		String month = sdf.format(date).substring(5,7);
//		System.out.println("当前系统时间年份："+year);
//		System.out.println("当前系统时间月份："+month)
		//遍历菜单
		for(MenuBean bean : listBean) {
			//菜单ID 
			Integer menuID = bean.getId();
			//判断该文档是否需要发送通知 false 发送通知
			boolean flag = false;
			for(Map<String,Object> map : list) {
				//文件上传记录中的菜单ID
				Integer map_menuID = (Integer) map.get("menuid");
				if(menuID == map_menuID) {
					//完成时间 两种写法例   year,1   moth,1
					String doneTime = bean.getDoneTime();
					//文件上传时间
					Date fileDate = (java.sql.Date) map.get("fdate");
					//每年完成
					if("year".equals(doneTime.split(",")[0])) {
						if(fileDate == null) {
							//发送通知
							flag = false;
						}else if(fileDate.toString().substring(0,4).equals(year)) {
							/*
							 * 如果文件上传时间的年份等于系统时间的年份，说明今年已上传过，不发通知
							 * 不考虑月份
							 */
							flag = true;
						}
					//每月完成
					}else if("moth".equals(doneTime.split(",")[0])) {
						if(fileDate == null) {
							//发送通知
							flag = false;
						}else if(fileDate.toString().substring(0,4).equals(year)) {
							/*
							 * 1.文件上传时间年份等于系统时间年份
							 * 2.判断是否有当前月份
							 * 3.不考虑天
							 */
							if(fileDate.toString().substring(5,7).equals(month)) {
								flag = true;
							}else {
								flag = false;
							}
							
						}
					}
				}
			}
			if(!flag) {
				String c = "";
				if("year".equals(bean.getDoneTime().split(",")[0])) {
					c += "每年"+bean.getDoneTime().split(",")[1]+"月";
				}else if("moth".equals(bean.getDoneTime().split(",")[0])) {
					c += "每月"+bean.getDoneTime().split(",")[1]+"号";
				}
				NoticeBean noticeBean = new NoticeBean();
				noticeBean.setState(1);
				noticeBean.setUrl("");
				noticeBean.setUser(bean.getFuser());
				noticeBean.setCount("文档【"+bean.getName()+"】完成时间为【"+c+"】之前，您还未完成。");
				noticeDao.insertNotices(noticeBean);
//				System.out.println("文档【"+bean.getName()+"】完成时间为【"+c+"】之前，您还未完成。");
			}
		}
		
	}
}


