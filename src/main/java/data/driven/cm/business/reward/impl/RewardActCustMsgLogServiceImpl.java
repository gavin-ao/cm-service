package data.driven.cm.business.reward.impl;

import data.driven.cm.business.reward.RewardActCustMsgLogService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.reward.RewardActCustMsgLogEntity;
import data.driven.cm.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author hejinkai
 * @date 2018/11/30
 */
@Service
public class RewardActCustMsgLogServiceImpl implements RewardActCustMsgLogService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public void addLog(RewardActCustMsgLogEntity custMsgLog) {
        custMsgLog.setLogId(UUIDUtil.getUUID());
        custMsgLog.setCreateAt(new Date());
        jdbcBaseDao.insert(custMsgLog, "reward_act_cust_msg_log");
    }

    @Override
    public boolean getSuccessMsg(String openid, String golbalId, Integer type) {
        String sql = "select log_id from reward_act_cust_msg_log where global_id = ? and openid = ? and reward_type = ? and stats = 1 order by create_at desc limit 1";
        Object logId = jdbcBaseDao.getColumn(sql, golbalId, openid, type);
        return logId != null;
    }
}
