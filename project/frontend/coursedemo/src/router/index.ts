import { createRouter, createWebHistory } from 'vue-router'
import HelloWorld from '~/views/HelloWorld.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HelloWorld,
    },
    {
      path: '/course/:course_id(\\d+)/:labels*',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('~/components/BaseContent.vue'),
    },
    {
      path: '/manager',
      redirect: '/manager/NotPass'
    },
    {
      path: '/manager/NotPass',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../components/manager/ManagerCourseNotPass.vue'),
    },
    {
      path: '/manager/Pass',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../components/manager/ManagerCoursePassed.vue'),
    },
  ],
})

export default router;