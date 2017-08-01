package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.JobInfo;
import com.admin.web.model.Someone;
import com.admin.web.model.UserCollection;
import com.admin.web.model.UserInfo;
import com.admin.web.util.R;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  招聘 用户 controller
 */
@ControllerBind(controllerKey = "/job/user")
public class JobUserController extends BaseBussinessController {

    /**
     * 更新user 参数
     * 不支持图片上传
     */
    public void updateUser(){
        Integer userId = getParaToInt("userId",0);
        String tel = getPara("tel");
        String userName = getPara("userName");

        if (userId == 0 ){
            renderJson(R.error("参数错误"));
        }else {
            UserInfo userInfo = new UserInfo();

            userInfo.setId(userId);

            if (StrKit.notBlank(tel)){
                userInfo.setTel(tel);
            }

            if (StrKit.notBlank(userName)){
                userInfo.setName(userName);
            }

            userInfo.update();
            renderJson(R.ok());
        }

    }

    /**
     * 获取 用户
     * 参数 openid
     */
    public void getUserInfo(){
        Integer openId = getParaToInt("openId");

        UserInfo userInfo = UserInfo.dao.findFirst("select * from userInfo where openId = ?",openId);

        renderJson(R.ok().put("user", userInfo));
    }

    /**
     * 我的发布
     *
     */
    public void myPublish(){
        Integer userId = getParaToInt("userId");

        // 职位 集合
        List<JobInfo> jobInfoList = JobInfo.dao.find("select * from j_job_info where userId = ?", userId);
        // 找人办事 集合
        List<Someone> someoneList = Someone.dao.find("select * from j_job_info where userId = ?", userId);

        Map<String , Object> map = new HashMap<>();
        map.put("jobList", jobInfoList);
        map.put("someoneList", someoneList);

        renderJson(R.ok().put(map));
    }

    /**
     * 我的收藏
     */
    public void myCollection(){
        Integer userId = getParaToInt("userId");

        List<UserCollection> collectionList = UserCollection.dao.find("select * from user_collection where userId = ?",
                                                 userId);

        renderJson(R.ok().put(collectionList));

    }



	@Override
	public void onExceptionError(Exception e) {
		// TODO Auto-generated method stub
		
	}

}
