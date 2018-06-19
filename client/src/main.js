// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import VueResource  from 'vue-resource'
import iView from 'iview'
import 'iview/dist/styles/iview.css'    // 使用 CSS
import echarts from 'echarts'
import iviewArea from 'iview-area';
import cascaderMulti from "cascader-multi";
// import ElementUI from 'element-ui';
// import 'element-ui/lib/theme-chalk/index.css';


Vue.use(VueResource);
Vue.use(iView);
Vue.prototype.$echarts = echarts;
Vue.use(iviewArea);
Vue.use(cascaderMulti);
// Vue.use(ElementUI);

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
