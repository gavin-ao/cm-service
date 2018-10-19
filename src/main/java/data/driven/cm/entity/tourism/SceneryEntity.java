package data.driven.cm.entity.tourism;

/**
 * 景区景观Entity
 * @author 何晋凯
 * @date 2018/06/06
 */
public class SceneryEntity {
    /** 景区景观id **/
    private String sceneryId;
    /** 景区外键 **/
    private String scenicSpotId;
    /** 景区景观名称 **/
    private String sceneryName;
    /** 景观介绍 **/
    private String sceneryIntroduce;
    /** 景观图片外键 **/
    private String pictureId;
    /** 排序 **/
    private Integer ord;

    public String getSceneryId() {
        return sceneryId;
    }

    public void setSceneryId(String sceneryId) {
        this.sceneryId = sceneryId;
    }

    public String getScenicSpotId() {
        return scenicSpotId;
    }

    public void setScenicSpotId(String scenicSpotId) {
        this.scenicSpotId = scenicSpotId;
    }

    public String getSceneryName() {
        return sceneryName;
    }

    public void setSceneryName(String sceneryName) {
        this.sceneryName = sceneryName;
    }

    public String getSceneryIntroduce() {
        return sceneryIntroduce;
    }

    public void setSceneryIntroduce(String sceneryIntroduce) {
        this.sceneryIntroduce = sceneryIntroduce;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }
}
