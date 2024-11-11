<template>
  <el-container>
    <!-- 表单弹窗 -->
    <userInfo-form ref="userInfoDialog" @reload="refreshUserInfo"/>
    <password-form ref="passwordDialog" @success="passwordSuccess"/>
    <el-aside class="el-aside" :class="[{'aside-hidden': isHidden,'aside-horizontal':mode=='horizontal'}]">
      <el-menu
        :mode="mode"
        text-color="#fff"
        active-text-color="#409EFF"
        :default-openeds="defaultOpen"
        :default-active="$route.path" @open="handleOpen" @close="handleClose" :collapse="isCollapse" router>
        <el-submenu v-if="menu.children&&!menu.href" v-for="(menu,index) in menuList" :key="index"  :index="index">
          <template slot="title">
            <i :class="menu.icon"></i><span class="span" slot="title">{{menu.name}}</span>
          </template>
          <el-menu-item @click="addTab(item)" :index="item.href" v-for="(item,childrenIndex) in menu.children" :key="childrenIndex">
            <span class="span" slot="title">{{item.name}}</span>
          </el-menu-item>
        </el-submenu>
        <el-menu-item class="el-submenu__title" v-else @click="addTab(menu)" :index="menu.href">
          <i :class="menu.icon"></i><span slot="title" class="span">{{menu.name}}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container style="height: 100vh;" :class="[{'body-horizontal':mode=='horizontal'}]">
      <el-header class="el-header" :class="[{'header-horizontal':mode=='horizontal'}]">
        <div v-if="mode!='horizontal'" style="float: left" >
          <svg t="1637039383052" @click="collapse" :class="{'is-active':!isHidden}" class="icon hamburger pointer" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M935.27 12.34H89.78c-41.38 0-75.23 33.85-75.23 75.23S48.4 162.8 89.78 162.8h845.49c41.38 0 75.23-33.85 75.23-75.23 0-41.37-33.85-75.23-75.23-75.23zM935.27 295.17H496.64c-41.38 0-75.23 33.85-75.23 75.23s33.85 75.23 75.23 75.23h438.63c41.38 0 75.23-33.85 75.23-75.23 0-41.37-33.85-75.23-75.23-75.23zM935.27 578.27H496.64c-41.38 0-75.23 33.85-75.23 75.23s33.85 75.23 75.23 75.23h438.63c41.38 0 75.23-33.85 75.23-75.23 0-41.37-33.85-75.23-75.23-75.23zM935.27 861.64H89.78c-41.38 0-75.23 33.85-75.23 75.23s33.85 75.23 75.23 75.23h845.49c41.38 0 75.23-33.85 75.23-75.23 0-41.37-33.85-75.23-75.23-75.23zM26.95 544.85l235.2 176.67c25.2 18.93 61.19 0.95 61.19-30.57V332.96c0-31.74-36.44-49.65-61.57-30.28L26.57 484.01c-20.01 15.42-19.82 45.67 0.38 60.84z" fill="#ffffff"></path></svg>
          <span class="project-name">{{projectName}}</span>
        </div>
        <el-dropdown>
          <span class="el-dropdown-link pointer">
            <el-image class="head-img" :src="formatUrl(userInfo.photo)"></el-image>
            <span style="color: white;font-weight: bold;font-size: medium">{{userInfo.loginName}}</span>
            <i class="el-icon-arrow-down" style="color: white"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="openUserInfoDialog({title:'用户信息',pageFrom:'edit',id:userInfo.id})">用户信息</el-dropdown-item>
            <el-dropdown-item @click.native="openPasswordDialog({title:'修改密码'})">修改密码</el-dropdown-item>
            <el-dropdown-item @click.native="loginOut">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-dropdown>
          <span style="color: ghostwhite" class="pointer" v-for="role in userInfo.roleList">【{{role.roleName}}】</span>
          <el-dropdown-menu v-if="userInfo.office" slot="dropdown">
            <el-dropdown-item>所属机构:【{{userInfo.office.name}}】</el-dropdown-item>
          </el-dropdown-menu>
          <el-dropdown-menu v-else slot="dropdown">
            <el-dropdown-item style="display: none"></el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-header>
      <vue-context-menu :contextMenuData="contextMenuData" @refresh="refresh" @delRight="delRight" @delOther="delOther" @delCurrent="delCurrent" @closeAll="closeAll" style="width: auto"></vue-context-menu>
      <el-tabs v-model="editableTabsValue" type="border-card" class="menu-tabs" style="background: white;border: none;" closable @tab-click="changeTab" @tab-remove="removeTab">
        <el-tab-pane v-for="item in editableTabs" :key="item.name" :label="item.title" :name="item.href">
        </el-tab-pane>
      </el-tabs>
      <el-main style="background: white">
        <router-view :key="rootKey"></router-view>
      </el-main>
      <el-footer class="copyright">Copyright © {{new Date().getFullYear()}} by {{projectName}}</el-footer>
    </el-container>
  </el-container>
