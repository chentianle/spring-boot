package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.modules.generator.dao.WebUserDayorderbyDao;
import io.renren.modules.generator.entity.WebUserDayorderbyEntity;
import io.renren.modules.generator.service.WebUserDayorderbyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("webUserDayorderby")
@Transactional
public class WebUserDayorderbyServiceImpl extends ServiceImpl<WebUserDayorderbyDao, WebUserDayorderbyEntity> implements WebUserDayorderbyService {



}
