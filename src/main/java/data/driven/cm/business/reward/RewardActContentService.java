package data.driven.cm.business.reward;

import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.entity.reward.RewardActContentEntity;
import data.driven.cm.vo.reward.RewardActContentVO;

import java.util.List;

/**
 * 活动奖励文案Service
 * @author hejinkai11
 * @date 2018/8/8
 */
public interface RewardActContentService {

    /**
     * 根据活动id和奖励类型获取文案
     * @param actId
     * @param commandType
     * @return
     */
    public RewardActContentEntity getRewardActContentByActAndType(String actId, Integer commandType);

    /**
     * 分页查询
     * @param keyword
     * @param storeId
     * @param actStats
     * @param type
     * @param pageBean
     * @return
     */
    public Page<RewardActContentVO> findRewardActContentPage(String keyword, String storeId, Integer actStats, Integer type, PageBean pageBean);

    /**
     * 根据活动id查询奖励文案列表
     * @param actId
     * @return
     */
    public List<RewardActContentEntity> findRewardActContentList(String actId);

}
