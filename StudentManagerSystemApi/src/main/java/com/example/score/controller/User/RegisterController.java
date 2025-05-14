package com.example.score.controller.User;

import com.example.score.dto.RegisterUser;
import com.example.score.service.User.RegisterService;
import com.example.score.utils.PagingResult;
import com.example.score.utils.PassToken;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description 注册用户控制层
 **/
@RestController
@RequestMapping("/api/sms/user/register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    /**
     * 添加注册申请
     * @param registerUser
     * @return Map<String, Object>
     */
    @PostMapping
    @PassToken
    public Map<String, Object> addRegister(@RequestBody RegisterUser registerUser) {
        Map<String, Object> result = new HashMap<>();
        
        // 检查用户名是否已存在
        if (registerService.checkUsernameExists(registerUser.getUsername())) {
            result.put("success", false);
            result.put("message", "用户名已存在");
            return result;
        }
        
        try {
            registerService.addRegister(registerUser);
            result.put("success", true);
            result.put("message", "注册申请提交成功，请等待管理员审核");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "注册申请提交失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取注册申请列表
     * @param condition
     * @return PagingResult<RegisterUser>
     */
    @GetMapping("/list")
    public PagingResult<RegisterUser> getRegisterList(@RequestParam Map<String, Object> condition) {
        int limit = Integer.parseInt(condition.getOrDefault("$limit", "10").toString());
        int offset = Integer.parseInt(condition.getOrDefault("$offset", "0").toString());
        RowBounds rowBounds = new RowBounds(offset, limit);
        return registerService.getRegisterList(rowBounds, condition);
    }

    /**
     * 根据ID获取注册申请
     * @param id
     * @return RegisterUser
     */
    @GetMapping("/{id}")
    public RegisterUser getRegisterById(@PathVariable("id") String id) {
        return registerService.getRegisterById(id);
    }

    /**
     * 批准注册申请
     * @param id
     * @return Map<String, Object>
     */
    @PutMapping("/approve/{id}")
    public Map<String, Object> approveRegister(@PathVariable("id") String id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            registerService.approveRegister(id);
            result.put("success", true);
            result.put("message", "注册申请已批准");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "批准失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 拒绝注册申请
     * @param id
     * @return Map<String, Object>
     */
    @PutMapping("/reject/{id}")
    public Map<String, Object> rejectRegister(@PathVariable("id") String id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            registerService.rejectRegister(id);
            result.put("success", true);
            result.put("message", "注册申请已拒绝");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "拒绝失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 删除注册申请
     * @param ids
     * @return Map<String, Object>
     */
    @DeleteMapping("/{ids}")
    public Map<String, Object> deleteRegister(@PathVariable("ids") String[] ids) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<String> idsList = Arrays.asList(ids);
            registerService.deleteRegister(idsList);
            result.put("success", true);
            result.put("message", "删除成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败：" + e.getMessage());
        }
        
        return result;
    }

    /**
     * 检查用户名是否已存在
     * @param username
     * @return Map<String, Object>
     */
    @GetMapping("/check/{username}")
    @PassToken
    public Map<String, Object> checkUsernameExists(@PathVariable("username") String username) {
        Map<String, Object> result = new HashMap<>();
        boolean exists = registerService.checkUsernameExists(username);
        result.put("exists", exists);
        return result;
    }
}
