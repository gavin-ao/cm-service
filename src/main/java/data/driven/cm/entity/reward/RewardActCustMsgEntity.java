package data.driven.cm.entity.reward;

import java.util.Date;

/**
 * 活动奖励-客服消息
 * @author hejinkai
 * @date 2018/11/27
 */
public class RewardActCustMsgEntity {
    /** 主键 **/
    private String globalId;
    /** 活动id **/
    private String actId;
    /** 活动用户id **/
    private String userId;
    /** 门店id **/
    private String storeId;
    /** 小程序id **/
    private String appInfoId;
    /** 奖励回复内容 **/
    private String content;
    /** 类型，1 - 发起人奖励，2 - 参与人奖励 **/
    private Integer contentType;
    /** 奖励类型，1-文本，2-图片 **/
    private Integer type;
    /** 口令创建时间 **/
    private Date createAt;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
