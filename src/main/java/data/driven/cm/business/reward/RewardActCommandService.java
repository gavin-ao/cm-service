package data.driven.cm.business.reward;

import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.entity.reward.RewardActCommandEntity;
import data.driven.cm.entity.wechat.WechatHelpInfoEntity;
import data.driven.cm.vo.material.MatActivityVO;

/**
 * 活动奖励口令service
 * @author hejinkai
 * @date 2018/7/24
 */
public interface RewardActCommandService {

    /**
     * 根据活动id获取未使用的奖励口令
     * @param actId
     * @param commandType
     * @return
     */
    public RewardActCommandEntity getNextRewardActCommandByActId(String actId, Integer commandType);

    /**
     * 根据helpId获取奖励口令文本
     * @param helpId
     * @param wechatUserId
     * @return
     */
    public String getCommandByHelpId(String helpId, String wechatUserId);

    /**
     * 根据helpId获取奖励二维码路径
     * @param helpId
     * @param wechatUserId
     * @return
     */
    public String getCommandQrcodeByHelpId(String helpId, String wechatUserId);

    /**
     * 将奖励口令状态修改为已使用
     * @param commandId
     */
    public void updateRewardActCommandUsed(String commandId);

    /**
     * 存储口令与活动助力的关联关系
     * @param command
     * @param helpInfoEntity
     */
    public void insertRewardActCommandHelpMapping(RewardActCommandEntity command, WechatHelpInfoEntity helpInfoEntity);

    /**
     * 根据活动自动生成奖励数据
     * @param rewardNum
     * @param matActivity
     * @param type 1 - 只生成发起人奖励， 2 - 只生成助力奖励， 3 - 所有奖励都生成
     */
    public void insertRewardActCommandAuto(Integer rewardNum, MatActivityVO matActivity, int type);
    /**
     * 根据活动生成奖励数据 - 只生成某一种
     * @param rewardNum
     * @param matActivity
     * @param commandType
     */
    public void insertRewardActCommand(Integer rewardNum, MatActivityVO matActivity, Integer commandType);


    /**
     * 分页查询奖励码
     * @param actId
     * @param commandType
     * @param storeId
     * @param pageBean
     * @return
     */
    public Page<RewardActCommandEntity> findRewardActCommandPage(String actId, Integer commandType, String storeId, PageBean pageBean);

    /**
     * 删除数据
     * @param actId
     * @param commandType
     * @param storeId
     * @param ids
     */
    public boolean deleteRewardActCommand(String actId, Integer commandType, String storeId, String ids);

    /**
     * 清除活动下的所有数据
     * @param actId
     * @param commandType
     * @param storeId
     * @return
     */
    public boolean deleteRewardActCommandByActId(String actId, Integer commandType, String storeId);
}
