$(function(){
	init();

//	$('#upimg').fileinput({
//		allowedFileExtensions: ['jpg', 'gif', 'png'],
//		uploadUrl: '../tztfDoc/f/upimg.do'
//	});
//	$("#upimg").on("upload", function (event, data, previewId, index){
//		console.log(1);
//	});
	$("#upimg").on("filebatchselected", function(event, files) {
		formData =  new FormData(); 
		formData.append("author", "hooyes"); // 可以增加表单数据
		formData.append("file", files[0]); // 文件对象
		console.log(formData);
	});
});
/*
 *文件对象 
 */
var formData = null;
/*
 * 表格单机行数据
 */
var select_row;


function init() {

	$('#tab').bootstrapTable({
		toolbar: '#toolbar',
		singleSelect:true,
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        clickToSelect: true,                //是否启用点击选中行
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        columns: [ 
                   {
            field: 'id',
            title: 'id',
            visible:false
        }, {
            field: 'username',
            title: '账号'
        },  {
            field: 'name',
            title: '姓名'
        },
        {
        	field:'dept',
        	title:'部门'
        },
        {
        	field:'phone',
        	title:'联系方式'
        },
        {
        	field:'path1',
        	title:'签名图片',
        	formatter:function(value,row,index){
                var s = '<a class = "view"  href="javascript:void(0)"><img style="width:70;height:30px;"  src="f/loadimg.do?p='+row.path+'" /></a>';
                return s;
            },
            events: 'operateEvents'
        },
        {
        	field:'path',
        	title:'图片名称',
        	visible:false
        }
//        ,
//        {
//            field: 'noaudit',
//            title: '反审核权限',
//            formatter: function (value, row, index) {
//                if (value == '0' || value == 0) {
//                    return "是";
//                } else if (value == '1' || value == 1){
//                    return "否";
//                }else {
//                	return value;
//                }
//            }
//        }
        ],
        onClickRow:function(row,$element,field) {
        	$('.info').removeClass('info');//移除class
            $($element).addClass('info');//添加class
            select_row = row;
//        	console.log(gv_select_id);
        },
        onDblClickRow:function(row,$element,field) {
        	$('.info').removeClass('info');//移除class
            $($element).addClass('info');//添加class
            select_row = row;
            update();
        }
	});
	
	$.ajax({
        url:  '../tztfDoc/u/sel.do',
        type: "POST",
        dataType : "json",
        success:function(r) {
       	 	
	       $('#tab').bootstrapTable('load',r.data);  
       	 }
        });
}
/**
 * 判断是否为新增还是修改
 */
var modalType = null;
/**
 * 新增用户
 */
function insert() {
	formData = null;
	clearmodal();
	$('#usermodal').modal('show');
	$('#usermodaltitle').text('用户新增');
	modalType = 'add';
	$('#upimg').fileinput('clear');
};
/**
 * 删除用户
 */
function deleteuser() {
	if(select_row != null) {
		if(select_row.id != 1) {
			$('#delmodal').modal('show');
			$('#del_span').text('是否确认删除用户【'+select_row.username+'--'+select_row.name+'】？');
		}else {
			alert('不可删除');
		}
	}else {
		alert('请选择一行数据');
	}
	
};
/**
 * 删除确定
 */
function del_ok() {
	showmsg();
	$.ajax({
        url:  '../tztfDoc/u/del.do?id='+select_row.id,
        type: "POST",
        dataType : "json",
        success:function(r) {
        	closemsg();
        	if(r.resultTypeID == 0) {
        		$('#tab').bootstrapTable('remove',{
        			field:'id',
        			values:[parseInt(r.data)]
        		});
        		$('#delmodal').modal('hide');
        	}else {
        		
        	}
       	 }
        });
};
/**
 * 用户修改
 */
function update() {
	if(select_row != null) {
		formData = null;
		$('#usermodal').modal('show');
		$('#usermodaltitle').text('用户修改');
		modalType = 'update';
		$('#upimg').fileinput('clear');
		setmodal(select_row);
	}else {
		alert("请选择一行数据");
	}
	
};
/**
 * 用户确定
 */
function userok() {
	var obj = getmodalval();
	if(obj.username != '' && obj.name != '') {
		showmsg();
		/*
		 * 文件地址
		 */
		var path = '';
		if(formData != null) {
			$.ajax({
		         url:  '../tztfDoc/f/upimg.do',
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
		         async:false,
		         success:function(result) {
		        	 if(result.resultTypeID == 0) {
		        		 console.log(result);
		        		 path = result.data;
		        	 }else {
		        		 path = '';
		        	 }
		         }
			 });
		}else {
			if(modalType == 'update' ) {
				path = select_row.path;
			}
		}
		
		obj.path = path;
		console.log(obj);
		if(modalType == 'add') {
			//新增用户
			 $.ajax({
    	         url:  '../tztfDoc/u/in.do',
    	         type: "POST",
    	         dataType : "json",
    	         data:JSON.stringify(obj),
    	         success:function(r) {
    	        	 closemsg();
    	        	 	if(r.resultTypeID == 0) {
    		        		 $('#tab').bootstrapTable('append',r.data);  
    		        		
    		        		 $('#usermodal').modal('hide');
    	        	 	}else {
    	        	 		alert(r.head);
    	        	 	}
    	        	 }
    	         });
		}else if(modalType == 'update') {
			obj.id=select_row.id;
			 $.ajax({
    	         url:  '../tztfDoc/u/update.do',
    	         type: "POST",
    	         dataType : "json",
    	         data:JSON.stringify(obj),
    	         success:function(r) {
    	        	 closemsg();
    	        	 	if(r.resultTypeID == 0) {
//    		        		 $('#tab').bootstrapTable('append',r.data);  
    		        		 $('#tab').bootstrapTable('updateByUniqueId', {
    		 					id: r.data.id,
    		 					row:r.data
    	        	 		});
    		        		
    		        		 $('#usermodal').modal('hide');
    	        	 	}else {
    	        	 		alert(r.head);
    	        	 	}
    	        	 }
    	         });
		}
	}else {
		alert('用户名和姓名不能为空');
	}
};
/**
 * 模态框赋值
 */
function setmodal(obj) {
	$('#username').val(obj.username);
	$('#dept').val(obj.dept);
	$('#phone').val(obj.phone);
	$('#name').val(obj.name);
//	$('#noaudit').val(obj.noaudit);
	if(obj.path != '' && obj.path != null) {
		$("#upimg").fileinput('destroy');  
		  $("#upimg").fileinput({
			  initialPreviewAsData: true,  
	             initialPreviewFileType: 'image',  
	             initialPreview:'f/loadimg.do?p='+obj.path , //要显示的图片的路径  
		  });
	}
};
/**
 * 清空模态框
 */
function clearmodal() {
	$('#username').val('');
	$('#dept').val('');
	$('#phone').val('');
	$('#name').val('');
//	$('#noaudit').val('1');
};
/**
 * 获取窗口数据
 */
function getmodalval() {
	var obj = {id:0,
			username:$('#username').val().trim(),
			password:'',
			dept:$('#dept').val(),
			phone:$('#phone').val(),
			name:$('#name').val().trim(),
			path:'',
			noaudit:0
	};
	return obj;
};

window.operateEvents = {
	    'click .view': function (e, value, row, index) {
	        layer.open({
	          type: 1,
	          title: false,
	          closeBtn: 0,
	          area: 'auto;',
	          skin: 'layui-layer-nobg', //没有背景色
	          shadeClose: true,
	          content: '<img src="f/loadimg.do?p='+row.path+'"/>'
	        });
	    },
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
