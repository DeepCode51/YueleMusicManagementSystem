import * as types from './mutation-types'
const matations={
  /**
   * state:当前状态树
   * v: 提交matations时传的参数
   */
  [types.HAS_LOGIN] (state, v) {
    state.user.hasLogin = v;
  },
  [types.ROLES] (state, v) {
    state.user.roles = v;
  },
  [types.ROUTES] (state, v) {
    state.user.routes = v;
  },
  [types.USER_INFO] (state, v) {
    state.user.userInfo = v;
  },
  [types.PERMISSIONS] (state, v) {
    state.user.permissions = v;
  },
  // 系统配置
  [types.CONFIG] (state, v) {
    state.config = v;
  },
  [types.DICT_LIST] (state, v) {
    state.dictList = v;
  },
  [types.NOTICE_LIST] (state, v) {
    state.noticeList = v;
  },
  [types.PLAY_LIST] (state, v) {
    state.playList = v;
  },
  [types.IS_SHOW] (state, v) {
    state.isShow = v;
  }

}

export default matations
