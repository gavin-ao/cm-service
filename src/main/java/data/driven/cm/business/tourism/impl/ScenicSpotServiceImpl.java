package data.driven.cm.business.tourism.impl;

import data.driven.cm.business.tourism.ScenicSpotService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.tourism.ScenicSpotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 景区service
 * @author 何晋凯
 * @date 2018/06/06
 */
@Service
public class ScenicSpotServiceImpl implements ScenicSpotService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public ScenicSpotEntity getScenicSpot(String scenicSpotId) {
        String sql = "select scenic_spot_id,scenic_spot_name,create_at from ss_scenic_spot where scenic_spot_id = ?";
        List<ScenicSpotEntity> entityList = jdbcBaseDao.queryList(ScenicSpotEntity.class, sql, scenicSpotId);
        if(entityList != null && entityList.size() > 0){
            return entityList.get(0);
        }
        return null;
    }
}
