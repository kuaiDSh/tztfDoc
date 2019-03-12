$(function(){
	gv_userID = cookie.get("userID");
	init();
//	btn(1);
//	 window.setTimeout(btn(1),1000);
	
});
$(document).ready(function(){
	 window.setInterval(btn(1), 1000);
	 window.setInterval(a(), 1000);
});

function a() {
	console.log(1);
}
/*
 * 用户id
 */
var gv_userID;
/*
 * 选择行
 */
var gv_row = null;
function init() {
	$('#tab').bootstrapTable({
		singleSelect:true,
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        clickToSelect: true,                //是否启用点击选中行
//        height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        columns: [ 
                   {
            field: 'id',
            title: 'id',
            visible:false
        }, {
            field: 'count',
            title: '通知内容'
        },  {
            field: 'url',
            title: '地址',
            visible:false
        },
        {
            field: 'state',
            title: '状态',
            formatter: function (value, row, index) {
                if (value == '0' || value == 0) {
                    return "已浏览";
                } else if (value == '1' || value == 1){
                    return "未浏览";
                }else {
                	return value;
                }
            }
        }
        ],
        onClickRow:function(row,$element,field) {
        	$('.info').removeClass('info');//移除class
            $($element).addClass('info');//添加class
            gv_row = row;
        },
        onDblClickRow:function(row,$element,field) {
        	$('.info').removeClass('info');//移除class
            $($element).addClass('info');//添加class
//            window.open('a.html?id='+row.id);
//            gv_select_row = row;
        	console.log(row);
            if(row.url.indexOf('html') > -1) {
            	window.open(row.url+'&n='+row.id+'&u='+row.user);
            }else{
            	$('#noticeModal').modal('show');
            	$('#span_notice').text(row.count);
            	$('#noticeModal').on('hide.bs.modal', function () {
            		if(row.state == 1) {
            			$.ajax({
               	         url:  '../tztfDoc/n/update.do?id='+row.id,
               	         type: "POST",
               	         dataType : "json",
               	         success:function(r) {
               	        	 	
               	        	 }
               	         });
            		}
            		
            });
            }
        }
	});
}
/**
 * 按钮事件
 * @param type
 */
function btn(type) {
	console.log(type);
	var ajaxdata = {user:parseInt(gv_userID),state:parseInt(type)};
	$.ajax({
		 url:  '../tztfDoc/n/get.do',
       type: "POST",
       dataType : "json",
       data:JSON.stringify(ajaxdata),
       success:function(result){
    	   gv_row = null;
    	   $('#tab').bootstrapTable('load',[]);
      	 if(result.resultTypeID == 0) {
      		 console.log(result.data);
      		$('#tab').bootstrapTable('load',result.data);
      	 }else {
      		
      	 }
       }
	});
}

/**
 * 删除通知信息
 */
function del() {
	if(gv_row != null) {
		$('#delmodal').modal('show');
		if(gv_row.state == 1) {
			$('#del_span').text('该通知【'+gv_row.count+'】您还未查看或完成，是否删除？');
		}else {
			$('#del_span').text('确定删除通知【'+gv_row.count+'】？');
		}
		
	}else {
		alert('请选择一行数据');
	}
}
/**
 * 删除确定
 */
function del_ok() {
	$.ajax({
	url:  '../tztfDoc/n/del.do?id='+gv_row.id,
      type: "POST",
      dataType : "json",
      success:function(result){
   	   	if(result.resultTypeID == 0) {
   	   		//删除成功
   	   	$('#delmodal').modal('hide');
   	   	$('#tab').bootstrapTable('remove',{
			field:'id',
			values:[parseInt(gv_row.id)]
		});
   	   	}
      }
	});
}















