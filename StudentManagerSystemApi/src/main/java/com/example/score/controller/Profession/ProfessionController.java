package com.example.score.controller.Profession;

import com.example.score.domain.Profession;
import com.example.score.service.Profession.ProfessionService;
import com.example.score.utils.PagingResult;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Description 专业管理控制层
 **/
@RestController
@RequestMapping("/api/sms/profession")
public class ProfessionController {

  @Autowired
  private ProfessionService professionService;

  /**
   * 获取所有专业列表（不分页）
   * @return 专业列表
   */
  @GetMapping("/getProfessionList")
  public List<Profession> getProfessionList() {
    return professionService.getProfessionList();
  }

  /**
   * 获取专业列表（分页）
   * @param condition 查询条件
   * @param limit 每页数量
   * @param offset 偏移量
   * @return 分页结果
   */
  @GetMapping
  public PagingResult<Profession> getProfessionListPaging(
          @RequestParam Map<String, Object> condition,
          @RequestParam(required = false, name = "$limit", defaultValue = "10") Integer limit,
          @RequestParam(required = false, name = "$offset", defaultValue = "0") Integer offset) {
    RowBounds rowBounds = new RowBounds(offset, limit);
    return professionService.getProfessionListPaging(rowBounds, condition);
  }

  /**
   * 添加专业
   * @param profession 专业对象
   */
  @PostMapping
  public void addProfession(@RequestBody Profession profession) {
    professionService.addProfession(profession);
  }

  /**
   * 删除专业
   * @param ids 专业ID数组
   */
  @DeleteMapping("/{ids}")
  public void deleteProfession(@PathVariable("ids") Integer[] ids) {
    List<Integer> idsList = Arrays.asList(ids);
    professionService.deleteProfession(idsList);
  }

  /**
   * 更新专业
   * @param profession 专业对象
   */
  @PutMapping
  public void updateProfession(@RequestBody Profession profession) {
    professionService.updateProfession(profession);
  }
}
