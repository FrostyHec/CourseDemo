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
    {
      path: '/MainPage/allCourse',
      component: () => import('@/views/MainPage/allCourse.vue'),
    },
    {
      path: '/MainPage',
      component: () => import('@/views/MainPage/MainPage.vue'),
    },
    {
      path: '/MainPage/teacher/manage',
      component: () => import('@/views/MainPage/teacherManage.vue'),
    },
    {
      path: '/MainPage/searchCourse',
      component: () => import('@/views/MainPage/searchCourse.vue'),
    },
  ],
})

export default router;