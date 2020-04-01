package io.renren.modules.generator.controller;

import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.utils.StringUtil;
import io.renren.modules.generator.entity.WebDoctorCommentEntity;
import io.renren.modules.generator.entity.WebHospitalEntity;
import io.renren.modules.generator.entity.WebQuestionnaireDetailEntity;
import io.renren.modules.generator.entity.WebQuestionnaireEntity;
import io.renren.modules.generator.entity.WebUserEntity;
import io.renren.modules.generator.service.WebDoctorCommentService;
import io.renren.modules.generator.service.WebHospitalService;
import io.renren.modules.generator.service.WebQuestionnaireDetailService;
import io.renren.modules.generator.service.WebQuestionnaireService;
import io.renren.modules.generator.service.WebUserRatingDayService;
import io.renren.modules.generator.service.WebUserService;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysUserEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;



/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@RestController
@RequestMapping("generator/webuser")
public class WebUserController extends AbstractController {
    @Autowired
    private WebDoctorCommentService webDoctorCommentService;

    @Autowired
    private WebQuestionnaireService webQuestionnaireService;

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebHospitalService webHospitalService;

    @Autowired
    private WebQuestionnaireDetailService webQuestionnaireDetailService;

    @Autowired
    private WebUserRatingDayService webUserRatingDayService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        paramValue(params);
        PageUtils page = webUserService.queryPage(params);
        QueryWrapper<WebHospitalEntity> queryWrapper = new QueryWrapper<>();
        List<Long> hoList = new ArrayList<>();
        if(page.getList()!=null && page.getList().size()>0) {
            for (WebUserEntity user : (List<WebUserEntity>) page.getList()) {
                if (user.getHospitalId() != null) {
                    hoList.add(user.getHospitalId());
                }
            }
            queryWrapper.in("id",hoList.toArray());
            List<WebHospitalEntity> list = webHospitalService.list(queryWrapper);
            for(WebUserEntity user : (List<WebUserEntity>)page.getList()){
                if(user.getHospitalId()!=null && list!=null && list.size()>0){
                    for(WebHospitalEntity hbean:list){
                        if(hbean.getId().equals(user.getHospitalId())){
                            user.setHospitalName(hbean.getHospitalName());
                        }
                    }
                }
            }
        }
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{userId}")
    public R info(@PathVariable("userId") Long userId){
        if(userId == null) {
            return R.error("userId不能为空");
        }
        WebUserEntity webUser = webUserService.getById(userId);
        if(webUser == null){
            return R.error("没有获取到用户信息");
        }
        webUser.setPassword("");

        //获取当前登录用户的最新诊断信息
        QueryWrapper<WebQuestionnaireEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.isNotNull("noun_value");
        queryWrapper.orderByDesc("sub_time","verb_status","score");
        List<WebQuestionnaireEntity> webQuestionnaireEntity = webQuestionnaireService.list(queryWrapper);
        if (webQuestionnaireEntity != null && webQuestionnaireEntity.size()>0) {
            webUser.setNounValue(webQuestionnaireEntity.get(0).getNounValue());
            webUser.setNounValueTime(webQuestionnaireEntity.get(0).getSubTime());
            //获取结果明细
            List<WebQuestionnaireDetailEntity> list = webQuestionnaireDetailService.list(new QueryWrapper<WebQuestionnaireDetailEntity>().eq("web_questionnaire_id",webQuestionnaireEntity.get(0).getId()));
            List<Map<String,Object>> listNonu = new ArrayList<>();
            if(list!=null && list.size()>0){
                //在患者端只显示最严重的级别，比如有红，有黄，显示红，并提示红色警告状态   索超改，已确认，两个共同的红都展示，两个共同都黄都展示
                Integer type = 0;
                for(WebQuestionnaireDetailEntity bean:list){
                    if(bean.getLevel() !=null && bean.getLevel() == 2){
                        type = 2;
                        break;
                    }
                    if(bean.getLevel() !=null && bean.getLevel() == 1){
                        type = 1;
                    }
                }
                for(WebQuestionnaireDetailEntity bean:list){
                   if(bean.getLevel() !=null && bean.getLevel() == type) {
                       Map<String, Object> map = new HashMap<>();
                       map.put("nounValue", bean.getNounValue());
                       map.put("nounValueTime", bean.getCreateTime());
                       listNonu.add(map);
                   }
                }
                webUser.setNounValueList(listNonu);
            }
        }
        //获取当前登录用户的最新医生建议
        QueryWrapper<WebDoctorCommentEntity> wdcQw = new QueryWrapper<>();
        wdcQw.eq("web_user_id", userId);
        wdcQw.orderByDesc("create_time");
        List<WebDoctorCommentEntity> webDoctorCommentEntity = webDoctorCommentService.list(wdcQw);
        if (webDoctorCommentEntity != null && webDoctorCommentEntity.size()>0) {
            webUser.setDoctorComment(webDoctorCommentEntity.get(0).getDoctorComment());
            webUser.setDoctorCommentTime(webDoctorCommentEntity.get(0).getCreateTime());
        }

        return R.ok().put("webUser", webUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WebUserEntity webUser){
		webUserService.save(webUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WebUserEntity webUser){
        if(webUser.getPassword()!=null && !webUser.getPassword().equals("")){
            webUser.setPassword(DigestUtils.sha256Hex(webUser.getPassword()));
        }else{
            webUser.setPassword(null);
        }
		webUserService.updateById(webUser);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] userIds){
		webUserService.removeByIds(Arrays.asList(userIds));

        return R.ok();
    }



    /**
     * 获取非健康人员分布数据
     */
    @RequestMapping("/distribution")
    public R distribution(String hospitalId){
    	List<WebUserEntity> list = null;
        //获取当前登录人信息
        Map<String,Object> params = new HashMap<String,Object>();
        paramValue(params);
        QueryWrapper<WebUserEntity>  selectQuery = new QueryWrapper<>();
        selectQuery.isNotNull("rating");
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
        list = webUserService.list(selectQuery);

    	int health = 0;
    	int suspected = 0;
    	int mild = 0;
    	int severe = 0;
    	for(WebUserEntity li:list){
    		switch (li.getRating()) {
    		case 0:
    			health++;
				break;
			case 1:
				suspected++;
				break;
			case 2:
				mild++;
				break;
			case 3:
				severe++;
				break;
			default:
				break;
			}
    	}
    	Map<String,Object> map0 = new HashMap<String, Object>();
    	map0.put("value", health);
    	map0.put("name", "健康");

    	Map<String,Object> map1 = new HashMap<String, Object>();
    	map1.put("value", suspected);
    	map1.put("name", "风险");

    	Map<String,Object> map2 = new HashMap<String, Object>();
    	map2.put("name", "轻症");
    	map2.put("value", mild);

    	Map<String,Object> map3 = new HashMap<String, Object>();
    	map3.put("name", "重症");
    	map3.put("value", severe);


    	List<Map<String,Object>> listMap = new ArrayList<>();

    	listMap.add(map0);
    	listMap.add(map1);
    	listMap.add(map2);
    	listMap.add(map3);

        return R.ok().put("data", listMap);
    }

    /**
     * 获取各症状排名前五的社区
     */

    @RequestMapping("/ranking")
    public R ranking(){
        //获取当前登录人信息
        Map<String,Object> params = new HashMap<String,Object>();
        paramValue(params);
    	List<Map<String,Object>> list = webUserService.ranking(params);
    	if(null == list){
    		return R.error("获取排行异常");
    	}
        return R.ok().put("data",list);
    }


    /**
     * 每日数据总览接口
     */
    @RequestMapping("/collect")
    public R collect(){
        Map<String,Object> params = new HashMap<String,Object>();
        paramValue(params);
        QueryWrapper<WebHospitalEntity>  selectQuery = new QueryWrapper<>();
        if(params.get("area")!=null && !params.get("area").equals(""))
        {
            selectQuery.eq("area",params.get("area"));
        }
        if(params.get("district")!=null && !params.get("district").equals(""))
        {
            selectQuery.eq("district",params.get("district"));
        }
        if(params.get("hospitalName")!=null && !params.get("hospitalName").equals(""))
        {
            selectQuery.eq("hospitalName",params.get("hospitalName"));
        }


    	int communityNum = webHospitalService.count(selectQuery);//社区个数

        QueryWrapper<WebUserEntity>  selectUserQuery1 = new QueryWrapper<>();
        setSelectUserQuery(selectUserQuery1,params);
    	int userNum = webUserService.count(selectUserQuery1);//注册人员总数

        QueryWrapper<WebUserEntity>  selectUserQuery2 = new QueryWrapper<>();
        setSelectUserQuery(selectUserQuery2,params);
        selectUserQuery2.eq("rating", 0);
    	int healthNum = webUserService.count(selectUserQuery2);//健康人员总数

        QueryWrapper<WebUserEntity>  selectUserQuery3 = new QueryWrapper<>();
        setSelectUserQuery(selectUserQuery3,params);
        selectUserQuery3.eq("rating", 1);
    	int suspectedNum = webUserService.count(selectUserQuery3);//风险人员总数

        QueryWrapper<WebUserEntity>  selectUserQuery4 = new QueryWrapper<>();
        setSelectUserQuery(selectUserQuery4,params);
        selectUserQuery4.eq("rating", 2);
    	int mildNum = webUserService.count(selectUserQuery4);//轻症总数

        QueryWrapper<WebUserEntity>  selectUserQuery5 = new QueryWrapper<>();
        setSelectUserQuery(selectUserQuery5,params);
        selectUserQuery5.eq("rating", 3);
    	int severeNum = webUserService.count(selectUserQuery5);//重症总数
    	int illnessNum = mildNum + severeNum;//确诊人数

        QueryWrapper<WebUserEntity>  selectUserQuery6 = new QueryWrapper<>();
        setSelectUserQuery(selectUserQuery6,params);
    	String date = DateUtils.format(new Date());
        selectUserQuery6.like("create_time", date);
    	int newUserNum = webUserService.count(selectUserQuery6);//今日新增的注册人数

        params.put("reportDate", date);
        params.put("rating", 1);
    	int newSuspectedNum = webUserRatingDayService.collect(params);//今日新增的风险人数
        params.put("rating", 2);
    	int newMildNum = webUserRatingDayService.collect(params);//今日新增的轻症人数
        params.put("rating", 3);
    	int newSevereNum = webUserRatingDayService.collect(params);//今日新增的重症人数
    	int newIllnessNum = newMildNum + newSevereNum;//今日新增确诊人数

    	Map<String,Object> map = new HashMap<>();
    	map.put("communityNum", communityNum);
    	map.put("userNum", userNum);
    	map.put("healthNum", healthNum);
    	map.put("suspectedNum", suspectedNum);
    	map.put("mildNum", mildNum);
    	map.put("severeNum", severeNum);
    	map.put("illnessNum", illnessNum);
    	map.put("newUserNum", newUserNum);
    	map.put("newSuspectedNum", newSuspectedNum);
    	map.put("newMildNum", newMildNum);
    	map.put("newSevereNum", newSevereNum);
    	map.put("newIllnessNum", newIllnessNum);

        return R.ok().put("data", map);
    }

    public void setSelectUserQuery(QueryWrapper<WebUserEntity>  selectUserQuery, Map<String,Object> params ){
        if(params.get("area")!=null && !params.get("area").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.area = \""+params.get("area")+"\" and hospital_id = wh.id";
            selectUserQuery.exists(existsSql);
        }
        if(params.get("district")!=null && !params.get("district").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.district = \""+params.get("district")+"\" and hospital_id = wh.id";
            selectUserQuery.exists(existsSql);
        }
        if(params.get("hospitalName")!=null && !params.get("hospitalName").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.hospital_name = \""+params.get("hospitalName")+"\" and hospital_id = wh.id";
            selectUserQuery.exists(existsSql);
        }
    }

    /**
     * 数据总览接口
     */
    @RequestMapping("/pandect")
    public R pandect(){
        Map<String,Object> params = new HashMap<String,Object>();
        paramValue(params);
        QueryWrapper<WebHospitalEntity>  selectQuery = new QueryWrapper<>();
        if(params.get("area")!=null && !params.get("area").equals(""))
        {
            selectQuery.eq("area",params.get("area"));
        }
        if(params.get("district")!=null && !params.get("district").equals(""))
        {
            selectQuery.eq("district",params.get("district"));
        }
        if(params.get("hospitalName")!=null && !params.get("hospitalName").equals(""))
        {
            selectQuery.eq("hospitalName",params.get("hospitalName"));
        }
    	int communityNum = webHospitalService.count(selectQuery);//社区个数

        QueryWrapper<WebUserEntity>  selectUserQuery1 = new QueryWrapper<>();
        setSelectUserQuery(selectUserQuery1,params);
    	int userNum = webUserService.count(selectUserQuery1);//注册人员总数
        QueryWrapper<WebQuestionnaireEntity>  selectUserQuery2 = new QueryWrapper<>();
        if(params.get("area")!=null && !params.get("area").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.area = \""+params.get("area")+"\" and hospital_id = wh.id";
            selectUserQuery2.exists(existsSql);
        }
        if(params.get("district")!=null && !params.get("district").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.district = \""+params.get("district")+"\" and hospital_id = wh.id";
            selectUserQuery2.exists(existsSql);
        }
        if(params.get("hospitalName")!=null && !params.get("hospitalName").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.hospital_name = \""+params.get("hospitalName")+"\" and hospital_id = wh.id";
            selectUserQuery2.exists(existsSql);
        }
    	int questionnaireNum = webQuestionnaireService.count(selectUserQuery2);//上报信息数

    	Map<String,Object> map = new HashMap<>();
    	map.put("communityNum", communityNum);
    	map.put("userNum", userNum);
    	map.put("questionnaireNum", questionnaireNum);

        return R.ok().put("data", map);
    }

    /**
     * 数据总览接口
     */
    @GetMapping("/communityPandect")
    public R communityPandect(String hospitalId){
        Map<String,Object> params = new HashMap<String,Object>();
        paramValue(params);
    	List<Integer> ratingList = webUserService.communityState(params);
        int rating = 0 ;
        if(ratingList!=null && ratingList.size()>0) {
            rating = Collections.max(ratingList);
        }

        QueryWrapper<WebUserEntity>  selectUserQuery1 = new QueryWrapper<>();
        if(params.get("area")!=null && !params.get("area").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.area = \""+params.get("area")+"\" and hospital_id = wh.id";
            selectUserQuery1.exists(existsSql);
        }
        if(params.get("district")!=null && !params.get("district").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.district = \""+params.get("district")+"\" and hospital_id = wh.id";
            selectUserQuery1.exists(existsSql);
        }
        if(params.get("hospitalName")!=null && !params.get("hospitalName").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.hospital_name = \""+params.get("hospitalName")+"\" and hospital_id = wh.id";
            selectUserQuery1.exists(existsSql);
        }
    	int userNum = webUserService.count(selectUserQuery1);//注册人员总数

        QueryWrapper<WebQuestionnaireEntity>  selectUserQuery2 = new QueryWrapper<>();
        if(params.get("area")!=null && !params.get("area").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.area = \""+params.get("area")+"\" and hospital_id = wh.id";
            selectUserQuery2.exists(existsSql);
        }
        if(params.get("district")!=null && !params.get("district").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.district = \""+params.get("district")+"\" and hospital_id = wh.id";
            selectUserQuery2.exists(existsSql);
        }
        if(params.get("hospitalName")!=null && !params.get("hospitalName").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.hospital_name = \""+params.get("hospitalName")+"\" and hospital_id = wh.id";
            selectUserQuery2.exists(existsSql);
        }

    	int questionnaireNum = webQuestionnaireService.count(selectUserQuery2);//上报信息数

        QueryWrapper<WebQuestionnaireEntity>  selectUserQuery3 = new QueryWrapper<>();
    	String date = DateUtils.format(new Date());
        selectUserQuery3.like("sub_time", date);
        if(params.get("area")!=null && !params.get("area").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.area = \""+params.get("area")+"\" and hospital_id = wh.id";
            selectUserQuery3.exists(existsSql);
        }
        if(params.get("district")!=null && !params.get("district").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.district = \""+params.get("district")+"\" and hospital_id = wh.id";
            selectUserQuery3.exists(existsSql);
        }
        if(params.get("hospitalName")!=null && !params.get("hospitalName").equals(""))
        {
            String existsSql = "select id from web_hospital wh where wh.hospital_name = \""+params.get("hospitalName")+"\" and hospital_id = wh.id";
            selectUserQuery3.exists(existsSql);
        }
    	int newQuestionnaireNum = webQuestionnaireService.count(selectUserQuery3);//上报信息数

    	Map<String,Object> map = new HashMap<>();
    	map.put("newQuestionnaireNum", newQuestionnaireNum);
    	map.put("userNum", userNum);
    	map.put("questionnaireNum", questionnaireNum);
    	map.put("rating", rating);

        return R.ok().put("data", map);
    }


}
