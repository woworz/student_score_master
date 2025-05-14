package com.example.score.service.Timetable.impl;

import com.example.score.dao.CourseInfo.CourseInfoMapper;
import com.example.score.dao.TeacherCourse.TeacherCourseMapper;
import com.example.score.dao.Timetable.TimetableMapper;
import com.example.score.dao.User.StudentMapper;
import com.example.score.dao.WeekCourse.WeekCourseMapper;
import com.example.score.domain.CourseInfo;
import com.example.score.domain.TeacherCourse;
import com.example.score.domain.WeekCourse;
import com.example.score.dto.Timetable;
import com.example.score.dto.User;
import com.example.score.service.Timetable.TimetableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Description
 **/
@Service
public class TimetableServiceImpl implements TimetableService {

  @Resource
  private TimetableMapper timetableMapper;
  @Resource
  private StudentMapper studentMapper;
  @Resource
  private WeekCourseMapper weekCourseMapper;
  @Resource
  private TeacherCourseMapper teacherCourseMapper;
  @Resource
  private CourseInfoMapper courseInfoMapper;

  @Override
  @Transactional
  public void add(List<WeekCourse> list) {
    Map<String, Object> condition = new HashMap<>();
    condition.put("profession", list.get(0).getProfession());
    condition.put("grade", list.get(0).getGrade());
    condition.put("year", list.get(0).getYear());
    condition.put("term", list.get(0).getTerm());
    condition.put("week", list.get(0).getWeek());

    // 获取原本课程表信息
    List<Timetable> timeTableList = timetableMapper.getTimetable(condition);
    Set<Integer> ids = new HashSet<>();
    Set<Integer> weekIds = new HashSet<>();
    for (Timetable timetable : timeTableList) {
      ids.add(timetable.getId());
      weekIds.add(timetable.getWeekId());
    }
    if (ids.size() > 0) {
      // 修改
      List<Integer> weekList = new ArrayList<>(weekIds);
      // 排序
      Collections.sort(weekList);

      // 确保不会越界
      int minSize = Math.min(list.size(), weekList.size());
      for (int i = 0; i < minSize; i++) {
        WeekCourse weekCourse = list.get(i);
        weekCourse.setId(weekList.get(i));
        dealWeek(list.get(i));
        weekCourseMapper.update(list.get(i));
      }

      // 如果还有未处理的课程，则新增
      if (list.size() > weekList.size()) {
        for (int i = weekList.size(); i < list.size(); i++) {
          WeekCourse weekCourse = list.get(i);
          dealWeek(weekCourse);
          weekCourseMapper.add(weekCourse);
          Timetable timetable = new Timetable();
          timetable.setWeekId(weekCourse.getId());
          timetable.setProfession(weekCourse.getProfession());
          timetable.setGrade(weekCourse.getGrade());
          timetable.setYear(weekCourse.getYear());
          timetable.setTerm(weekCourse.getTerm());
          timetable.setWeekNum(weekCourse.getWeek());
          timetableMapper.add(timetable);
        }
      }
    } else {
      // 新增课程表
      // 删除旧课程表
      timetableMapper.deleteTimeTable(condition);
      // 新增
      for (WeekCourse weekCourse : list) {
        dealWeek(weekCourse);
        weekCourseMapper.add(weekCourse);
        Timetable timetable = new Timetable();
        timetable.setWeekId(weekCourse.getId());
        timetable.setProfession(weekCourse.getProfession());
        timetable.setGrade(weekCourse.getGrade());
        timetable.setYear(weekCourse.getYear());
        timetable.setTerm(weekCourse.getTerm());
        timetable.setWeekNum(weekCourse.getWeek());
        timetableMapper.add(timetable);
      }
    }
  }

  private void dealWeek(WeekCourse weekCourse) {
    if (weekCourse.getMonday() == null || weekCourse.getMonday().equals("")) {
      weekCourse.setMonday("一");
    }
    if (weekCourse.getTuesday() == null || weekCourse.getTuesday().equals("")) {
      weekCourse.setTuesday("一");
    }
    if (weekCourse.getWednesday() == null || weekCourse.getWednesday().equals("")) {
      weekCourse.setWednesday("一");
    }
    if (weekCourse.getThursday() == null || weekCourse.getThursday().equals("")) {
      weekCourse.setThursday("一");
    }
    if (weekCourse.getFriday() == null || weekCourse.getFriday().equals("")) {
      weekCourse.setFriday("一");
    }
    if (weekCourse.getSaturday() == null || weekCourse.getSaturday().equals("")) {
      weekCourse.setSaturday("一");
    }
    if (weekCourse.getSunday() == null || weekCourse.getSunday().equals("")) {
      weekCourse.setSunday("一");
    }
  }

