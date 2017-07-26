package com.admin.web.model.weixin;

/**
 * Created by xvtie on 2017/7/14.
 */
public class WxMediaArticles {

    /**
     * 消息名称
     *
     */
    private String Title;
    /**
     * 消息描述
     */
    private String Description;

    /**
     * 图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80
     */
    private String PicUrl;

    /**
     * 点击图文消息跳转链接
     */
    private String Url;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"Title\":\"")
                .append(Title).append('\"');
        sb.append(",\"Description\":\"")
                .append(Description).append('\"');
        sb.append(",\"PicUrl\":\"")
                .append(PicUrl).append('\"');
        sb.append(",\"Url\":\"")
                .append(Url).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public WxMediaArticles() {
    }

    public WxMediaArticles(String title, String description, String picUrl, String url) {
        Title = title;
        Description = description;
        PicUrl = picUrl;
        Url = url;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
