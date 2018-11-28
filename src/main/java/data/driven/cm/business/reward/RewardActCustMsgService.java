package data.driven.cm.business.reward;

import data.driven.cm.entity.reward.RewardActCustMsgEntity;

/**
 * 活动奖励-客服消息Service
 * @author hejinkai
 * @date 2018/11/27
 */
public interface RewardActCustMsgService {

    /**
     * 新增/修改活动奖励-客服消息
     * @param rewardActCustMsgEntity
     */
    public void addRewardActCustMsg(RewardActCustMsgEntity rewardActCustMsgEntity);

    /**
     * 根据活动查询活动奖励
     * @param actId
     * @param contextType
     * @return
     */
    public RewardActCustMsgEntity getRewardActCustMsg(String actId, Integer contextType);

    /**
     * 根据活动id删除活动奖励 - 微信客服
     * @param actId
     * @param contextType
     * @param storeId
     * @return
     */
    public boolean deleteRewardByActId(String actId, Integer contextType, String storeId);
}
