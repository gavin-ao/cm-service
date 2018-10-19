package data.driven.cm.vo.tourism;

import data.driven.cm.entity.tourism.SceneryEntity;

/**
 * 景观vo
 * @author 何晋凯
 * @date 2018/06/06
 */
public class SceneryVO extends SceneryEntity {
    /** 景观图片磁盘路径path **/
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
