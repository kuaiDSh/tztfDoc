/*
 * 审核id
 */
var gv_auditID;
/*
 * 菜单id
 */
var gv_menuID;
/*
 * 文件id
 */
var gv_fileID;
/*
 * 用户id
 */
var gv_userid;
/*
 * 通知id
 */
var gv_noticeID;
/*
 * 通知人id
 */
var gv_noticeUser;
/*
 * 下一个节点顺序
 */
var nextIndex;
/*
 * 判断当前登录用户是否为下一个审核人之一  true是其中之一
 */
var boolean_flag = false;
$(function(){
	$('#pdf_div').hide();
	$('#no_div').hide();
	gv_auditID = $.getUrlParam('a');
	gv_menuID = $.getUrlParam('m');
	gv_fileID = $.getUrlParam('f');
	gv_noticeID = $.getUrlParam('n');
	gv_noticeUser = $.getUrlParam('u');
	gv_userid = cookie.get("userID");
	if(gv_auditID == null || gv_menuID == null || gv_fileID == null) {
		alert('url错误');
		$('#btn_ok').prop('disabled',true);
		$('#yulan').prop('disabled',true);
	}else {
		init();
		auditinit();
		/*
		 * 更改通知
		 * 1.判断当前用户是否为收到通知用户
		 * 
		 */
		if(gv_noticeUser == gv_userid) {
			//当前用户为收到通知用户
			if(!boolean_flag) {
				//不是下一个审核人、进入页面即修改通知状态
				updateNotice();
			}
		}else {
			//不是同一人不管
		}
	}

});
/**
 * 初始化文件数据
 */
function init(){
	$('#tab').bootstrapTable({
		singleSelect:true,
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        columns: [ {
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
                	return '审核未通过';
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
            field: 'downNum',
            title: '下载次数'
        }
        ]
	});
	 $.ajax({
         url:  '../tztfDoc/f/s.do',
         type: "POST",
         dataType : "json",
         data:JSON.stringify({type:'id',id:parseInt(gv_fileID)}),
         success:function(r) {
        	 	if(r.resultTypeID == 0) {
	        		 $('#tab').bootstrapTable('load',r.data);  
        	 	}
        	 }
         });
};
///**
// * 盖章
// */
//function addimg() {
//	 $.ajax({
//         url:  '../tztfDoc/f/addimg.do?id='+parseInt(gv_fileID),
//         type: "POST",
//         dataType : "json",
//         success:function(r) {
//        	 	
//        	 }
//         });
//}
/**
 * 审核相关初始化
 */
