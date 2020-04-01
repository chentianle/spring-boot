package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;

import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WebCommunityHelpEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
public interface WebCommunityHelpService extends IService<WebCommunityHelpEntity> {


    PageUtils queryPageBylist(Map<String, Object> params);

    List<Map<String, Object>> communityConstantly(Map<String, Object> params);

    List<Map<String, Object>> constantly(Map<String, Object> params);

    WebCommunityHelpEntity queryById(String id);
}

