$(function(){
	var gv_fileID = $.getUrlParam('id');;
	var url="../tztfDoc/f/load.do?id="+parseInt(gv_fileID);//这里就可以做url动态切换--主要是使用iframe
    $("#pdf_page").attr("src",url);  
    $(".pdf").media();
});





/*获取到Url里面的参数*/
(function ($) {
  $.getUrlParam = function (name) {
   var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
   var r = window.location.search.substr(1).match(reg);
   if (r != null) return unescape(r[2]); return null;
  };
 })(jQuery);