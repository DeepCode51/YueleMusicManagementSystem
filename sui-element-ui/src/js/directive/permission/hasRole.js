/**
 * 角色权限处理
 */
import store from '@/js/vuex/store'

export default {
  inserted(el, binding, vnode) {
    const {value} = binding;
    //const super_admin = "adminRole";
    const roles = store.getters && store.getters.roles;
    if (value && value instanceof Array && value.length > 0) {
      const roleFlag = value
      const hasRole = roles.some(role => {
        //return super_admin === role || roleFlag.includes(role)
        return roleFlag.includes(role)
      })
      if (!hasRole) {//去除表单验证
        if (el.__vue__ && el.__vue__._props && el.__vue__._props.prop && el.__vue__.elForm && el.__vue__.elForm._props.rules) {
          let rules = el.__vue__.elForm._props.rules;
          delete rules[el.__vue__._props.prop]
        }
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error(`请正确设置角色权限标签值`)
    }
  }
}
