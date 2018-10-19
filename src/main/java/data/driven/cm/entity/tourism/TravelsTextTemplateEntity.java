package data.driven.cm.entity.tourism;

import java.util.Date;

/**
 * 景区游记文本模板Entity
 * @author 何晋凯
 * @date 2018/06/06
 */
public class TravelsTextTemplateEntity {
    /** 游记模板主键 **/
    private String ttId;
    /** 游记模板文字 **/
    private String templateText;
    /** 景区外键 **/
    private String scenicSpotId;
    /** 模板创建时间 **/
    private Date createAt;

    public String getTtId() {
        return ttId;
    }

    public void setTtId(String ttId) {
        this.ttId = ttId;
    }

    public String getTemplateText() {
        return templateText;
    }

    public void setTemplateText(String templateText) {
        this.templateText = templateText;
    }

    public String getScenicSpotId() {
        return scenicSpotId;
    }

    public void setScenicSpotId(String scenicSpotId) {
        this.scenicSpotId = scenicSpotId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