</template>
<script>
  import userInfoForm from '../../module/user/userInfo'
  import passwordForm from '../../module/user/userPassword'
  import {mapActions} from "vuex";
  export default {
    name: "adminHome",
    components: {userInfoForm,passwordForm},
    data() {
      return {
        //路由的key
        rootKey: new Date().getTime(),
        defaultOpen:[],
        contextMenuData: {
          menu: {name:"",href:""},
          axis: {
            x: null,
            y: null
          },
          menulists: [
            {
              fnHandler: "refresh",
              icoName: "el-icon-refresh-right",
              btnName: "刷新页面"
            },
            {
              fnHandler: "delOther",
              icoName: "el-icon-circle-close",
              btnName: "关闭其他"
            },
            {
              fnHandler: "delCurrent",
              icoName: "el-icon-close",
              btnName: "关闭当前"
            },
            {
              fnHandler: "delRight",
              icoName: "el-icon-right",
              btnName: "关闭右侧"
            },
            {
              fnHandler: "closeAll",
              icoName: "el-icon-circle-close",
              btnName: "关闭全部"
            }
          ]
        },
        isHidden:false,
        isCollapse: false,
        editableTabsValue: '/main/index',
        editableTabs: [{
          title: '首页',
          href: '/main/index',
        }],
        menuList:[],
      }
    },
    mounted() {
      if (this.$route.path == '/main/home') {//进入首页
        this.changeRouter("/main/index");
      }else{
        this.addTab({name:this.$route.meta.title,href:this.$route.path})
      }
      this.$request.getMenuUserTree().then(res => {
        if (res.isOk) {
          this.menuList = res.list
        } else {
          const that = this;
          that.$router.push('/login');
        }
      })
    },
    computed:{
      mode(){
        return this.$store.getters.config.isHorizontal?'horizontal':'vertical';
      },
      userInfo(){
        return this.$store.getters.userInfo
      },
      projectName(){
        return this.$store.getters.config.projectName
      },
    },
    methods: {
      ...mapActions(['commitLoginOut','commitUserInfo']),
      showMenu($this) {
        event.preventDefault();
        var x = event.clientX;
        var y = event.clientY;
        // Get the current location
        this.contextMenuData.menu={name:$this.currentTarget.innerText,href:$this.currentTarget.id};
        this.contextMenuData.axis = {x,y};
      },
      //刷新页面
      refresh() {
        let targetHref = this.contextMenuData.menu.href.split('-')[1];
        if(this.editableTabsValue == targetHref){
          this.rootKey = new Date().getTime();
        }
        this.editableTabsValue = targetHref;
        this.changeRouter(targetHref)
      },
      //关闭右侧
      delRight() {
        const that = this;
        let targetHref = this.contextMenuData.menu.href.split('-')[1];
        let index = 0;
        for (let i = 0; i < this.editableTabs.length; i++) {
          if (this.editableTabs[i].href == targetHref) {
            index = i;
            break;
          }
        }
        let editableTabs = this.editableTabs.splice(index+1,this.editableTabs.length);
        editableTabs.forEach(function (v) {
          that.removeTab(v.href)
        })
        this.editableTabsValue = targetHref;
        this.changeRouter(targetHref)
      },
      //关闭其他
      delOther() {
        const that = this;
        let targetHref = this.contextMenuData.menu.href.split('-')[1];
        let editableTabs = this.editableTabs.filter(function (v) {
          return v.href != targetHref && v.href != "/main/index";
        });
        editableTabs.forEach(function (v) {
          that.removeTab(v.href)
        })
      },
      //关闭当前
      delCurrent(){
        let targetHref = this.contextMenuData.menu.href.split('-')[1];
        this.removeTab(targetHref)
      },
      closeAll(){
        this.editableTabs = [{
          title: '首页',
          href: '/main/index',
        }],
        this.changeRouter("/main/index");
        this.editableTabsValue = '/main/index';
      },
      refreshUserInfo(){
        this.commitUserInfo()
      },
      //点击查看
      openUserInfoDialog(option) {
        this.$refs.userInfoDialog.open(option);
      },
      //点击查看
      openPasswordDialog(option) {
        this.$refs.passwordDialog.open(option);
      },
      passwordSuccess(){
        const that = this;
        that.showConfirm('您的密码已修改成功，是否进行重新登录?',function (){
          that.commitLoginOut().then(res=>{
            if(res.isOk){
              that.showToast("退出成功");
              that.$router.push('/login');
            }
          })
        })
      },
      loginOut(){
        const that = this;
        that.showConfirm('确认退出吗?',function (){
          that.commitLoginOut().then(res=>{
            if(res.isOk){
              that.showToast("退出成功");
              that.$router.push('/login');
            }
          })
        })
      },
      collapse() {
        this.isCollapse=!this.isCollapse
        if(this.isCollapse){
          this.isHidden=true
        }else{
          this.isHidden=false
        }
      },
      handleOpen(key, keyPath) {
        //不全部展开
        //this.defaultOpen = [key];
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      },
      changeRouter(targetHref){
        this.$router.push(targetHref); //跳转到指定页面
      },
      changeTab(menu){
        let targetHref = menu.name;
        this.$router.push(targetHref); //跳转到指定页面
      },
      addTab(menu) {
        let tabs = this.editableTabs.map(function (v) {
          return v.href
        })
        let hasTab = tabs.contain(menu.href);
        if(!hasTab){
          this.editableTabs.push({
            title: menu.name,
            href: menu.href,
          });
        }
        this.editableTabsValue = menu.href;
        const that = this;
        setTimeout(function () {
          let tab_top_dom = document.getElementById('tab-'+menu.href);
          tab_top_dom.oncontextmenu = that.showMenu
        },500)
      },
      removeTab(targetHref) {
        const that = this;
        let tabs = this.editableTabs;
        let activeName = this.editableTabsValue;
        if (activeName === targetHref) {
          tabs.forEach((tab, index) => {
            if (tab.href === targetHref) {
              let nextTab = tabs[index + 1] || tabs[index - 1];
              if (nextTab) {
                activeName = nextTab.href;
                that.changeRouter(nextTab.href)
              }
            }
          });
        }
        this.editableTabsValue = activeName;
        this.editableTabs = tabs.filter(tab => tab.href !== targetHref);
      }
    }
  };
