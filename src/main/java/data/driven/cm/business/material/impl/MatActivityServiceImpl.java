package data.driven.cm.business.material.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.material.BtnCopywritingService;
import data.driven.cm.business.material.MatActivityService;
import data.driven.cm.business.reward.RewardActCommandService;
import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.material.BtnCopywritingEntity;
import data.driven.cm.entity.reward.RewardActContentEntity;
import data.driven.cm.util.DateFormatUtil;
import data.driven.cm.util.JSONUtil;
import data.driven.cm.util.UUIDUtil;
import data.driven.cm.vo.material.MatActivityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 活动-素材service
 * @author hejinkai
 * @date 2018/7/15
 */
@Service
public class MatActivityServiceImpl implements MatActivityService{

    /** 一天的毫秒数 **/
    private static final int one_day = 86400000;

    @Autowired
    private JDBCBaseDao jdbcBaseDao;
    @Autowired
    private BtnCopywritingService btnCopywritingService;
    @Autowired
    private RewardActCommandService rewardActCommandService;


    @Override
    public String getMatActivityPictureUrl(String actId) {
        String sql = "select p.file_path from mat_activity ma left join sys_picture p on p.picture_id = ma.picture_id where ma.act_id = ?";
        Object filePath = jdbcBaseDao.getColumn(sql, actId);
        if(filePath != null){
            return filePath.toString();
        }
        return null;
    }

