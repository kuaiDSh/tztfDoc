$(function(){
	initTree();
	tableInit();
	initUser();
});
/*
 * 目录树 选择的节点
 */
var select_tree;
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
         				select_tree = data;
         				console.log(data.tags[2]);
         				var obj = {
         						'id':data.id,
         						'name':data.text,
         						'parent':$('#treeview1').treeview('getParent',data).text.name == 'text'?'':$('#treeview1').treeview('getParent',data).text,
         						'content':data.tags[0],
         						'doneTime':data.tags[1],
         						'fUser':data.tags[2]};
         				console.log(select_tree);
         				$('#tab').bootstrapTable('load',[obj]);
         			}
         		});
        	 }else {
        		 console.log('获取菜单目录失败');
        	 }
         }
	});
	/*
	 * 树右键菜单
	 */
	 $('#treeview1').contextmenu({
		target: '#context-menu',
		onItem: function (context, e) {
			if($(e.target).text() == '新建下级目录') {
				openModal('新建下级目录','','');
//				$('#treeview1').treeview('addNode',[select_tree.nodeId,{node:{text:'s',id:4}}]);	
			}else if($(e.target).text() == '修改目录') {
				openModal('修改目录',select_tree.text,select_tree.tags[0]);
//				$('#treeview1').treeview('editNode',[select_tree.nodeId,{text:'修改', id: 2 }]);
			}else if($(e.target).text() == '删除目录') {
				if(select_tree.id != 1) {
					$('#delModal').modal('show');
					$('#delspan').text('是否确定删除目录【'+select_tree.text+'】?');
				}
				// $("#treeview1").treeview("deleteNode",[[7,8]]);//批量删除
			}else if($(e.target).text() == '新建同级目录') {
				if(select_tree.id != 1) {
					openModal('新建同级目录','','');
				}else {
					console.log('bun');
				}
			}else if($(e.target).text() == '新建文档') {
				openDoneModal('新建文档','','','-1,-1',-1);
				
			}else if($(e.target).text() == '修改文档') {
				openDoneModal('修改文档',select_tree.text,select_tree.tags[0],select_tree.tags[1],select_tree.tags[2]);
			}
			
		}
	});
};
var userdata;
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
       		userdata = result.data;
//       		console.log(result.data);
       		 for(var i=0;l=result.data.length,i<l;i++) {
       			select.append("<option value='"+result.data[i].id+"'>"+result.data[i].name+"</option>");
       		 }
       		$('#user').selectpicker('refresh');  
            $('#user').selectpicker('render');   
       	 }
        }
	});
};
var modalTitle;
var doneTitle;
/**
 * 打开文档窗口
 */
function openDoneModal(title,name,content,done,fuser) {
	$('#done_title').text(title);
	doneTitle = title;
	$('#treeModal1').modal('show');
	$('#m').hide();
	$('#d').hide();
	
	$("#done").change(function(){
		if($("#done").val() == 'year') {
			$('#d').hide();
			$('#m').show();
		}else if($("#done").val() == 'moth') {
			$('#m').hide();
			$('#d').show();
		}
	});
	if(done != '' && done != null) {
		var a = done.split(',');
		$('#done').selectpicker('val',a[0]);
		if(a[0] == 'year') {
			$('#done_moth').selectpicker('val',a[1]);
		}else if(a[0] == 'moth') {
			$('#done_day').selectpicker('val',a[1]);
		}
	}
	$('#user').selectpicker('val',fuser);
	$('#treename1').val(name);
	$('#treecontent1').val(content);
	
}
/**
 * 得到文档窗口数据
 */
function getDoneVal() {
	
}
/**
 * 模态框开启
 * @param title 标题
 * @param name  名称
 * @param content  备注
 */
function openModal(title,name,content) {
	modalTitle = title;
	$('#treeTitle').text(title);
	$('#treeModal').modal('show');
	$('#treename').val(name);
	$('#treecontent').val(content);
};
/**
 * 删除菜单确定
 */
