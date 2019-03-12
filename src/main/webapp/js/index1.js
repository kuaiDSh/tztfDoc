$(function(){
	gv_userID = cookie.get("userID");
	
	init();
	 inituser();
	addTab('通知', '', 'notice.html');
		document.onmousemove=function(ev){
		    ev=ev||window.event;
		    mousePos=mouseCoords(ev);
		}
		notice(1);
})
/**
 * 鼠标位置
 */
var mousePos;
/*
 * 用户id
 */
var gv_userID;
/*
 * 对于文档类型  判断工作区等
 */
var gv_menu_sel;
/*
 * 菜单数据
 */
var gv_menudata;
function init() {
	 var timestamp =Date.parse(new Date());
	$.ajax({
		 url:  '../tztfDoc/m/get.do',
        type: "POST",
        dataType : "json",
        success:function(result){
       	 if(result.resultTypeID == 0) {
       		gv_menudata = result.data[0].nodes;
       		 for (var i in result.data[0].nodes) {
       			 var obj = result.data[0].nodes[i];
       			 addmenu('mywork',obj);
       			 addmenu('book',obj);
       			 addmenu('audit',obj);
       			 addmenu('bookok',obj);
       			 addmenu('del',obj);
       		 }
       	 }else {
       		 console.log('获取菜单目录失败');
       	 }
        }
	});
}
function inituser() {
	$.ajax({
		 url:  '../tztfDoc/u/getu.do?id='+gv_userID,
       type: "POST",
       dataType : "json",
       success:function(result){
    	   if(result.resultTypeID == 0) {
    		   $('#span_username').text(result.data.username);
    		   $('#span_name').text(result.data.name);
    	   }
       }
	});
}

/**
 * 添加二级目录
 */
function addmenu(id,obj) {
	$('#'+id).append(
		'<li>'+
			'<a id="menu_'+id+'"'+obj.id+' href="#" onmouseover="show('+id+','+obj.id+');"><i class="glyphicon glyphicon-folder-close menuItemSapn"></i> <span class="menuItemSapn">'+obj.text+'<span></a>'+
		'<li>'
		
	);
};

function show(z,id) {
	if(z.id == 'mywork') {
		gv_menu_sel = '工作区';
	}else if(z.id == 'book') {
		gv_menu_sel = '全部文档';
	}else if(z.id == 'audit') {
		gv_menu_sel = '待审核';
	}else if(z.id == 'bookok') {
		gv_menu_sel = '归档区';
	}else if(z.id == 'del') {
		gv_menu_sel = '作废区';
	}
	var nodes;
	for(var i in gv_menudata) {
		var obj = gv_menudata[i];
		if(id == obj.id) {
			if(obj.nodes != undefined) {
				if(obj.nodes.length == 0) {
					nodes = null;
				}else {
					nodes = obj.nodes;
				}
			}
			break;
		}
	}
//	console.log(nodes);
	if(nodes != undefined && nodes != null) {
		addmenuitem(nodes);
		$('#mm').menu('show', {
		    left: $('#menu').width(),
		    top: mousePos.y,
//		    itemHeight:50,
		    onClick:function(item) {
//		    	console.log(item.temp);
		    	if(item.temp) {
		    		//如果点击的临时属性为true  说明没有子节点
			    	if(gv_menu_sel == '工作区') {
			    		//工作区
//			    		window.open('upload.html?m='+item.id+'&u='+gv_userID);
			    		addTab('('+gv_menu_sel+')'+item.text, '', 'upload.html?m='+item.id+'&u='+gv_userID);
			    	}else if(gv_menu_sel == '全部文档') {
			    		//全部文档
//			    		window.open('upload.html?m='+item.id+'&s=0');
			    		addTab('('+gv_menu_sel+')'+item.text, '', 'upload.html?m='+item.id+'&s=0');
			    	}else if(gv_menu_sel == '待审核') {
			    		//待审核
//			    		window.open('upload.html?m='+item.id+'&s=1');
			    		addTab('('+gv_menu_sel+')'+item.text, '', 'upload.html?m='+item.id+'&s=1');
			    	}else if(gv_menu_sel == '归档区') {
			    		//归档
//			    		window.open('upload.html?m='+item.id+'&s=2');
			    		addTab('('+gv_menu_sel+')'+item.text, '', 'upload.html?m='+item.id+'&s=2');
			    	}else if(gv_menu_sel == '作废区') {
			    		//作废
//			    		window.open('upload.html?m='+item.id+'&s=4');
			    		addTab('('+gv_menu_sel+')'+item.text, '', 'upload.html?m='+item.id+'&s=4');
			    	}
		    	}
		    }
		});
	}else {
		$('#mm').menu('hide');
	}
}

/**
 * 添加菜单节点
 * @param nodes
 */
