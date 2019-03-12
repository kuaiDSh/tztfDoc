$(function(){
	gv_userID = cookie.get("userID");
	init();
	addtab('0','通知','audit.html');
})
var gv_userID;
var gv_menu_sel;
function init() {
	$.ajax({
		 url:  '../tztfDoc/m/get.do',
        type: "POST",
        dataType : "json",
        success:function(result){
       	 if(result.resultTypeID == 0) {
//       		 console.log(result.data);
//       		 for(var i in result.data) {
//       			 var obj = result.data[i];
//       			 if() {
//       				 
//       			 }
//       		 }
       		 console.log(result.data[0].nodes);
       		 var data = [
       		             {text:'文档管理',nodes:[
       		                      {text:'通知'},
       		                   {text:'文档区',nodes:[
       		                                      {text:'工作区',nodes:result.data[0].nodes},
       		                                      {text:'全部文档',nodes:result.data[0].nodes}, 
       		                                     {text:'待审核',nodes:result.data[0].nodes}, 
       		                                     {text:'归档区',nodes:result.data[0].nodes}, 
       		                                     {text:'作废区',nodes:result.data[0].nodes}
       		                                      ]},
       		                {text:'系统管理',nodes:[
       		                         {text:'目录管理'},
       		                         {text:'流程配置'},
       		                         {text:'用户管理'}
       		                                    ]},
       		                    ]},
       		             ];
       		 $('#menu').treeview({ 
        			data: data,
        			/*选择节点事件*/
        			onNodeSelected:function(event, data) {
        				console.log(data.text);
        				if(data.text == "工作区") {
        					gv_menu_sel = '工作区';
        				}else if(data.text == '全部文档') {
        					gv_menu_sel = '全部文档';
        				}else if(data.text == '待审核') {
        					gv_menu_sel = '待审核';
        				}else if(data.text == '归档区') {
        					gv_menu_sel = '归档区';
        				}else if(data.text == '作废区') {
        					gv_menu_sel = '作废区';
        				}
//        				select_tree = data;
        				console.log(data.nodes);
        				if(data.nodes == undefined) {
        					var url = 'upload.html?';
        					console.log(gv_menu_sel);
        					if(gv_menu_sel == '工作区') {
        						url += 'm='+data.id+'&u='+gv_userID;
        					}else if(gv_menu_sel == '全部文档') {
        						url += 'm='+data.id+'&s=0';
        					}else if(gv_menu_sel == '待审核') {
        						url += 'm='+data.id+'&s=1';
        					}else if(gv_menu_sel == '归档区') {
        						url += 'm='+data.id+'&s=2';
        					}else if(gv_menu_sel == '作废区') {
        						url += 'm='+data.id+'&s=4';
        					}
        					if(data.text == '目录管理') {
//        						window.open('menu.html');
        						addtab(data.id,data.text,'menu.html');
        					}else if(data.text == '流程配置') {
        						window.open('audit.html');
        					}else if(data.text == '用户管理') {
        						window.open('user.html');
        					}else {
//        						window.open(url);
        						addtab(data.id,data.text,url);
        					}
        					
//        					var parent = $('#menu').treeview('getParent', data);
//        					while(parent.text != '已上传' && parent.text != '已审核' && parent.text != '审核中' && parent.text != '文件上传') {
//        						console.log(1);
//        						parent = $('#menu').treeview('getParent', parent);
//        					}
//        					var url = 'upload.html?';
//        					if(parent.text == '文件上传') {
//        						url += 'm='+data.id+'&u='+gv_userID;
//        					}else if(parent.text == '已上传') {
//        						url += 'm='+data.id+'&s=0';
//        					}else if(parent.text == '审核中') {
//        						url += 'm='+data.id+'&s=1';
//        					}else if(parent.text == '已审核') {
//        						url += 'm='+data.id+'&s=2';
//        					}
//        					window.open(url);  
//        					console.log(parent);
//        					console.log(data);
        				}

        			}
        		});
       	 }else {
       		 console.log('获取菜单目录失败');
       	 }
        }
	});
}

/**
 * 添加选项卡
 * @param id
 * @param title
 * @param url
 */
function addtab(id,title,url) {
	nthTabs = $("#editor-tabs").nthTabs();
	nthTabs.addTab({
		id:id,
		title:title,
		content:'<iframe scrolling="auto" frameborder="0" src="'+url
		+ '" style="width:100%;height:800px;"></iframe>'
	}).setActTab("#"+id);
//	nthTabs.addTab({
//		id:'a',
//		title:'孙悟空',
//		content:'看我七十二变',
//	}).addTab({
//		/*换个姿势*/
//		id:'b',
//		title:'猪八戒-关不掉',
//		content:'高老庄娶媳妇',
//		active:true,
//		allowClose:false,
//	}).addTab({
//		id:'c',
//		title:'沙和尚',
//		content:'请叫我大叔',
//	}).addTab({
//		id:'d',
//		title:'唐僧',
//		content:'光头一个',
//	}).addTab({
//		id:'e',
//		title:'武松',
//		content:'打虎猛汉',
//	}).addTab({
//		id:'f',
//		title:'潘金莲',
//		content:'求爱爱',
//	}).addTab({
//		id:'g',
//		title:'来个标题长一点的的的来个标题长一点的的的',
//		content:'你赢了',
//	}).addTab({
//		id:'h',
//		title:'支持连贯操作',
//		content:'没错就是这么爽',
//	}).addTab({
//		id:'i',
//		title:'欢迎提意见',
//		content:'一定一定',
//	}).addTab({
//		id:'j',
//		title:'熬夜写的啊',
//		content:'码农苦逼~',
//	}).setActTab("#c");
}