  @Override
  public List<WeekCourse> getTimetable(Map<String, Object> condition) {
    int num = timetableMapper.checkCount(condition);
    List<WeekCourse> list = new ArrayList<>();
    if (num == 0) {
      for (int i = 1; i < 11; i++) {
        WeekCourse week = new WeekCourse();
        list.add(week);
      }
    } else {
      list = weekCourseMapper.getWeekCourse(condition);
      dealMethod(list, condition);
    }
    return list;
  }
  //  根据当前周过滤课程表中不属于当前周的课程
  private void dealMethod (List<WeekCourse> list, Map<String, Object> condition) {
    for (WeekCourse weekCourse : list) {
      Map<String, Object> map = new HashMap<>();
      map.put("profession", condition.get("profession").toString());
      map.put("week", condition.get("week").toString());

      // 处理周一课程
      String monday = weekCourse.getMonday() != null ? weekCourse.getMonday().toString() : "一";
      if (!"一".equals(monday)) {
        map.put("name", monday);
        weekCourse.setMonday(dealCourseInfo(map));
      }

      // 处理周二课程
      String tuesday = weekCourse.getTuesday() != null ? weekCourse.getTuesday().toString() : "一";
      if (!"一".equals(tuesday)) {
        map.put("name", tuesday);
        weekCourse.setTuesday(dealCourseInfo(map));
      }

      // 处理周三课程
      String wednesday = weekCourse.getWednesday() != null ? weekCourse.getWednesday().toString() : "一";
      if (!"一".equals(wednesday)) {
        map.put("name", wednesday);
        weekCourse.setWednesday(dealCourseInfo(map));
      }

      // 处理周四课程
      String thursday = weekCourse.getThursday() != null ? weekCourse.getThursday().toString() : "一";
      if (!"一".equals(thursday)) {
        map.put("name", thursday);
        weekCourse.setThursday(dealCourseInfo(map));
      }

      // 处理周五课程
      String friday = weekCourse.getFriday() != null ? weekCourse.getFriday().toString() : "一";
      if (!"一".equals(friday)) {
        map.put("name", friday);
        weekCourse.setFriday(dealCourseInfo(map));
      }

      // 处理周六课程
      String saturday = weekCourse.getSaturday() != null ? weekCourse.getSaturday().toString() : "一";
      if (!"一".equals(saturday)) {
        map.put("name", saturday);
        weekCourse.setSaturday(dealCourseInfo(map));
      }

      // 处理周日课程
      String sunday = weekCourse.getSunday() != null ? weekCourse.getSunday().toString() : "一";
      if (!"一".equals(sunday)) {
        map.put("name", sunday);
        weekCourse.setSunday(dealCourseInfo(map));
      }
    }
  }
  private String dealCourseInfo (Map<String, Object> map) {
    // 如果课程名是默认值“一”，直接返回，不查询数据库
    String courseName = map.get("name").toString();
    if ("一".equals(courseName)) {
      return "一";
    }

    CourseInfo courseInfo = courseInfoMapper.getInfo(map);
    if (courseInfo != null) {
      int start = courseInfo.getStart();
      int end = courseInfo.getEnd();
      int content = Integer.parseInt(map.get("week").toString());
      if (content < start || content > end) {
        return "一";
      }
    }
    return courseName;
  }
  @Override
  public List<WeekCourse> getTimetableByStudent(Map<String, Object> condition) {

    User user = studentMapper.getStudentById(condition.get("studentName").toString());
    Map<String, Object> oldMap = new HashMap<>();
    oldMap.put("profession", user.getProfession());
    oldMap.put("grade", user.getGrade());
    oldMap.put("year", condition.get("year"));
    oldMap.put("term", condition.get("term"));
    oldMap.put("week", condition.get("week"));
    List<WeekCourse> list = weekCourseMapper.getWeekCourse(oldMap);
    dealMethod(list, oldMap);
    return list;
  }

