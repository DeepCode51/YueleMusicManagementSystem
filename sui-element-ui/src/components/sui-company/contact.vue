<template>
  <div class="front-page">
    <home-header></home-header>
    <div class="front-body">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/front/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>联系我们</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <h2 style="text-align: center">联系我们</h2>
        <div>
          <el-image style="width: 100%;height: 100%" src="/static/images/front/company/contact.jpg"></el-image>
        </div>
      </el-card>
    </div>
    <home-footer></home-footer>
  </div>
</template>
