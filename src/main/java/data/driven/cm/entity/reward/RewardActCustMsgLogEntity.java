package data.driven.cm.entity.reward;

import java.util.Date;

/**
 * @author hejinkai
 * @date 2018/11/30
 */
public class RewardActCustMsgLogEntity {
    /** 主键 **/
    private String logId;
    /** 微信奖励id **/
    private String globalId;
    /** 活动id **/
    private String actId;
    /** 门店id **/
    private String storeId;
    /** 小程序id **/
    private String appInfoId;
    /** 用户的openid **/
    private String openid;
    /** 回复内容 **/
    private String content;
    /** 回复内容类型，1-文本，2-图片 **/
    private Integer contentType;
    /** 奖励类型，1 - 发起人奖励，2 - 参与人奖励 **/
    private Integer rewardType;
    /** 1 - 从小程序打开微信客服，2 - 直接在微信客服窗口回复。 **/
    private Integer type;
    /** 发送状态，1 - 发送成功， 2 - 发送失败 **/
    private Integer stats;
    /** 错误信息 **/
    private String errorMsg;
    /** 创建时间 **/
    private Date createAt;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAppInfoId() {
        return appInfoId;
    }

    public void setAppInfoId(String appInfoId) {
        this.appInfoId = appInfoId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public Integer getRewardType() {
        return rewardType;
    }

    public void setRewardType(Integer rewardType) {
        this.rewardType = rewardType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStats() {
        return stats;
    }

    public void setStats(Integer stats) {
        this.stats = stats;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