function auditinit() {
	$.ajax({
        url:  '../tztfDoc/a/s.do',
        type: "POST",
        dataType : "json",
        data:JSON.stringify({menu:parseInt(gv_menuID)}),
        async:false,
        success:function(r) {
        	if(r.resultTypeID == 0) {
        		$('#h2').text(r.data.audit.name);
//        		r.data.item
        		$.ajax({
        	        url:  '../tztfDoc/a/gr.do',
        	        type: "POST",
        	        dataType : "json",
        	        async:false,
        	        data:JSON.stringify({menuid:parseInt(gv_menuID),fileid:parseInt(gv_fileID)}),
        	        success:function(result) {
        	        	if(result.resultTypeID == 0) {
        	        		if(result.data.length !=0) {
        	        			//最新审核节点顺序
        	        			var lastindex = result.data[result.data.length-1].fIndex;
        	        			//下一个审核节点
        	        			nextIndex = lastindex+1;
        	        			//最大审核节点
        	        			var maxindex = r.data.item[r.data.item.length-1].index;
        	        			//下一步审核人
        	        			var next_auditor;
        	        			for(var i in r.data.item) {
        	        				if(nextIndex == r.data.item[i].index) {
        	        					next_auditor = r.data.item[i].auditor;
        	        					break;
        	        				}
        	        			}
        	        			var boolean_state = false;
        	        			if(result.data[result.data.length-1].auditState == 0) {
        	        				boolean_state = true;
        	        			}else {
        	        				boolean_state = false;
        	        			}
        	        			
        	        			if(next_auditor != undefined) {
        	        				//判断当前登录账户是否为下一步审核人
        	        				if(next_auditor.indexOf(gv_userid) > -1) {
        	        					boolean_flag = true;
        	        				}else {
        	        					boolean_flag = false;
        	        				}
        	        			}else {
        	        				boolean_flag = false;
        	        			}
        	        			for(var i in r.data.item) {
                        			var obj = r.data.item[i];
                        			//节点顺序
                        			var index = obj.index;
                        			var itemName = obj.name;
                        			if(lastindex == 0 && boolean_flag && boolean_state) {
                        				$('#auditbody').append(
                        					'<h3>'+itemName+'</h3><span class="label label-info">审核意见</span><div style="height: 5px;"></div>'+
                        					'<textarea class="form-control" id="content" rows="5" name=textarea style="width: 400px;"></textarea>'+
                        					'<div style="margin-top: 5px;">'+
                        					'<label class="radio-inline">'+
                        					'<input type="radio" name="radio1" id="optionsRadios3" value="0" > 同意</label>'+
                        					'<label class="radio-inline">'+
                        					'<input type="radio" name="radio1" id="optionsRadios4"  value="1"> 不同意</label>'+
//                        					'<label class="radio-inline">'+
//                        					'<input type="radio" name="radio1" id="optionsRadios5"  value="2"> 驳回</label>'+
                        					'</div>'
                        				);
                        				break;
                        			}else if(lastindex < maxindex){
                        				if((index == lastindex+1) && boolean_flag && boolean_state) {
                        					$('#auditbody').append(
                                					'<h3>'+itemName+'</h3><span class="label label-info">审核意见</span><div style="height: 5px;"></div>'+
                                					'<textarea class="form-control" id="content" rows="5" name=textarea style="width: 400px;"></textarea>'+
                                					'<div style="margin-top: 5px;">'+
                                					'<label class="radio-inline">'+
                                					'<input type="radio" name="radio1" id="radio0" value="0" > 同意</label>'+
                                					'<label class="radio-inline">'+
                                					'<input type="radio" name="radio1" id="radio1"  value="1"> 不同意</label>'+
//                                					'<label class="radio-inline">'+
//                                					'<input type="radio" name="radio1" id="radio2"  value="2"> 驳回</label>'+
                                					'</div>'
                                				);
                                				break;
                        				}else {
                        					for(var j in result.data) {
                        						var objr = result.data[j];
                        						var objindex = objr.fIndex;
                        						if(index == objindex) {
                        							$('#auditbody').append(
                                        					'<h3>'+itemName+'</h3><span class="label label-info">审核意见</span><div style="height: 5px;"></div>'+
                                        					'<textarea class="form-control" rows="5" disabled="disabled" name=textarea style="width: 400px;">'+objr.aditContent+'</textarea>'+
                                        					'<div style="margin-top: 5px;">'+
                                        					'<label class="radio-inline">'+
                                        					'<input type="radio" name="radio1'+index+'" id="radio1'+index+'" value="0" > 同意</label>'+
                                        					'<label class="radio-inline">'+
                                        					'<input type="radio" name="radio1'+index+'" id="radio2'+index+'"  value="1"> 不同意</label>'+
//                                        					'<label class="radio-inline">'+
//                                        					'<input type="radio" name="radio1'+index+'" id="radio3'+index+'"  value="2"> 驳回</label>'+
                                        					'</div>'+
                                        					'<div class="container" style="width:400px;margin-left:0px;">'+
                                        						'<div class="row">'+
                                        							'<div class="col-xs-6">'+
                                        							'<span class="label label-info">审核人</span><div style="height: 5px;"></div>'+
                                                					'<input type="text" id="user'+index+'"  class="form-control" disabled="disabled">'+
                                        							'</div>'+
                                        							'<div class="col-xs-6">'+
                                        							'<span class="label label-info">审核时间</span><div style="height: 5px;"></div>'+
                                                					'<input type="text" id="time'+index+'"  class="form-control" disabled="disabled">'+
                                        							'</div>'+
                                        						'</div>'+
                                        					'</div>'
//                                        					'<span class="label label-info">审核人</span><div style="height: 5px;"></div>'+
//                                        					'<input type="text" id="user'+index+'" style="width: 400px;" class="form-control" disabled="disabled">'+
//                                        					'<span class="label label-info">审核时间</span><div style="height: 5px;"></div>'+
//                                        					'<input type="text" id="time'+index+'" style="width: 400px;" class="form-control" disabled="disabled">'
                                        			)
                                        			$('#user'+index).val(objr.auditor);
                        							$('#time'+index).val(objr.auditDate);
                        							if(objr.auditState == 0) {
                        								$('#radio1'+index).attr('checked',true);
                        							}else if(objr.auditState == 1) {
                        								$('#radio2'+index).attr('checked',true);
                        							}else if(objr.auditState == 2) {
                        								$('#radio3'+index).attr('checked',true);
                        							}
                        						}
                        					}
                        					
                        				}
                        			}else {
                        				for(var j in result.data) {
                    						var objr = result.data[j];
                    						var objindex = objr.fIndex;
                    						if(index == objindex) {
                    							$('#auditbody').append(
                                    					'<h3>'+itemName+'</h3><span class="label label-info">审核意见</span><div style="height: 5px;"></div>'+
                                    					'<textarea class="form-control"  disabled="disabled" rows="5" name=textarea style="width: 400px;">'+objr.aditContent+'</textarea>'+
                                    					'<div style="margin-top: 5px;">'+
                                    					'<label class="radio-inline">'+
                                    					'<input type="radio" name="radio1'+index+'" id="radio1'+index+'" value="0" > 同意</label>'+
                                    					'<label class="radio-inline">'+
                                    					'<input type="radio" name="radio1'+index+'" id="radio2'+index+'"  value="1"> 不同意</label>'+
//                                    					'<label class="radio-inline">'+
//                                    					'<input type="radio" name="radio1'+index+'" id="radio3'+index+'"  value="2"> 驳回</label>'+
                                    					'</div>'+
                                    					'<div class="container" style="width:400px;margin-left:0px;">'+
                                						'<div class="row">'+
                                							'<div class="col-xs-6">'+
                                							'<span class="label label-info">审核人</span><div style="height: 5px;"></div>'+
                                        					'<input type="text" id="user'+index+'"  class="form-control" disabled="disabled">'+
                                							'</div>'+
                                							'<div class="col-xs-6">'+
                                							'<span class="label label-info">审核时间</span><div style="height: 5px;"></div>'+
                                        					'<input type="text" id="time'+index+'"  class="form-control" disabled="disabled">'+
                                							'</div>'+
                                						'</div>'+
                                					'</div>'
//                                    					'<span class="label label-info">审核人</span><div style="height: 5px;"></div>'+
//                                    					'<input type="text" id="user'+index+'" style="width: 400px;" class="form-control" disabled="disabled">'+
//                                    					'<span class="label label-info">审核时间</span><div style="height: 5px;"></div>'+
//                                    					'<input type="text" id="time'+index+'" style="width: 400px;" class="form-control" disabled="disabled">'
                                    			)
                                    			$('#user'+index).val(objr.auditor);
                    							$('#time'+index).val(objr.auditDate);
                    							if(objr.auditState == 0) {
                    								$('#radio1'+index).attr('checked',true);
                    							}else if(objr.auditState == 1) {
                    								$('#radio2'+index).attr('checked',true);
                    							}else if(objr.auditState == 2) {
                    								$('#radio3'+index).attr('checked',true);
                    							}
                    						}
                    					}
                        				$('#btn_ok').prop('disabled',true);
                        			}
                        			
                        		}
        	        			if(!boolean_flag) {
        	        				$('#btn_ok').prop('disabled',true);
        	        			}
        	        			if(!boolean_state) {
        	        				$('#btn_ok').prop('disabled',true);
        	        			}
        	        		}else {
        	        			alert('该文件未送审');
        	        		}
        	        		
        	        	}else {
        	        		alert('获取审核记录失败');
        	        	}
        	        	
        	        	
        	        	
        	       	 }
        	        });
        	}
       	 }
        });
	
};


