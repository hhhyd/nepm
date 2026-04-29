import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import Decisioner from '../views/decision/Decisioner.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/admin',
      children: [
        {
          path: 'dashboard',
          name: 'AdminDashboard',
          component: () => import('@/views/admin/AdminDashboard.vue')
        },
        {
          path: 'feedback',
          name: 'AdminFeedback',
          component: () => import('@/views/admin/FeedbackList.vue')
        },
        {
          path: 'statistics',
          name: 'AdminStatistics',
          component: () => import('@/views/admin/StatisticsView.vue')
        },
        {
          path: 'supervisionList',
          name: 'SupervisionList',
          component: () => import('@/views/admin/SupervisionList.vue')
        },
        {
          path: 'aqilist',
          name: 'AqiList',
          component: () => import('@/views/admin/AqiList.vue')
        },
        {
          path: 'provincestat',
          name: 'ProvinceStat',
          component: () => import('@/views/admin/ProvinceStat.vue')
        },
        {
          path: 'distributionstat',
          name: 'DistributionStat',
          component: () => import('@/views/admin/DistributionStat.vue')
        },
        {
          path: 'trendstat',
          name: 'TrendStat',
          component: () => import('@/views/admin/TrendStat.vue')
        },
          {
            path: 'otherstat',
            name: 'OtherStat',
            component: () => import('@/views/admin/OtherStat.vue')
          }
      ]
    },
    {
      path: '/supervisor',
      children: [
        {
          path: 'dashboard',
          name: 'SupervisorDashboard',
          component: () => import('@/views/supervisor/SupervisorDashboard.vue')
        },
        {
          path: 'myFeedback',
          name: 'SupervisorMyFeedback',
          component: () => import('@/views/supervisor/MyFeedback.vue')
        }
      ]
    },
    {
      path: '/gridMember',
      children: [
        {
          path: 'dashboard',
          name: 'GridDashboard',
          component: () => import('@/views/gridMember/GridDashboard.vue')
        },
        {
          path: 'myTasks',
          name: 'GridMyTasks',
          component: () => import('@/views/gridMember/MyTasks.vue')
        },
        {
          path: 'detect',
          name: 'GridDetect',
          component: () => import('@/views/gridMember/Detect.vue')
        }
      ]
    },
    {
      path: '/decisioner',
      name: 'decisioner',
      component: Decisioner
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const user = JSON.parse(localStorage.getItem('user') || '{}')

  if (to.path !== '/login' && to.path !== '/register' && to.path !== '/decisioner' && !user.userType) {
    next('/login')
  } else {
    next()
  }
})

export default router
