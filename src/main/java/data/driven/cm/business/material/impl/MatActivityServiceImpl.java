package data.driven.cm.business.material.impl;

import data.driven.cm.business.material.MatActivityService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.vo.material.MatActivityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动-素材service
 * @author hejinkai
 * @date 2018/7/15
 */
@Service
public class MatActivityServiceImpl implements MatActivityService{

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

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
        String sql = "select p.file_path,ma.act_id,ma.act_name,ma.act_reply,ma.act_title,ma.act_share_title,ma.act_share_copywriting,ma.act_rule,ma.exchange_rule,ma.partake_num from mat_activity ma left join sys_picture p on p.picture_id = ma.picture_id where ma.act_id = ?";
        List<MatActivityVO> list = jdbcBaseDao.queryList(MatActivityVO.class, sql, actId);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public MatActivityVO getMatActivityInfoByApp(String appInfoId) {
        String sql = "select p.file_path,ma.act_id,ma.act_name,ma.act_reply,ma.act_title,ma.act_share_title,ma.act_share_copywriting,ma.act_rule,ma.exchange_rule,ma.partake_num from mat_activity ma" +
                " left join sys_picture p on p.picture_id = ma.picture_id where ma.app_info_id = ? and ma.status = 1";
        List<MatActivityVO> list = jdbcBaseDao.queryList(MatActivityVO.class, sql, appInfoId);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
    @Override
    public MatActivityVO getAnyMatActivityInfoByApp(String appInfoId) {
        String sql = "select ma.act_id,ma.reward_url from mat_activity ma where ma.app_info_id = ? and ma.status = 1";
        List<MatActivityVO> list = jdbcBaseDao.queryList(MatActivityVO.class, sql, appInfoId);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
