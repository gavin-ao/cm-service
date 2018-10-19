package data.driven.cm.business.behavioranalysis;

/**
 * 行为分析_助力奖励埋点
 * @author hejinkai
 * @date 2018/9/19
 */
public interface BehaviorAnalysisHelpCommandService {

    /**
     * 领取奖励并弹出窗，插入行为数据
     * @param help_id
     * @param act_id
     * @param app_info_id
     * @param wechat_user_id
     * @return
     */
    public boolean openWindowInsert(String help_id, String act_id, String app_info_id, String wechat_user_id);

    /**
     * 点击url触发的接口
     * @param id
     * @return
     */
    public boolean updateClickUrl(String id);

    /**
     * 判断是否存在
     * @param id
     * @return
     */
    public boolean exitsId(String id);

}
