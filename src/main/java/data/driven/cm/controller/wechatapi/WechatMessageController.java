package data.driven.cm.controller.wechatapi;

import data.driven.cm.util.wx.AesException;
import data.driven.cm.util.wx.SHA1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信客服消息
 * @author hejinkai
 * @date 2018/11/26
 */
@Controller
@RequestMapping(path = "/wechatapi/msg")
public class WechatMessageController {

    private static final Logger logger = LoggerFactory.getLogger(WechatMessageController.class);
    String encodingAesKey = "4yS6SVipHFcVDkcQlI53a4bhbFCNkt2uLHUz73Nijld";
    String token = "mendian";
    String appId = "wx032f66ebe0dcce8f";

    /**
     * 获取微信消息
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/getMessage")
    public String getMessage(String signature, String timestamp, String nonce, String echostr){
        logger.info("signature : " + signature + "----timestamp : " + timestamp + "----nonce : " + nonce + "----echostr : " + echostr);
        try{
            String temp = SHA1.getSHA1(token, timestamp, nonce);
            logger.info("signature : " + signature + "----temp : " + temp);
            if(signature.equals(temp)){
                logger.info("getMessage : " + true);
                return echostr;
            }else{
                logger.info("getMessage : " + false);
            }
            return echostr;
        }catch (AesException e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
