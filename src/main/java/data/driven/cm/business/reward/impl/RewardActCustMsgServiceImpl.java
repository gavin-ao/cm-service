package data.driven.cm.business.reward.impl;

import data.driven.cm.business.reward.RewardActCustMsgService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.reward.RewardActCustMsgEntity;
import data.driven.cm.util.UUIDUtil;
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
            rewardActCustMsgEntity.setGlobalId(UUIDUtil.getUUID());
            jdbcBaseDao.insert(rewardActCustMsgEntity, "reward_act_cust_msg");
        }else{
            jdbcBaseDao.update(rewardActCustMsgEntity, "reward_act_cust_msg", "global_id", false);
        }
    }

    @Override
    public RewardActCustMsgEntity getRewardActCustMsg(String actId, Integer contentType) {
        String sql = "select global_id,act_id,store_id,app_info_id,content,content_type,type from reward_act_cust_msg where act_id = ? and content_type = ?";
        List<RewardActCustMsgEntity> list = jdbcBaseDao.queryList(RewardActCustMsgEntity.class, sql, actId, contentType);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public boolean deleteRewardByActId(String actId, Integer contentType, String storeId) {
        String sql = "delete from reward_act_cust_msg where act_id = ? and content_type = ? and store_id = ?";
        jdbcBaseDao.executeUpdate(sql, actId, contentType, storeId);
        return true;
    }
}
