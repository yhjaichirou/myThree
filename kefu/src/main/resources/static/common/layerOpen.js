function layer_open(parameter,[width,height],title,getHtml,callback,position){
	var lay = layer.open({
  	  type: 1 // Page层类型
  	  ,area: [width, height]
  	  ,title: title
  	  ,shade: 0.6 // 遮罩透明度
  	  ,maxmin: true // 允许全屏最小化
  	  ,anim: 0 // 0-6的动画形式，-1不开启
  	  ,content: getHtml
  	  ,offset: position
  	  ,success:function(dom){
  		callback(dom);
  	  }
	});
	return lay;
}

function layer_msg(type,title){
	
	switch(type)
	{
	    case 1:// 黑色底框
	    	layer.msg(title, function(){
	    		// 关闭后的操作
	    	});
	        break;
	    case 2:// 白色底框
	    	layer.msg(title, {icon: 5, time: 1500});
	        break;
	    case 3:// 白色底框 OK
	    	layer.msg(title,{icon:1,anim:0, time: 1500});
	        break;
	    case 4:// 白色底框 FAIL
	    	layer.msg(title,{icon:5,anim:6,time: 1500});
	        break;
	    case 5:// 黑底选择框
	    	layer.msg(title,{
	    		  time: 0 // 不自动关闭
	    		  ,btn: ['必须啊', '丑到爆']
	    			,yes: function(index){
	    		    layer.close(index);
	    		    layer.msg('', {
	    		      icon: 6
	    		      ,btn: ['嗷','嗷','嗷']
	    		    });
	    		  }
	    	});
	        break;
	    default:
	    	layer.msg('参数不正确');
	}

	
}

function post(url,parameter,type,okCallBack,errorCallBack,completCallBack,dataType='json'){
	$.ajax({
		url: url,
		data: parameter,
		type: type,
		datatype:dataType,
		success: function(res) {
			okCallBack(res);
		}
		,error:function(res) {
			errorCallBack(res);
		}
		,complete:function(res) {
			var dataObj=eval("("+res+")");
			completCallBack(dataObj);
		}
	});
}
