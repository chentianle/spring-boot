package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.modules.generator.entity.WebQuestionnaireDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
public interface WebQuestionnaireDetailService extends IService<WebQuestionnaireDetailEntity> {

    /**
     * Admin 获取诊断详情
     * @param type  1：每日的情况 id为用户ID ；2：历史每条结果的详情 id为答案表的详情
     * @param id
     * @return
     */
    Map<String, Object> allInfoByUserid(String type, Long id);

}

