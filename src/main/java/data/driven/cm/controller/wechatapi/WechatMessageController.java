package data.driven.cm.controller.wechatapi;

import com.alibaba.dubbo.common.utils.IOUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import data.driven.cm.business.reward.RewardActCustMsgLogService;
import data.driven.cm.business.reward.RewardActCustMsgService;
import data.driven.cm.business.reward.RewardActCustMsgTotalService;
import data.driven.cm.business.system.PictureService;
import data.driven.cm.common.Constant;
import data.driven.cm.entity.reward.RewardActCustMsgEntity;
import data.driven.cm.entity.reward.RewardActCustMsgLogEntity;
import data.driven.cm.util.HttpUtil;
import data.driven.cm.util.WXUtil;
import data.driven.cm.util.wx.AesException;
import data.driven.cm.util.wx.SHA1;
import data.driven.cm.util.wx.WXBizMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 微信客服消息
 * @author hejinkai
 * @date 2018/11/26
 */
@Controller
@RequestMapping(path = "/wechatapi/msg")
public class WechatMessageController {

    private static final Logger logger = LoggerFactory.getLogger(WechatMessageController.class);
    private static final String encodingAesKey = "4yS6SVipHFcVDkcQlI53a4bhbFCNkt2uLHUz73Nijld";
    private static final String token = "mendian";
    private static final String appId = "wx032f66ebe0dcce8f";
    private static final String secret = "64a61af911d59a37a6fe86a8087a1bfa";
    @Autowired
    private RewardActCustMsgService rewardActCustMsgService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private RewardActCustMsgTotalService rewardActCustMsgTotalService;
    @Autowired
    private RewardActCustMsgLogService rewardActCustMsgLogService;

    private Lock lock = new ReentrantLock();
    /** 发送消息的url **/
    private static final String msgUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
    /** 发送消息的url **/
    private static final String imgUploadUrl = "https://api.weixin.qq.com/cgi-bin/media/upload?type=image&access_token=";
    /** 控制线程的大小，用于异步发送消息时限制并发数量。 **/
    private static final ExecutorService putMsgFixedThreadPool = Executors.newFixedThreadPool(200);

