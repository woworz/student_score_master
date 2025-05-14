package com.example.score.controller.CourseInfo;

import com.example.score.domain.CourseInfo;
import com.example.score.service.CourseInfo.CourseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description 课程详情控制层
 **/
@RestController
@RequestMapping("/api/sms/course/info")
public class CourseInfoController {

    @Autowired
    private CourseInfoService courseInfoService;

    /**
     * 根据课程ID获取课程详情
     * @param courseId 课程ID
     * @return 课程详情
     */
    @GetMapping("/{courseId}")
    public CourseInfo getCourseInfo(@PathVariable("courseId") String courseId) {
        return courseInfoService.getCourseInfo(courseId);
    }

    /**
     * 更新课程详情
     * @param courseInfo 课程详情
     */
    @PutMapping
    public void updateCourseInfo(@RequestBody CourseInfo courseInfo) {
        courseInfoService.updateCourseInfo(courseInfo);
    }
}
