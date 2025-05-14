package com.example.score.service.User.impl;

import com.example.score.dao.User.AdminMapper;
import com.example.score.dao.User.RegisterMapper;
import com.example.score.dao.User.StudentMapper;
import com.example.score.dao.User.TeacherMapper;
import com.example.score.dto.RegisterUser;
import com.example.score.dto.User;
import com.example.score.service.User.RegisterService;
import com.example.score.utils.PageRowBounds;
import com.example.score.utils.PagingResult;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Description 注册用户Service实现类
 **/
@Service
public class RegisterServiceImpl implements RegisterService {

    @Resource
    private RegisterMapper registerMapper;
    
    @Resource
    private StudentMapper studentMapper;
    
    @Resource
    private TeacherMapper teacherMapper;
    
    @Resource
    private AdminMapper adminMapper;

    @Override
    public void addRegister(RegisterUser registerUser) {
        // 设置ID等于用户名
        registerUser.setId(registerUser.getUsername());
        registerMapper.addRegister(registerUser);
    }

    @Override
    public PagingResult<RegisterUser> getRegisterList(RowBounds rowBounds, Map<String, Object> condition) {
        PageRowBounds pageRowBounds = new PageRowBounds(rowBounds.getOffset(), rowBounds.getLimit());
        List<RegisterUser> registerList = registerMapper.getRegisterList(pageRowBounds, condition);
        return new PagingResult<>(registerList, pageRowBounds.getTotal());
    }

    @Override
    public RegisterUser getRegisterById(String id) {
        return registerMapper.getRegisterById(id);
    }

    @Override
    @Transactional
    public void approveRegister(String id) {
        // 获取注册申请信息
        RegisterUser registerUser = registerMapper.getRegisterById(id);
        if (registerUser == null || registerUser.getStatus() != 0) {
            return;
        }
        
        // 更新注册状态为已批准
        registerMapper.updateStatus(id, 1);
        
        // 将用户信息添加到对应的用户表中
        User user = new User();
        user.setId(registerUser.getId());
        user.setUsername(registerUser.getUsername());
        user.setPassword(registerUser.getPassword());
        user.setRealName(registerUser.getRealName());
        user.setLevel(registerUser.getLevel());
        user.setSchool(registerUser.getSchool());
        user.setAdmissionTime(registerUser.getAdmissionTime());
        user.setEmail(registerUser.getEmail());
        user.setProfession(registerUser.getProfession());
        user.setPhone(registerUser.getPhone());
        user.setSex(registerUser.getSex());
        user.setGrade(registerUser.getGrade());
        
        // 根据用户级别添加到对应的表
        if (registerUser.getLevel() == 0) {
            adminMapper.add(user);
        } else if (registerUser.getLevel() == 1) {
            teacherMapper.addTeacher(user);
        } else if (registerUser.getLevel() == 2) {
            studentMapper.addStudent(user);
        }
    }

    @Override
    public void rejectRegister(String id) {
        registerMapper.updateStatus(id, 2);
    }

    @Override
    public void deleteRegister(List<String> ids) {
        registerMapper.deleteRegister(ids);
    }

    @Override
    public boolean checkUsernameExists(String username) {
        Integer count = registerMapper.checkUsernameExists(username);
        return count > 0;
    }
}
