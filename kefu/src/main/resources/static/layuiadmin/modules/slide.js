layui.define("element",function(exports){ //提示：模块也可以依赖其它模块，如：layui.define('layer', callback);
	var slide = function(navList){
			var element = layui.element;
			var slidehtml = "";
			for(var i=0;i<navList.length;i++){
				var child = "",inrrChild = navList[i]['children'];
				if(!!inrrChild && inrrChild.length> 0){
					var childhtml='';
					for(var j = 0;j< inrrChild.length;j++){
						childhtml +=`
								 <dd data-name="console">
									 <a href="javascript:;" lay-href="${contextPath}/${inrrChild[j].url}">
									 	<i class="layui-icon ${inrrChild[j].icon}"></i>${inrrChild[j].name}
									 </a>
								</dd>
						`
					}
					child= `
						  <dl class="layui-nav-child" >
						        ${childhtml}
						  </dl>
						 `
					
				}
//					<li ${i == 0? 'class="layui-nav-item  layui-nav-itemed"': 'class="layui-nav-item"'}>
				slidehtml += `
					<li class="layui-nav-item">
	                    <a href="javascript:;" class="firstList">
	                    	<i class="layui-icon ${navList[i]['icon']}"></i>
	                    	 <cite>${navList[i]['name']}</cite>
	                    </a>
	                        ${child}
                	</li>
	
					`;
			}
			$("#LAY-system-side-menu").html(slidehtml);
			element.render('nav'); //重新对导航进行渲染。注：layui 2.1.6 版本新增
			//一些事件监听
			element.on('tab(demo)', function(data){
				  console.log(data);
			});
	}
	exports('slide', slide);
}); 
