package data.driven.cm.business.reward;

import data.driven.cm.entity.reward.RewardActCustMsgLogEntity;

/**
 * 活动奖励客服消息发送日志service
 * @author hejinkai
 * @date 2018/11/30
 */
public interface RewardActCustMsgLogService {

    /**
     * 添加发送微信客服信息日志
     * @param custMsgLog
     */
    public void addLog(RewardActCustMsgLogEntity custMsgLog);

    /**
     * 根据用户和奖励、奖励类型查询是否成功发送过日志
     * @param openid
     * @param golbalId
     * @param type
     * @return
     */
    public boolean getSuccessMsg(String openid, String golbalId, Integer type);
}
