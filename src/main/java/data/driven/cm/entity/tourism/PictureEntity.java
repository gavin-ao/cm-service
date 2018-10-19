package data.driven.cm.entity.tourism;

import java.util.Date;

/**
 * 图片文件信息
 * @author 何晋凯
 * @date 2018/06/07
 */
public class PictureEntity {
    /** 图片主键 **/
    private String pictureId;
    /** 图片存储路径 **/
    private String filePath;
    /** 文件真实名称 **/
    private String realName;
    /** 微信用户 **/
    private String wechatUser;
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

    public String getWechatUser() {
        return wechatUser;
    }

    public void setWechatUser(String wechatUser) {
        this.wechatUser = wechatUser;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
