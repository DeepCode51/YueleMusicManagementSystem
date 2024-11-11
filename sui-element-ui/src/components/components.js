import Vue from 'vue';
function capitalizeFirstLetter(string){
  return string.charAt(0).toUpperCase() + string.slice(1);
}
//components下组件
const requireComponent = require.context(
  '.',true,/\.vue$/
  //找到components文件夹下以.vue命名的文件
)
requireComponent.keys().forEach(fileName => {
  const componetConfig = requireComponent(fileName);
  let a = fileName.lastIndexOf('/');
  fileName = '.' + fileName.slice(a);
  const componetName = fileName.replace(/^\.\//,'').replace(/\.\w+$/,'')
  Vue.component(componetName,componetConfig.default || componetConfig)
});

//front下组件
const requireFrontComponent = require.context(
  '../pages/front',true,/\.vue$/
  //找到front文件夹下以.vue命名的文件
)
requireFrontComponent.keys().forEach(fileName => {
  const componetConfig = requireFrontComponent(fileName);
  let a = fileName.lastIndexOf('/');
  fileName = '.' + fileName.slice(a);
  const componetName = fileName.replace(/^\.\//,'').replace(/\.\w+$/,'')
  Vue.component(componetName,componetConfig.default || componetConfig)
})

