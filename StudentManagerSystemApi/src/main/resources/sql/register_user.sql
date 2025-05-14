-- 创建注册用户表
CREATE TABLE IF NOT EXISTS `register_user` (
  `id` varchar(50) NOT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `level` int(11) NOT NULL COMMENT '用户级别：0-管理员，1-教师，2-学生',
  `school` varchar(50) DEFAULT NULL COMMENT '学校',
  `admission_time` varchar(50) DEFAULT NULL COMMENT '入学时间',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  `profession` varchar(50) DEFAULT NULL COMMENT '专业',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `sex` int(11) DEFAULT NULL COMMENT '性别：0-男，1-女',
  `grade` varchar(50) DEFAULT NULL COMMENT '班级',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` int(11) DEFAULT 0 COMMENT '状态：0-待审核，1-已批准，2-已拒绝',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='注册用户表';
