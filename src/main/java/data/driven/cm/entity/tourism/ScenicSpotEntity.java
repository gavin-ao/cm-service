package data.driven.cm.entity.tourism;

import java.util.Date;

/**
 * 景区信息
 * @author 何晋凯
 * @date 2018/06/06
 */
public class ScenicSpotEntity {
    /** 景区主键 **/
    private String scenicSpotId;
    /** 景区名称 **/
    private String scenicSpotName;
    /** 创建时间 **/
    private Date createAt;

    public String getScenicSpotId() {
        return scenicSpotId;
    }

    public void setScenicSpotId(String scenicSpotId) {
        this.scenicSpotId = scenicSpotId;
    }

    public String getScenicSpotName() {
        return scenicSpotName;
    }

    public void setScenicSpotName(String scenicSpotName) {
        this.scenicSpotName = scenicSpotName;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
