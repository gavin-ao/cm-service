package data.driven.cm.business.reward.impl;

import data.driven.cm.business.reward.RewardActCurrencyService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.reward.RewardActCurrencyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动奖励-通用奖励淘口令service
 * @author hejinkai
 * @date 2018/11/27
 */
@Service
public class RewardActCurrencyServiceImpl implements RewardActCurrencyService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public void addRewardActCurrency(RewardActCurrencyEntity rewardActCurrencyEntity) {
        if(rewardActCurrencyEntity.getCurrencyId() == null){
            jdbcBaseDao.insert(rewardActCurrencyEntity, "reward_act_currency");
        }else{
            jdbcBaseDao.update(rewardActCurrencyEntity, "reward_act_currency", "currency_id", false);
        }
    }

    @Override
    public String getCommand(String actId, Integer commandType) {
        String sql = "select command from reward_act_currency where act_id = ? and command_type = ?";
        Object obj = jdbcBaseDao.getColumn(sql, actId, commandType);
        if(obj != null){
            return obj.toString();
        }
        return null;
    }

    @Override
    public RewardActCurrencyEntity getRewardActCurrency(String actId, Integer commandType) {
        String sql = "select currency_id,command,command_type from reward_act_currency where act_id = ? and command_type = ?";
        List<RewardActCurrencyEntity> list = jdbcBaseDao.queryList(RewardActCurrencyEntity.class, sql, actId, commandType);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public boolean deleteRewardByActId(String actId, Integer commandType, String storeId) {
        String sql = "delete from reward_act_currency where act_id = ? and command_type = ? and store_id = ?";
        jdbcBaseDao.executeUpdate(sql, actId, commandType, storeId);
        return true;
    }
}
