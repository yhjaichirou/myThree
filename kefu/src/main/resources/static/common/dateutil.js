var formatNumber = function(n) {
  n = n.toString()
  return n[1] ? n : '0' + n
}

//yhj
var dataTypeCycle = function(num){
	//周期类型（1:日 ， 2:月 ，3：季度 4:年,5：周）
	var str = '';
	switch(num){
	case 1:
		str = "每日"
		break;
	case 2:
		str = "每月"
		break;
	case 3:
		str = "每季度"
		break;
	case 4:
		str = "每年"
		break;
	case 5:
		str = "每周"
		break;
	}
	return str;
}
var getQuarterStr = function(){
	return ["春季","夏季","秋季","冬季"];
}
var getMonthsStr = function(){
	return ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"];
}
var getWeeksStr = function(){
	return ["星期一","星期二","星期三","星期四","星期五","星期六","星期日"];
}




var dateFormat = function(date) {

  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()

  var hour = date.getHours()
  var minute = date.getMinutes()
  var second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('-');
}

var dateFormatyMdhms = function(date) {

  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()

  var hour = date.getHours()
  var minute = date.getMinutes()
  var second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('-') + ' ' + [hour, minute, second].map(formatNumber).join(':');
}

function formatTime(date) {
  if (!!date) {
    if (!(date instanceof Date))
      date = new Date(date);
    var month = date.getMonth() + 1
    var day = date.getDate()
    return `${month}月${day}日`;
  }
}

function formatDay(date) {
  if (!!date) {
    var year = date.getFullYear()
    var month = date.getMonth() + 1
    var day = date.getDate()
    return [year, month, day].map(formatNumber).join('-');
  }
}

function formatDay2(date) {
  if (!!date) {
    var year = date.getFullYear()
    var month = date.getMonth() + 1
    var day = date.getDate()
    return [year, month, day].map(formatNumber).join('/');
  }
}

function formatWeek(date) {
  if (!!date) {
    var day = date.getDay();
    switch (day) {
      case 0:
        return '周日'
        break;
      case 1:
        return '周一'
        break;
      case 2:
        return '周二'
        break;
      case 3:
        return '周三'
        break;
      case 4:
        return '周四'
        break;
      case 5:
        return '周五'
        break;
      case 6:
        return '周六'
        break;
    }
  }
}

function formatHour(date) {
  if (!!date) {
    var hour = new Date(date).getHours();
    var minute = new Date(date).getMinutes();
    return [hour, minute].map(formatNumber).join(':');
  }
}

function timestamp(date, divisor = 1000) {
  if (date == undefined) {
    return;
  } else if (typeof date == 'number') {
    return Math.floor(date / divisor);
  } else if (typeof date == 'string') {
    var strs = date.split(/[^0-9]/);
    return Math.floor(+new Date(strs[0] || 0, (strs[1] || 0) - 1, strs[2] || 0, strs[3] || 0, strs[4] || 0, strs[5] || 0) / divisor);
  } else if (Date.prototype.isPrototypeOf(date)) {
    return Math.floor(+date / divisor);
  }
}

function detimestamp(date) {
  if (!!date) {
    return new Date(date * 1000);
  }
}


function dateDiff(startDate, endDate) {
  var start_date = new Date(startDate.replace(/-/g, "/"));
  var end_date = new Date(endDate.replace(/-/g, "/"));

  // //console.log(start_date);
  // //console.log(end_date);

  //时间差的毫秒数
  var dateDiff = end_date.getTime() - start_date.getTime();
  //计算出相差天数
  var dayDiff = Math.floor(dateDiff / (24 * 3600 * 1000));

  //计算天数后剩余的毫秒数
  var leave1 = dateDiff % (24 * 3600 * 1000);

  //计算出小时数
  var hours = Math.floor(leave1 / (3600 * 1000));


  //计算相差分钟数
  //计算小时数后剩余的毫秒数
  var leave2 = leave1 % (3600 * 1000)
  //计算相差分钟数
  var minutes = Math.floor(leave2 / (60 * 1000));


  //计算相差秒数
  var leave3 = leave2 % (60 * 1000) //计算分钟数后剩余的毫秒数
  var seconds = Math.round(leave3 / 1000)

  return dayDiff;
}

function add0(m) {
  return m < 10 ? '0' + m : m
}

function formatToDate(timestamp) {
  var time = new Date(timestamp);
  var y = time.getFullYear();
  var m = time.getMonth() + 1;
  var d = time.getDate();
  var h = time.getHours();
  var mm = time.getMinutes();
  var s = time.getSeconds();
  return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
}
function formatToDate2(timestamp) {
  var time = new Date();
  time.setTime(timestamp);
  var y = time.getFullYear();
  var m = time.getMonth() + 1;
  var d = time.getDate();
  var h = time.getHours();
  var mm = time.getMinutes();
  var s = time.getSeconds();
  return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
}
