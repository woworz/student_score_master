package com.example.score.service.CourseInfo.impl;

import com.example.score.dao.CourseInfo.CourseInfoMapper;
import com.example.score.domain.CourseInfo;
import com.example.score.service.CourseInfo.CourseInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Description 课程详情Service实现类
 **/
@Service
public class CourseInfoServiceImpl implements CourseInfoService {

    @Resource
    private CourseInfoMapper courseInfoMapper;

    @Override
    public CourseInfo getCourseInfo(String courseId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("courseId", courseId);
        return courseInfoMapper.getInfoByCourseId(condition);
    }

    @Override
    public void updateCourseInfo(CourseInfo courseInfo) {
        courseInfoMapper.updateCourseInfo(courseInfo);
    }
}
