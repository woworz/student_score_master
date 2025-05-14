package com.example.score.dao.CourseInfo;

import com.example.score.domain.CourseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * Description 课程具体安排表
 **/
@Mapper
public interface CourseInfoMapper {
  /**
   * description: 新增课程具体安排（周数等）
   * @param: courseInfo
   * return: void
   */
  void addCourseInfo(CourseInfo courseInfo);
  /**
  * description: 修改课程具体安排（周数等）
  * @param: courseInfo
  * return: void
  */
  void updateCourseInfo(CourseInfo courseInfo);
  /**
  * description: 删除课程具体安排
  * @param: String
  * return: void
  */
  void deleteInfo(@Param("id") String id);
  /**
  * description: 获取课程信息详情
  * @param: Map<String, Object> condition
  * return: CourseInfo
  */
  CourseInfo getInfo(@Param("condition") Map<String, Object> condition);

  /**
  * description: 根据课程ID获取课程详情
  * @param condition 查询条件（包含courseId）
  * @return CourseInfo
  */
  CourseInfo getInfoByCourseId(@Param("condition") Map<String, Object> condition);
}
