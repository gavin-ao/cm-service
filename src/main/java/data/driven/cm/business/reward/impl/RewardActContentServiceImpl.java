package data.driven.cm.business.reward.impl;

import data.driven.cm.business.reward.RewardActContentService;
import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.reward.RewardActContentEntity;
import data.driven.cm.util.DateFormatUtil;
import data.driven.cm.vo.reward.RewardActContentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 活动奖励文案Service
 * @author hejinkai
 * @date 2018/8/8
 */
@Service
public class RewardActContentServiceImpl implements RewardActContentService{

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public RewardActContentEntity getRewardActContentByActAndType(String actId, Integer commandType) {
        String sql = "select content_id,content_title,content_head,content_mid,content_foot,content_btn,remark from reward_act_content where act_id = ? and command_type = ? order by create_at,content_id limit 1";
        List<RewardActContentEntity> list = jdbcBaseDao.queryList(RewardActContentEntity.class, sql, actId, commandType);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public Page<RewardActContentVO> findRewardActContentPage(String keyword, String storeId, Integer actStats, Integer type, PageBean pageBean) {
        String sql = "select ma.act_id,ma.act_num,ma.act_name,ma.act_introduce,ma.start_at,ma.end_at,rac.remark,rac.command_type from reward_act_content rac " +
                " left join mat_activity ma on rac.act_id = ma.act_id where (ma.initiator_reward_type = 1 or ma.assistance_reward_type = 1) ";
        StringBuffer where = new StringBuffer();
        List<Object> paramList = new ArrayList<Object>();
        if(keyword != null){
            where.append(" and ma.act_name like ?");
            paramList.add("%" + keyword.trim() + "%");
        }
        if(storeId != null){
            where.append(" and ma.store_id = ?");
            paramList.add(storeId);
        }else{
            return new Page<RewardActContentVO>();
        }

        if(actStats != null){
            Date date = DateFormatUtil.convertDate(new Date());
            if(actStats == 0){//未开始
                where.append(" and ma.start_at > ?");
                paramList.add(date);
            }else if(actStats == 1){//进行中
                where.append(" and ma.start_at <= ? and end_at >= ?");
                paramList.add(date);
                paramList.add(date);
            }else if(actStats == 2){//已结束
                where.append(" and end_at < ?");
                paramList.add(date);
            }
        }

        if(type != null){
            where.append(" and rac.command_type = ?");
            paramList.add(type);
        }

        if(where.length() > 0){
            sql += where;
        }
        sql += " order by rac.create_at desc,rac.act_id";
        return jdbcBaseDao.queryPageWithListParam(RewardActContentVO.class, pageBean, sql, paramList);
    }

    @Override
    public List<RewardActContentEntity> findRewardActContentList(String actId) {
        String sql = "select content_id, act_id, content_title, content_head, content_mid, content_foot, content_btn, command_type, remark from reward_act_content where act_id = ?";
        List<RewardActContentEntity> list = jdbcBaseDao.queryList(RewardActContentEntity.class, sql, actId);
        return list;
    }
}
