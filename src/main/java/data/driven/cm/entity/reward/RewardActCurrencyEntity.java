package data.driven.cm.entity.reward;

import java.util.Date;

/**
 * 活动奖励-通用奖励淘口令
 * @author hejinkai
 * @date 2018/11/27
 */
public class RewardActCurrencyEntity {
    /** 主键 **/
    private String currencyId;
    /** 奖励的口令 **/
    private String command;
    /** 奖励口令类型，1 - 发起人奖励，2 - 参与人奖励 **/
    private Integer commandType;
    /** 活动id **/
    private String actId;
    /** 活动用户id **/
    private String userId;
    /** 门店id **/
    private String storeId;
    /** 小程序id **/
    private String appInfoId;
    /** 口令创建时间 **/
    private Date createAt;

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Integer getCommandType() {
        return commandType;
    }

    public void setCommandType(Integer commandType) {
        this.commandType = commandType;
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
