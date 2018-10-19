package data.driven.cm.business.tourism.impl;

import data.driven.cm.business.tourism.SceneryService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.vo.tourism.SceneryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 何晋凯
 * @date 2018/06/06
 */
@Service
public class SceneryServiceImpl implements SceneryService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public List<SceneryVO> findSceneryList(String scenicSpotId) {
        String sql = "select sce.scenery_id,sce.scenery_name,sce.scenery_introduce,spic.file_path from ss_scenery sce" +
                " left join sys_picture spic on spic.picture_id = sce.picture_id" +
                " where sce.scenic_spot_id = ? order by sce.ord";
        List<SceneryVO> sceneryList = jdbcBaseDao.queryList(SceneryVO.class, sql, scenicSpotId);
        return sceneryList;
    }
}
