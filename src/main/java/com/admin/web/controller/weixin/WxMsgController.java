package com.admin.web.controller.weixin;

import com.admin.web.model.WxMsg;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.jfinal.MsgControllerAdapter;
import com.jfinal.weixin.sdk.msg.in.*;
import com.jfinal.weixin.sdk.msg.in.event.*;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import com.jfinal.weixin.sdk.msg.out.OutCustomMsg;
import com.jfinal.weixin.sdk.msg.out.OutImageMsg;
import com.jfinal.weixin.sdk.msg.out.OutNewsMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;

/**
 * 微信 消息
 */
@ControllerBind(controllerKey = "/wx/msg")
public class WxMsgController extends MsgControllerAdapter {

    static Log logger = Log.getLog(WxMsgController.class);
    private final String appUrl = "http://hboss.htmlk.cn/#!/";

    /**
     * 文字消息 / 关键字
     * @param inTextMsg
     */
    protected void processInTextMsg(InTextMsg inTextMsg) {

        OutTextMsg outMsg = new OutTextMsg(inTextMsg);
        String msgContent = inTextMsg.getContent().trim();
        String appid = getPara("appId");
        logger.info(inTextMsg.toString());


        if (appid.equals("wx3ce66e0d82944807")){    //订阅号
            if (msgContent.contains("招聘")){
                OutNewsMsg newMsg = new OutNewsMsg(inTextMsg);      //图文消息


                newMsg.addNews("招聘，找工作？点我呢！！",
                        "点击开启",
                        "http://hboss.htmlk.cn/images/icons/zhaopin.jpg",
                        appUrl);
                render(newMsg);
            }else if(msgContent.equals("聊天记录") || msgContent.equals("结论")){
                OutImageMsg imageMsg = new OutImageMsg(inTextMsg);
                imageMsg.setMediaId("DSa17754oslGqN2uHyYW6KzgBYXAe6LEaSk9HcfDboE");

                render(imageMsg);
            }else if(msgContent.equals("避税")){
                OutImageMsg imageMsg = new OutImageMsg(inTextMsg);
                imageMsg.setMediaId("DSa17754oslGqN2uHyYW6PJrgiWDiQjBBdQgr-Mjyng");

                render(imageMsg);
            } else {       //关键字 消息返回
                WxMsg wxMsg = this.keyWord(msgContent);
                if (wxMsg != null){
                    System.out.println("wxmsg " + wxMsg.toString());

                    if (wxMsg.getMsgType().equals("0")){        //文字消息
                        outMsg.setContent(wxMsg.getText());
                        render(outMsg);
                    }else if (wxMsg.getMsgType().equals("1")){  //图文消息
                        OutNewsMsg imgMsg = new OutNewsMsg(inTextMsg);

                        imgMsg.addNews(wxMsg.getNewTitle(), wxMsg.getNewDescription(), wxMsg.getNewPicUrl(), wxMsg.getNewUrl());
                        render(imgMsg);
                    }else if (wxMsg.getMsgType().equals("2")){  //图片消息
                        OutImageMsg imageMsg = new OutImageMsg(inTextMsg);
                        imageMsg.setMediaId(wxMsg.getText());

                        render(imageMsg);
                    }
                }else {
                    render(outMsg);
                }
            }
        }else if (appid.equals("wxc6fffa9280cb63e9")){
            OutNewsMsg newMsg = new OutNewsMsg(inTextMsg);      //图文消息

            newMsg.addNews("招聘，找工作？点我呢！！",
                    "点击开启",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502034011790&di=d1e15a890ec2b3d2292d42e7c0381464&imgtype=0&src=http%3A%2F%2Fimg161.poco.cn%2Fmypoco%2Fmyphoto%2F20100424%2F19%2F53310080201004241856521800459127582_005.jpg",
                    "http://tianye.work");
            render(newMsg);
        }else {
            OutNewsMsg newMsg = new OutNewsMsg(inTextMsg);      //图文消息

            newMsg.addNews("招聘，找工作？点我呢！！",
                    "点击开启",
                    "http://hboss.htmlk.cn/images/icons/zhaopin.jpg",
                    appUrl);
            render(newMsg);
        }

    }

    /**
     * 关键字回复
     * @return
     */
    private WxMsg keyWord(String key){
        return WxMsg.dao.findByKeyWord(key);
    }


