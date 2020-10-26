package io.renren.modules.employment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.modules.employment.dao.EmploymentDao;
import io.renren.modules.employment.entity.Employment;
import io.renren.modules.employment.service.EmploymentService;
import org.springframework.stereotype.Service;

@Service("employmentService")
public class EmploymentServiceImpl extends ServiceImpl<EmploymentDao, Employment> implements EmploymentService {
}
