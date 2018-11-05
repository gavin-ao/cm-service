package data.driven.cm.business.user;

import data.driven.cm.entity.user.RoleEntity;

import java.util.List;

/**
 * 角色service
 * @author hejinkai
 * @date 2018/11/5
 */
public interface RoleService {

    /**
     * 根据用户id查询角色列表
     * @param userId
     * @return
     */
    public List<RoleEntity> findRoleByUserId(String userId);
}
