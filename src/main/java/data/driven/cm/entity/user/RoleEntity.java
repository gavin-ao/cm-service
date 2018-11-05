package data.driven.cm.entity.user;

import java.util.Date;

/**
 * 角色信息
 * @author hejinkai
 * @date 2018/11/5
 */
public class RoleEntity {
    /** 主键 **/
    private String roleId;
    /** 角色名称 **/
    private String roleName;
    /** 角色编号 **/
    private String roleCode;
    /** 备注 **/
    private String remark;
    /** 创建人 **/
    private String creator;
    /** 创建时间 **/
    private Date createAt;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
