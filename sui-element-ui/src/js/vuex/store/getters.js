const getters={
  // 用户配置
  hasLogin: state => state.user.hasLogin,
  office: state => state.user.office,
  roles: state => state.user.roles,
  routes: state => state.user.routes,
  userInfo: state => state.user.userInfo,
  userType: state => state.user.userInfo.userType == undefined ? 0 : state.user.userInfo.userType,
  permissions: state => state.user.permissions,
  // 系统配置
  config: state => state.config,
  dictList: state => state.dictList,
  playList: state => state.playList,
  isShow: state => state.isShow,
  getDictArray: (state) => (type) => {
    if(state.dictList[type]==undefined){
      console.error("没有找到字典:【"+type+"】")
    }else{
      return state.dictList[type].map(function (v) {
        //value 字典大类时时字符串
        return {dictLabel:v.label,dictValue:parseInt(v.value),style:v.style};
      });
    }
  },
}
export default getters
