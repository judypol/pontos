import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/pages/login'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect:'/trigger'
    },
    {
      path:'/',
      name:'manager',
      component:resolve=>require(['../components/Home'],resolve),
      meta:{title:'管理'},
      children:[
        {
          path:'/trigger',
          component:resolve=>require(['../pages/trigger'],resolve),
          meta:{title:'trigger'}
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: resolve=>require(['../pages/login'],resolve)
    },
  ]
})
