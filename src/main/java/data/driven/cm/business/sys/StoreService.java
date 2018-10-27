package data.driven.cm.business.sys;

import data.driven.cm.entity.sys.StoreEntity;

/**
 * 门店service
 * @author hejinkai
 * @date 2018/10/27
 */
public interface StoreService {

    /**
     * 根据id查询数据对象
     * @param storeId
     * @return
     */
    public StoreEntity getStoreById(String storeId);

}
