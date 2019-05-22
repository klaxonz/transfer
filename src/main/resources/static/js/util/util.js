
function transferFileSizeFormat(limit) {
    var size = "";
    if( limit < 0.1 * 1024 ){ //如果小于0.1KB转化成B
        size = limit.toFixed(2) + "B";
    }else if(limit < 0.1 * 1024 * 1024 ){//如果小于0.1MB转化成KB
        size = (limit / 1024).toFixed(2) + " KB";
    }else if(limit < 1024 * 1024 * 1024){ //如果小于1GB转化成MB
        size = (limit / (1024 * 1024)).toFixed(2) + " MB";
    }else{ //其他转化成GB
        size = (limit / (1024 * 1024 * 1024)).toFixed(2) + " GB";
    }

    var sizeStr = size + "";
    var len = sizeStr.indexOf("\.");
    var dec = sizeStr.substr(len + 1, 2);
    if(dec === "00"){//当小数点后为00时 去掉小数部分
        return sizeStr.substring(0,len) + sizeStr.substr(len + 3,2);
    }
    return sizeStr;

}

function transferDateTime (dateMillSec) {
    var date = new Date(dateMillSec);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    minute = minute < 10 ? ('0' + minute) : minute;
    return y + '-' + m + '-' + d+' '+h+':'+minute;
}
