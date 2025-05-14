-- 插入course表数据
INSERT INTO `course` (`id`, `name`, `credits`, `score`, `number`, `year`, `term`, `type`, `profession`) VALUES
('1001', '高等数学', 3.0, 100, 48, 2024, 1, 1, '数学专业'),
('1002', '大学英语', 2.5, 100, 32, 2024, 1, 1, '英语专业'),
('1003', '计算机编程', 3.5, 100, 64, 2024, 2, 2, '计算机专业');

-- 插入course_info表数据
INSERT INTO `course_info` (`course_id`, `start`, `end`, `room`, `profession`) VALUES
('1001', 1, 2, 'A101', '数学专业'),
('1002', 3, 4, 'B202', '英语专业'),
('1003', 5, 6, 'C303', '计算机专业');

-- 插入profession表数据`student_score_sys`
INSERT INTO `profession` (`name`) VALUES
('数学专业'),
('英语专业'),
('计算机专业');

-- 插入student表数据
INSERT INTO `student` (`id`, `username`, `password`, `real_name`, `level`, `school`, `admission_time`, `email`, `profession`, `grade`, `phone`, `sex`) VALUES
('3168901101', '3168901101', 'pass1', '学生1', 2, '清华大学', '2024-09', 'student1@tsinghua.edu.cn', '数学专业', 2024, '13312345678', 1);

-- 插入student_course表数据


-- 插入teacher表数据
INSERT INTO `teacher` (id,`username`, `password`, `real_name`, `level`, `school`, `email`, `phone`, `sex`) VALUES
(38900291,'3890291', 'pass1', '教师1', 1, '清华大学', 'teacher1@tsinghua.edu.cn', '13612345678', 1);

-- 插入teacher_course表数据
INSERT INTO `teacher_course` (`teacher_id`, `username`, `name`, `profession`, `grade`, `term`, `course_id`) VALUES
('3890291', '3890291', '高等数学', '数学专业', '2024', 1, '1001');

-- 插入timetable表数据
INSERT INTO `timetable` (`week_id`, `profession`, `grade`, `year`, `term`, `week_num`) VALUES
(1, '数学专业', '2024', 2024, 1, 1),
(2, '英语专业', '2024', 2024, 1, 1);

-- 插入week表数据
INSERT INTO `week` (`monday`, `tuesday`, `wednesday`, `thursday`, `friday`, `saturday`, `sunday`) VALUES
('无课', '高等数学', '无课', '无课', '无课', '无课', '无课');