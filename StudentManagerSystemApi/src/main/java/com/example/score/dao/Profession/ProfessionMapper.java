package com.example.score.dao.Profession;

import com.github.pagehelper.PageRowBounds;
import com.example.score.domain.Profession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Description 专业信息 Mapper层
 **/
@Mapper
public interface ProfessionMapper {
  /**
  * description: 获取所有专业
  * return: List<Profession>
  */
  List<Profession> getProfessionList();

  /**
  * description: 获取专业列表（分页）
  * @param pageRowBounds 分页参数
  * @param condition 查询条件
  * @return List<Profession>
  */
  List<Profession> getProfessionListPaging(PageRowBounds pageRowBounds, @Param("condition") Map<String, Object> condition);

  /**
  * description: 根据专业名查询专业数量（判断是否存在该专业）
  * @param: String
  * return: Integer
  */
  Integer checkProfessionCount(@Param("name") String name);

  /**
  * description: 新增专业
  * return: void
  */
  void addProfession(Profession profession);

  /**
  * description: 删除专业
  * @param: Integer
  * return: void
  */
  void deleteProfession(@Param("id") Integer id);

  /**
  * description: 根据专业名查询专业信息
  * @param: String
  * return: Profession
  */
  Profession getProfessionIdByName(@Param("name") String name);

  /**
  * description: 根据ID查询专业
  * @param id 专业ID
  * @return Profession
  */
  Profession getProfessionById(@Param("id") Integer id);

  /**
  * description: 更新专业
  * @param profession 专业对象
  * @return void
  */
  void updateProfession(Profession profession);
}
