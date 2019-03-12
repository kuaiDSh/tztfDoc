$(function(){
	gv_menu = $.getUrlParam('m');
	gv_userid = $.getUrlParam('u');
	gv_status = $.getUrlParam('s');
	gv_cookie_userid = cookie.get("userID");
	console.log(gv_menu);
	console.log(gv_userid);
	console.log(gv_status);
	init();
	var ajaxdata = {};
	if(gv_status == null && gv_menu != null && gv_userid != null) {
		//查看本人的
		ajaxdata['type'] = 'user';
		ajaxdata['userid'] = parseInt(gv_userid);
		ajaxdata['menuid'] = parseInt(gv_menu);
		$('#btn_del').prop('disabled',true);
	}else if(gv_userid == null && gv_menu != null && gv_status != null) {
		//根据状态查看
		ajaxdata['type'] = 'status';
		ajaxdata['status'] = parseInt(gv_status);
		ajaxdata['menuid'] = parseInt(gv_menu);
		//按钮禁用
		$('#btn_up').prop('disabled',true);
		$('#btn_audit').prop('disabled',true);
		if(gv_status == 4) {
			$('#btn_del').prop('disabled',false);
		}else {
			$('#btn_del').prop('disabled',true);
		}
	}
	 $.ajax({
         url:  '../tztfDoc/f/s.do',
         type: "POST",
         dataType : "json",
         data:JSON.stringify(ajaxdata),
         success:function(r) {
        	 	if(r.resultTypeID == 0) {
	        		 $('#tab').bootstrapTable('load',r.data);  
        	 	}
        	 }
         });
});
var gv_menu;
var gv_userid;
var gv_status;
//表格选择行id
var gv_select_row = null;
/**
 * 登陆用户id
 */
var gv_cookie_userid;
/**
 * 表格初始化
 */
function init() {
	$('#tab').bootstrapTable({
		toolbar: '#toolbar',                //工具按钮用哪个容器
		singleSelect:true,
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
//        showColumns: true,                  //是否显示所有的列
//        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
//        height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
        columns: [ 
                   {
            field: 'id',
            title: 'id',
            visible:false
        }, {
            field: 'oldName',
            title: '文件名称'
        },  {
            field: 'version',
            title: '文件版本'
        },
        {
            field: 'fdate',
            title: '上传时间'
        },
        {
            field: 'status',
            title: '文件状态',
            formatter: function (value, row, index) {
                if (value == '0' || value == 0) {
                    return "已上传";
                } else if (value == '1' || value == 1){
                    return "审核中";
                }else if(value == '2' || value == 2) {
                	return '已审核';
                }else if(value == '3' || value == 3) {
                	return '被驳回';
                }else if(value == '4' || value == 4) {
                	return '已作废';
                }else if(value == '5' || value == 5) {
                	return '已删除';
                }else {
                	return value;
                }
                
            }
        },
        {
            field: 'userid',
            title: '上传用户'
        },
        {
            field: 'menuid',
            title: '所属目录'
        },
        {
            field: 'path',
            title: '文件地址',
            visible:false
        },
        {
            field: 'downNum',
            title: '下载次数',
            visible:false
        },
        {
            field: 'newName',
            title: '加密名称',
            visible:false
        }
        ],
        onClickRow:function(row,$element,field) {
        	$('.info').removeClass('info');//移除class
            $($element).addClass('info');//添加class
            gv_select_row = row;
//        	console.log(gv_select_id);
        },
        onDblClickRow:function(row,$element,field) {
        	$('.info').removeClass('info');//移除class
            $($element).addClass('info');//添加class
            window.open('load.html?id='+row.id);
//            gv_select_row = row;
//        	console.log(gv_select_id);
        }
	});
	
	/*
	 * 树右键菜单
	 */
	 $('#tab').contextmenu({
		target: '#context-menu',
		onItem: function (context, e) {
			if($(e.target).text() == '作废文件') {
				if(gv_status == 2) {
					console.log('作废');
					if(gv_select_row != null) {
						if(gv_cookie_userid == 1) {
							$.ajax({
				    	         url:  '../tztfDoc/f/updateS.do?i='+gv_select_row.id+'&s=4',
				    	         type: "POST",
				    	         dataType : "json",
				    	         success:function(result) {
				    	        	if(result.resultTypeID == 0) {
				    	        		alert('文件【'+gv_select_row.oldName+'】已作废');
				    	        		$('#tab').bootstrapTable('remove',{
				    	        			field:'id',
				    	        			values:[parseInt(gv_select_row.id)]
				    	        		});
				    	        		gv_select_row = null;
				    	        	}
				    	         }
				        	 });
						}else {
							alert('没有权限');
						}
						 
					}
				}
			}else if($(e.target).text() == '还原文件') {
				if(gv_status == 4) {
					console.log('还原');
					if(gv_select_row != null) {
						if(gv_cookie_userid == 1) {
							$.ajax({
				    	         url:  '../tztfDoc/f/updateS.do?i='+gv_select_row.id+'&s=2',
				    	         type: "POST",
				    	         dataType : "json",
				    	         success:function(result) {
				    	        	if(result.resultTypeID == 0) {
				    	        		alert('文件【'+gv_select_row.oldName+'】已还原');
				    	        		$('#tab').bootstrapTable('remove',{
				    	        			field:'id',
				    	        			values:[parseInt(gv_select_row.id)]
				    	        		});
				    	        		gv_select_row = null;
				    	        	}
				    	         }
				        	 });
						}else {
							alert('没有权限');
						}
						
					}
				}
			}
		}
	});
};
var formData = null;
/**
 * 上传文件按钮
 */
