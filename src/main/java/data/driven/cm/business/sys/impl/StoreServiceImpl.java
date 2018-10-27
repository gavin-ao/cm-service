package data.driven.cm.business.sys.impl;

import data.driven.cm.business.sys.StoreService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.sys.StoreEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 门店service
 * @author hejinkai
 * @date 2018/10/27
 */
@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public StoreEntity getStoreById(String storeId) {
        String sql = "select store_id,user_id,store_name,store_addr,province,city,country from sys_store where store_id = ?";
        List<StoreEntity> storeList = jdbcBaseDao.queryList(StoreEntity.class, sql, storeId);
        if(storeList != null && storeList.size() > 0){
            return storeList.get(0);
        }
        return null;
    }
}
