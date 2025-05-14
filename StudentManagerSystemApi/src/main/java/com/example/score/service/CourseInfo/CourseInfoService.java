package com.example.score.service.CourseInfo;

import com.example.score.domain.CourseInfo;

/**
 * Description 课程详情Service层
 **/
public interface CourseInfoService {
    /**
     * 根据课程ID获取课程详情
     * @param courseId 课程ID
     * @return 课程详情
     */
    CourseInfo getCourseInfo(String courseId);

    /**
     * 更新课程详情
     * @param courseInfo 课程详情
     */
    void updateCourseInfo(CourseInfo courseInfo);
}
