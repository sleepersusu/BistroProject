import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  linkActiveClass: 'active',
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/Home.vue'),
      children: [
        {
          path: '/index',
          component: () => import('../views/Index.vue'),
        },
        {
          path: '/profile',
          component: () => import('../views/Profile.vue'),
        },
        {
          path: '/login',
          component: () => import('../views/Login.vue'),
        },
        {
          path: '/reservations',
          component: () => import('../views/Reservations.vue'),
        },
        {
          path: '/menu',
          component: () => import('../views/Menu.vue'),
        },
        {
          path: '/cart',
          component: () => import('../views/Cart.vue'),
        },
        {
          path: '/order',
          component: () => import('../views/Order.vue'),
        },
        {
          path: '/memberPoints',
          component: () => import('../views/MemberPoints.vue'),
        },
        {
          path: '/campaign',
          component: () => import('../views/Campaign.vue'),
        },
        {
          path: '/CommentPost',
          component: () => import('../views/CommentPost.vue'),
        },
      ],
    },
  ],
})

export default router
