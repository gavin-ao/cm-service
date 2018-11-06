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
}
