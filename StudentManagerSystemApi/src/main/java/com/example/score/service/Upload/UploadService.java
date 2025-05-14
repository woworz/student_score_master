package com.example.score.service.Upload;

import com.example.score.domain.Upload;

import java.util.Map;

/**
 * Description 上传文件service层
 **/
public interface UploadService {
  /**
   * description: 上传头像
   * @param: upload
   * return: void
   */
  void upload(Upload upload);
  /**
  * description: 获取头像
  * @param: condition
  * return: String
  */
  String getHeader(Map<String, Object> condition);
}
