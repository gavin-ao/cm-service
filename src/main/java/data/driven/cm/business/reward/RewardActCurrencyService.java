package data.driven.cm.business.reward;

import data.driven.cm.entity.reward.RewardActCurrencyEntity;

/**
 * 活动奖励-通用奖励淘口令service
 * @author hejinkai
 * @date 2018/11/27
 */
public interface RewardActCurrencyService {

    /**
     * 新增/修改奖励通用奖励淘口令
     * @param rewardActCurrencyEntity
     */
    public void addRewardActCurrency(RewardActCurrencyEntity rewardActCurrencyEntity);

    /**
     * 根据活动id和奖励类型获取通用口令
     * @param actId
     * @param commandType
     * @return
     */
    public String getCommand(String actId, Integer commandType);

    /**
     * 根据活动主键删除所有奖励，淘口令
     * @param actId
     * @param commandType
     * @param storeId
     * @return
     */
    public boolean deleteRewardByActId(String actId, Integer commandType, String storeId);
}