</script>
<style scoped lang="scss">
  @import '../../../../assets/styles/theme';
  .el-header {
    border-left: solid 1px #606266;
    height: 56px!important;
    text-align: right;
    font-size: 12px;
    background: $theme-admin-90-color!important;
    padding: 0 10px;
    line-height: 60px;
  }
  .el-aside {
    overflow: auto;
    height: 100vh;
    transition: width 0.3s;
    -webkit-transition:width 0.3s;
    background: $theme-admin-color!important;
    width: 250px!important;
  }
  aside{
    background: $theme-admin-color!important;
    padding: 0;
  }
  .el-aside /deep/ .el-menu{
    border-right: none;
    background: $theme-admin-90-color!important;;
  }

  /deep/ .el-tabs--border-card > .el-tabs__content {
    padding: 0px;
  }

  /deep/ .el-tabs__nav .el-tabs__item:nth-child(1) span {
    display: none;
  }

  /deep/ .menu-tabs .el-tabs__item {
    border: solid 1px #dfe6ec !important;
  }

  .user-info{
    vertical-align:middle;
    color: white;
    font-weight: bold;
    font-size: medium;
    padding: 10px
  }
  .user-info:hover{
    border-radius: 5px;
    background: rgba(43, 74, 120, 0.21);
  }
  .head-img {
    vertical-align:middle;
    margin-right: 5px;
    width: 35px;
    height: 35px;
    border-radius: 50%
  }
  .span{
    margin-left: 5px;
  }
  .header-horizontal{
    position: absolute;top: -60px;right: 0;
  }
  .body-horizontal{
    position: absolute;top: 60px;width: 100%;
  }
  .copyright {
    border-top: 1px solid #e7eaec;
    background: white;
    padding: 5px;
    height: 30px!important;
    text-align: center;
    font-size: smaller;
    color: grey;
  }
  .aside-horizontal{
    width: 100%!important;
  }
  .aside-hidden {
    transition: width 0.3s;
    -webkit-transition:width 0.3s;
    width: 64px!important;
  }
  .hamburger {
    color: white;
    display: inline-block;
    vertical-align: middle;
    width: 18px;
    height: 18px;
  }
  .hamburger:hover {
    padding: 0.5px;
    background: rgba(255, 255, 255, 0.11);
  }
  .hamburger.is-active {
    transform: rotate(180deg);
  }
  .project-name{
    padding-left: 5px;
    vertical-align: middle;
    font-size: 20px;
    color: white;
    font-weight: bold;
  }

</style>