  @Override
  public List<WeekCourse> getTimetableByTeacher(Map<String, Object> condition) {
    // Deprecated Function
    int num = timetableMapper.checkCount(condition);
    List<WeekCourse> weekCourseList = new ArrayList<>();
    if (num == 0) {
      // 空课程表
      for (int i = 1; i < 11; i++) {
        WeekCourse week = new WeekCourse();
        weekCourseList.add(week);
      }
    } else {
      weekCourseList = weekCourseMapper.getWeekCourse(condition);
      dealMethod(weekCourseList, condition);
    }

    // 获取教师负责的专业、班级、课程
    List<TeacherCourse> teacherCourseList = teacherCourseMapper.getCourseListById(condition.get("teacherId").toString());
    List<WeekCourse> newList = new ArrayList<>();
    // 将新课程的每一项设为"一"
    for (int i = 1; i < 11; i++) {
      WeekCourse week = new WeekCourse();
      dealWeek(week);
      newList.add(week);
    }
    if(num == 0) {
      return newList;
    }
    for (TeacherCourse teacherCourse : teacherCourseList) {

      for (int i = 0; i < weekCourseList.size(); i++) {
        WeekCourse weekCourse = dealWeekCourse(newList.get(i), weekCourseList.get(i), teacherCourse.getName());
        newList.set(i, weekCourse);
      }
    }

    return newList;


    // 获取教师负责的专业、班级、课程
//    List<TeacherCourse> list = teacherCourseMapper.getCourseListById(condition.get("teacherId").toString());
//    List<WeekCourse> newList = new ArrayList<>();
//    // 将新课程的每一项设为"一"
//    for (int i = 1; i < 11; i++) {
//      WeekCourse week = new WeekCourse();
//      dealWeek(week);
//      newList.add(week);
//    }
//    for (TeacherCourse teacherCourse : list) {
//      Map<String, Object> map = new HashMap<>();
//      map.put("profession", teacherCourse.getProfession());
//      // 计算年份
//      Calendar calendar = Calendar.getInstance();
//      int month = calendar.get(Calendar.MONTH) + 1;
//      int year = calendar.get(Calendar.YEAR);
//      if(month > 9) {
//        year += 1;
//      }
//      map.put("year", year);
//      map.put("grade", teacherCourse.getGrade());
//      map.put("term", teacherCourse.getTerm());
//      map.put("week", condition.get("week"));
//      // 根据专业班级获取课程表
//      List<WeekCourse> weekCourseList = weekCourseMapper.getWeekCourse(map);
//      dealMethod(weekCourseList, map);
//      for (int i = 0; i < weekCourseList.size(); i++) {
//        WeekCourse weekCourse = dealWeekCourse(newList.get(i), weekCourseList.get(i), teacherCourse.getName());
//        newList.set(i, weekCourse);
//      }
//    }
//    return newList;
  }

  @Override
  public void updateCourseInfo(CourseInfo courseInfo) {
    courseInfoMapper.updateCourseInfo(courseInfo);
  }


  private WeekCourse dealWeekCourse(WeekCourse newWeek, WeekCourse oldWeek, String name) {
    // 当课程表的课程等于教师负责的课程时，存入新的课程表newWeek

    if (oldWeek.getMonday().equals(name)) {
      newWeek.setMonday(oldWeek.getMonday());
    }
    if (oldWeek.getTuesday().equals(name)) {
      newWeek.setTuesday(oldWeek.getTuesday());
    }
    if (oldWeek.getWednesday().equals(name)) {
      newWeek.setWednesday(oldWeek.getWednesday());
    }
    if (oldWeek.getThursday().equals(name)) {
      newWeek.setThursday(oldWeek.getThursday());
    }
    if (oldWeek.getFriday().equals(name)) {
      newWeek.setFriday(oldWeek.getFriday());
    }
    if (oldWeek.getSaturday().equals(name)) {
      newWeek.setSaturday(oldWeek.getSaturday());
    }
    if (oldWeek.getSunday().equals(name)) {
      newWeek.setSunday(oldWeek.getSunday());
    }
    return newWeek;
  }
}
