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
      component: () => import('@/views/Course/BaseCourse.vue'),
    },
    {
      path: '/course/hotCourse',
      component: () => import('@/views/Course/hotCourse.vue'),
    },
    {
      path: '/manager',
      redirect: '/manager/NotPass'
    },
    {
      path: '/manager/NotPass',
      component: () => import('@/views/manager/ManagerCourseNotPass.vue'),
    },
    {
      path: '/manager/Passed',
      component: () => import('@/views/manager/ManagerCoursePassed.vue'),
    },
    {
      path: '/MainPage/student',
      component: () => import('@/views/MainPage/student.vue'),
    },
    {
      path: '/MainPage/teacher',
      component: () => import('@/views/MainPage/teacher.vue'),
    },
    {
      path: '/MainPage/login',
      component: () => import('@/views/MainPage/login.vue'),
    },
    {
      path: '/courseEvaluation',
      component: () => import('@/views/Course/CourseEvaluate.vue'),
    },
  ],
})

export default router;