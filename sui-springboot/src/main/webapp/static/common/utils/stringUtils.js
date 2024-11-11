function StringBuffer() {
    this.__strings__ = [];
};
StringBuffer.prototype.append = function(str) {
    this.__strings__.push(str);
};
StringBuffer.prototype.toString = function() {
    return this.__strings__.join('');

};
Date.prototype.format = function(format){
    //format="YYYY-MM-dd hh:mm:ss";
    var o = {
        "M+" :  this.getMonth()+1,  //month
        "d+" :  this.getDate(),     //day
        "h+" :  this.getHours(),    //hour
        "m+" :  this.getMinutes(),  //minute
        "s+" :  this.getSeconds(), //second
        "q+" :  Math.floor((this.getMonth()+3)/3),  //quarter
        "S"  :  this.getMilliseconds() //millisecond
    }

    if(/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }

    for(var k in o) {
        if(new RegExp("("+ k +")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
        }
    }
    return format;
}


function dateFormat(p,format) {
    var d = new Date(p);
    var date = {
        "M+": d.getMonth() + 1,
        "d+": d.getDate(),
        "h+": d.getHours(),
        "m+": d.getMinutes(),
        "s+": d.getSeconds(),
        "q+": Math.floor((d.getMonth() + 3) / 3),
        "S+": d.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (d.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
}

function idCardForm(idCard)
{
    return idCard.substring(0,6) + "********" + idCard.substring(14,idCard.length);
}

function phoneForm(phone)
{
    return phone.substring(0,3) + "****" + phone.substring(7,phone.length);
}

Array.prototype.contain = function (e) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == e)
            return true;
    }
    return false;
}
