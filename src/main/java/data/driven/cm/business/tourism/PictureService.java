package data.driven.cm.business.tourism;

import data.driven.cm.entity.tourism.PictureEntity;

/**
 * 图片文件信息Service
 * @author 何晋凯
 * @date 2018/06/07
 */
public interface PictureService {

    /**
     * 新增图片信息
     * @param picture
     * @return
     */
    public boolean addPicture(PictureEntity picture);
}
