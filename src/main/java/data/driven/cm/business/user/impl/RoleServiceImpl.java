package data.driven.cm.business.user.impl;

import data.driven.cm.business.user.RoleService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.user.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hejinkai
 * @date 2018/11/5
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public List<RoleEntity> findRoleByUserId(String userId) {
        String sql = "select r.role_id,r.role_name,r.role_code from sys_role r" +
                " left join sys_user_role_mapping urm on r.role_id = urm.role_id" +
                " where urm.user_id = ?";
        List<RoleEntity> roleList = jdbcBaseDao.queryList(RoleEntity.class, sql, userId);
        return roleList;
    }
}
