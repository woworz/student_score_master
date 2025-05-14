package com.example.score.service.TeacherCourse;

import com.example.score.domain.TeacherCourse;
import com.example.score.dto.Course;

import java.util.List;
import java.util.Map;

/**
 * Description 教师课程Service层
 **/
public interface TeacherCourseService {
  /**
   * description: 新增
   * @param list
   * @return void
   */
  void add(List<TeacherCourse> list);

  /**
   * description: 删除
   *
   * @param ids
   * @return void
   */
  void delete(List<Integer> ids);

  /**
   * description: 修改
   * @param teacherCourse
   * @return void
   */
  void update(TeacherCourse teacherCourse);

  /**
   * description: 根据教师id获取教师课程信息
   * @param: String
   * return: List<TeacherCourse>
   */
  List<TeacherCourse> getCourseListById(String id);
  /**
  * description: 根据教师id获取教师用户所管理的专业、班级、课程名等信息
  * @param: String
  * return: List<Map<String, Object>>
  */
  List<Map<String, Object>> getProfessionInfo(String teacherId);
  /**
  * description: 管理员用户获取所有的班级和课程，渲染成下拉框选择
  * return: List<Map<String, Object>>
  */
  List<Map<String, Object>> getProfessionInfoByAdmin();
  /**
  * description: 课程表中获取课程详细信息
  * @param: Map<String, Object> condition
  * return: TeacherCourse
  */
  Course getCourseInfo(Map<String, Object> condition);

  /**
   * description: 获取教师所教课程
   * @param: teacherId
   * @return
   */
  List<TeacherCourse> getCoursesByTeacherId(String teacherId);
}
