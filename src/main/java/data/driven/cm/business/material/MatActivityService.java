package data.driven.cm.business.material;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.vo.material.MatActivityVO;

/**
 * 活动-素材service
 * @author hejinkai
 * @date 2018/7/15
 */
public interface MatActivityService {

    /**
     * 根据活动id获取图片的url
     * @param actId
     * @return
     */
    public String getMatActivityPictureUrl(String actId);

    /**
     * 根据活动id获取图片的url，活动规则，兑换规则
     * @param actId
     * @return
     */
    public MatActivityVO getMatActivityInfo(String actId);

    /**
     * 根据活动id和storeId获取所有活动信息
     * @param actId
     * @return
     */
    public MatActivityVO getMatActivityAllInfo(String actId, String storeId);

    /**
     * 根据活动id获取四个id  ma.act_id,ma.user_id,ma.store_id,ma.app_info_id,ma.start_at,ma.end_at
     * @param actId
     * @return
     */
    public MatActivityVO getMatActivityAnyID(String actId);

    /**
     * 根据获取有效的活动信息
     * @param actId
     * @return
     */
    public MatActivityVO getValidMatActivityInfo(String actId);

    /**
     * 根据门店id获取图片的url，活动规则，兑换规则
     * @param storeId
     * @return
     */
    public MatActivityVO getMatActivityInfoByStore(String storeId);

    /**
     * 根据门店id获取活动id，活动奖励url
     * @param storeId
     * @return
     */
    public MatActivityVO getAnyMatActivityInfoByStore(String storeId);



    //后台新增代码 20181106

    /**
     * 分页查询活动列表
     * @param keyword
     * @param storeId
     * @param pageBean
     * @return
     */
    public Page<MatActivityVO> findActivityPage(String keyword, String storeId, PageBean pageBean);

    /**
     * 更新活动 - 新增actId为空，修改时actId不为空
     * @param activity 活动信息
     * @param btnCopywritingJson 活动文案部分信息
     * @param rewardActContentJson 活动奖励文案部分信息
     * @param rewardNum 奖励数量
     * @param creator   当前用户
     */
    public JSONObject updateActivity(MatActivityVO activity, String btnCopywritingJson, String rewardActContentJson, Integer rewardNum, String creator);

    /**
     * 验证时间是否正确
     * @param activity
     * @return
     */
    public JSONObject checkActivityStartDate(MatActivityVO activity);

    /**
     * 获得下次能修改的时间
     * @param storeId
     * @return
     */
    public JSONObject getNextActivityStartDate(String storeId);

    public void deleteActivity(String ids);


}
