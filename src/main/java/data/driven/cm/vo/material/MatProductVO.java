package data.driven.cm.vo.material;

import data.driven.cm.entity.material.MatProductEntity;

/**
 * 素材-产品信息表VO
 * @author hejinkai
 * @date 2018/7/13
 */
public class MatProductVO extends MatProductEntity {
    /** 产品图片磁盘路径path **/
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