    /**
     * 处理语音消息
     * @param inVoiceMsg
     */
    @Override
    protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
        OutTextMsg outMsg = new OutTextMsg(inVoiceMsg);
        WxMsg wxMsg = WxMsg.dao.findByType("1");
        outMsg.setContent(wxMsg.getText());
    }

    /**
     * 处理视频消息
     * @param inVideoMsg
     */
    @Override
    protected void processInVideoMsg(InVideoMsg inVideoMsg) {
        OutTextMsg outMsg = new OutTextMsg(inVideoMsg);
        WxMsg wxMsg = WxMsg.dao.findByType("1");
        outMsg.setContent(wxMsg.getText());
    }

    /**
     * 小视频 消息
     * @param inShortVideoMsg
     */
    @Override
    protected void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg) {
        OutTextMsg outMsg = new OutTextMsg(inShortVideoMsg);
        WxMsg wxMsg = WxMsg.dao.findByType("1");
        outMsg.setContent(wxMsg.getText());
    }

    /**
     * 处理地址位置消息
     * @param inLocationMsg
     */
    @Override
    protected void processInLocationMsg(InLocationMsg inLocationMsg) {
        //转发给多客服PC客户端
        OutCustomMsg outCustomMsg = new OutCustomMsg(inLocationMsg);
        render(outCustomMsg);
    }

    /**
     * 处理链接消息 特别注意：测试时需要发送我的收藏中的曾经收藏过的图文消息，直接发送链接地址会当做文本消息来发送
     * @param inLinkMsg
     */
    @Override
    protected void processInLinkMsg(InLinkMsg inLinkMsg) {
        //转发给多客服PC客户端
        OutCustomMsg outCustomMsg = new OutCustomMsg(inLinkMsg);
        render(outCustomMsg);
    }

    @Override
    protected void processInCustomEvent(InCustomEvent inCustomEvent) {
        logger.debug("测试方法：processInCustomEvent()");
        renderNull();
    }

    /**
     * 处理图片消息
     * @param inImageMsg
     */
    protected void processInImageMsg(InImageMsg inImageMsg) {
        //转发给多客服PC客户端
        OutCustomMsg outCustomMsg = new OutCustomMsg(inImageMsg);
        render(outCustomMsg);
    }

    /**
     * 实现父类抽方法，处理关注/取消关注消息
     */
    protected void processInFollowEvent(InFollowEvent inFollowEvent) {
        if (InFollowEvent.EVENT_INFOLLOW_SUBSCRIBE.equals(
                inFollowEvent.getEvent())) {
            logger.debug("关注：" + inFollowEvent.getFromUserName());

            OutTextMsg outMsg = new OutTextMsg(inFollowEvent);
            WxMsg wxMsg = WxMsg.dao.findByType("2");
            outMsg.setContent(wxMsg.getText());
            render(outMsg);
        }

        // 如果为取消关注事件，将无法接收到传回的信息
        if (InFollowEvent.EVENT_INFOLLOW_UNSUBSCRIBE.equals(
                inFollowEvent.getEvent())) {
            logger.debug("取消关注：" + inFollowEvent.getFromUserName());
        }
    }

    @Override
    protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
        if (InQrCodeEvent.EVENT_INQRCODE_SUBSCRIBE.equals(
                inQrCodeEvent.getEvent())) {
            logger.debug("扫码未关注：" + inQrCodeEvent.getFromUserName());

            OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
            WxMsg wxMsg = WxMsg.dao.findByType("2");
            outMsg.setContent(wxMsg.getText());
            render(outMsg);
        }

        if (InQrCodeEvent.EVENT_INQRCODE_SCAN.equals(inQrCodeEvent.getEvent())) {
            logger.debug("扫码已关注：" + inQrCodeEvent.getFromUserName());
        }
    }

    @Override
    protected void processInLocationEvent(InLocationEvent inLocationEvent) {
        logger.debug("发送地理位置事件：" + inLocationEvent.getFromUserName());

        OutTextMsg outMsg = new OutTextMsg(inLocationEvent);
        outMsg.setContent("地理位置是：" + inLocationEvent.getLatitude());
        render(outMsg);
    }

    @Override
    protected void processInMassEvent(InMassEvent inMassEvent) {
        logger.debug("测试方法：processInMassEvent()");
        renderNull();
    }

    /**
     * 实现父类抽方法，处理自定义菜单事件
     */
    protected void processInMenuEvent(InMenuEvent inMenuEvent) {
        logger.debug("菜单事件：" + inMenuEvent.getFromUserName());

        OutTextMsg outMsg = new OutTextMsg(inMenuEvent);
        outMsg.setContent("菜单事件内容是：" + inMenuEvent.getEventKey());
        render(outMsg);
    }

    @Override
    protected void processInSpeechRecognitionResults(
            InSpeechRecognitionResults inSpeechRecognitionResults) {
        logger.debug("语音识别事件：" + inSpeechRecognitionResults.getFromUserName());

        OutTextMsg outMsg = new OutTextMsg(inSpeechRecognitionResults);
        outMsg.setContent("语音识别内容是：" +
                inSpeechRecognitionResults.getRecognition());
        render(outMsg);
    }

    /**
     * 处理接收到的模板消息是否送达成功通知事件
     * @param inTemplateMsgEvent
     */
    @Override
    protected void processInTemplateMsgEvent(
            InTemplateMsgEvent inTemplateMsgEvent) {
        logger.debug("测试方法：processInTemplateMsgEvent()");
        renderNull();
    }
}
