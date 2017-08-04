package com.admin.web.controller.job;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.*;
import com.admin.web.swagger.annotation.Api;
import com.admin.web.swagger.annotation.ApiOperation;
import com.admin.web.swagger.annotation.Param;
import com.admin.web.swagger.annotation.Params;
import com.admin.web.util.R;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  招聘 用户 controller
 */
@Api(tag = "JobUserController", description = "用户 接口")
@ControllerBind(controllerKey = "/job/user")
public class JobUserController extends BaseBussinessController {

    @ApiOperation(description = " 更新user 参数(目前不支持图片上传, 头像修改)" ,url = "/job/user/updateUser", tag = "JobUserController", httpMethod = "get")
    @Params({
            @Param(name = "userId", description = "用户id 必填", dataType = "int"),
            @Param(name = "tel", description = "电话号码 选填", dataType = "String"),
            @Param(name = "userName", description = "用户名 选填", dataType = "String")
    })
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


    @ApiOperation(description = " 通过openid 获取 用户" ,url = "/job/user/getUserInfo", tag = "JobUserController", httpMethod = "get")
    @Params({
            @Param(name = "openId", description = "openId 必填", dataType = "String")
    })
    public void getUserInfo(){
        Integer openId = getParaToInt("openId");

        UserInfo userInfo = UserInfo.dao.findFirst("select * from userInfo where openId = ?",openId);

        renderJson(R.ok().put("user", userInfo));
    }

    @ApiOperation(description = "我的发布" ,url = "/job/user/myPublish", tag = "JobUserController", httpMethod = "get")
    @Params({
            @Param(name = "userId", description = "用户id 必填", dataType = "int")
    })
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

    @ApiOperation(description = " 我的收藏" ,url = "/job/user/myCollection", tag = "JobUserController", httpMethod = "get")
    @Params({
            @Param(name = "userId", description = "用户id 必填", dataType = "int")
    })
    public void myCollection(){
        Integer userId = getParaToInt("userId");

        List<UserCollection> collectionList = UserCollection.dao.find("select * from user_collection where userId = ?",
                                                 userId);

        renderJson(R.ok().put(collectionList));

    }

    @ApiOperation(description = " 意见反馈 保存" ,url = "/job/user/saveProposal", tag = "JobUserController", httpMethod = "get")
    @Params({
            @Param(name = "userId", description = "反馈人id(当前人) 必填", dataType = "int"),
            @Param(name = "userName", description = "反馈人名称 必填", dataType = "String"),
            @Param(name = "details", description = "反馈详情 必填", dataType = "String")
    })
    public void saveProposal(){
        Integer userId = getParaToInt("userId");
        String userName = getPara("userName");
        String details = getPara("details");

        Proposal proposal = new Proposal();
        proposal.setUserId(userId);
        proposal.setUserName(userName);
        proposal.setDetails(details);

        proposal.save();

        renderJson(R.ok());
    }



	@Override
	public void onExceptionError(Exception e) {
		// TODO Auto-generated method stub
		
	}

}