    @Override
    public MatActivityVO getMatActivityInfo(String actId) {
        String sql = "select p.file_path,ma.store_id,ma.act_id,ma.act_type,ma.act_name,ma.act_reply,ma.act_title,ma.act_share_title,ma.act_share_copywriting,ma.act_rule,ma.exchange_rule,ma.partake_num from mat_activity ma left join sys_picture p on p.picture_id = ma.picture_id where ma.act_id = ?";
        List<MatActivityVO> list = jdbcBaseDao.queryList(MatActivityVO.class, sql, actId);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public MatActivityVO getMatActivityAllInfo(String actId, String storeId) {
        String sql = "select p.file_path,ma.act_id, ma.act_type, ma.act_name, ma.act_introduce, ma.picture_id, ma.act_url, ma.act_reply, ma.act_title, ma.act_share_title, ma.act_share_copywriting, ma.act_rule, ma.exchange_rule, ma.partake_num, ma.reward_url, ma.start_at, ma.end_at from mat_activity ma" +
                " left join sys_picture p on p.picture_id = ma.picture_id where ma.act_id = ? and ma.store_id = ?";
        List<MatActivityVO> list = jdbcBaseDao.queryList(MatActivityVO.class, sql, actId, storeId);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public MatActivityVO getMatActivityAnyID(String actId) {
        String sql = "select ma.act_id,ma.user_id,ma.store_id,ma.app_info_id,ma.start_at,ma.end_at from mat_activity ma where ma.act_id = ?";
        List<MatActivityVO> list = jdbcBaseDao.queryList(MatActivityVO.class, sql, actId);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public MatActivityVO getValidMatActivityInfo(String actId) {
        Date date = new Date();
        String sql = "select ma.store_id,ma.act_id,ma.act_type,ma.act_name,ma.act_reply,ma.act_title,ma.act_share_title,ma.act_share_copywriting,ma.act_rule,ma.exchange_rule,ma.partake_num from mat_activity ma where ma.act_id = ? and ma.start_at <= ? and ma.end_at >= ?";
        List<MatActivityVO> list = jdbcBaseDao.queryList(MatActivityVO.class, sql, actId, date, date);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public MatActivityVO getMatActivityInfoByStore(String storeId) {
        Date date = new Date();
        String sql = "select p.file_path,ma.act_id,ma.act_type,ma.act_name,ma.act_reply,ma.act_title,ma.act_share_title,ma.act_share_copywriting,ma.act_rule,ma.exchange_rule,ma.partake_num from mat_activity ma" +
                " left join sys_picture p on p.picture_id = ma.picture_id where ma.store_id = ? and ma.start_at <= ? and ma.end_at >= ?";
        List<MatActivityVO> list = jdbcBaseDao.queryList(MatActivityVO.class, sql, storeId, date, date);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
    @Override
    public MatActivityVO getAnyMatActivityInfoByStore(String storeId) {
        Date date = new Date();
        String sql = "select ma.act_id,ma.reward_url from mat_activity ma where ma.store_id = ? and ma.start_at <= ? and ma.end_at >= ?";
        List<MatActivityVO> list = jdbcBaseDao.queryList(MatActivityVO.class, sql, storeId, date, date);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public Page<MatActivityVO> findActivityPage(String keyword, String storeId, PageBean pageBean) {
        String sql = "select ma.act_id,ma.store_id,ma.app_info_id,ma.act_type,ma.act_name,ma.act_introduce,ma.act_title,ma.start_at,ma.end_at from mat_activity ma";
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
            return new Page<MatActivityVO>();
        }
        if(where.length() > 0){
            sql += " where" + where.delete(0,4);
        }
        sql += " order by ma.create_at desc,ma.act_id";
        return jdbcBaseDao.queryPageWithListParam(MatActivityVO.class, pageBean, sql, paramList);
    }

    @Override
    public JSONObject updateActivity(MatActivityVO activity, String btnCopywritingJson, String rewardActContentJson, Integer rewardNum, String creator) {
        if(activity == null || btnCopywritingJson == null || rewardActContentJson == null){
            return JSONUtil.putMsg(false, "101", "参数为空");
        }
        if(activity.getStartAt() == null || activity.getEndAt() == null){
            return JSONUtil.putMsg(false, "103", "活动开始日期和结束日期不能为空");
        }
        JSONObject temp = checkActivityStartDate(activity);
        if(temp.containsValue(false)){
            return JSONUtil.putMsg(false, "102", "开始日期不允许");
        }
        JSONObject btnJsonObj = JSONObject.parseObject(btnCopywritingJson);
        Date date = new Date();
        if(activity.getEndAt().before(date)){
            return JSONUtil.putMsg(false, "104", "活动结束日期必须大于当前日期");
        }
        List<RewardActContentEntity> rewardActContentList = JSONArray.parseArray(rewardActContentJson, RewardActContentEntity.class);
        if(activity.getActId() == null){//新增
            activity.setActId(UUIDUtil.getUUID());
            activity.setCreateAt(date);
            activity.setUserId(creator);
            //插入活动
            jdbcBaseDao.insert(activity, "mat_activity");
            String btnSql = "insert into btn_copywriting(id,act_id,btn_text,btn_code,create_at,creator)";
            String btnValuesSql = "(:id,:act_id,:btn_text,:btn_code,:create_at,:creator)";
            List<BtnCopywritingEntity> btnList = getBtnCopywritingEntities(btnJsonObj, activity.getActId(), creator, date);
            //插入活动文案数据
            jdbcBaseDao.executeBachOneSql(btnSql, btnValuesSql, btnList);
            //插入奖励文案数据
            String rewardSql = "insert into reward_act_content(content_id,act_id,content_title,content_head,content_mid,content_foot,content_btn,command_type,remark,create_at)";
            String rewardValuesSql = "(:content_id,:act_id,:content_title,:content_head,:content_mid,:content_foot,:content_btn,:command_type,:remark,:create_at)";
            for(RewardActContentEntity rewardActContentEntity : rewardActContentList){
                rewardActContentEntity.setContentId(UUIDUtil.getUUID());
                rewardActContentEntity.setActId(activity.getActId());
                rewardActContentEntity.setCreateAt(date);
            }
            jdbcBaseDao.executeBachOneSql(rewardSql, rewardValuesSql, rewardActContentList);
            rewardActCommandService.insertRewardActCommandAuto(rewardNum, activity);
        }else{//更新
            String canUpdateSql = "select act_id from mat_activity where act_id = ? and start_at > ?";
            Object obj = jdbcBaseDao.getColumn(canUpdateSql, activity.getActId(), date);
            if(obj == null){
                return JSONUtil.putMsg(false, "105", "未开始的活动才允许修改，进行中和已经完成的活动不允许修改");
            }
            jdbcBaseDao.update(activity, "mat_activity", "act_id", false);
            //查询原有的数据，然后比对更新或者插入未存在的
            Map<String, String> btnMap = btnCopywritingService.findBtnCopyWritingMapByActId(activity.getActId());
            List<BtnCopywritingEntity> addBtnList = new ArrayList<BtnCopywritingEntity>();
            List<BtnCopywritingEntity> updateBtnList = new ArrayList<BtnCopywritingEntity>();
            for(Map.Entry<String, Object> entry : btnJsonObj.entrySet()){
                String btnText = (String) entry.getValue();
                if(btnMap.containsKey(entry.getKey())){
                    String oldText = btnMap.get(entry.getKey());
                    if(oldText.equals(btnText)){
                        continue;
                    }
                    BtnCopywritingEntity btnCopywritingEntity = setBtnEntity(activity.getActId(), creator, date, entry);
                    updateBtnList.add(btnCopywritingEntity);
                }else{
                    BtnCopywritingEntity btnCopywritingEntity = setBtnEntity(activity.getActId(), creator, date, entry);
                    addBtnList.add(btnCopywritingEntity);
                }
            }
            if(addBtnList.size() > 0){
                String sql = "insert into btn_copywriting(id,act_id,btn_text,btn_code,create_at,creator)";
                String valuesSql = "(:id,:act_id,:btn_text,:btn_code,:create_at,:creator)";
                jdbcBaseDao.executeBachOneSql(sql, valuesSql, addBtnList);
            }
            if(updateBtnList.size() > 0){
                String sql = "update btn_copywriting set btn_text = :btn_text where act_id = :act_id and btn_code = :btn_code";
                jdbcBaseDao.executeBach(sql, updateBtnList);
            }

            for(RewardActContentEntity rewardActContentEntity : rewardActContentList){
//                rewardActContentEntity.setActId(activity.getActId());
                jdbcBaseDao.update(rewardActContentEntity, "reward_act_content", "content_id", false);
            }

        }
        return JSONUtil.putMsg(true, "200", "更新成功");
    }

    /**
     * 将json对象转成集合 - addActivity专属方法
     * @param btnMap
     * @param actId
     * @param creator
     * @param date
     * @return
     */
    private List<BtnCopywritingEntity> getBtnCopywritingEntities(Map<String, Object> btnMap, String actId, String creator, Date date) {
        List<BtnCopywritingEntity> btnList = new ArrayList<BtnCopywritingEntity>();
        for(Map.Entry<String, Object> entry : btnMap.entrySet()){
            BtnCopywritingEntity btnCopywritingEntity = setBtnEntity(actId, creator, date, entry);
            btnList.add(btnCopywritingEntity);
        }
        return btnList;
    }

    /** 设置btn属性 **/
    private BtnCopywritingEntity setBtnEntity(String actId, String creator, Date date, Map.Entry<String, Object> entry) {
        BtnCopywritingEntity btnCopywritingEntity = new BtnCopywritingEntity();
        btnCopywritingEntity.setId(UUIDUtil.getUUID());
        btnCopywritingEntity.setActId(actId);
        btnCopywritingEntity.setBtnCode(entry.getKey());
        btnCopywritingEntity.setBtnText(entry.getValue() != null?entry.getValue().toString(): null);
        btnCopywritingEntity.setCreateAt(date);
        btnCopywritingEntity.setCreator(creator);
        return btnCopywritingEntity;
    }

    @Override
    public JSONObject checkActivityStartDate(MatActivityVO activity) {
        if(activity == null){
            return JSONUtil.putMsg(false, "101", "参数为空");
        }
        JSONObject temp = getNextActivityStartDate(activity.getStoreId(), activity.getActId());
        Long nextTime = temp.getLong("nextDate");
        boolean success = activity.getStartAt().getTime() - nextTime >= 0;
        JSONObject result = JSONUtil.putMsg(success, "200", "调用成功");
        return result;
    }

    @Override
    public JSONObject getNextActivityStartDate(String storeId) {
        return getNextActivityStartDate(storeId, null);
    }

    private JSONObject getNextActivityStartDate(String storeId, String actId){
        Date date = DateFormatUtil.convertDate(new Date());
        String sql = "select end_at from mat_activity where store_id = ?";
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(storeId);
        if(actId != null){
            sql += " and act_id != ?";
            paramList.add(actId);
        }
        sql += " order by end_at desc,act_id limit 1";
        Object endDateObj = jdbcBaseDao.getColumnWithListParam(sql, paramList);
        Date endDate = (Date) endDateObj;
        JSONObject result = JSONUtil.putMsg(true, "200", "操作成功");
        long nowTime = date.getTime();
        if(endDate != null){
            long nextDate = endDate.getTime() + one_day;
            if(nextDate < nowTime){
                nextDate = nowTime;
            }
            result.put("nextDate", nextDate);
        }else{
            result.put("nextDate", date.getTime());
        }
        return result;
    }


    @Override
    public void deleteActivity(String ids) {

    }
}
