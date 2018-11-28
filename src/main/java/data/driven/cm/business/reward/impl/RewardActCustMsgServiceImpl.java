package data.driven.cm.business.reward.impl;

import data.driven.cm.business.reward.RewardActCustMsgService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.reward.RewardActCustMsgEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动奖励-客服消息Service
 * @author hejinkai
 * @date 2018/11/27
 */
@Service
public class RewardActCustMsgServiceImpl implements RewardActCustMsgService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public void addRewardActCustMsg(RewardActCustMsgEntity rewardActCustMsgEntity) {
        if(rewardActCustMsgEntity.getGlobalId() == null){
            jdbcBaseDao.insert(rewardActCustMsgEntity, "reward_act_cust_msg");
        }else{
            jdbcBaseDao.update(rewardActCustMsgEntity, "reward_act_cust_msg", "global_id", false);
        }
    }

    @Override
    public RewardActCustMsgEntity getRewardActCustMsg(String actId, Integer contextType) {
        String sql = "select act_id,store_id,context,context_type,type from reward_act_cust_msg where act_id = ? and context_type = ?";
        List<RewardActCustMsgEntity> list = jdbcBaseDao.queryList(RewardActCustMsgEntity.class, sql, actId, contextType);
        if(list != null){
            return list.get(0);
        }
        return null;
    }

    @Override
    public boolean deleteRewardByActId(String actId, Integer contextType, String storeId) {
        String sql = "delete from reward_act_cust_msg where act_id = ? and context_type = ? and store_id = ?";
        jdbcBaseDao.executeUpdate(sql, actId, contextType, storeId);
        return true;
    }
}
