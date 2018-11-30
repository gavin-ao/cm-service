package data.driven.cm.business.reward.impl;

import data.driven.cm.business.reward.RewardActCustMsgTotalService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author hejinkai
 * @date 2018/11/30
 */
@Service
public class RewardActCustMsgTotalServiceImpl implements RewardActCustMsgTotalService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public void totalReward(String globalId, String content) {
        String totalId = exists(globalId, content);
        Date date = new Date();
        if(totalId == null){
            totalId = UUIDUtil.getUUID();
            String sql = "insert into reward_act_cust_msg_total(total_id,global_id,content,total_num,last_at) values(?,?,?,?,?)";
            jdbcBaseDao.executeUpdate(sql, totalId, globalId, content, 1, date);
        }else{
            String sql = "update reward_act_cust_msg_total set total_num = total_num + 1,last_at = ? where total_id = ?";
            jdbcBaseDao.executeUpdate(sql, date, totalId);
        }
    }

    /**
     * 判断对象是否存在，存在则返回主键
     * @param globalId
     * @param content
     * @return
     */
    private String exists(String globalId, String content){
        String sql = "select total_id from reward_act_cust_msg_total where global_id = ? and content = ? limit 1";
        Object totalId = jdbcBaseDao.getColumn(sql, globalId, content);
        if(totalId != null){
            return totalId.toString();
        }
        return null;
    }

    @Override
    public Map<String, Object> getLastContentAndNum(String globalId) {
        String sql = "select total_id as totalId,content,total_num as totalNum from reward_act_cust_msg_total where global_id = ? order by last_at desc limit 1";
        Map<String, Object> map = jdbcBaseDao.getMapResult(sql, globalId);
        if(map != null && map.size() > 0){
            return map;
        }
        return null;
    }
}