function upload(){
	$('#uploadModal').modal('show');
	$('#upfile').fileinput('refresh');
	$("#upfile").on("filebatchselected", function(event, files) {
		console.log(files);
		formData =  new FormData(); 
		formData.append("author", "hooyes"); // 可以增加表单数据
		formData.append("file", files[0]); // 文件对象
		console.log(formData);
	});
};

/**
 * 上传确定按钮点击事件
 */
function upok() {
	if(formData != null) {
		
		showmsg();
		$.ajax({
	         url:  '../tztfDoc/f/upload.do',
	         type: "POST",
	         dataType : "json",
	         data: formData,
	         /**
	         *必须false才会自动加上正确的Content-Type
	         */
	         contentType: false,
	         /**
	         * 必须false才会避开jQuery对 formdata 的默认处理
	         * XMLHttpRequest会对 formdata 进行正确的处理
	         */
	         processData: false,
	         success:function(result) {
	        	 console.log(result);
	        	 if(result.resultTypeID == 0) {
	        		//获取文件版本
	        		var version = $('#version').val();
	        		 result.data.version = version;
	        		 result.data.menuid = gv_menu;
	        		 result.data.userid = gv_userid;
	        		 
	        		 $.ajax({
	        	         url:  '../tztfDoc/f/in.do',
	        	         type: "POST",
	        	         dataType : "json",
	        	         data:JSON.stringify(result.data),
	        	         success:function(r) {
	        	        	 closemsg();
	        	        	 	if(r.resultTypeID == 0) {
	        		        		 $('#tab').bootstrapTable('append',r.data[0]);
	        		        		 $('#uploadModal').modal('hide');
	        	        	 	}else {
	        	        	 		alert(r.head);
	        	        	 	}
	        	        	 }
	        	         });
 
	        		 
	        	 }else {
	        		 closemsg();
	        		 alert(result.head);
	        	 }
//	        	 var obj = {'version':version,'menuid':gv_menu,'userid':gv_userid,'formData':formData};
	         }
		 });
	}
	 
};
/**
 * 提交审核
 */
function audit() {
	if(gv_select_row != null) {
		if(gv_select_row.status == 0) {
			//根据菜单id  判断是否存在审核流程
			$.ajax({
		         url:  '../tztfDoc/a/s.do',
		         type: "POST",
		         dataType : "json",
		         data:JSON.stringify({menu:parseInt(gv_menu)}),
		         success:function(r) {
		        	 if(r.data.audit == undefined) {
		        		 alert('该目录暂时没有审核流程');
		        	 }else {
		        		 var ajaxdata = {'auditID':r.data.audit.id,'menuID':parseInt(gv_menu),'fileID':parseInt(gv_select_row.id),
		        				 'auditor':-1,'fIndex':0,'auditDate':getNowFormatDate(),'aditContent':'','auditState':0};
		        	 }
		        	 $.ajax({
		    	         url:  '../tztfDoc/a/ir.do',
		    	         type: "POST",
		    	         dataType : "json",
		    	         data:JSON.stringify(ajaxdata),
		    	         success:function(result) {
		    	        	if(result.resultTypeID == 0) {
		    	        		alert('审核已提交');
		    	        	}
		    	         }
		        	 });
		        }
		     });
		}else {
			alert('不能提交审核');
		}
		
	}else {
		alert('请选择行');
	}
};
/**
 * 查看审核记录
 */
function auditRecord() {
	if(gv_select_row != null) {
		if(gv_select_row.status != 0) {
			$.ajax({
		         url:  '../tztfDoc/a/s.do',
		         type: "POST",
		         dataType : "json",
		         data:JSON.stringify({menu:parseInt(gv_menu)}),
		         success:function(r) {
		        	if(r.data.audit != undefined) {
		        		window.open('a.html?m='+gv_menu+'&f='+gv_select_row.id+'&a='+r.data.audit.id);
		        	}
		         }
			});
	
		}else {
			alert('文件【'+gv_select_row.oldName+'】还未提交审核');
		}
	}else {
		alert('请选择行');
	}
	
}

/**
 * 删除文件
 */
function delfile() {
	if(gv_select_row != null) {
		if(gv_cookie_userid == 1) {
			$.ajax({
		         url:  '../tztfDoc/f/delete.do?id='+gv_select_row.id,
		         type: "POST",
		         dataType : "json",
		         success:function(result) {
		        	if(result.resultTypeID == 0) {
		        		alert('文件【'+gv_select_row.oldName+'】已删除');
		        		$('#tab').bootstrapTable('remove',{
		        			field:'id',
		        			values:[parseInt(gv_select_row.id)]
		        		});
		        		gv_select_row = null;
		        	}
		         }
	   	 });
		}else {
			alert('没有权限');
		}
		
	}else {
		alert('请选择一行数据');
	}
};

/*获取到Url里面的参数*/
(function ($) {
  $.getUrlParam = function (name) {
   var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
   var r = window.location.search.substr(1).match(reg);
   if (r != null) return unescape(r[2]); return null;
  };
 })(jQuery);


function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
    return currentdate;
}
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