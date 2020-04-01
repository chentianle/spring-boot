package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.generator.dao.WebDoctorCommentDao;
import io.renren.modules.generator.entity.WebDoctorCommentEntity;
import io.renren.modules.generator.service.WebDoctorCommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service("webDoctorCommentService")
@Transactional
public class WebDoctorCommentImpl
        extends ServiceImpl<WebDoctorCommentDao, WebDoctorCommentEntity>
        implements WebDoctorCommentService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebDoctorCommentEntity> page = this.page(
                new Query<WebDoctorCommentEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

}
