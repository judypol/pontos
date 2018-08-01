// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import axios from './assets/js/axiosUtils';
import 'element-ui/lib/theme-chalk/index.css';    // 默认主题
import store from './store';
import conf from './assets/js/config';

Vue.use(ElementUI, { size: 'default' });
Vue.prototype.$axios = axios;
Vue.prototype.$conf=conf;

Vue.config.productionTip = false

router.beforeEach((to,from,next)=>{
  window.document.title=to.meta.title;
  store.commit('getAuthKey',conf.authKey);
  if(store.state.authKey==null&&to.fullPath==='/login'){   //没有登录
    next();
  }else if(store.state.authKey&&to.fullPath==='/login'){   //已经登录
    next({path:'/'});
  }else if(store.state.authKey==null&&to.fullPath!='/login'){ //没有登录
    next({path:'/login'});
  }else{
    next();
  }
});
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
