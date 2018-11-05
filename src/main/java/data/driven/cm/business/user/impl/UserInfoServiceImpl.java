package data.driven.cm.business.user.impl;

import data.driven.cm.business.user.UserInfoService;
import data.driven.cm.dao.JDBCBaseDao;
import data.driven.cm.entity.user.UserInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户信息
 * @author hejinkai
 * @date 2018/7/1
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private JDBCBaseDao jdbcBaseDao;

    @Override
    public UserInfoEntity getUser(String userName, String pwd) {
        String sql = "select user_id,user_name,nick_name,real_name,email,mobile_phone,wechat_number,sex,company_name,job,create_at from sys_user_info where user_name = ? and pwd = ?";
        List<UserInfoEntity> list = jdbcBaseDao.queryList(UserInfoEntity.class, sql, userName, pwd);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public String getUserIdByUserName(String userName) {
        String sql = "select user_id from sys_user_info where user_name = ? ";
        Object userId = jdbcBaseDao.getColumn(sql, userName);
        if(userId != null){
            return userId.toString();
        }
        return null;
    }

    @Override
    public boolean addUser(UserInfoEntity userInfo) {
        String userId = getUserIdByUserName(userInfo.getUserName());
        if(userId != null){
            return false;
        }
        jdbcBaseDao.insert(userInfo, "sys_user_info");
        return true;
    }

    @Override
    public Map<String, String> findUserNameMapByUserId(List<String> userIdList) {
        if(userIdList != null && userIdList.size() > 0){
            StringBuilder sb = new StringBuilder();
            for(String str : userIdList){
                sb.append(",?");
            }
            sb.delete(0,1);
            String sql = "select user_id,user_name from sys_user_info where user_id in (" + sb + ")";
            List<UserInfoEntity> list = jdbcBaseDao.queryListWithListParam(UserInfoEntity.class, sql, userIdList);
            if(list != null && list.size() > 0){
                return list.stream().collect(Collectors.toMap(o -> o.getUserId(), o -> o.getUserName()));
            }
        }
        return null;
    }
}
