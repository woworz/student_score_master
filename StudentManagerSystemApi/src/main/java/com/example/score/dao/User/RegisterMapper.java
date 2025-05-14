package com.example.score.dao.User;

import com.example.score.dto.RegisterUser;
import com.example.score.utils.PageRowBounds;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Description 注册用户Mapper层
 **/
@Mapper
public interface RegisterMapper {
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
   * @return List<RegisterUser>
   */
  List<RegisterUser> getRegisterList(PageRowBounds rowBounds, @Param("condition") Map<String, Object> condition);

  /**
   * description: 根据ID获取注册申请
   * @param id
   * @return RegisterUser
   */
  RegisterUser getRegisterById(@Param("id") String id);

  /**
   * description: 更新注册申请状态
   * @param id
   * @param status
   * @return void
   */
  void updateStatus(@Param("id") String id, @Param("status") Integer status);

  /**
   * description: 删除注册申请
   * @param ids
   * @return void
   */
  void deleteRegister(@Param("ids") List<String> ids);

  /**
   * description: 检查用户名是否已存在
   * @param username
   * @return Integer
   */
  Integer checkUsernameExists(@Param("username") String username);
}
