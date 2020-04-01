package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WebUserReviewEntity;

import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
public interface WebUserReviewService extends IService<WebUserReviewEntity> {

    PageUtils queryPagelist(Map<String, Object> params);

    Map<String, Object> allInfoByUserid(Long id);
}

