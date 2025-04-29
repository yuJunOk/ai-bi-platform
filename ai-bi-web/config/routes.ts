export default [
  {
    path: '/user',
    layout: false,
    routes: [{ name: '登录', path: '/user/login', component: './User/Login' }],
  },
  { path: '/', redirect: '/chart/add' },
  { path: '/chart/add', name: '智能分析', icon: 'barChart', component: './AddChart' },
  { path: '/chart/add-async', name: '智能分析（异步）', icon: 'barChart', component: './AddChartAsync' },
  { path: '/chart/my', name: '我的图表', icon: 'pieChart', component: './MyChart' },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      { path: '/admin', name: 'admin', icon: 'table', component: './Admin'},
    ],
  },
  //{ path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];
