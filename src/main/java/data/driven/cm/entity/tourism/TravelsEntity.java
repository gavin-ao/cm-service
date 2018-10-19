package data.driven.cm.entity.tourism;

import java.util.Date;

/**
 * 景区游记信息
 * @author 何晋凯
 * @date 2018/06/07
 */
public class TravelsEntity {
    /** 游记id **/
    private String travelsId;
    /** 景区主键 **/
    private String scenicSpotId;
    /** 景区景观外键 **/
    private String sceneryId;
    /** 游记文字 **/
    private String travelsText;
    /** 微信用户 **/
    private String wechatUser;
    /** 创建时间 **/
    private Date createAt;

    public String getTravelsId() {
        return travelsId;
    }

    public void setTravelsId(String travelsId) {
        this.travelsId = travelsId;
    }

    public String getScenicSpotId() {
        return scenicSpotId;
    }

    public void setScenicSpotId(String scenicSpotId) {
        this.scenicSpotId = scenicSpotId;
    }

    public String getSceneryId() {
        return sceneryId;
    }

    public void setSceneryId(String sceneryId) {
        this.sceneryId = sceneryId;
    }

    public String getTravelsText() {
        return travelsText;
    }

    public void setTravelsText(String travelsText) {
        this.travelsText = travelsText;
    }

    public String getWechatUser() {
        return wechatUser;
    }

    public void setWechatUser(String wechatUser) {
        this.wechatUser = wechatUser;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
