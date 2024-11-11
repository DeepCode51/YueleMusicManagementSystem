//弹窗提示
var resultInfo = {
    ok: function (content) {
        top.layer.msg(content, {icon: 1});
    },
    warn: function (content) {
        top.layer.msg(content, {icon: 5});
    },
    error: function (content) {
        top.layer.alert(content, {icon: 2});
    }
};
//页面相关
var page = {
    closeCurrentPage: function () {
        var indexFrame = parent.layer.getFrameIndex(window.name); //获取窗口索引
        top.layer.close(indexFrame); //关闭当前窗口
    },
    reloadListData: function () {
        var showTabWin = winIframe.getShowTabWin();
        showTabWin.reloadList();
    }
}
//延迟下载
setTimeout(function () {
    ajaxSetup()
},1000)
function ajaxSetup(){
    $.ajaxSetup({
        complete:function(XMLHttpRequest,textStatus){
            if(textStatus=="parsererror"){//没有权限也是这个错误
                top.layer.alert('响应错误,请检查后再重试！', {
                    //skin: 'layui-layer-molv', //样式类名
                    closeBtn: 0
                });
            } else if(textStatus=="error"){
                top.layer.alert('请求错误,请检查后再重试！', {
                    //skin: 'layui-layer-molv', //样式类名
                    closeBtn: 0
                });
            }
        }
    });
}
//弹窗相关
var open = {
    save: function (title, url, width, height, flag) {
        top.layer.open({
            title: title,
            type: 2,
            maxmin: true, //开启最大化最小化按钮
            area: [width == undefined ? 900 : width, height == undefined ? 600 : height],//宽,高
            content: url,
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                var iframeWin = top.window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                iframeWin.subForm(flag);//关闭弹窗由新增或者编辑页面保存后进行关闭
            },
            btn2: function (index, layero) {
                return true;//取消按钮
            },
            cancel: function (index, layero) {
                return true;//关闭按钮
            }
        });
    },
    view: function (title, url, width, height) {
        top.layer.open({
            title: title,
            type: 2,
            maxmin: true, //开启最大化最小化按钮
            area: [width == undefined ? 900 : width, height == undefined ? 600 : height],//宽,高
            content: url,
            btn: ['关闭'],
            yes: function (index, layero) {
                top.layer.close(index);
            }
        });
    },
    del: function (title, url,successCallBack) {
        top.layer.confirm("确认删除该数据吗？", {title: "删除提醒", icon: 3}, function (index) {
            $.ajax({
                type: 'post',
                url: url,
                dataType: 'json',//后台需要返回json字符串
                success: function (data) {
                    if (data.isOk) {
                        resultInfo.ok(data.info);
                        reloadList();//刷新列表页面数据
                        if(successCallBack){
                            successCallBack(data);//删除成功后回调
                        }
                    } else {
                        resultInfo.error(data.info);
                    }
                },
                error: function (data, textstatus, errObj) {
                    errorHandle(data, textstatus, errObj, url);
                }
            });
            top.layer.close(index);
        });
    },
    doPost: function (title, url,successCallBack) {
        top.layer.confirm(title, {title: "操作提醒", icon: 3}, function (index) {
            $.ajax({
                type: 'post',
                url: url,
                dataType: 'json',//后台需要返回json字符串
                success: function (data) {
                    if (data.isOk) {
                        resultInfo.ok(data.info);
                        reloadList();//刷新列表页面数据
                        if(successCallBack){
                            successCallBack(data);//删除成功后回调
                        }
                    } else {
                        resultInfo.error(data.info);
                    }
                },
                error: function (data, textstatus, errObj) {
                    errorHandle(data, textstatus, errObj, url);
                }
            });
            top.layer.close(index);
        });
    },
    delAll: function (title, url,successCallBack) {
        var checkStatus = table.checkStatus('dataList');
        var rows = checkStatus.data;
        if (rows.length < 1) {
            resultInfo.error("至少选择一条数据！");
            return;
        }
        top.layer.confirm("确认删除勾选的数据吗？", {title: "删除提醒", icon: 3}, function (index) {
            var idsArr = rows.map(function (v) {
                return v.id;
            });
            var ids = idsArr.join(",");
            $.ajax({
                type: 'post',
                url: url + "?ids=" + ids,
                dataType: 'json',//后台需要返回json字符串
                success: function (data) {
                    if (data.isOk) {
                        resultInfo.ok(data.info);
                        reloadList();//刷新列表页面数据
                        if(successCallBack){
                            successCallBack(data);//删除成功后回调
                        }
                    } else {
                        resultInfo.error(data.info);
                    }
                }
            });
            top.layer.close(index);
        });
    }
};
//请求相关
var request = {
    doAjaxPost: function (url, dataParams, successCallBack, errorCallBack) {
        var load_index;
        $.ajax({
            url: url,
            async: true,
            type: "post",
            data: dataParams, // 参数放这里
            dataType: "json",
            cache: false,
            beforeSend: function (XMLHttpRequest) {
            },
            // 成功是执行此方法
            success: function (data, textstatus) {
                successHandle(data, textstatus, successCallBack, errorCallBack);
            },
            // 错误信息被捕捉到这里（ActionInterceptor已经统一拦截了异常，正常是不会进来，
            // 还有别的错误会触发这个方法， 比如url地址错误等）
            error: function (data, textstatus, errObj) {
                errorHandle(data, textstatus, errObj, url);
            }
        });
    },
    doAjaxPostSynch: function (url, dataParams, successCallBack, errorCallBack) {
        $.ajax({
            url: url,
            async: false,
            type: "post",
            data: dataParams, // 参数放这里
            dataType: "json",
            cache: false,
            // 成功是执行此方法
            success: function (data, textstatus) {
                successHandle(data, textstatus, successCallBack, errorCallBack);
            },
            // 错误信息被捕捉到这里（ActionInterceptor已经统一拦截了异常，正常是不会进来，
            // 还有别的错误会触发这个方法， 比如url地址错误等）
            error: function (data, textstatus, errObj) {
                errorHandle(data, textstatus, errObj, url);
            }
        });
    }
}

