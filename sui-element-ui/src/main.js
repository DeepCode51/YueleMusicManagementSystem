// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './js/router'
Vue.config.silent = true//去掉Vue warn警告
import '@/assets/styles/index.scss' // index css 重置默认样式
import './assets/styles/element-variables.scss'//主题样式
import '@/assets/styles/suifeng.scss' // suifeng css
import '@/assets/styles/global.scss' // global css
import '@/assets/styles/theme.scss' // theme css
// 引入element-ui
import ElementUI from 'element-ui'
// import 'element-ui/lib/theme-chalk/index.css'//已在element-variables.scss引入
import 'font-awesome/css/font-awesome.min.css'//图标样式
Vue.use(ElementUI, { zIndex: 1000 })
import 'nprogress/nprogress.css'
import MinRequest from './js/request/MinRequest'
import minRequest from './js/request/apis'
import validator from './js/config/validator.js'
import permission from './js/directive/permission'
import './js/directive/drag/dialogdrag.js';
import './components/components.js'
Vue.use(permission)
//百度地图
import BaiduMap from 'vue-baidu-map'
Vue.use(BaiduMap, {
  ak: 'k7ILtYBEvwy3piyGcdWnwMtAqDOLKTMs'
})
import VueContextMenu from 'vue-contextmenu' //右键菜单
Vue.use(VueContextMenu)
import echarts from 'echarts'
Vue.prototype.$echarts = echarts
Vue.prototype.$validator=validator;
Vue.prototype.$minRequest=minRequest;
Vue.use(MinRequest);
// 富文本
import VueQuillEditor from 'vue-quill-editor'
Vue.use(VueQuillEditor);
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
// 视频预览
import VideoPlayer from 'vue-video-player'
require('video.js/dist/video-js.css')
require('vue-video-player/src/custom-theme.css')
Vue.use(VideoPlayer)
import store from './js/vuex/store/index'
import global from './js/config/global'
Vue.prototype.$store=store;
Vue.prototype.$global=global;
Vue.config.silent = true;
Vue.config.productionTip = false;
/* eslint-disable no-new */
new Vue({
  el: '#app',
  minRequest,
  router,
  components: { App },
  template: '<App/>',
  // 安装全局事件总线
  beforeCreate(){
    Vue.prototype.$bus = this
  }
})
