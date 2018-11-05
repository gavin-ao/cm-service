package data.driven.cm.common;

import data.driven.cm.entity.user.RoleEntity;

import java.util.List;

/**
 * 登录授权信息
 * @author hejinkai
 * @date 2018/11/5
 */
public class AuthorizeBean {
    /** 角色信息 **/
    private List<RoleEntity> roleList;

    public List<RoleEntity> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleEntity> roleList) {
        this.roleList = roleList;
    }
}
