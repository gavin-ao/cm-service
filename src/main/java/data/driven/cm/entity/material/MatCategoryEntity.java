package data.driven.cm.entity.material;

import java.util.Date;

/**
 * 素材-产品分类表信息
 * @author 何晋凯
 * @date 2018/07/13
 */
public class MatCategoryEntity {
    /** 类型主键 **/
    private String catgId;
    /** 用户id **/
    private String userId;
    /** 父级id **/
    private String pid;
    /** 分类类型名称 **/
    private String catgName;
    /** 层级码 **/
    private String levelCode;
    /** 当前层级 **/
    private Integer level;
    /** 创建时间 **/
    private Date createAt;

    public String getCatgId() {
        return catgId;
    }

    public void setCatgId(String catgId) {
        this.catgId = catgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCatgName() {
        return catgName;
    }

    public void setCatgName(String catgName) {
        this.catgName = catgName;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
