/**
 * LoadPage
 */

(function(window,document){  
    var loader = function(options){  
    	this.settings = $.extend({
            'title': '加载中...',
            'htmlTmp':'<div class="card loader">'
            			+'<div class="loader-container text-center color-white">'
            				+'<div><i class="fa fa-spinner fa-pulse fa-3x"></i></div>'
            				+'<div id="loadingtitle" style="padding-top:8px;"></div>'
            			+'</div>'
            			+'<div class="card-body">'
            				+'<div class="text-indent"></div>'
            				+'<div class="sub-title"></div>'
            				+'<div class="text-indent"></div>'
            			+'</div>',
            'container':'body'
            				
        }, options);
    };  
    loader.prototype = {
    		show:function(){
    			$(this.settings.htmlTmp).prependTo(this.settings.container);
    			$('#loadingtitle').text(this.settings.title);
    			$(".loader").height($(this.settings.container).height())
    		},
    		hide:function(){
    			$(".card").remove();
    			}
    };  
    window.loader = loader
}(window,document));  

function Loader(options) {
	return new window.loader(options);
}

//调用函数
function LoadPage(mainArea,url,paramJson) {
	var load = Loader({container:mainArea});
	load.show();
	 $(mainArea).load(url, paramJson||{},function(){
			load.hide();
			$(mainArea).show();
	 });
}
/*var loadPage = function(mainArea, url, paramJson) {
	var load = Loader({
		container :mainArea
	});
	load.show();
	$("'" + mainArea + "'").load(url, paramJson || {}, function() {
		load.hide();
	});
}*/