package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.dao.WebCommunityHelpDao;
import io.renren.modules.generator.entity.WebCommunityHelpEntity;
import io.renren.modules.generator.entity.WebQuestionnaireEntity;
import io.renren.modules.generator.service.WebCommunityHelpService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("webCommunityHelpService")
@Transactional
public class WebCommunityHelpServiceImpl extends ServiceImpl<WebCommunityHelpDao, WebCommunityHelpEntity> implements WebCommunityHelpService {

	@Resource
	private WebCommunityHelpDao webCommunityHelpDao;

    /**
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageBylist(Map<String, Object> params) {
        List<WebQuestionnaireEntity> list = new ArrayList<>();
        Integer  count = baseMapper.queryPageByCount(params);
        if(count!=null && count>0) {
            //分页计算
            PageUtils.pageCalculation(params);
            list = baseMapper.queryPageBylist(params);
        }
        return new PageUtils(list,count,Integer.parseInt(params.get("limit")+""),Integer.parseInt(params.get("page")+""));
    }

	@Override
	public List<Map<String, Object>> communityConstantly(
			Map<String, Object> params) {
		return webCommunityHelpDao.communityConstantly(params);
	}

	@Override
	public List<Map<String, Object>> constantly(Map<String, Object> params) {
		return webCommunityHelpDao.constantly(params);
	}

    @Override
    public WebCommunityHelpEntity queryById(String id) {
        return baseMapper.queryById(id);
    }
}
