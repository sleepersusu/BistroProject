import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/userStore';

const router = createRouter({
  history: createWebHistory(),
  linkActiveClass: 'active',
  scrollBehavior() {
    return { top: 0 }
  },
  routes: [
    {
      path: '/',
      name: 'home',
      redirect: '/index',
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
          path: '/membercenter',
          component: () => import('../views/MemberCenter.vue'),
          children: [
            {
              path: 'index', // 不需要斜線，Nav-link路徑是/membercenter/index
              component: () => import('../views/membercenter/UserIndex.vue'),
            },
            {
              path: 'profile', // 不需要斜線，Nav-link路徑是/membercenter/profile
              component: () => import('../views/membercenter/UserProfile.vue'),
            },
            {
              path: 'orders', // 不需要斜線，Nav-link路徑是/membercenter/orders
              component: () => import('../views/membercenter/UserOrder.vue'),
            },
            {
              path: 'comment',
              component: () => import('../views/membercenter/Comment.vue'),
            },
            {
              path: 'lotteryresult',
              component: () => import('../views/membercenter/LotteryResult.vue'),
            },
            {
              path: 'MerberOwnPrize',
              component: () => import('../views/membercenter/MerberOwnPrize.vue'),
            },
            {
              path: 'PromoRecord',
              component: () => import('../views/membercenter/MemberPromoRecord.vue'),
            },
          ],
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
          path: '/memberPoints',
          component: () => import('../views/MemberPoints.vue'),
        },
        {
          path: '/campaign',
          component: () => import('../views/Campaign.vue'),
        },
        {
          path: '/cartCheckout',
          component: () => import('../views/CartCheckout.vue'),
        },
        {
          path: '/cartCheckSuc',
          component: () => import('../views/CartCheckSuc.vue'),
        },
        {
          path: '/cartCheckFail',
          component: () => import('../views/CartCheckFail.vue'),
        },
        {
          path: '/reset-password/:encryptedEmail',
          component: () => import('../views/ResetPassword.vue'),
        },
      ],
    },
  ],
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore();

  // 檢查是否訪問的是 membercenter 路徑
  if (to.path.startsWith('/membercenter')) {
    if (userStore.memberId) {
      next();
    } else {
      next('/index');
    }
  } else {
    next();
  }
});

export default router