function deltreeok() {
	$.ajax({
		 url:  '../tztfDoc/m/delete.do',
       type: "POST",
       dataType : "json",
       data:JSON.stringify({id:select_tree.id}),
       success:function(result){
    	   if(result.resultTypeID == 0) {
    		   $('#treeview1').treeview('deleteNode',[select_tree.nodeId]);
    		   $('#delModal').modal('hide');
    		   $('#tab').bootstrapTable('load',[]);
    	   }else {
    		   alert(result.head);
    	   }
    	   
       }
	});
};
/**
 * 模态框新增修改确定
 */
function treemodalok1() {
	var name = $('#treename1').val();
	var userval = $('#user').val();
	if(userval == null) {
		userval = -1;
	}
	if(name != '') {
		var obj = {'name':name,'content':$('#treecontent1').val()};
		var done = $('#done').val();
		var done1;
		if(done == 'year') {
			done1 = $('#done_moth').val();
		}else if(done == 'moth') {
			done1 = $('#done_day').val();
		}
		done += ','+done1;
		if(doneTitle == '新建文档') {
			obj['id'] = 0;
			obj['parent'] = select_tree.id;
			obj['doneTime'] = done;
			obj['fuser'] = userval;
			console.log(obj);
			addtree1(obj);
		}else if(doneTitle == '修改文档') {
			obj['id'] = select_tree.id;
			var parent = $('#treeview1').treeview('getParent', select_tree);
			obj['parent'] = parent.id;
			obj['doneTime'] = done;
			obj['fuser'] = userval;
			updatetree1(obj);
		}
		
	}else {
		console.log('目录名称不允许为空');
	}
};

/**
 * 模态框新增修改确定
 */
function treemodalok() {
	var name = $('#treename').val();
	if(name != '') {
		var obj = {'name':name,'content':$('#treecontent').val()};
		if(modalTitle == '新建下级目录') {
			obj['id'] = 0;
			obj['parent'] = select_tree.id;
			obj['doneTime'] = '';
			obj['fuser'] = -1;
			addtree(obj);
		}else if(modalTitle == '新建同级目录') {
			obj['id'] = 0;
			var parent = $('#treeview1').treeview('getParent', select_tree);
			if(parent != undefined) {
				obj['parent'] = parent.id;
				obj['doneTime'] = '';
				obj['fuser'] = -1;
			}
			addtree(obj);
		}else if(modalTitle == '修改目录') {
			obj['id'] = select_tree.id;
			var parent = $('#treeview1').treeview('getParent', select_tree);
			obj['parent'] = parent.id;
			obj['doneTime'] = '';
			obj['fuser'] = -1;
			updatetree(obj);
		}
		
	}else {
		console.log('目录名称不允许为空');
	}
};
/**
 * 新增菜单节点
 * @param obj
 */
function addtree1(obj) {
	$.ajax({
		 url:  '../tztfDoc/m/in.do',
        type: "POST",
        dataType : "json",
        data:JSON.stringify(obj),
        success:function(result){
        	if(result.resultTypeID == 0) {
        		var add = {text:result.data.name,id:result.data.id,tags:[result.data.content,result.data.doneTime,result.data.fuser]};
        		$('#treeview1').treeview('addNode',[select_tree.nodeId,{node:add}]);	
        		$('#treeModal1').modal('hide');
        	}else {
        		alert('新建失败');
        	}
        }
	});
};
/**
 * 新增菜单节点
 * @param obj
 */
