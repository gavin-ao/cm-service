package data.driven.cm.entity.system;

import java.util.Date;

/**
 * 门店实体
 * @author hejinkai
 * @date 2018/10/27
 */
public class StoreEntity {
    /** 主键 **/
    private String storeId;
    /** 小程序id **/
    private String appInfoId;
    /** 系统用户id **/
    private String userId;
    /** 门店名称 **/
    private String storeName;
    /** 门店地址 **/
    private String storeAddr;
    /** 省份 **/
    private String province;
    /** 城市 **/
    private String city;
    /** 国家 **/
    private String country;
    /** 门店二维码 **/
    private String pictureId;
    /** 门店管理员 **/
    private String manager;
    /** 门店状态，1 - 营业， 2 - 暂停营业 **/
    private Integer stats;
    /** 创建日期 **/
    private Date createAt;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAppInfoId() {
        return appInfoId;
    }

    public void setAppInfoId(String appInfoId) {
        this.appInfoId = appInfoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddr() {
        return storeAddr;
    }

    public void setStoreAddr(String storeAddr) {
        this.storeAddr = storeAddr;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Integer getStats() {
        return stats;
    }

    public void setStats(Integer stats) {
        this.stats = stats;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
