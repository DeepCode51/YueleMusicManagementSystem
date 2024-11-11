<template>
  <el-image class="column-image" :src="formatUrl(value)">
    <div slot="error" class="image-slot">
      <i class="el-icon-picture-outline" style="font-size: 25px"></i>
    </div>
  </el-image>
</template>
<script>
  export default {
    props:{
      value: {
        type: String,
        default: ''
      },
    },
  };
</script>
