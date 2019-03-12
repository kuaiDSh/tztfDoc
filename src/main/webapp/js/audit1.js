$(function(){
	var bodyH = window.screen.availHeight;
	initTree();
	
	console.log(bodyH);
	paper = new Raphael("holder", $('#holder').width(), 620-50); 
	scale = {vari:.05,zoom:1,w:$('#holder').width(),h:$('#holder').height(),x:0,y:0};
	initUser();
	initPaper();
	papermeth();
});
var scale;
function papermeth() {
	/*
	 * 中间画布鼠标按下操作
	 * 1.判断鼠标为左键按下
	 * 2.判断自定义类型是否为2-画图类型
	 * 		1.改变鼠标样式
	 * 		2.为鼠标对象自定义key：tempx tempy用来记录鼠标按下的初始坐标
	 * 		3.判断自定义data类型中type
	 * 			rect 画矩形
	 * 			text 文本
	 * 		4.为鼠标对象自定义【状态】key： ok 当为true时、鼠标已松开
	 */
	$('#holder').mousedown(function(e){
		if(e.which == 1) {/*左键*/
			if(mouseType == 2) {/*画图类型*/
				$('#holder').css('cursor','Crosshair');
				this.tmpx = e.offsetX/scale.zoom+scale.x;
				this.tmpy = e.offsetY/scale.zoom+scale.y;
				if(paperdata.type == 'rect') {/*矩形*/
					this.temp = rect(paper,this.tmpx,this.tmpy,0,0,paperdata.r,paperdata.fill,paperdata.tips);
				}else if(paperdata.type == 'text') {/*文字*/
					this.temp = text(paper, this.tmpx, this.tmpy,paperdata.tips);
				}
				/*鼠标按下状态为false*/
				this.ok = false;
			}
		}
		
	});
	/*
	 * 画布鼠标松开
	 * 1.将鼠标自定义状态：ok改为true
	 * 2.自定义类型为2
	 * 		1.将当前被选中图形改为当前创建的图形temp
	 * 		2.创建选择框
	 * 		3.将选择框置于顶层
	 */
	$('#holder').mouseup(function(e){
		if(e.which == 1) {
			this.ok = true;
			if(mouseType == 2) {
				select_paper = this.temp;
				if(select_paper.type == 'rect') {
				}else if($selectRect.type == 'text') {
					
				}
				mouseType = 1;
			}
		}
	});
	/*
	 * 中间画布鼠标移动
	 * 1.
	 */
	$('#holder').mousemove(function(e){
		if(mouseType == 2) {
			$('#holder').css('cursor','Crosshair');
			if(!this.ok && this.temp != undefined) {
				if(this.temp.type == 'rect') {
					if((e.offsetX/scale.zoom+scale.x-this.tmpx) > 0 && (e.offsetY/scale.zoom+scale.y-this.tmpy) > 0 ) {
						this.temp.attr({'width':(e.offsetX/scale.zoom+scale.x-this.tmpx),'height':(e.offsetY/scale.zoom+scale.y-this.tmpy)});
					}	
				}
			}
		}else {
			$('#holder').css('cursor','default');
		}
	});
}
/*
 * 目录树 选择的节点
 */
var select_tree = null;
/*
 * 画布
 */
var paper;
/*
 *选中的画布 
 */
var select_paper = null;
var menuID;
/**
 * 初始化树
 */
