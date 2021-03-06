package data.driven.cm.business.system;

import data.driven.cm.entity.system.PictureEntity;

import java.util.List;

/**
 * 图片service
 * @author hejinkai
 * @date 2018/11/7
 */
public interface PictureService {

    /**
     * 插入数据库
     * @param list
     */
    public void insertManyPicture(List<PictureEntity> list);

    /**
     * 插入数据
     * @param pictureEntity
     */
    public void insertPicture(PictureEntity pictureEntity);

    /**
     * 根据id集合查询图片地址
     * @param ids
     * @return
     */
    public List<String> findPictureByIds(List<String> ids);

    /**
     * 根据id获取路径
     * @param pictureId
     * @return
     */
    public String getPicturePath(String pictureId);
}
