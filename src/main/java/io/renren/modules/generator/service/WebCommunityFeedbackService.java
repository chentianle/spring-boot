package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.modules.generator.entity.WebCommunityFeedbackEntity;

import java.util.List;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
public interface WebCommunityFeedbackService extends IService<WebCommunityFeedbackEntity> {


    List<WebCommunityFeedbackEntity> getByHelpId(Long id);
}