function addmenuitem(nodes) {
	$('#mm').empty();
	for(var n in nodes) {
		var o = nodes[n];
//		console.log(o.nodes.length);
		if(o.nodes != undefined && o.nodes.length != 0) {
			$('#mm').menu('appendItem', {
				text: o.text,
				id:o.id,
				temp:false
			});
//			$('#mm').append(
//					'<div ><span>'+o.text+'</span><div id="menu_'+o.id+'"></div></div>'	
//				);
			var item =  $('#mm').menu('findItem', o.text);
			for(var c in o.nodes) {
//				$('#menu_'+o.id).append(
//						'<div>'+o.nodes[c].text+'</div>'
//				);
				$('#mm').menu('appendItem', {
					text: o.nodes[c].text,
					id:o.nodes[c].id,
					parent: item.target,
					temp:true
				});
			}
		}else {
//			console.log('走这里');
			$('#mm').menu('appendItem', {
				id:o.id,
				text: o.text,
				temp:true
			});
//			$('#mm').append(
//				'<div>'+o.text+'</div>'	
//			);
		}
	}
	$.parser.parse("#mm");
}
/**
 * 一级菜单点击
 * @param type
 */
function menuclick(type) {
	gv_menu_sel = type;
}
/**
 * 添加选项卡
 */

function addTab(title,icon,url) {
	var f = $('#easyuiTab').tabs('exists',title);
	if(f) {
		$('#easyuiTab').tabs('select',title);
	}else {
		$('#easyuiTab').tabs('add',{
			title: title,
			iconCls:icon,
			selected: true,
			closable:true,
			tools:[{
				iconCls:'icon-mini-refresh',
				handler:function(){
					var tab = $('#easyuiTab').tabs('getSelected');
					var url = $(tab.panel('options')).attr('href');
					$('#easyuiTab').tabs('update', {
					 tab: tab,
					           options: {
					            href: url
					          }
					});
				}
			}],
			
			content:'<iframe scrolling="auto" frameborder="0" src="'+url
				+ '" style="width:100%;height:90%;"></iframe>'
		});
	}
	
	
	
};
/**
 * 其他菜单点击事件
 * @param title
 */
function menuadd(title) {
	if(title == '通知') {
		addTab('通知', '', 'notice.html');
	}else if(title == '文档完成情况') {
		addTab('文档完成情况', '', 'fenxi.html');
	}else {
		if(gv_userID == 1) {
			if(title == '用户管理') {
				window.open('user.html');
			}else if(title == '目录管理') {
				window.open('menu.html');
			}else if(title == '审核管理') {
				window.open('audit.html');
			}
		}
	}
}
/**
 * 通知
 * @param type
 */
function notice(type) {
	var ajaxdata = {user:parseInt(gv_userID),state:parseInt(type)};
	$.ajax({
		 url:  '../tztfDoc/n/get.do',
       type: "POST",
       dataType : "json",
       data:JSON.stringify(ajaxdata),
       success:function(result){
    	   
      	 if(result.resultTypeID == 0) {
      		$('#noticeSpan').text(result.data.length);
      	 }else {
      		
      	 }
       }
	});
}
/**
 * 退出登录
 */
function loginto() {
	location.replace("login.html");
};
/**
 * 修改密码
 */
function updatepassword() {
	$('#old').val('');
	$('#new1').val('');
	$('#new2').val('');
	$('#pwModal').modal('show');
}
/**
 * 修改密码确定
 */
function updatepwok() {
	var old = $('#old').val();
	var new1 = $('#new1').val();
	var new2 = $('#new2').val();
	if(old != '' && new1 != '' && new2 != '' ) {
		if(new1 == new2) {
			old = hex_md5(old);
			new1 = hex_md5(new1);
			new2 = hex_md5(new2);
			var ajxadata = {'old':old,'new':new1,'id':parseInt(gv_userID)};
			$.ajax({
				url:  '../tztfDoc/u/uppw.do',
		        type: "POST",
		        dataType : "json",
		        data:JSON.stringify(ajxadata),
		        success:function(result){
		        	if(result.resultTypeID == 0) {
		        		$('#pwModal').modal('hide');
		        		alert('密码修改成功');
		        		location.replace("login.html");
		        	}else {
		        		alert(result.head);
		        	}
		        }
			});
		}else {
			alert('两次密码输入不一致');
		}
	}else {
		alert('不能为空');
	}
}

/**
 * 获取
 * @param ev
 * @returns
 */
function mouseCoords(ev){
    if(ev.PageX &&ev.PageY){
        return {x:ev.PageX,y:ev.PageY}
    }
    //做兼容
    d=document.documentElement||document.body;
    return {x:ev.clientX+d.scrollLeft-d.clientLeft, y:ev.clientY+d.scrollTop-d.clientTop};
} 