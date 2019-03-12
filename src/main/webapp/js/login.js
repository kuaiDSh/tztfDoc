

function login() {
	var obj = {'username':$('#username').val(),'password':hex_md5($('#password').val())};
	$.ajax({
		 url:  '../tztfDoc/u/login.do',
        type: "POST",
        dataType : "json",
        data:JSON.stringify(obj),
        success:function(result){
        	if(result.resultTypeID == 0) {
        		var id = result.data[0].id;
        		var name = result.data[0].name;
        		cookie.set('userID', id, 1);
//        		cookie.set('name', name, 1);
        		location.replace("index1.html");
        	}else{
        		alert('登录失败');
        	}	
        }
	})
}