    /**
     * 获取微信消息 - 验证使用
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/getMessage")
    public String getMessage(HttpServletRequest request, String signature, String timestamp, String nonce, String msg_signature, String echostr){
        String postData = null;
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String body = IOUtils.read(reader);
            if(body == null || body.isEmpty()){
                logger.info("business receive somthing with body :"+body);
                return "fail";
            }
            logger.info("body:"+body);
            JSONObject bodyJson = JSONObject.parseObject(body);
            postData = bodyJson.getString("Encrypt");
        }catch (IOException e){
            logger.error(e.getMessage(), e);
            return "fail";
        }
        return getUserMessage(msg_signature, timestamp, nonce, postData);
    }

    /**
     * 用于验证的方法
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    private String getMessageVali(String signature, String timestamp, String nonce, String echostr){
        logger.info("signature : " + signature + "----timestamp : " + timestamp + "----nonce : " + nonce + "----echostr : " + echostr);
        try{
            String temp = SHA1.getSHA1(token, timestamp, nonce, echostr);
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

    /**
     * 微信客服推送微信消息
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/getUserMessage")
    public String getUserMessage(String msgSignature, String timeStamp, String nonce, String postData){
        logger.info("msgSignature : " + msgSignature + "----timeStamp : " + timeStamp + "----nonce : " + nonce + "----postData : " + postData);
        try{
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(token, encodingAesKey, appId);
            String newData = wxBizMsgCrypt.decryptMsg(msgSignature, timeStamp, nonce, postData);
            logger.info("newData:" + newData);
            //异步调用微信接口给用户发送消息
            putMsgFixedThreadPool.execute(() -> putMsgToUser(JSONObject.parseObject(newData)));
            return "success";
        }catch (AesException e){
            logger.error(e.getMessage(), e);
        }
        return "fail";
    }

    /**
     * 向用户发送消息
     * @param newData
     */
    private void putMsgToUser(JSONObject newData){
        JSONObject accessTokenJson = WXUtil.getAccessToken(appId, secret);
        if(accessTokenJson.containsKey("access_token")){
            if(newData.containsKey("SessionFrom")){
                JSONObject sessionFrom = JSONObject.parseObject(newData.getString("SessionFrom"));
                String actId = sessionFrom.getString("actId");
                Integer type = sessionFrom.getInteger("type");
                RewardActCustMsgEntity rewardActCustMsgEntity = rewardActCustMsgService.getRewardActCustMsg(actId, type);
                if(rewardActCustMsgEntity != null){
                    String openid = newData.getString("FromUserName");
                    boolean logSuccess = rewardActCustMsgLogService.getSuccessMsg(openid, rewardActCustMsgEntity.getGlobalId(), type);
                    if(logSuccess){//如果是已经成功发送过奖励，则不再发送
                        logger.info("已经成功发送过，不再进行发送。actId:"+actId+"--openid:"+openid);
                        return;
                    }
                    String logContent = null;
                    String accessToken = accessTokenJson.getString("access_token");
                    JSONObject paramJson = new JSONObject();
                    paramJson.put("touser", openid);
                    try{
                        lock.lock();
                        if(rewardActCustMsgEntity.getType().intValue() == 1){
                            paramJson.put("msgtype", "text");
                            JSONObject content = new JSONObject();
                            content.put("content", rewardActCustMsgEntity.getContent());
                            paramJson.put("text", content);
                            logContent = rewardActCustMsgEntity.getContent();
                        }else if(rewardActCustMsgEntity.getType().intValue() == 2){
                            paramJson.put("msgtype", "image");
                            JSONObject image = new JSONObject();
                            String pictureId = rewardActCustMsgEntity.getContent();
                            Map<String, Object> lastContentMap = rewardActCustMsgTotalService.getLastContentAndNum(rewardActCustMsgEntity.getGlobalId());
                            if(lastContentMap != null){
                                String content = (String) lastContentMap.get("content");
                                Integer totalNum = Integer.valueOf(lastContentMap.get("totalNum").toString());
                                if(totalNum < Constant.MAX_WXQ_NUMBER){
                                    pictureId = content;
                                }else{
                                    if(pictureId.indexOf(",") > 0){
                                        List<String> pictureIdList = Arrays.asList(pictureId.split(","));
                                        int index = pictureIdList.indexOf(content);
                                        if(index < pictureIdList.size()){
                                            pictureId = pictureIdList.get(index + 1);
                                        }else{
                                            logger.info("putMsgToUser失败，已经达到峰值，不能再继续发送了。actId:"+actId+"--openid:"+openid+"--GlobalId:"+rewardActCustMsgEntity.getGlobalId());
                                            return;
                                        }
                                    }else{
                                        logger.info("putMsgToUser失败，已经达到峰值，不能再继续发送了。actId:"+actId+"--openid:"+openid+"--GlobalId:"+rewardActCustMsgEntity.getGlobalId());
                                        return;
                                    }
                                }
                            }else{
                                List<String> pictureIdList = Arrays.asList(pictureId.split(","));
                                pictureId = pictureIdList.get(0);
                            }
                            if(pictureId == null){
                                logger.info("putMsgToUser失败，pictureId为空。pictureId:"+pictureId);
                                return;
                            }

                            String filePath = pictureService.getPicturePath(pictureId);
                            if(filePath == null){
                                logger.info("putMsgToUser失败，filePath为空。filePath:"+filePath);
                                return;
                            }

                            filePath = Constant.FILE_UPLOAD_PATH + filePath;

                            String resultStr = HttpUtil.doPostSSLUploadImg(imgUploadUrl + accessToken, null, "media", filePath);
                            JSONObject result = JSONObject.parseObject(resultStr);
                            if(result.containsKey("media_id")){
                                image.put("media_id", result.getString("media_id"));
                                paramJson.put("image", image);
                                logContent = pictureId;
                            }else{
                                logger.info("putMsgToUser失败，图片上传失败，msg：" + resultStr);
                                return;
                            }
                        }else{
                            logger.info("putMsgToUser失败，活动奖励类型不正确,type" + rewardActCustMsgEntity.getType());
                            return;
                        }

                        String resultStr = HttpUtil.doPostSSLJson(msgUrl + accessToken, paramJson);

                        JSONObject result = JSON.parseObject(resultStr);
                        addSendWXMsgLog(type, rewardActCustMsgEntity, openid, logContent, resultStr, result);
                        logger.info("putMsgToUser成功： resultStr:" + resultStr);
                    }catch (Exception e){
                        logger.error(e.getMessage(), e);
                        return;
                    }finally {
                        lock.unlock();
                    }
                }
            }else{
                logger.info("putMsgToUser失败： SessionFrom为空");
                return ;
            }
        }
    }

    /**
     * 发送微信客服消息日志记录
     * @param type
     * @param rewardActCustMsgEntity
     * @param openid
     * @param logContent
     * @param resultStr
     * @param result
     */
    private void addSendWXMsgLog(Integer type, RewardActCustMsgEntity rewardActCustMsgEntity, String openid, String logContent, String resultStr, JSONObject result) {
        if(result.containsKey("errcode")){
            RewardActCustMsgLogEntity rewardActCustMsgLogEntity = new RewardActCustMsgLogEntity();
            rewardActCustMsgLogEntity.setGlobalId(rewardActCustMsgEntity.getGlobalId());
            rewardActCustMsgLogEntity.setActId(rewardActCustMsgEntity.getActId());
            rewardActCustMsgLogEntity.setStoreId(rewardActCustMsgEntity.getStoreId());
            rewardActCustMsgLogEntity.setAppInfoId(rewardActCustMsgEntity.getAppInfoId());
            rewardActCustMsgLogEntity.setOpenid(openid);
            rewardActCustMsgLogEntity.setContent(logContent);
            rewardActCustMsgLogEntity.setContentType(rewardActCustMsgEntity.getType());
            rewardActCustMsgLogEntity.setRewardType(type);
            rewardActCustMsgLogEntity.setOpenid(openid);
            rewardActCustMsgLogEntity.setType(1);
            if(result.getInteger("errcode").intValue() == 0){
                rewardActCustMsgLogEntity.setStats(1);
                if(rewardActCustMsgEntity.getType().intValue() == 2){
                    rewardActCustMsgTotalService.totalReward(rewardActCustMsgEntity.getGlobalId(), logContent);
                }
            }else{
                rewardActCustMsgLogEntity.setStats(2);
                rewardActCustMsgLogEntity.setErrorMsg(resultStr);
            }
            rewardActCustMsgLogService.addLog(rewardActCustMsgLogEntity);
        }
    }

}
