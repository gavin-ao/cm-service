package data.driven.cm.business.reward;

import java.util.Map;

/**
 * 活动奖励-客服消息多个奖励的单个发送计数
 * @author hejinkai
 * @date 2018/11/29
 */
public interface RewardActCustMsgTotalService {

    /**
     * 统计发送奖励的数量
     * @param globalId
     * @param content
     */
    public void totalReward(String globalId, String content);

    /**
     * 获取最近一次的content和对应的统计数量
     * @param globalId
     * @return
     */
    public Map<String, Object> getLastContentAndNum(String globalId);
}
