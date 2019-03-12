$(function(){
	var bodyH = window.screen.availHeight;
	initTree();
	initUser();
	initTab();

});

/*
 * 目录树 选择的节点
 */
var select_tree = null;
/*
 * 菜单id
 */
var menuID;
/*
 * 审核id
 */
var gv_auditID;
/*
 * 用户数据
 */
var userdata;
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
         						$('#tab').bootstrapTable('load',[]);
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
         				menuID = parseInt(data.id);
         				$.ajax({
         					url:  '../tztfDoc/a/s.do',
	   				        type: "POST",
	   				        dataType : "json",
	   				        data:JSON.stringify({menu:parseInt(data.id)}),
	   				        success:function(result){
	   				        	if(result.resultTypeID == 0) {
	   				        		//有数据
	   				        		var audit = result.data.audit;
	   				        		console.log(audit);
	   				        		menuID = audit.menuID;
	   				        		gv_auditID = audit.id;
	   				        		$('#auditTitle').val(audit.name);
	   				        		var item = result.data.item;
	   				        		console.log(item);
	   				        		for(var i in item) {
	   				        			gv_index++;
	   				        			item[i]['index'] = gv_index;
	   				        		}
	   				        		$('#tab').bootstrapTable('load',item);
//	   				        		for(var i in item) {
//	   				        			var o = item[i];
//	   				        			var attr = JSON.parse(o.chart);
//	   				        			var obj = {'user':o.auditor,'auditName':o.name};
//	   				        		}
	   				        	}else {
	   				        		//无数据
//	   				        		menuID = 0;
	   				        		gv_auditID = 0;
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
/*
 * 窗口打开类型
 */
var modaltype;
/**
 * 新增节点
 */
function insertitem() {
	$('#auditmodal').modal('show');
	$('#modaltitle').text('新增节点');
	modaltype = 'add';
	clearmodalvalue();
};
/**
 * 修改节点
 */
function updateitem() {
	if(select_row != null) {
		$('#auditmodal').modal('show');
		$('#modaltitle').text('修改节点');
		modaltype = 'update';
		setmodalvalue(select_row);
	}else {
		alert('请选择一行数据');
	}
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
/*
 * 表格单机行数据
 */
var select_row;
var gv_index = 0;
/**
 * 初始化表格
 */
function initTab() {
	$('#tab').bootstrapTable({
		toolbar: '#toolbar',
		singleSelect:true,
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        clickToSelect: true,                //是否启用点击选中行
        uniqueId: "index",                     //每一行的唯一标识，一般为主键列
        columns: [
				{
				    field: 'index',
				    title: '行号',
				    visible:false
				},
                    {
            field: 'name',
            title: '节点名称'
        },  {
            field: 'auditor',
            title: '审核人',
            formatter:function(value,row,index) {
            	return getformatter(value);
            }
        }
        ],
        onClickRow:function(row,$element,field) {
        	$('.info').removeClass('info');//移除class
            $($element).addClass('info');//添加class
//            $("#tab").bootstrapTable('refresh', $("#tab").bootstrapTable('getData'));
            select_row = row;
//            select_row = $("#tab").bootstrapTable('getSelections');
//            console.log(select_row);
//        	console.log(gv_select_id);
        },
        onDblClickRow:function(row,$element,field) {
        	$('.info').removeClass('info');//移除class
            $($element).addClass('info');//添加class
//            $("#tab").bootstrapTable('refresh', $("#tab").bootstrapTable('getData'));
            select_row = row;
//            select_row = $("#tab").bootstrapTable('getSelections');
//            console.log(select_row);
            updateitem();
        }
	});
};
/**
 * 保存配置
 */
function saveaudit() {
//	console.log($('#tab').bootstrapTable('getData'));
	var save = $('#tab').bootstrapTable('getData');
	console.log(save);
	if(save.length != 0) {
		if($('#auditTitle').val().trim() != '') {
			showmsg();
			var audit = {id:parseInt(gv_auditID),menuID:menuID,name:$('#auditTitle').val().trim()};
			var item = [];
			for(var i in save) {
				var obj = {id:parseInt(gv_auditID),name:save[i].name,index:parseInt(i)+1,auditor:save[i].auditor,chart:''};
				item.push(obj);
			}
			var ajaxdata = {audit:audit,item:item};
			$.ajax({
					url:  '../tztfDoc/a/c.do',
			        type: "POST",
			        dataType : "json",
			        data:JSON.stringify(ajaxdata),
			        success:function(result){
			        	closemsg();
			        }
			});
		}else {
			alert('审核名称不能为空');
		}
	}else {
		alert('没有节点无法保存');
	}
};
/**
 * 提交
 */
function auditok() {
	var obj= getmodalvalue();
	if(obj.name != '' && obj.auditor != '' && obj.auditor != null) {
		if(modaltype == 'add') {
			gv_index++;
			obj['index'] = gv_index;
			$('#tab').bootstrapTable('append',obj);
			$('#auditmodal').modal('hide');
		}else if(modaltype == 'update') {
			var data = $('#tab').bootstrapTable('getData');
			var index;
			for(var i in data) {
				if(select_row == data[i]) {
					index = i;
					break;
				}
			}
			$('#tab').bootstrapTable('updateRow',{index:index,row:obj});
			$('#auditmodal').modal('hide');
		}
	}else {
		alert('不能为空数据');
	}
};
/**
 * 删除节点
 */
function deleteitem() {
	if(select_row != null) {
		$('#tab').bootstrapTable('removeByUniqueId',select_row.index);
	}
	
};

/**
 * 得到窗口数据
 */
function getmodalvalue() {
	var v = $("#user").val();
	if(v != null) {
		v = $("#user").val().join(',');
	}
	var obj = {name:$('#name').val(),auditor:v};
	return obj;
}
/**
 * 清空窗口数据
 */
function clearmodalvalue() {
	$('#name').val('');
	$("#user").selectpicker('val','');
}
/**
 * 赋值
 * @param obj
 */
function setmodalvalue(obj) {
	$('#name').val(obj.name);
	console.log(obj.auditor);
	$("#user").selectpicker('val',obj.auditor.split(','));
};

/**
 * 审核人formatter
 * @param str
 */
function getformatter(str) {
	var o = [];
	console.log(str.split(','));
	var strs = str.split(',');
	for(var i in strs) {
		var v = strs[i];
		console.log(v);
		for(var j in userdata) {
			var obj = userdata[j];
			var id = obj.id;
			console.log(id);
			if(id == v) {
				o.push(obj.name);
			}
		}
	}
	console.log(o.join(','));
	return o.join(',');
};
/**
 * 组件禁用
 */
function controlDisable() {
	$('#btn_insert').prop('disabled',true);
	$('#btn_update').prop('disabled',true);
	$('#btn_del').prop('disabled',true);
	$('#btn_save').prop('disabled',true);
	$('#auditTitle').val('');
	$('#auditTitle').prop('disabled',true);
};
/**
 * 组件启用
 */
function controlDisableFalse() {
	
	$('#btn_insert').prop('disabled',false);
	$('#btn_update').prop('disabled',false);
	$('#btn_del').prop('disabled',false);
	$('#btn_save').prop('disabled',false);
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
 * 显示loading..
 */
function showmsg() {
	layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        area: 'auto;',
        skin: 'layui-layer-nobg', //没有背景色
        shadeClose: false,
        content: '<img  src="bootstarp/css/img/loading.gif"/>'
      });
};
/**
 * 关闭loading..
 */
function closemsg() {
	layer.close(layer.index);
};