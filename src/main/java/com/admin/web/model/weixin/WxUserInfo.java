package com.admin.web.model.weixin;

/**
 * 微信用户信息
 */
public class WxUserInfo {

    private String city;
    private String country;
    private String groupid;
    private String headimgurl;
    private String language;
    private String nickname;
    private String openid;
    private String province;
    private String remark;
    private String sex;
    private String subscribe;
    private String subscribe_time;
    private String unionid;


    public WxUserInfo(String city, String country, String groupid, String headimgurl, String language, String nickname,
                      String openid, String province, String remark, String sex, String subscribe, String subscribe_time,
                      String unionid) {
        this.city = city;
        this.country = country;
        this.groupid = groupid;
        this.headimgurl = headimgurl;
        this.language = language;
        this.nickname = nickname;
        this.openid = openid;
        this.province = province;
        this.remark = remark;
        this.sex = sex;
        this.subscribe = subscribe;
        this.subscribe_time = subscribe_time;
        this.unionid = unionid;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"city\":\"")
                .append(city).append('\"');
        sb.append(",\"country\":\"")
                .append(country).append('\"');
        sb.append(",\"groupid\":\"")
                .append(groupid).append('\"');
        sb.append(",\"headimgurl\":\"")
                .append(headimgurl).append('\"');
        sb.append(",\"language\":\"")
                .append(language).append('\"');
        sb.append(",\"nickname\":\"")
                .append(nickname).append('\"');
        sb.append(",\"openid\":\"")
                .append(openid).append('\"');
        sb.append(",\"province\":\"")
                .append(province).append('\"');
        sb.append(",\"remark\":\"")
                .append(remark).append('\"');
        sb.append(",\"sex\":\"")
                .append(sex).append('\"');
        sb.append(",\"subscribe\":\"")
                .append(subscribe).append('\"');
        sb.append(",\"subscribe_time\":\"")
                .append(subscribe_time).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getSubscribe_time() {
        return subscribe_time;
    }

    public void setSubscribe_time(String subscribe_time) {
        this.subscribe_time = subscribe_time;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
