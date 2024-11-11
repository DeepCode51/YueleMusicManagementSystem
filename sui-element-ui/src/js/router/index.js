import Vue from 'vue'
import NProgress from 'nprogress'
import Router from 'vue-router'
import $store from '@/js/vuex/store'
Vue.use(Router);
const router = new Router({
  mode:'history',
  routes: [
    //404页面
    {path: '/404', component: (resolve) => require(['@/pages/admin/sys/error/404.vue'], resolve)},
    //admin路由
    {meta: {title: "项目地址"}, name: 'index', path: '', redirect: '/login', hidden: true},//空地址重定向到登录页面
    {meta: {title: "系统登录"}, name: 'login', path: '/login', component: (resolve) => require(['@/pages/admin/module/login/login.vue'], resolve),},
    {
      meta: {title: "系统主页"}, name: 'adminHome', path: '/main/home', component: (resolve) => require(['@/pages/admin/module/main/home.vue'], resolve),
      children: [
        {meta: {title: "系统首页"}, name: "adminIndex", path: '/main/index',component: (resolve) => require(['@/pages/admin/module/main/index.vue'], resolve),},
      ]
    },
    //front路由
    {meta: {title: "网站主页"}, name: 'frontIndex', path: '/front', component: (resolve) => require(['@/pages/front/index/index.vue'], resolve),
      children: [
        {meta: {title: "网站搜索"}, name: 'frontHome', path: '/front/home', component: (resolve) => require(['@/pages/front/service/home/home.vue'], resolve)},
        {meta: {title: "资讯详情"}, name: 'noticeDetails', path: '/front/noticeDetails', component: (resolve) => require(['@/pages/front/service/notice/noticeDetails.vue'], resolve)},
        {meta: {title: "分类列表"}, name: 'categoryList', path: '/front/categoryList', component: (resolve) => require(['@/pages/front/service/category/categoryList.vue'], resolve)},
        {meta: {title: "推荐歌曲"}, name: 'recommendList', path: '/front/recommendList', component: (resolve) => require(['@/pages/front/service/song/recommendList.vue'], resolve)},
        {meta: {title: "歌手列表"}, name: 'singerList', path: '/front/singerList', component: (resolve) => require(['@/pages/front/service/singer/singerList.vue'], resolve)},
        {meta: {title: "歌手详情"}, name: 'singerDetails', path: '/front/singerDetails', component: (resolve) => require(['@/pages/front/service/singer/singerDetails.vue'], resolve)},
        {meta: {title: "我的音乐"}, name: 'userSongList', path: '/front/userSongList', component: (resolve) => require(['@/pages/front/service/song/userSongList.vue'], resolve)},
        {meta: {title: "歌单歌曲"}, name: 'listSongList', path: '/front/listSongList', component: (resolve) => require(['@/pages/front/service/listSong/listSongList.vue'], resolve)},
        {meta: {title: "搜索列表"}, name: 'searchSongList', path: '/front/searchSongList', component: (resolve) => require(['@/pages/front/service/song/searchSongList.vue'], resolve)},
        {meta: {title: "播放歌曲"}, name: 'songDetails', path: '/front/songDetails', component: (resolve) => require(['@/pages/front/service/song/songDetails.vue'], resolve)},
      ]
    },
  ]
});

//将获取到的用户菜单数据转换成路由
function generateRoutes(accessRoutes) {
  let routers = router.options['routes'];
  routers.forEach(item => {
    if (item.name === 'adminHome') {
      let userRoutes = accessRoutes.map(item => {
        return {
          name: item.name,
          path: item.path,
          meta: {title: item.title},
          component: (resolve) => require(["@/pages/" + item.component], resolve),
        }
      });
      item.children.push(...userRoutes);
      console.log("userRoutes", userRoutes);
      routers.push({path: '*', redirect: '/404', hidden: true})
      router.addRoutes(routers);//重新覆盖,避免叠加
      console.log("allRoutes", routers);
    }
  })
}

//路由拦截守卫
router.beforeEach((to, from, next) => {
  console.log("to:", to);
  NProgress.start();
  /**************************************** admin端 ****************************************/
  if (to.path.indexOf('/front') < 0) {
    if (to.path != "/login") {
      if ($store.getters.routes.length == 0) {//有没配置信息
        $store.dispatch('commitConfig');
        $store.dispatch('commitDictList');
        $store.dispatch('commitAdminLoginInfo').then(res => {
          if ($store.getters.hasLogin) {//如果有登录加载路由。
            $store.dispatch('commitUserRoutes').then(res => {
              let accessRoutes = res.list;
              generateRoutes(accessRoutes);
              next({...to, replace: true}) // hack方法 确保addRoutes已完成
            }).catch(err => {
              console.log("err:", to)
              $store.dispatch('commitLoginOut').then(() => {
                next({path: '/login'})
                NProgress.done();
              })
            })
          } else {//如果有登录跳转登录页面。
            $store.dispatch('commitLoginOut').then(() => {
              next({path: '/login'});
              NProgress.done();
            });
          }
        });
      } else {//路由加载过放行
        next();
        NProgress.done();
      }
    } else {
      $store.dispatch('commitAdminLoginInfo').then(res => {
        if ($store.getters.hasLogin) {//如果有登录加载路由。
          next({path: '/main/home'});
          NProgress.done();
        } else {
          next();
          NProgress.done();
        }
      });
    }
  }
  /**************************************** front端 ****************************************/
  if (to.path.indexOf('/front') >= 0) {
    if ($store.getters.dictList.length == 0) {//如果没有配置信息
      $store.dispatch('commitConfig');//复制页面地址重新加载配置
      $store.dispatch('commitDictList');
      if (sessionStorage.getItem("token")) {//有登录
        $store.dispatch('commitFrontLoginInfo');
      } else {
        $store.dispatch('commitFrontLoginOut')
      }
    }
    next();
    NProgress.done();
  }
});

router.afterEach(() => {
  document.title = "欢迎登录";
  if ($store.getters.config.projectName) {
    document.title = $store.getters.config.projectName;
  }
  NProgress.done();
});
export default router
