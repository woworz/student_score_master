package com.example.score.service.Profession.impl;

import com.github.pagehelper.PageRowBounds;
import com.example.score.dao.Profession.ProfessionMapper;
import com.example.score.domain.Profession;
import com.example.score.service.Profession.ProfessionService;
import com.example.score.utils.PagingResult;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Description 专业信息Service实现类
 **/
@Service
public class ProfessionServiceImpl implements ProfessionService {
  @Resource
  private ProfessionMapper professionMapper;

  @Override
  public List<Profession> getProfessionList() {
    return professionMapper.getProfessionList();
  }

  @Override
  public PagingResult<Profession> getProfessionListPaging(RowBounds rowBounds, Map<String, Object> condition) {
    PageRowBounds pageRowBounds = new PageRowBounds(rowBounds.getOffset(), rowBounds.getLimit());
    List<Profession> professionList = professionMapper.getProfessionListPaging(pageRowBounds, condition);
    return new PagingResult<>(professionList, pageRowBounds.getTotal());
  }

  @Override
  public void addProfession(Profession profession) {
    // 检查专业名是否已存在
    Integer count = professionMapper.checkProfessionCount(profession.getName());
    if (count > 0) {
      throw new RuntimeException("专业名称已存在");
    }
    professionMapper.addProfession(profession);
  }

  @Override
  public void deleteProfession(List<Integer> ids) {
    for (Integer id : ids) {
      professionMapper.deleteProfession(id);
    }
  }

  @Override
  public void updateProfession(Profession profession) {
    // 检查专业名是否已存在（排除自身）
    Profession existingProfession = professionMapper.getProfessionById(profession.getId());
    if (!existingProfession.getName().equals(profession.getName())) {
      Integer count = professionMapper.checkProfessionCount(profession.getName());
      if (count > 0) {
        throw new RuntimeException("专业名称已存在");
      }
    }
    professionMapper.updateProfession(profession);
  }
}
