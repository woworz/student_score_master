package com.example.score.service.User;

import com.example.score.dto.RegisterUser;
import com.example.score.utils.PagingResult;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Description 注册用户Service层
 **/
public interface RegisterService {
  /**
   * description: 添加注册申请
   * @param registerUser
   * @return void
   */
  void addRegister(RegisterUser registerUser);

  /**
   * description: 获取注册申请列表
   * @param rowBounds
   * @param condition
   * @return PagingResult<RegisterUser>
   */
  PagingResult<RegisterUser> getRegisterList(RowBounds rowBounds, Map<String, Object> condition);

  /**
   * description: 根据ID获取注册申请
   * @param id
   * @return RegisterUser
   */
  RegisterUser getRegisterById(String id);

  /**
   * description: 批准注册申请
   * @param id
   * @return void
   */
  void approveRegister(String id);

  /**
   * description: 拒绝注册申请
   * @param id
   * @return void
   */
  void rejectRegister(String id);

  /**
   * description: 删除注册申请
   * @param ids
   * @return void
   */
  void deleteRegister(List<String> ids);

  /**
   * description: 检查用户名是否已存在
   * @param username
   * @return boolean
   */
  boolean checkUsernameExists(String username);
}
