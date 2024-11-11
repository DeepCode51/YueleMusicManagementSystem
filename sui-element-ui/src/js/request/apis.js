import MinRequest from './MinRequest'
import $router from './../router'
import $store from './../vuex/store'
import {Loading,Message, MessageBox} from 'element-ui';

const minRequest = new MinRequest()
// 请求拦截器
minRequest.interceptors.request((request) => {
  return request
})
// 响应拦截器
minRequest.interceptors.response((response) => {
    if (!response.code) {//没存在code:跨域访问可能报错
      MessageBox.alert('访问异常，请检查后重试！', '系统提示', {
        confirmButtonText: '确定',
        type: 'error'
      });
    } else if (response.code == 400) {//错误提示
      Message({
        message: response.info,
        type: 'warning',
        center: true
      });
    } else if (response.code == 401) {//权限问题|session过期
      MessageBox.alert(response.info, '异常提示', {
        confirmButtonText: '确定',
        type: 'error',
        callback: action => {
          $store.dispatch('commitLoginOut').then(() => {
            $router.push("/login")
          });
        }
      });
    } else if (response.code == 402) {//token过期
      MessageBox.alert(response.info, '异常提示', {
        confirmButtonText: '确定',
        type: 'error',
        callback: action => {
          $store.dispatch('commitFrontLoginOut').then(() => {
            $router.push("/front/home")
          });
        }
      });
    }
    return response;
  },
  error => {
    return Promise.reject(error)
  })
// 设置默认配置:小程序端需要设置基础路径
const baseURL = "";
console.log(baseURL)
minRequest.setConfig((config) => {
  config.baseURL = baseURL;
  return config
})
export default {
  //get请求
  doGet(url, data) {
    return minRequest.get(url, data)
  },
  //post请求
  doPost(url, data) {
    return minRequest.post(url, data)
  },
  getToken(data) {
    return minRequest.get('/api/login/getToken', data)
  },
  //获取用户菜单
  getMenuUserTree(data) {
    return minRequest.post('/api/sys/getMenuUserTree', data)
  },
  //获取用户路由
  getUserRoutes(data) {
    return minRequest.post('/api/sys/getUserRoutes', data)
  },
  //获取数据字典
  getDictList(data) {
    return minRequest.post('/api/sys/getDictList', data)
  },
  //获取配置信息
  getConfig(data) {
    return minRequest.post('/api/sys/getAdminConfig', data)
  },
  //退出登录
  getLoginOut(data) {
    return minRequest.post('/admin/login/loginOut', data)
  },
  //获取用户数据
  getUserInfo(data) {
    return minRequest.post('/admin/login/getUserInfo', data)
  },
  //修改用户数据
  updateUserInfo(data) {
    return minRequest.post('/admin/sysUser/updateUserInfo', data)
  },
  //获取登录信息
  getLoginInfo(data) {
    return minRequest.post('/admin/login/getLoginInfo', data)
  },
  //获取文件历史
  getFileHistory(data) {
    return minRequest.get('/admin/sysAttach/getFileHistory?refId=' + data.refId)
  },
  //删除文件
  deleteFile(data) {
    return minRequest.get('/admin/sysAttach/deleteFile?id=' + data.id)
  },
  //*************************************客户端接口************************************
  //用户登录
  getFrontLogin(data) {
    return minRequest.post('/api/login/userLogin', data)
  },
  //退出登录
  getFrontLoginOut(data) {
    return minRequest.post('/api/login/loginOut', data)
  },
  //获取登录信息
  getFrontLoginInfo(data) {
    return minRequest.post('/api/register/getLoginInfo', data)
  },
  //修改用户
  updateFrontUserInfo(data) {
    return minRequest.post('/api/register/updateUserInfo', data)
  },
  //添加播放清单
  getPlayList(data) {
    return minRequest.post('/api/playList/getList', data)
  }

}
