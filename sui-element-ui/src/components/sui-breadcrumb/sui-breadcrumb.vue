<template>
  <el-breadcrumb separator-class="el-icon-arrow-right">
    <!--<el-breadcrumb-item>当前位置</el-breadcrumb-item>-->
    <el-breadcrumb-item v-if="$route.path!='/front/home'" to="/front/home">返回首页</el-breadcrumb-item>
    <el-breadcrumb-item>{{$route.meta.title}}</el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script>
  export default {
    name: "sui-breadcrumb"
  }
</script>

<style scoped>

</style>
