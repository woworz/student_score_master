import Vue from 'vue'
import Router from 'vue-router'
import login from '../components/login/login'
import register from '../components/register/register'
import home from '../components/home'
import score from '../components/score/score'
import course from '../components/course/course'
import dashboard from '../components/dashboard/index'
import account from '../components/account/account'
import student from '../components/student/student'
import teacher from '../components/teacher/teacher'
import admin from '../components/admin/admin'
import profession from '../components/profession/profession'
import timetable from '../components/timetable/timetable'
import analysis from '../components/analysis/index'
import registerApproval from '../components/register/register-approval'
import error from '../common/error/error'

// 导航栏点击点击多次报错警告处理
const originalPush = Router.prototype.push;
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
};

Vue.use(Router);
export default new Router({
  routes: [
    {
      path: '/404',
      name: 'error',
      component: error,
    }, {
      path: '/login',
      name: 'login',
      component: login,
    }, {
      path: '/register',
      name: 'register',
      component: register,
    }, {
      path: '/home',
      name: 'home',
      component: home,
      redirect: '/dashboard',
      children: [
        {
          path: '/dashboard',
          name: '主页',
          meta: { requireAuth: true },
          component: dashboard
        },
        {
          path: '/score',
          name: '成绩查询',
          meta: { requireAuth: true },
          component: score,
        }, {
          path: '/account',
          name: '账号管理',
          meta: { requireAuth: true, level: 0},
          component: account,
        }, {
          path: '/course',
          name: '课程录入',
          meta: { requireAuth: true, level: 0},
          component: course,
        }, {
          path: '/student',
          name: '学生用户',
          meta: { requireAuth: true, level: 0},
          component: student,
        }, {
          path: '/teacher',
          name: '教师用户',
          meta: { requireAuth: true, level: 0},
          component: teacher,
        }, {
          path: '/admin',
          name: '管理员用户',
          meta: { requireAuth: true, level: 0},
          component: admin,
        }, {
          path: '/profession',
          name: '专业管理',
          meta: { requireAuth: true, level: 0},
          component: profession,
        }, {
          path: '/timetable',
          name: '课程表',
          meta: { requireAuth: true},
          component: timetable,
        }, {
          path: '/analysis',
          name: '成绩详情',
          meta: { requireAuth: true},
          component: analysis,
        }, {
          path: '/register-approval',
          name: '注册审批',
          meta: { requireAuth: true, level: 0},
          component: registerApproval,
        }
      ]
    }
  ]
})
