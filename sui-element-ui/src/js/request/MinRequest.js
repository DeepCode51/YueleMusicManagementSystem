import axios from 'axios';
import qs from 'qs'
const config = Symbol('config')
const isCompleteURL = Symbol('isCompleteURL')
const requestBefore = Symbol('requestBefore')
const requestAfter = Symbol('requestAfter')

class MinRequest {
  [config] = {
    header: {
      'content-type': 'application/x-www-form-urlencoded; charset=UTF-8'//配合qs
      // 'content-type': 'application/json'
    },
    dataType: 'json',
    responseType: 'text'
  }
  interceptors = {
    request: (func) => {
      if (func) {
        MinRequest[requestBefore] = func
      } else {
        MinRequest[requestBefore] = (request) => request
      }
    },
    response: (func) => {
      if (func) {
        MinRequest[requestAfter] = func
      } else {
        MinRequest[requestAfter] = (response) => response
      }
    }
  }

  static [requestBefore](config) {
    return config
  }

  static [requestAfter](response) {
    return response
  }

  static [isCompleteURL](url) {
    return /(http|https):\/\/([\w.]+\/?)\S*/.test(url)
  }

  setConfig(func) {
    this[config] = func(this[config])
  }

  request(options = {}) {
    options.baseURL = options.baseURL || this[config].baseURL
    options.dataType = options.dataType || this[config].dataType
    options.url = MinRequest[isCompleteURL](options.url) ? options.url : (options.baseURL + options.url)
    options.params = options.data
    //数据进行转换,复杂对象处理:'content-type': 'application/x-www-form-urlencoded; charset=UTF-8'
    options.data = qs.stringify(options.data,{allowDots : true,skipNulls: true});//post接受需要qs加持：不然后端得用注解接收
    options.header = {...options.header, ...this[config].header}
    options.method = options.method || this[config].method
    options = {...options, ...MinRequest[requestBefore](options)}

    return new Promise((resolve, reject) => {
      let requestParams = {
        method: options.method,
        url: options.url,
        dataType: options.dataType,
        headers: options.header,
      };
      if (options.method == 'GET') {
        requestParams.params = options.params
        requestParams.paramsSerializer = function (p) { //固定写法
          return qs.stringify(p, {arrayFormat: 'repeat'})
        }
      } else {
        requestParams.data = options.data
      }
      axios(requestParams).then(res => {
        resolve(MinRequest[requestAfter](res.data))
      }).catch(err => {
        //提示弹窗
        reject(MinRequest[requestAfter](err))
      });
    });
  }

  get(url, data, options = {}) {
    if(sessionStorage.getItem("token")){
      this[config].header.token=sessionStorage.getItem("token");
    }else{
      delete this[config].header['token'];
    }
    options.url = url
    options.data = data
    options.method = 'GET'
    return this.request(options)
  }

  post(url, data, options = {}) {
    if(sessionStorage.getItem("token")){
      this[config].header.token=sessionStorage.getItem("token");
    }else{
      delete this[config].header['token'];
    }
    options.url = url
    options.data = data
    options.method = 'POST'
    return this.request(options)
  }
}

MinRequest.install = function (Vue) {
  Vue.mixin({
    beforeCreate: function () {
      if (this.$options.minRequest) {
        console.log("api:", this.$options.minRequest);
        Vue._minRequest = this.$options.minRequest
      }
    }
  })
  Object.defineProperty(Vue.prototype, '$request', {
    get: function () {
      return Vue._minRequest
    }
  })
}

export default MinRequest
