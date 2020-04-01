package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.exception.RRException;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.common.validator.Assert;
import io.renren.modules.app.EnumPack.IfBasicFilledEnum;
import io.renren.modules.app.form.LoginForm;
import io.renren.modules.app.form.RegisterForm;
import io.renren.modules.generator.dao.WebUserDao;
import io.renren.modules.generator.entity.WebUserEntity;
import io.renren.modules.generator.service.WebUserService;
import io.renren.modules.sys.dao.SysCaptchaDao;
import io.renren.modules.sys.entity.SysCaptchaEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service("webUserService")
@Transactional
public class WebUserServiceImpl extends ServiceImpl<WebUserDao, WebUserEntity> implements WebUserService {

    @Resource
    private SysCaptchaDao sysCaptchaDao;

    @Resource
    private WebUserDao webUserDao;

    @Override
    public R validateUserPhone(RegisterForm form) {
        LambdaQueryWrapper<WebUserEntity> lqwebUser = Wrappers.lambdaQuery();
        lqwebUser.eq(WebUserEntity::getMobile,form.getMobile());
        lqwebUser.eq(WebUserEntity::getAreaCode,form.getAreaCode());
        List<WebUserEntity> webUserList = webUserDao.selectList(lqwebUser);
        if(webUserList.size() > 0){
            return R.error("手机号已注册");
        }

        return R.ok();
    }

    private R validateCaptcha(RegisterForm form) {
        LambdaQueryWrapper<SysCaptchaEntity> lqCaptcha = Wrappers.lambdaQuery();
        lqCaptcha.eq(SysCaptchaEntity::getUuid,form.getUuid());
        List<SysCaptchaEntity> captchaList = sysCaptchaDao.selectList(lqCaptcha);
        if(captchaList == null || captchaList.size() == 0){
            return R.error("没有获取到对应的验证码");
        }
        if(captchaList.size() > 1){
            return R.error("一个uuid 对应多个验证码");
        }
        SysCaptchaEntity sysCaptchaEntity = captchaList.get(0);
        if(!form.getCaptchaCode().equals(sysCaptchaEntity.getCode())){
            return R.error("验证码校验不正确");
        }
        long currentTime = new Date().getTime();
        long expireTime = sysCaptchaEntity.getExpireTime().getTime();
        if(currentTime > expireTime) {
            return R.error("验证码超时，请刷新");
        }
        return null;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WebUserEntity>  selectQuery = new QueryWrapper<>();
        if(params.get("username")!=null && !params.get("username").equals(""))
        {
            selectQuery.like("username",params.get("username"));
        }
        if(params.get("mobile")!=null && !params.get("mobile").equals(""))
        {
            selectQuery.like("mobile",params.get("mobile"));
        }
        if(params.get("area")!=null && !params.get("area").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.area = \""+params.get("area")+"\" and hospital_id = wh.id";
            selectQuery.exists(existsSql);
        }
        if(params.get("district")!=null && !params.get("district").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.district = \""+params.get("district")+"\" and hospital_id = wh.id";
            selectQuery.exists(existsSql);
        }
        if(params.get("hospitalName")!=null && !params.get("hospitalName").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.hospital_name = \""+params.get("hospitalName")+"\" and hospital_id = wh.id";
            selectQuery.exists(existsSql);
        }
        if(params.get("userStatus")!=null && !params.get("userStatus").equals(""))
        {
            selectQuery.eq("user_status",params.get("userStatus"));
        }
        if(params.get("rating")!=null && !params.get("rating").equals(""))
        {
            if(params.get("rating").equals("null")){
                selectQuery.isNull("rating");
            }else {
                selectQuery.eq("rating", params.get("rating"));
            }
        }
        selectQuery.orderByDesc("create_time");


        IPage<WebUserEntity> page = this.page(
                new Query<WebUserEntity>().getPage(params),selectQuery

        );

        return new PageUtils(page);
    }

