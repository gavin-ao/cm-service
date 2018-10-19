package data.driven.cm.business.material;

import data.driven.cm.entity.material.MatCategoryEntity;

import java.util.List;

/**
 * 素材-产品分类service
 * @author hejinkai
 * @date 2018/7/13
 */
public interface MatCategoryService {

    /**
     * 根据分类id获取分类信息 - 脱敏信息,不包含用户id，小程序id
     * @param catgId
     * @return
     */
    public MatCategoryEntity getMatCategoryNoUser(String catgId);

    /**
     * 根据小程序获取所有的分类
     * @param appInfoId
     * @return
     */
    public List<MatCategoryEntity> findMatCategoryListByAppInfo(String appInfoId);
}