function initTree(){
	$.ajax({
		 url:  '../tztfDoc/m/get.do',
         type: "POST",
         dataType : "json",
         success:function(result){
        	 if(result.resultTypeID == 0) {
        		 $('#treeview1').treeview({ 
         			data: result.data,
         			/*选择节点事件*/
         			onNodeSelected:function(event, data) {
         				if(select_tree != null) {
         					if(select_tree != data) {
         						/*
         						 * 如果当前选中的菜单 不等于上一次选中的菜单
         						 * 清空画布
         						 */
         						paper.clear();
         					}
         					
         				}
         				
         				select_tree = data;
         				if(data.nodes != undefined) {
         					controlDisable();
         				}else {
         					//没有子菜单时才可创建流程
         					controlDisableFalse();
         					$('#auditTitle').val(data.text+'审核流程');
         				}
         				console.log(data);
         				$.ajax({
         					url:  '../tztfDoc/a/s.do',
	   				        type: "POST",
	   				        dataType : "json",
	   				        data:JSON.stringify({menu:parseInt(data.id)}),
	   				        success:function(result){
	   				        	if(result.resultTypeID == 0) {
	   				        		//有数据
	   				        		var audit = result.data.audit;
	   				        		menuID = audit.menuID;
	   				        		var item = result.data.item;
	   				        		console.log(item);
	   				        		for(var i in item) {
	   				        			var o = item[i];
	   				        			var attr = JSON.parse(o.chart);
	   				        			var obj = {'user':o.auditor,'auditName':o.name};
	   				        			console.log(attr.x);
	   				        			rect(paper, attr.x, attr.y, attr.width, attr.height, attr.r, attr.fill, '', 'create', {'audit':obj});
//	   				        		 paper.rect(attr.x,y,w,h,r).attr({'fill':fill}).data('content',typed).drag(chartOnMove,chartOnStartMove).click(chartOnClick);
	   				        		}
	   				        	}else {
	   				        		//无数据
	   				        		menuID = 0;
	   				        	}
	   				        	
   				        }
         				});
         			}
         		});
        	 }else {
        		 console.log('获取菜单目录失败');
        	 }
         }
	});
};

/**
 * 初始化审核人下拉选
 */
function initUser() {
	$.ajax({
		 url:  '../tztfDoc/u/get.do',
        type: "POST",
        dataType : "json",
        success:function(result){
       	 if(result.resultTypeID == 0) {
       		var select = $("#user");
       		 for(var i=0;l=result.data.length,i<l;i++) {
       			select.append("<option value='"+result.data[i].id+"'>"+result.data[i].name+"</option>");
       		 }
       		$('#user').selectpicker('refresh');  
            $('#user').selectpicker('render');   
       	 }
        }
	});
};
/*
 * 选择画布属性
 */
var paperdata;
/*
 * 鼠标类型
 */
var mouseType;
/**
 * 初始化上方画布
 */
function initPaper() {
	var paper1 = new Raphael("holderp", $('#holderp').width(), $('#holderp').height()); 
	paper1.rect(10,10,40,30,0).attr({'fill':'#FFF0F5','title':'选择'}).data('content',{'tips':'选择'})
	.hover(fin,fout).click(click);
	paper1.rect(65,10,40,30,0).attr({'fill':'#FFF0F5','title':'开始'}).data('content',{'tips':'开始','config':{'type':'rect','r':5,'fill':'#FFF0F5'}})
	.hover(fin,fout).click(click);
	paper1.rect(120,10,40,30,5).attr({'fill':'#FFF0F5','title':'审核节点'}).data('content',{'tips':'审核节点','config':{'type':'rect','r':5,'fill':'#FFF0F5'}})
	.hover(fin,fout).click(click);
	paper1.rect(175,10,40,30,5).attr({'fill':'#FFF0F5','title':'结束'}).data('content',{'tips':'结束','config':{'type':'rect','r':5,'fill':'#FFF0F5'}})
	.hover(fin,fout).click(click);
	/*鼠标滑过进入*/
	function fin() {
		/*自定义属性、记录其初始填充颜色*/
		this.tempFill = this.attr('fill');
		/*鼠标进入时、改变其填充色为白色*/
		this.attr('fill','white');
	}
	/*鼠标滑出*/
	function fout() {
		/*将填充色变为其初始色*/
		this.attr('fill',this.tempFill);
	}
	/*点击*/
	function click() {
		/*如果点击不是选择类型的*/
		if(this.data('content').tips != '选择') {
			/*将鼠标类型赋值为2*/
			mouseType = 2;
			/*改变即将画图的属性*/
			paperdata = this.data('content').config;
			paperdata['tips'] = this.data('content').tips;
		}else {
			mouseType = 1;
			paperdata = null;
		}
	}
};


