<template>
  <span @click="link()">{{tagName}}</span>
</template>
<script>
  export default {
    props: {
      to: {
        type: Object,
        default: {path: "", needLogin: false}
      },
    },
    computed: {
      tagName() {
        return this.$slots.default[0].text
      },
    },
    methods: {
      link() {
        if (this.to.needLogin && !this.$store.getters.hasLogin) {
          this.showWarn("请先进行登录。")
        } else {
          this.toPage(this.to.path)
        }
      }
    }
  };
</script>

