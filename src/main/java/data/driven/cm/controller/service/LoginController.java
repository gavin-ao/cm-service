package data.driven.cm.controller.service;

import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.user.RoleService;
import data.driven.cm.business.user.UserInfoService;
import data.driven.cm.common.ApplicationSessionFactory;
import data.driven.cm.common.AuthorizeBean;
import data.driven.cm.entity.user.RoleEntity;
import data.driven.cm.entity.user.UserInfoEntity;
import data.driven.cm.util.JSONUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户登录
 * @author hejinkai
 * @date 2018/7/1
 */
@Controller
@RequestMapping(path = "/service")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RoleService roleService;

    @RequestMapping(path = "/login")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView("/service/login");

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(path = "/execuLogin")
    public JSONObject execuLogin(HttpServletRequest request, HttpServletResponse response, String userName, String pwd){
        UserInfoEntity user = userInfoService.getUser(userName, pwd);
        if(user == null){
            return JSONUtil.putMsg(false, "101", "登录失败");
        }
        ApplicationSessionFactory.setUser(request, response, user);
        List<RoleEntity> roleList = roleService.findRoleByUserId(user.getUserId());
        AuthorizeBean authorizeBean = new AuthorizeBean();
        authorizeBean.setRoleList(roleList);
        ApplicationSessionFactory.setAuthorize(request, response, authorizeBean);
        return JSONUtil.putMsg(true, "200", "登录成功。\n用户昵称：" + user.getNickName());
    }

    @ResponseBody
    @RequestMapping(path = "/logout")
    public JSONObject logout(HttpServletRequest request, HttpServletResponse response){
        ApplicationSessionFactory.clearSession(request, response);
        return JSONUtil.putMsg(true, "200", "登出成功");
    }

}
