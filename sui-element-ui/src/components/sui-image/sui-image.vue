<template>
  <el-image class="column-image" :src="formatUrl(src)">
    <div slot="error" class="image-slot">
      <i class="el-icon-picture-outline" style="font-size: 25px"></i>
    </div>
  </el-image>
</template>
<script>
  export default {
    props: {
      src: {
        type: String,
        default: ""
      },
    }
  };
</script>
