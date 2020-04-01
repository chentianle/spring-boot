package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.modules.generator.dao.WebCommunityFeedbackDao;
import io.renren.modules.generator.entity.WebCommunityFeedbackEntity;
import io.renren.modules.generator.service.WebCommunityFeedbackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("webCommunityFeedbackService")
@Transactional
public class WebCommunityFeedbackServiceImpl extends ServiceImpl<WebCommunityFeedbackDao, WebCommunityFeedbackEntity> implements WebCommunityFeedbackService {


    @Override
    public List<WebCommunityFeedbackEntity> getByHelpId(Long id) {
        return baseMapper.getByHelpId(id);
    }
}
