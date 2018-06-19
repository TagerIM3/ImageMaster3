import Vue from 'vue'
import Router from 'vue-router'
import login from '../pages/login'
import register from '../pages/register'
import forgetPassword from '../pages/forgetPassword'
import requestorMain from '../pages/requestorMain'
import workerMain from '../pages/workerMain'
import adminLogin from '../pages/adminLogin'
import administrator from '../pages/administrator'
import testMain from '../components/test/testMain'
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: login
    },
    {
      path: '/Register',
      name: 'Register',
      component: register
    },
    {
      path: '/requestor/requestorMain',
      name: 'requestorMain',
      component: requestorMain
    },
    {
      path: '/worker/workerMain',
      name: 'workerMain',
      component: workerMain
    },
    {
      path: '/forgetPassword',
      name: 'forgetPassword',
      component: forgetPassword
    },
    {
      path: '/adminLogin',
      name: 'adminLogin',
      component: adminLogin
    },
    {
      path:'/administrator',
      name:'administrator',
      component: administrator
    },
    {
      path:'/test',
      name:'testMain',
      component: testMain
    }
  ]
})
