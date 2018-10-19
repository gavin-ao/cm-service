package data.driven.cm.business.material.impl;

import data.driven.cm.business.material.MatCategoryService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.material.MatCategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 素材-产品分类service
 * @author hejinkai
 * @date 2018/7/13
 */
@Service
public class MatCategoryServiceImpl implements MatCategoryService{

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public MatCategoryEntity getMatCategoryNoUser(String catgId) {
        String sql = "select catg_id,catg_name,level_code,create_at from mat_category where catg_id = ?";
        List<MatCategoryEntity> entityList = jdbcBaseDao.queryList(MatCategoryEntity.class, sql, catgId);
        if(entityList != null && entityList.size() > 0){
            return entityList.get(0);
        }
        return null;
    }

    @Override
    public List<MatCategoryEntity> findMatCategoryListByAppInfo(String appInfoId) {
        //TODO
        return null;
    }
}
