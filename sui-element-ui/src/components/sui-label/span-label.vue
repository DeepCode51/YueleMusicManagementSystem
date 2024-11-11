<template>
<span>
  <span v-for="item in dict" :key="item.id">
    <span v-if="item.id==value">{{item[label]}}</span>
  </span>
</span>
</template>
<script>
  export default {
    props: {
      dict: {
        type: Array,
        default: []
      },
      value: {
        type: String,
        default: ''
      },
      label: {
        type: String,
        default: ''
      }
    },
  };
</script>