function successHandle(data, textstatus, successCallBack, errorCallBack) {
    if (data.isOk && successCallBack) {//成功
        successCallBack(data, textstatus);
    } else if (data instanceof Array && successCallBack) {//成功,数组
        successCallBack(data, textstatus);
    } else if (errorCallBack) {
        errorCallBack(data, textstatus);
    } else {
        if (data.info) {
            if (data.type == "ok") {
                top.layer.alert(data.info, {icon: 1});
            } else if (data.type == "warn") {
                top.layer.msg(data.info, {icon: 5});
            } else {
                top.layer.alert(data.info, {icon: 2});
            }
        } else {
            top.layer.alert("请求失败！", {icon: 2});
        }
    }
}

function errorHandle(data, textstatus, errObj, url) {
    if (data) {
        var errorInfo;
        if (data.statusText == 'ok') {
            errorInfo = data.responseText;
            top.layer.alert('操作失败！' + errorInfo, {icon: 5});
        }
        if (data.statusText == 'abort') {//用户取消请求
            return;
        } else if (data.statusText == 'Not Found') {
            errorInfo = '此请求地址[' + url + ']无效:';
            top.layer.alert('操作失败！' + errorInfo, {icon: 5});
        } else if (!(textstatus == "parsererror" || textstatus == "error")) {//这部分交给complete处理
            var detailErrorInfo = data.responseText == null ? "" : data.responseText;
            var maxMsgSize = 1000;
            errorInfo = detailErrorInfo.substr(0, maxMsgSize);
            errorInfo = '请求地址[' + url + ']出错,textstatus=' + textstatus + ',错误信息:' + errorInfo;
            if (detailErrorInfo.length > maxMsgSize) {
                errorInfo += "...";
            }
            top.layer.alert('操作失败！' + errorInfo, {icon: 5});
        }
    } else {
        top.layer.alert('操作失败！请联系管理人员！错误信息：' + textstatus + errObj, {icon: 5});
    }
}

//验证相关
var validForm = {
    validFieldUnique: function (params, tableName, id) {
        var validResult;
        request.doAjaxPostSynch(ctx + "/sys/common/unique", {
                fields: params,
                tableName: tableName,
                id: id
            },
            function success(data) {//操作成功
                if (data.isOk) {
                    validResult = data.info
                }
            },
            function error(data) {//操作成功
                //不处理
            }
        );
        return validResult
    },
    validFieldCode: function (value) {
        var validResult;
        var reg = /^\w+$/;// 由数字、26个英文字母或者下划线组成的字符串
        if (reg.test(value) != true) {
            validResult ="只能由英文、数字或者下划线组成";
        }
        return validResult
    },
    validFieldNumber: function (value) {
        var validResult;
        var reg = /^([1-9]?\d|100)$/;// 由数字、26个英文字母或者下划线组成的字符串
        if (reg.test(value) != true) {
            validResult ="只能输入 0～100 的数字";
        }
        return validResult
    }
};
//定义工具集合
var tools = {
    getImgUrl:function (url) {
        if(url.indexOf('http')>-1){
            return url
        }else{
            return fileBasePath+'/'+url
        }
    },
    selectInput: function (elem, url, placeholder, defaultValue,disabled,clickCallBack) {
        treeSelect.render({  // 编辑页面初始化数据需要 : lay-filter 属性 并且无需给value赋值
            // 选择器
            elem: "#" + elem,
            data: url,
            type: 'get',
            key: {id: 'id'},
            placeholder: placeholder,
            // 是否开启搜索功能：true/false，默认false
            search: true,
            style: {
                disabled:disabled,
                folder: {
                    enable: true
                },
                line: {
                    enable: false
                }
            },
            click: function (d) {
                var id = d.current.id;
                if (clickCallBack) {
                    clickCallBack(id);
                }
                console.log(d);
            },
            // 加载完成后的回调函数
            success: function (d) {
                if(defaultValue){
                    treeSelect.checkNode(elem, defaultValue);
                    treeSelect.refresh(elem);
                }
                //var treeObj = treeSelect.zTree(elem);
            }
        });
    }
}

//获取当前选中的tab内容窗口
var winIframe = {
    getShowTabWin: function () {
        var showTab;
        top.$('#mainFrame_tab').children().each(function () {
            var attrName = $(this).attr("class");
            if (attrName.indexOf("layui-show") != -1) {
                try {
                    showTab = $(this).find('iframe')[0].contentWindow;
                } catch (e) {
                    console.log("当前tab是目录")
                }
            }
        });
        return showTab;
    }
}