    @Override
    public R register(WebUserEntity form) {

        if(null == form){
            return R.error("请填写注册消息");
        }

        if(StringUtils.isBlank(form.getMobile())){
            return R.error("请填写手机号");
        }

        if(StringUtils.isBlank(form.getPassword())){
            return R.error("请填写登录密码");
        }

        if(form.getHospitalId() == null){
            return R.error("请选择地区");
        }

        if(StringUtils.isBlank(form.getAreaCode())){
            return R.error("请选择国家或者地区");
        }

        Pattern pattern3 = Pattern.compile("^\\d{9,11}$");
        Matcher matcher3 = pattern3.matcher(form.getMobile());
        if(!matcher3.matches()){
            return R.error("请输入正确的手机号");
        }

        LambdaQueryWrapper<WebUserEntity> lqwebUser = Wrappers.lambdaQuery();
        lqwebUser.eq(WebUserEntity::getMobile,form.getMobile());
        lqwebUser.eq(WebUserEntity::getAreaCode,form.getAreaCode());
        List<WebUserEntity> webUserList = webUserDao.selectList(lqwebUser);
        if(webUserList.size() > 0){
            return R.error("手机号已注册");
        }

        form.setPassword(DigestUtils.sha256Hex(form.getPassword()));
        form.setCreateTime(new Date());
        form.setIfBasicFilled(IfBasicFilledEnum.unfilled.getValue());
        int insertNum = webUserDao.insert(form);
        if(insertNum != 1){
            return R.error();
        }
        return R.ok();
    }

    @Override
    public long login(LoginForm form) {
        LambdaQueryWrapper<WebUserEntity> lq = Wrappers.lambdaQuery();
        lq.eq(WebUserEntity::getMobile,form.getMobile());
        lq.eq(WebUserEntity::getAreaCode,form.getAreaCode());
        WebUserEntity user = webUserDao.selectOne(lq);
        Assert.isNull(user, "手机号或密码错误");

        //密码错误
        if(!user.getPassword().equals(DigestUtils.sha256Hex(form.getPassword()))){
            throw new RRException("手机号或密码错误");
        }

        return user.getUserId();
    }

//	@Override
//	public List<List<Map<String,Object>>> ranking() {
//		List<List<Map<String,Object>>> list = new ArrayList<>();
//		List<Map<String,Object>> mapList = webUserDao.ranking();
//
//		List<Map<String,Object>> suspectedMapList = new ArrayList<>();
//		List<Map<String,Object>> mildMapList = new ArrayList<>();
//		List<Map<String,Object>> severeMapList = new ArrayList<>();
//		List<Map<String,Object>> healthMapList = new ArrayList<>();
//
//		try {
//			suspectedMapList = listSortDesc(mapList,"suspected").subList(0, 5);
//			mildMapList = listSortDesc(mapList,"mild").subList(0, 5);
//			severeMapList = listSortDesc(mapList,"severe").subList(0, 5);
//			healthMapList = listSortDesc(mapList,"health").subList(0, 5);
//			listSortAsc(suspectedMapList,"value");
//			listSortAsc(mildMapList,"value");
//			listSortAsc(severeMapList,"value");
//			listSortAsc(healthMapList,"value");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		list.add(suspectedMapList);
//		list.add(mildMapList);
//		list.add(severeMapList);
//		list.add(healthMapList);
//		return list;
//	}


    @Override
	public List<Map<String,Object>> ranking(Map<String,Object> params) {

		List<Map<String,Object>> mapList = webUserDao.ranking(params);
		try {
//			list = format(mapList,"illness");
			listSortAsc(mapList,"illness");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return mapList;
	}


    public static List<Map<String, Object>> format(List<Map<String, Object>> list,String name) throws Exception {
        List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
        for(Map<String, Object> m:list){
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("hospitalName", m.get("hospitalName"));
        	map.put("value", m.get(name)!=null?Integer.valueOf(m.get(name).toString()):0);
        	mapList.add(map);
        }
		return mapList;
    }

    public static void listSortAsc(List<Map<String, Object>> list,String name) throws Exception {
        //   list是需要排序的list，其内放的是Map
        //   返回的结果集
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                //  o1，o2是list中的Map，可以在其内取得值，按其排序，此例为降序，s1和s2是排序字段值
                Integer s1 = null!=o1.get(name)?Integer.valueOf(o1.get(name).toString()):0;
                Integer s2 = null!=o2.get(name)?Integer.valueOf(o2.get(name).toString()):0;
                if (s1 > s2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

	@Override
	public List<Integer> communityState( Map<String,Object> params) {
		return webUserDao.communityState(params);
	}

}
