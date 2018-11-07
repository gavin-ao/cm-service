package data.driven.cm.vo.reward;

import data.driven.cm.entity.reward.RewardActContentEntity;

import java.util.Date;

/**
 * 奖励vo
 * @author hejinkai
 * @date 2018/11/7
 */
public class RewardActContentVO extends RewardActContentEntity {
    /** 门店id **/
    private String storeId;
    /** 活动名称 **/
    private String actName;
    /** 活动介绍 **/
    private String actIntroduce;
    /** 活动开始日期 **/
    private Date startAt;
    /** 活动结束日期 **/
    private Date endAt;
    /** 活动状态 0 - 未开始； 1 进行中 ； 2 - 已结束 **/
    private Integer status;

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActIntroduce() {
        return actIntroduce;
    }

    public void setActIntroduce(String actIntroduce) {
        this.actIntroduce = actIntroduce;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
