package com.example.score.service.Timetable;

import com.example.score.domain.CourseInfo;
import com.example.score.domain.WeekCourse;

import java.util.List;
import java.util.Map;

/**
 * Description 课程表Service层
 **/
public interface TimetableService {
  /**
   * description: 新增课程表
   * @param list
   * @return void
   */
  void add(List<WeekCourse> list);

  /**
   * description: 管理员用户获取课程表
   * @param condition
   * @return List<WeekCourse>
   */
  List<WeekCourse> getTimetable(Map<String, Object> condition);
  /**
  * description: 学生用户获取课程表
  * @param: condition
  * return: List<WeekCourse>
  */
  List<WeekCourse> getTimetableByStudent(Map<String, Object> condition);
  /**
  * description: 教师用户获取课程表
  * @param: condition
  * return: List<WeekCourse>
  */
  List<WeekCourse> getTimetableByTeacher(Map<String, Object> condition);
  /**
  * description: 新增课程具体安排（周数等）
  * @param: CourseInfo
  * return: void
  */
  void updateCourseInfo(CourseInfo courseInfo);
}
