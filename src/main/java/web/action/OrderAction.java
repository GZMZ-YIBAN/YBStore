package web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import entity.YibanProductEntity;
import entity.YibanProductGetEntity;
import entity.YibanProductOrderEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import service.IOrderService;
import service.IProductService;
import service.IUserService;
import service.impl.OrderServiceImpl;
import utils.AppContext;
import utils.DoUtil;
import utils.HMACSHA256;
import utils.HttpMethod;
import utils.redis.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * -----------------------------
 * Copyright © 2017, EchoCow
 * All rights reserved.
 *
 * @author EchoLZY
 * @version 2.0
 * <p> 支付成功后跳转，对订单信息存放
 * -----------------------------
 * @description 订单结果查询
 * @date 23:02 2018/4/18
 * @modified By EchoLZY
 */
public class OrderAction extends ActionSupport{
    /**
     * 日志
     */
    private static Logger logger = LogManager.getLogger(OrderAction.class.getName());
    /**
     * 支付完成时间错
     */
    private String end_time;
    /**
     * 支付结果
     */
    private String trade_end;
    /**
     * 订单号
     */
    private String trade_id;
    /**
     * 订单号+结束时间错后进行sha256_HMAC加密后的密文
     * 用于验证订单是否合法
     */
    private String yb_sign;
    /**
     * 缓存工具
     */
    private RedisUtil redisUtil;
    /**
     * 结果
     */
    private String result;
    /**
     * 依赖注入
     */
    private IProductService productService;
    /**
     * 依赖注入
     */
    private IUserService userService;
    /**
     * 依赖注入
     */
    private IOrderService orderService;
    /**
     * 短信提示拼接第一部分
     */
    private String tip1;
    /**
     * 短信提示拼接第二部分
     */
    private String tip2;

    /**
     * 订单支付成功后数据库，下发短信，并删除临时缓存数据
     * @description
     * 可能出现的错误代码 <br/>
     *      510     严重错误！支付成功但存入出现异常！！
     * @return 结果
     */
    public String getOrder(){
        try{
            System.out.println("trade_id" + trade_id);
            System.out.println("trade_end" + trade_end);
            System.out.println("end_time" + end_time);
            System.out.println("yb_sign" + yb_sign);

            String hmac = HMACSHA256.sha256_HMAC(trade_id + end_time, AppContext.APP_SEC);
            System.out.println(hmac.equals(yb_sign));
            if (!(hmac.equals(yb_sign))){
                logger.info("网薪支付错误！验证不通过。");
                System.out.println("网薪支付错误！验证不通过。");
                return ERROR;
            }
            System.out.println("网薪支付成功，开始存入数据库！");
            logger.info("网薪支付成功，开始存入数据库！");
            YibanProductOrderEntity yibanProductOrderEntity = orderService.findByTradeId(trade_id);
            if ("1".equals(yibanProductOrderEntity.getEnable())){
                return SUCCESS;
            }

            YibanProductEntity yibanProductEntity  = productService.findById(Integer.parseInt(yibanProductOrderEntity.getProductId()));
            /**
             * 发送推送消息
             */
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            System.out.println(yibanProductOrderEntity.getAccessToken());
            System.out.println(yibanProductOrderEntity.getUserId());
            String phone = yibanProductOrderEntity.getPhone();
            list.add(new BasicNameValuePair("access_token",yibanProductOrderEntity.getAccessToken()));
            list.add(new BasicNameValuePair("to_yb_uid",yibanProductOrderEntity.getUserId()));
            list.add(new BasicNameValuePair("content",tip1 + yibanProductEntity.getName() + tip2));
            String post = HttpMethod.getPOST(AppContext.SEND_URL, list);
            JSONObject res = JSON.parseObject(post);
            System.out.println(post);
            System.out.println("消息下发结果" + res.getString("status"));
            /**
             * 发送短信
             */
            Map<String, String> map = DoUtil.setPar(phone, yibanProductEntity.getName());
            post = HttpMethod.post(AppContext.XSEND_URL, map);
            System.out.println(post);
            res = JSON.parseObject(post);
            System.out.println("短信发送结果 + " + res.get("status"));
            productService.savePsy(yibanProductOrderEntity);
            productService.findAll();
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(YibanProductGetEntity.class);
            detachedCriteria.add(Restrictions.eq("yibanId",yibanProductOrderEntity.getUserId()));
            userService.findByUser(yibanProductOrderEntity.getUserId(),detachedCriteria);
            System.out.println("完成");
            return SUCCESS;
        }catch (Exception e){
            logger.error("支付完成后出现异常：" + e.getMessage());
            logger.fatal("异常详细信息：" + DoUtil.getExceptionInfo(e));
            return ERROR;
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getTrade_end() {
        return trade_end;
    }

    public void setTrade_end(String trade_end) {
        this.trade_end = trade_end;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }

    public String getYb_sign() {
        return yb_sign;
    }

    public void setYb_sign(String yb_sign) {
        this.yb_sign = yb_sign;
    }

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public void setProductService(IProductService productService) {
        this.productService = productService;
    }

    public void setFindByUserService(IUserService findByUserService) {
        this.userService = findByUserService;
    }

    public void setTip1(String tip1) {
        this.tip1 = tip1;
    }

    public String getTip1() {
        return tip1;
    }

    public void setTip2(String tip2) {
        this.tip2 = tip2;
    }

    public String getTip2() {
        return tip2;
    }

    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }
}
