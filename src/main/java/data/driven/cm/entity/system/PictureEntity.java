package data.driven.cm.entity.system;

import java.util.Date;

/**
 * 图片实体
 * @author hejinkai
 * @date 2018/11/7
 */
public class PictureEntity {
    /** 主键 **/
    private String pictureId;
    /** 图片存储路径 **/
    private String filePath;
    /** 文件真实名称 **/
    private String realName;
    /** 创建人 **/
    private String creator;
    /** 创建日期 **/
    private Date createAt;

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
