package data.driven.cm.entity.material;

import java.util.Date;

/**
 * 素材-产品信息表
 * @author hejinkai
 * @date 2018/7/13
 */
public class MatProductEntity {
    /** 产品主键 **/
    private String prodId;
    /** 用户id **/
    private String userId;
    /** 分类id **/
    private String catgId;
    /** 层级码 **/
    private String levelCode;
    /** 产品名称 **/
    private String prodName;
    /** 产品介绍 **/
    private String prodIntroduce;
    /** 产品图片 **/
    private String pictureId;
    /** 创建时间 **/
    private Date createAt;

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCatgId() {
        return catgId;
    }

    public void setCatgId(String catgId) {
        this.catgId = catgId;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdIntroduce() {
        return prodIntroduce;
    }

    public void setProdIntroduce(String prodIntroduce) {
        this.prodIntroduce = prodIntroduce;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