/**
 * 组件禁用
 */
function controlDisable() {
//	$('#holder').attr('disabled','disabled');
	$('#user').selectpicker('val',''); 
	$('#user').prop('disabled', true);
	$('#auditName').val('');
	$('#auditName').prop('disabled',true);
	$('#auditok').prop('disabled',true);
	$('#auditTitle').val('');
	$('#auditTitle').prop('disabled',true);
};
/**
 * 组件启用
 */
function controlDisableFalse() {
	
	$('#user').selectpicker('val',''); 
	$('#user').prop('disabled', false);
	$('#auditName').val('');
	$('#auditName').prop('disabled',false);
	$('#auditok').prop('disabled',false);
	$('#auditTitle').val('');
	$('#auditTitle').prop('disabled',false);
};
/**
 * 获取属性属性数据
 */
function getval() {
	console.log($('#user').val());
	var obj = {'user':$('#user').val(),'auditName':$('#auditName').val()};
	return obj;
};
/**
 * 画布、x坐标、y坐标、宽度、高度、圆角
 */
function rect(paper,x,y,w,h,r,fill,tips,type,typed) {
	var inRect;
	if(type == 'create') {
		inRect = paper.rect(x,y,w,h,r)
					.attr({'fill':fill}).data('content',typed).drag(chartOnMove,chartOnStartMove).click(chartOnClick);
	}else {
		inRect = paper.rect(x,y,w,h,r).attr({'fill':fill}).data('content',{'tips':tips}).drag(chartOnMove,chartOnStartMove).click(chartOnClick);
	}
	return inRect;
};


/**
 * 图形正在移动时
 * 1.改变自身坐标
 * 2.改变选择框的坐标
 * 3.改变包围选择框各个角的坐标
 */
function chartOnMove(dx,dy,x,y) {
	if(mouseType == 1) {
		dx /= scale.zoom;
		dy /= scale.zoom;
		//1
		this.attr({'x':this.tempx+dx,'y':this.tempy+dy});
	}
};
/**
 * 当前图形开始移动时
 * 1.为被选择图形赋值为当前移动图形
 * 2.创建选择框
 * 3.对其自身增加自定义属性、tempx、tempy 记录当开始拖动时其原本的x，y坐标
 */
function chartOnStartMove(x,y) {
	if(mouseType == 1) {
		clickmeth(this);
//		select_paper = this;
		this.tempx = this.attr("x"); 
		this.tempy = this.attr("y"); 
	}
};
/**
 * 图形点击
 */
function chartOnClick(e) {
	if(mouseType == 1) {
		clickmeth(this);
		select_paper = this;
	}
};
function clickmeth(item) {
	if(item.data('content').tips != '开始' && item.data('content').tips != '结束') {
		if(select_paper != null) {
			select_paper.data('content')['audit'] = getval();
		}
		
		if(item.data("content").audit != undefined) {
			$('#user').selectpicker('val',item.data("content").audit.user); 
			$('#auditName').val(item.data("content").audit.auditName);
		}
		select_paper = item;
	}
};

function auditok() {
	var index = 0;
	var arr = [];
	paper.forEach(function (el) {
		if(el.data('content').tips != '开始' && el.data('content').tips != '结束') {
			index++;
			var obj = {'id':0,'name':el.data('content').audit.auditName,'findex':index,
					'auditor':el.data('content').audit.user,'chart':JSON.stringify(el.attr())};
			arr.push(obj);
		}
	});
	var abj = {'id':0,'menuID':select_tree.id,'name':$('#auditTitle').val()};
	var ajaxdata = {'audit':abj,'item':arr};
	
	console.log(ajaxdata);
	$.ajax({
		 url:  '../tztfDoc/a/c.do',
        type: "POST",
        dataType : "json",
        data:JSON.stringify(ajaxdata),
        success:function(result){
        	if(result.resultTypeID == 0) {
        		alert('成功');
        	}
        }
	});
}

