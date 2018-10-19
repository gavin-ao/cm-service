package data.driven.cm.business.tourism.impl;

import data.driven.cm.business.tourism.TravelsService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.tourism.TravelsEntity;
import data.driven.cm.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 何晋凯
 * @date 2018/06/07
 */
@Service
public class TravelsServiceImpl implements TravelsService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public boolean addTravels(TravelsEntity travels, String pictureIds) {
        travels.setCreateAt(new Date());
        jdbcBaseDao.insert(travels, "ss_travels");
        addTravelsPicts(travels.getTravelsId(), pictureIds);
        return true;
    }

    private boolean addTravelsPicts(String travelId, String pictureIds) {
        String sql = "insert into ss_travels_picture_mapping(ss_map_id,travels_id,picture_id) values";
        StringBuilder sb = new StringBuilder();
        for(String pictureId : pictureIds.split(",")){
            sb.append(",('").append(UUIDUtil.getUUID()).append("','").append(travelId)
                    .append("','").append(pictureId).append("')");
        }
        if(sb.length() > 0){
            sb.delete(0,1);
            jdbcBaseDao.executeUpdate(sql + sb);

        }
        return false;
    }
}