/**
 * 审核确认
 */
function auditok() {
	if($('input[name="radio1"]:checked').val() != undefined) {
		var ajaxdata = {'auditID':parseInt(gv_auditID),'menuID':parseInt(gv_menuID),'fileID':parseInt(gv_fileID),
				 'auditor':gv_userid,'fIndex':nextIndex,'auditDate':getNowFormatDate(),
				 'aditContent':$('#content').val(),'auditState':$('input[name="radio1"]:checked').val()};
		 $.ajax({
	         url:  '../tztfDoc/a/ir.do',
	         type: "POST",
	         dataType : "json",
	         data:JSON.stringify(ajaxdata),
	         success:function(result) {
	        	if(result.resultTypeID == 0) {
	        		alert('审核已提交');
	        		if(boolean_flag) {
	        			if(gv_noticeUser == gv_userid) {
	        				updateNotice();
	        			}
	        		}
	        	}
	         }
    	 });
	}else {
		alert('不能提交');
	}
	
}
/**
 * 修改通知状态
 */
function updateNotice() {
	 $.ajax({
         url:  '../tztfDoc/n/update.do?id='+gv_noticeID,
         type: "POST",
         dataType : "json",
         success:function(r) {
        	 	
        	 }
         });
}







var flag = false;
/**
 * 预览
 */
function pdf() {
	$('#pdf_div').show();
	$('#no_div').show();
	if(!flag) {
		var url="../tztfDoc/f/load.do?id="+parseInt(gv_fileID);//这里就可以做url动态切换--主要是使用iframe
	    $("#pdf_page").attr("src",url);  
	    $(".pdf").media();
	}
	 
}

function nopdf() {
	$('#pdf_div').hide();
	$('#no_div').hide();
	flag = true;
}



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





