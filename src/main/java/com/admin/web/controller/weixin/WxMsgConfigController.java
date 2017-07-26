package com.admin.web.controller.weixin;

import com.admin.web.base.BaseBussinessController;
import com.admin.web.model.WxMsg;
import com.admin.web.util.WxUtils;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.rlax.framework.common.Consts;
import com.rlax.framework.exception.BusinessException;
import com.rlax.web.base.MessageBean;

/**
 * 微信 消息回复 设置
 */
@ControllerBind(controllerKey = "/weixin/msg")
public class WxMsgConfigController extends BaseBussinessController {

    /**
     * 关注回复/消息文字
     * 类型  1默认回复 2关注时回复 3关键字回复
     */
    public void index() {

        // 关注回复
        WxMsg wxMsg =  WxMsg.dao.findByType("1");
        String focus = wxMsg.getText();

        // 文字回复
        wxMsg = wxMsg.dao().findByType("2");
        String textMsg = wxMsg.getText();

        keepPara();
        setAttr("focusMsg", focus);
        setAttr("textMsg", textMsg);
        // helpStr.replaceAll ("#br#", "\n")
        render("main.html");
    }

    /**
     * 修改
     */
    public void update(){
        String focusMsg = getPara("focusMsg");
        String textMsg = getPara("textMsg");

        if (StrKit.notBlank(focusMsg)){
            WxMsg.dao.findByType("1").set("text", focusMsg).update();
        }
        if (StrKit.notBlank(textMsg)){
            WxMsg.dao.findByType("2").set("text", textMsg).update();
        }
        redirect("/weixin/msg");
    }

    /**
     * 关键列表
     */
    public void keyword(){
        int page = getParaToInt("_page", 1);

        Page<WxMsg> list = WxMsg.dao.findPage( page, Consts.PAGE_DEFAULT_SIZE);
        keepPara();
        setAttr("page", list);
        render("keyword.html");

    }

    public void getImageList(){
        ApiResult apiResult = WxUtils.batchGetMaterial("image", 0 ,20);
        System.out.println(apiResult.getJson());
    }

    /**
     * 添加/ 修改跳转
     */
    public void keyword_add(){
        createToken();
        render("keyword_add.html");
    }

    /**
     *
     * 关键字 保存
     */
    public void keyword_to_add(){
        if (!validateToken()) {
            throw new BusinessException("您已成功提交表单，请勿重复提交");
        }
        WxMsg info = getModel(WxMsg.class, "info");

        info.setType("3");

        info.save();
        redirect("/weixin/msg/keyword");
    }

    /**
     * 关键字删除
     */
    public void delete_keyword(){

        Integer keyId = getParaToInt("id");
        WxMsg.dao.deleteById(keyId);
        MessageBean message = new MessageBean();
        message.success();
        setSessionMessage(message);

        redirect("/weixin/msg/keyword");

    }

    @Override
    public void onExceptionError(Exception e) {

    }
}
