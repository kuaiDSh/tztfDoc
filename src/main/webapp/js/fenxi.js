$(function(){
	init();
});
function init() {
	$('#tab').bootstrapTable({
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
            field: 'name',
            title: '文档名称'
        },  {
            field: 'content',
            title: '文档备注'
        },
        {
        	field:'doneTime',
        	title:'完成时间',
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
        },
        {
        	field:'done',
        	title:'完成情况'
        	
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
//            select_row = row;
//        	console.log(gv_select_id);
        },
        onDblClickRow:function(row,$element,field) {
        	$('.info').removeClass('info');//移除class
            $($element).addClass('info');//添加class
            $('#fenximodal').modal('show');
            $('#fenxititle').text('(归档区)'+row.name);
            $('#iframe').attr('src','upload.html?m='+row.id+'&s=2');
        }
	});
	$.ajax({
		url:  '../tztfDoc/m/fenxi.do',
        type: "POST",
        dataType : "json",
        success:function(result){
        	$('#tab').bootstrapTable('load',result.data);
        }
	});
}


