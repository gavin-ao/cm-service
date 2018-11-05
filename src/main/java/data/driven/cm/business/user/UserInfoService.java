package data.driven.cm.business.user;

import data.driven.cm.entity.user.UserInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统用户service
 * @author hejinkai
 * @date 2018/7/1
 */

public interface UserInfoService {

    /**
     * 根据用户和密码获取用户信息
     * @param userName
     * @param pwd
     * @return
     */
    public UserInfoEntity getUser(String userName, String pwd);

    /**
     * 根据用户名称获取用户id
     * @param userName
     * @return
     */
    public String getUserIdByUserName(String userName);

    /**
     * 新建用户
     * @param userInfo
     * @return
     */
    public boolean addUser(UserInfoEntity userInfo);

    /**
     * 根据用户id集合查询用户名集合，并利用id做key，用户名做value返回map
     * @param userIdList
     * @return
     */
    public Map<String, String> findUserNameMapByUserId(List<String> userIdList);

}
