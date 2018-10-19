package data.driven.cm.business.tourism.impl;

import data.driven.cm.business.tourism.TravelsTextTemplateService;
import data.driven.cm.dao.JDBCBaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 何晋凯
 * @date 2018/06/07
 */
@Service
public class TravelsTextTemplateServiceImpl implements TravelsTextTemplateService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public List<String> findTravelsTextTemplateList(String scenicSpotId) {
        String sql = "select template_text from ss_travels_text_template where scenic_spot_id = ? order by create_at desc";
        return jdbcBaseDao.getColumns(String.class, sql, scenicSpotId);
    }
}
