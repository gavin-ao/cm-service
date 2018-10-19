package data.driven.cm.entity.tourism;

import java.util.Date;

/**
 * 分享详情
 * @author hejinkai
 * @date 2018/6/27
 */
public class ShareInfoEntity {
    /** 分享id **/
    private String shareId;
    /** 小程序id **/
    private String appInfoId;
    /** 分享者（用户与小程序关联表主键sys_map_id） **/
    private String formId;
    /** 分享者（用户表外键。多个应用id都一样） **/
    private String formUserInfoId;
    /** 被分享者（用户与小程序关联表主键sys_map_id） **/
    private String toId;
    /** 被分享者（用户表外键。多个应用id都一样） **/
    private String toUserInfoId;
    /** 分享次数 **/
    private Integer frequency;
    /** 分享时间 **/
    private Date shareAt;
    /** 最后一次分享时间 **/
    private Date lastShareAt;

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getAppInfoId() {
        return appInfoId;
    }

    public void setAppInfoId(String appInfoId) {
        this.appInfoId = appInfoId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormUserInfoId() {
        return formUserInfoId;
    }

    public void setFormUserInfoId(String formUserInfoId) {
        this.formUserInfoId = formUserInfoId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getToUserInfoId() {
        return toUserInfoId;
    }

    public void setToUserInfoId(String toUserInfoId) {
        this.toUserInfoId = toUserInfoId;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Date getShareAt() {
        return shareAt;
    }

    public void setShareAt(Date shareAt) {
        this.shareAt = shareAt;
    }

    public Date getLastShareAt() {
        return lastShareAt;
    }

    public void setLastShareAt(Date lastShareAt) {
        this.lastShareAt = lastShareAt;
    }
}