function addtree(obj) {
	$.ajax({
		 url:  '../tztfDoc/m/in.do',
        type: "POST",
        dataType : "json",
        data:JSON.stringify(obj),
        success:function(result){
        	if(result.resultTypeID == 0) {
        		var add = {text:result.data.name,id:result.data.id,tags:[result.data.content,result.data.doneTime,result.data.fuser]};
        		if(modalTitle == '新建下级目录') {
        			console.log(select_tree);
    				$('#treeview1').treeview('addNode',[select_tree.nodeId,{node:add}]);	
        		}else if(modalTitle == '新建同级目录') {
					var parent = $('#treeview1').treeview('getParent', select_tree);
					$('#treeview1').treeview('addNode',[parent.nodeId,{node:add}]);
        		}
        		$('#treeModal').modal('hide');
        	}else {
        		console.log('失败');
        	}
        }
	});
};
/**
 * 修改菜单节点
 * @param obj
 */
function updatetree1(obj) {
	$.ajax({
		 url:  '../tztfDoc/m/update.do',
       type: "POST",
       dataType : "json",
       data:JSON.stringify(obj),
       success:function(result){
       	if(result.resultTypeID == 0) {
       		var update = {text:result.data.name,id:result.data.id,tags:[result.data.content,result.data.doneTime,result.data.fuser]};
       		var parent = $('#treeview1').treeview('getParent', select_tree);
       		$('#treeview1').treeview('editNode',[select_tree.nodeId,update]);
       		var tabobj = {'id':result.data.id,'name':result.data.name,
       				'parent':parent.text,'content':result.data.content,
       				'doneTime':result.data.doneTime,'fUser':result.data.fuser};
       		$('#tab').bootstrapTable('load',[tabobj]);
       		$('#treeModal1').modal('hide');
       	}else {
       		alert('修改失败');
       	}
       }
	});
};
/**
 * 修改菜单节点
 * @param obj
 */
function updatetree(obj) {
	$.ajax({
		 url:  '../tztfDoc/m/update.do',
       type: "POST",
       dataType : "json",
       data:JSON.stringify(obj),
       success:function(result){
       	if(result.resultTypeID == 0) {
       		var update = {text:result.data.name,id:result.data.id,tags:[result.data.content,result.data.doneTime,result.data.fuser]};
       		var parent = $('#treeview1').treeview('getParent', select_tree);
       		$('#treeview1').treeview('editNode',[select_tree.nodeId,update]);
       		var tabobj = {'id':result.data.id,'name':result.data.name,'parent':parent.text,'content':result.data.content,
       				'doneTime':result.data.doneTime,'fUser':result.data.fuser};
       		$('#tab').bootstrapTable('load',[tabobj]);
       		$('#treeModal').modal('hide');
       	}else {
       		console.log('失败');
       	}
       }
	});
};


/**
 * 表格初始化
 */
function tableInit() {
	$('#tab').bootstrapTable({
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        clickToSelect: true,                //是否启用点击选中行
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        columns: [ {
            field: 'id',
            title: 'id',
            visible: false
        }, {
            field: 'name',
            title: '目录名称',
            width:100
        }, {
            field: 'parent',
            title: '上级目录',
            width:100
        }, {
            field: 'content',
            title: '目录备注',
            width:200
        }, {
            field: 'doneTime',
            title: '完成时间',
            width:200,
            formatter:function(value) {
            	if(value != '' && value != null) {
            		if(value.indexOf(',')>-1) {
            			var a = value.split(',');
            			var v = '';
            			if(a[0] == 'year') {
            				v += '每年,'+a[1]+'月';
            			}else if(a[0] == 'moth') {
            				v += '每月,'+a[1]+'号';
            			}
            			return v;
            		}else {
            			return value;
            		}
            	}else {
            		return value;
            	}
            	
            }
        },{
            field: 'fUser',
            title: '负责人',
            width:200,
            formatter:function(value) {
            	if(value == -1) {
            		return '-';
            	}else {
            		for(var i in userdata) {
            			if(value == userdata[i].id) {
            				
            				return userdata[i].name.substring(userdata[i].name.indexOf('|')+1);
            			}
            		}
            	}
            	
            }
        }]
	});
};









