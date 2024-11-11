import {Message, MessageBox} from 'element-ui';
import Vue from 'vue'

export default {
  //根据角标获取字典值
  getArrayValue(dict, index) {
    for (let i = 0; i < dict.length; i++) {
      if (i == index) {
        return dict[i].value;
        break;
      }
    }
  },
};
//获取图片完整路径
Vue.prototype.formatUrl = function (url) {
  if (url != null && url != undefined && url != "") {
    if (url.indexOf("http") > -1) {
      return url
    } else {
      let fileBasePath = this.$store.getters.config.fileBasePath;
      return fileBasePath + "/" + url
    }
  }
}

//过滤文本信息
Vue.prototype.formatText = function (content) {
  return content.replace(/<[^>]+>/g,"");  //正则去掉所有的html标记
},


//跳转页面
Vue.prototype.toPage = function (option) {
  this.$router.push(option)
}

//跳到页面顶部
Vue.prototype.scrollTop = function (option) {
  document.getElementsByClassName('front-page')[0].scrollTop = 0;
}

//清空对象值
Vue.prototype.resetForm = function (refForm) {
  if (refForm) {//添加prop属性的才会清除
    refForm.resetFields();
    this.resetFieldsValues(refForm._props.model)
  }
};
//重置对象
Vue.prototype.resetFieldsValues = function (obj) {
  for (let key in obj) {
    if (obj[key] instanceof Array) {
      obj[key] = [];
    }else{
      obj[key] = null;
    }
  }
  return obj
};
//去除前后空格
String.prototype.trim = function () {
  return this.replace(/(^[\s\n\t]+|[\s\n\t]+$)/g, "");
}

//替换所有
String.prototype.replaceAll = function (s1, s2) {
  return this.replace(new RegExp(s1, "gm"), s2);
}

//包含判断
Array.prototype.contain = function (e) {
  for (let i = 0; i < this.length; i++) {
    if (this[i] == e) {
      return true;
    }
  }
  return false;
}
// 克隆集合
Array.prototype.clone = function () {
  let a = [];
  for (let i = 0, l = this.length; i < l; i++) {
    a.push(this[i]);
  }
  return a;
}
//消息
Vue.prototype.showToast = function (title) {
  Message({
    message: title,
    center: true
  });
},
  //确认提示
  Vue.prototype.showConfirm = function (content, callBack) {
    MessageBox.confirm(content, '温馨提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: "warning"
    }).then(function () {
      if (callBack) {
        callBack()
      }
    })
  },
  //成功提示
  Vue.prototype.showSuccess = function (title) {
    Message({
      message: title,
      type: 'success',
      center: true
    });
  },
  //警告提示
  Vue.prototype.showWarn = function (title) {
    Message({
      message: title,
      type: 'warning',
      center: true
    });
  }
//错误提示
Vue.prototype.showError = function (content, callBack) {
  MessageBox.confirm(content, '错误提示', {
    confirmButtonText: '确定',
    type: "error"
  }).then(function () {
    if (callBack) {
      callBack()
    }
  })
},
//根据字典获取数据文本
Vue.prototype.getDictLabel = function (dict, value) {
  for (let i = 0; i < dict.length; i++) {
    if (dict[i].dictValue == value) {
      return dict[i].dictLabel
      break;
    }
  }
};
