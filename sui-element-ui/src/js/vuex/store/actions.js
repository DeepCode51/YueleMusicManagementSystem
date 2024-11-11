import $apis from "../../request/apis";
import * as types from './mutation-types'
import store from '@/js/vuex/store'

const actions = {
  //用户路由
  commitUserRoutes: ({ commit },params) => {
    return $apis.getUserRoutes(params).then(res => {
      commit(types.ROUTES, res.list);
      return res;
    });
  },
  //用户退出
  commitLoginOut: ({ commit },params) => {
    return $apis.getLoginOut(params).then(res => {
      commit(types.ROLES, []);
      commit(types.ROUTES, []);
      commit(types.PERMISSIONS, []);
      commit(types.USER_INFO, {});
      commit(types.HAS_LOGIN, false);
      return res;
    });
  },
  //获取用户信息
  commitUserInfo: ({ commit },params) => {
    return $apis.getUserInfo(params).then(res => {
      commit(types.USER_INFO, res.obj);
      return res
    });
  },
  //获取用户信息
  commitUpdateUserInfo: ({ commit },params) => {
    return $apis.updateUserInfo(params).then(res => {
      commit(types.USER_INFO, res.obj);
      return res
    });
  },
  //获取登录信息
  commitAdminLoginInfo: ({ commit }) => {
    return $apis.getLoginInfo().then(res => {
      commit(types.ROLES, res.data.roles);
      commit(types.USER_INFO, res.data.userInfo);
      commit(types.PERMISSIONS, res.data.permissions);
      commit(types.HAS_LOGIN, res.data.userInfo==null?false:true);
      return res
    });
  },
  //系统配置
  commitConfig: ({ commit }) => {
    return $apis.getConfig().then(res => {
      console.log("CONFIG:",res.data);
      commit(types.CONFIG, res.data);
      return res
    });
  },
  //获取数据字典
  commitDictList: ({ commit }) => {
    return $apis.getDictList().then(res => {
      console.log("DICT_LIST:",res.data)
      commit(types.DICT_LIST, res.data);
      return res
    });
  },
  //获取通知公告
  commitNoticeList: ({ commit }) => {
    return $apis.getNoticeList().then(res => {
      commit(types.NOTICE_LIST, res.data);
      return res
    });
  },
  //*************************前台接口****************************
  //获取token信息
  commitToken: ({ commit },params) => {
    return $apis.getToken(params).then(res => {
      sessionStorage.setItem("token",res.data.token);
      return res;
    });
  },
  //获取登录信息(刷新页面或者重新打开时检查)
  commitFrontLoginInfo: ({ commit }) => {
    return $apis.getFrontLoginInfo().then(res => {
      commit(types.USER_INFO, res.data.userInfo);
      commit(types.HAS_LOGIN, res.data.hasLogin);
      return res
    });
  },
  //获取登录
  commitFrontLogin: ({ commit },params) => {
    return $apis.getFrontLogin(params).then(res => {
      commit(types.USER_INFO, res.data.userInfo);
      commit(types.HAS_LOGIN, true);
      return res
    });
  },
  //客户端退出
  commitFrontLoginOut: ({ commit }) => {
    return $apis.getFrontLoginOut().then(res => {
      commit(types.USER_INFO, null);
      commit(types.HAS_LOGIN, false);
      commit(types.PLAY_LIST, null);
      sessionStorage.removeItem("token");
      return res;
    });
  },
  //填写用户信息
  commitFrontUserInfo: ({ commit },params) => {
    return $apis.updateFrontUserInfo(params).then(res => {
      commit(types.USER_INFO, res.obj);
      return res
    });
  },
  //添加音乐列表
  commitPlayList: ({ commit },params) => {
    return $apis.getPlayList(params).then(res => {
      commit(types.PLAY_LIST, res.list);
      return res
    });
  },
};

export default actions;
