package com.example.score.service.Profession;

import com.example.score.domain.Profession;
import com.example.score.utils.PagingResult;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Description 专业信息Service层
 **/
public interface ProfessionService {
  /**
  * description: 获取专业列表
  * return: List<Profession>
  */
  List<Profession> getProfessionList();

  /**
  * description: 获取专业列表（分页）
  * @param rowBounds 分页参数
  * @param condition 查询条件
  * @return PagingResult<Profession>
  */
  PagingResult<Profession> getProfessionListPaging(RowBounds rowBounds, Map<String, Object> condition);

  /**
  * description: 添加专业
  * @param profession 专业对象
  * @return void
  */
  void addProfession(Profession profession);

  /**
  * description: 删除专业
  * @param ids 专业ID列表
  * @return void
  */
  void deleteProfession(List<Integer> ids);

  /**
  * description: 更新专业
  * @param profession 专业对象
  * @return void
  */
  void updateProfession(Profession profession);
}
