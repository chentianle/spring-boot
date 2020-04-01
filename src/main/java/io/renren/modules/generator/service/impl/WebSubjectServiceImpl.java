package io.renren.modules.generator.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.generator.dao.WebSubjectDao;
import io.renren.modules.generator.entity.WebSubjectEntity;
import io.renren.modules.generator.service.WebSubjectService;
import org.springframework.transaction.annotation.Transactional;


@Service("webSubjectService")
@Transactional
public class WebSubjectServiceImpl extends ServiceImpl<WebSubjectDao, WebSubjectEntity> implements WebSubjectService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebSubjectEntity> page = this.page(
                new Query<WebSubjectEntity>().getPage(params),
                new QueryWrapper<WebSubjectEntity>().orderBy(true,true,"sort")
        );

        return new PageUtils(page);
    }

}
