package data.driven.cm.business.reward.impl;

import data.driven.cm.business.reward.RewardActCommandService;
import data.driven.cm.business.system.PictureService;
import data.driven.cm.common.Constant;
import data.driven.cm.component.Page;
import data.driven.cm.component.PageBean;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.reward.RewardActCommandEntity;
import data.driven.cm.entity.system.PictureEntity;
import data.driven.cm.entity.wechat.WechatHelpInfoEntity;
import data.driven.cm.util.DateFormatUtil;
import data.driven.cm.util.QRCodeUtil;
import data.driven.cm.util.UUIDUtil;
import data.driven.cm.vo.material.MatActivityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 活动奖励口令service
 * @author hejinkai
 * @date 2018/7/24
 */
@Service
public class RewardActCommandServiceImpl implements RewardActCommandService {

    /** 最大的sql拼接 **/
    private static final int maxCount = 50000;
    @Autowired
    private JDBCBaseDao jdbcBaseDao;
    @Autowired
    private PictureService pictureService;

    @Override
    public RewardActCommandEntity getNextRewardActCommandByActId(String actId, Integer commandType) {
        String sql = "select command_id,command,act_id,user_id,app_info_id,used,create_at from reward_act_command where act_id = ? and used = 0 and command_type = ? order by create_at,command_id";
        List<RewardActCommandEntity> list = jdbcBaseDao.queryList(RewardActCommandEntity.class, sql, actId, commandType);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public String getCommandByHelpId(String helpId, String wechatUserId) {
        String sql = "select c.command from reward_act_command_help_mapping m left join reward_act_command c on c.command_id = m.command_id where m.help_id = ? and m.wechat_user_id = ? limit 1";
        Object command = jdbcBaseDao.getColumn(sql, helpId, wechatUserId);
        if(command != null){
            return command.toString();
        }
        return null;
    }

    @Override
    public String getCommandQrcodeByHelpId(String helpId, String wechatUserId) {
        String sql = "select p.file_path,m.map_id from reward_act_command_help_mapping m left join sys_picture p on p.picture_id = m.picture_id where m.help_id = ? and m.wechat_user_id = ? limit 1";
        List<Map<String, Object>> list = jdbcBaseDao.queryMapList(sql, helpId, wechatUserId);
        if(list != null && list.size() > 0){
            Map<String, Object> map = list.get(0);
            if(map.get("file_path") == null){
                String mapId = map.get("map_id").toString();
                PictureEntity pictureEntity = insertPicture(wechatUserId, mapId);
                String updateSql = "update reward_act_command_help_mapping set picture_id = ? where map_id = ?";
                jdbcBaseDao.executeUpdate(updateSql, pictureEntity.getPictureId(), mapId);
                return pictureEntity.getFilePath();
            }else{
                return map.get("file_path").toString();
            }
        }
        return null;
    }

    @Override
    public void updateRewardActCommandUsed(String commandId){
        String sql = "update reward_act_command set used = 1 where command_id = ?";
        jdbcBaseDao.executeUpdate(sql, commandId);
    }

    @Override
    public void insertRewardActCommandHelpMapping(RewardActCommandEntity command, WechatHelpInfoEntity helpInfoEntity) {
        Date createAt = new Date();
        String sql = "insert into reward_act_command_help_mapping(map_id,help_id,command_id,act_id,store_id,app_info_id,wechat_user_id,create_at) values(?,?,?,?,?,?,?,?)";
        jdbcBaseDao.executeUpdate(sql, UUIDUtil.getUUID(), helpInfoEntity.getHelpId(), command.getCommandId(), helpInfoEntity.getActId(), helpInfoEntity.getStoreId(), helpInfoEntity.getAppInfoId(), helpInfoEntity.getWechatUserId(), createAt);
    }

    /**
     * 根据mapId生成二维码并存到数据库中
     * @param creator
     * @param mapId
     * @return
     */
    private PictureEntity insertPicture(String creator, String mapId){
        Date date = new Date();
        SimpleDateFormat sdf = DateFormatUtil.getLocal("yyyyMM");
        String tempFolderPath = sdf.format(date) + File.separator + Constant.WXQRCODE_TEMP_FILE_FOLDER_REWARD;
        String folderPath = Constant.FILE_UPLOAD_PATH + tempFolderPath;
        //判断文件夹不存在则新增
        Path path = Paths.get(folderPath);
        File file = path.toFile();
        if(!file.exists()){
            file.mkdirs();
        }
        String fileUUid = UUIDUtil.getUUID();
        String fileName = folderPath + fileUUid;
        try{
            QRCodeUtil.createQRCode(mapId, fileName);
        }catch (Exception e){
            return null;
        }
        PictureEntity pictureEntity = new PictureEntity();
        pictureEntity.setPictureId(UUIDUtil.getUUID());
        pictureEntity.setFilePath(tempFolderPath + fileUUid + "." +QRCodeUtil.fileType);
        pictureEntity.setRealName(fileUUid + "." + QRCodeUtil.fileType);
        pictureEntity.setCreator(creator);
        pictureEntity.setCreateAt(date);
        pictureService.insertPicture(pictureEntity);
        return pictureEntity;
    }

    @Override
    public void insertRewardActCommandAuto(Integer rewardNum, MatActivityVO matActivity) {
        String sql = "INSERT INTO `reward_act_command` (`command_id`, `command`, `command_type`, `act_id`, `user_id`, `store_id`, `app_info_id`, `used`, `create_at`, `being_used`)";
        String valueSql = "(:command_id, :command, :command_type, :act_id, :user_id, :store_id, :app_info_id, :used, :create_at, :being_used)";
        Date date = new Date();
        int tempCount = matActivity.getPartakeNum() * rewardNum;
        if(tempCount > maxCount){
            tempCount = maxCount;
        }
        List<RewardActCommandEntity> commandList = new ArrayList<RewardActCommandEntity>(tempCount);
        for(int i = 0; i < rewardNum; i++){
            commandList.add(getRewardActCommandEntity(matActivity, 1, date));
            for (int j = 0; j < matActivity.getPartakeNum(); j ++){
                commandList.add(getRewardActCommandEntity(matActivity, 2, date));
            }
            if(commandList.size() >= maxCount){
                jdbcBaseDao.executeBachOneSql(sql, valueSql, commandList);
                commandList.clear();
            }
        }
        if(commandList.size() > 0){
            jdbcBaseDao.executeBachOneSql(sql, valueSql, commandList);
        }
    }

    @Override
    public void insertRewardActCommand(Integer rewardNum, MatActivityVO matActivity, Integer commandType) {
        String sql = "INSERT INTO `reward_act_command` (`command_id`, `command`, `command_type`, `act_id`, `user_id`, `store_id`, `app_info_id`, `used`, `create_at`, `being_used`)";
        String valueSql = "(:command_id, :command, :command_type, :act_id, :user_id, :store_id, :app_info_id, :used, :create_at, :being_used)";
        Date date = new Date();
        int tempCount = rewardNum;
        if(tempCount > maxCount){
            tempCount = maxCount;
        }
        List<RewardActCommandEntity> commandList = new ArrayList<RewardActCommandEntity>(tempCount);
        for(int i = 0; i < rewardNum; i++){
            commandList.add(getRewardActCommandEntity(matActivity, commandType, date));
            if(commandList.size() >= maxCount){
                jdbcBaseDao.executeBachOneSql(sql, valueSql, commandList);
                commandList.clear();
            }
        }
        if(commandList.size() > 0){
            jdbcBaseDao.executeBachOneSql(sql, valueSql, commandList);
        }
    }

    /**
     * 设置奖励信息
     * @param matActivity
     * @param commandType
     * @param date
     * @return
     */
    private RewardActCommandEntity getRewardActCommandEntity(MatActivityVO matActivity, Integer commandType, Date date){
        RewardActCommandEntity rewardActCommandEntity = new RewardActCommandEntity();
        rewardActCommandEntity.setCommandId(UUIDUtil.getUUID());
        rewardActCommandEntity.setCommand(UUID.randomUUID().toString());
        rewardActCommandEntity.setCommandType(commandType);
        rewardActCommandEntity.setActId(matActivity.getActId());
        rewardActCommandEntity.setUserId(matActivity.getUserId());
        rewardActCommandEntity.setStoreId(matActivity.getStoreId());
        rewardActCommandEntity.setAppInfoId(matActivity.getAppInfoId());
        rewardActCommandEntity.setUsed(0);
        rewardActCommandEntity.setCreateAt(date);
        rewardActCommandEntity.setBeingUsed(0);
        return rewardActCommandEntity;
    }

    @Override
    public Page<RewardActCommandEntity> findRewardActCommandPage(String actId, Integer commandType, String storeId, PageBean pageBean) {
        String sql = "select rac.command_id,rac.command,rac.command_type,rac.being_used from reward_act_command rac where rac.act_id = ? and rac.command_type = ? and rac.store_id = ?";
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(actId);
        paramList.add(commandType);
        paramList.add(storeId);
        sql += " order by rac.create_at desc, rac.command_type, rac.command_id";
        return jdbcBaseDao.queryPageWithListParam(RewardActCommandEntity.class, pageBean, sql, paramList);
    }

    @Override
    public boolean deleteRewardActCommand(String actId, Integer commandType, String storeId, String ids) {
        StringBuilder sb = new StringBuilder();
        String[] idArr = ids.split(",");
        List<String> idList = Arrays.asList(idArr);
        for (String str : idArr){
            sb.append(",?");
        }
        sb.delete(0,1);
        String sql = "delete from reward_act_command where act_id = ? and command_type = ? and store_id = ? and command_id in (" + sb + ")";
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(actId);
        paramList.add(commandType);
        paramList.add(storeId);
        paramList.addAll(idList);
        jdbcBaseDao.executeUpdateWithListParam(sql, paramList);
        return true;
    }
}
