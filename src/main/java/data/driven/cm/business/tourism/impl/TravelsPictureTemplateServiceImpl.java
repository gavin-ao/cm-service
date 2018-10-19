package data.driven.cm.business.tourism.impl;

import data.driven.cm.business.tourism.TravelsPictureTemplateService;
import data.driven.cm.dao.JDBCBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 何晋凯
 * @date 2018/06/07
 */
@Service
public class TravelsPictureTemplateServiceImpl implements TravelsPictureTemplateService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public List<String> findTravelsPictureTemplateList(String scenicSpotId) {
        String sql = "select pict.file_path from ss_travels_picture_template tpt" +
                " left join sys_picture pict on pict.picture_id = tpt.picture_id" +
                " where tpt.scenic_spot_id = ? order by tpt.create_at desc";
        return jdbcBaseDao.getColumns(String.class, sql, scenicSpotId);
    }
}
