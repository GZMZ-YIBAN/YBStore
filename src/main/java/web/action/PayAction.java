package web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import entity.YibanProductEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import service.IOrderService;
import service.IProductService;
import service.impl.OrderServiceImpl;
import service.impl.ProductServiceImpl;
import utils.AppContext;
import utils.DoUtil;
import utils.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * -----------------------------
 * Copyright © 2017, EchoCow
 * All rights reserved.
 *
 * @author EchoLZY
 * @version 2.0
 * 网薪支付 Action ，用于获取网薪支付 url <br/>
 *
 * -----------------------------
 * @description 网薪支付
 * @date 11:05 2018/4/17
 * @modified By EchoLZY
 */
public class PayAction extends ActionSupport{
    /**
     * 日志
     */
    private static Logger logger = LogManager.getLogger(PayAction.class.getName());
    /**
     * 本商品需要支付的网薪
     */
    private String needMoney;
    /**
     * 当前用户所拥有的网薪
     */
    private String nowMoney;
    /**
     * 当前用户的电话号码
     */
    private String phone;
    /**
     * 当前商品的 id 号
     */
    private String pId;
    /**
     * 商品服务以来
     */
    private IProductService productService;
    /**
     * 订单
     */
    private IOrderService orderService;
    /**
     * 获取结果
     */
    private String subRes;

    /**
     * 支付的用户
     */
    private String payUser;

    /**
     * 获取网薪支付的 url 的方法
     * @description
     * 可能返回的错误代码 <br/>
     *      501     参数错误或程序出现异常     -->     前台修改参数或程序出现异常 <br/>
     *      600     网薪不足                 -->     前台修改参数后台再次判断   <br/>
     *      601     未知商品id               -->     前台修改参数后台再次判断   <br/>
     *      602     商品获取出现异常          -->     数据库或service出现错误   <br/>
     *      603     商品数目不足             -->      前台修改参数后台再次判断  <br/>
     *      604     手机格式错误             -->      前台修改参数后台再次判断  <br/>
     *      605     前后价格不一             -->      前台修改参数后台再次判断  <br/>
     *      606     网薪支付链接生成失败      -->      易班返回错误或者json解析失败<br/>
     * 成功时传递的 json <br/>
     *      {<br/>
     *          "status":"success", <br/>
     *          "code":"200",       <br/>
     *          "url":订单支付地址    <br/>
     *      }<br/>
     * @return  结果
     */
    public String pay() {
        Map<String,String> map = new HashMap<String, String>();
        try {
            if (StringUtils.isEmpty(pId) || StringUtils.isEmpty(needMoney)
                    || StringUtils.isEmpty(nowMoney) || StringUtils.isEmpty(phone)){
                logger.error("参数错误");
                return ERROR;
            }
            Integer id = Integer.parseInt(pId);
            Integer need = Integer.parseInt(needMoney);
            Integer now = Integer.parseInt(nowMoney);
            if (need > now){
                logger.error("网薪不足");
                return "error0";
            }
            if (id <= 0){
                logger.error("未知商品ID，错误。");
                return "error1";
            }
            YibanProductEntity yibanProductEntity = productService.findById(id);
            if (yibanProductEntity == null){
                logger.error("商品获取出现异常");
                return "error2";
            }
            if (yibanProductEntity.getNumber() <= 0){
                logger.error("商品数目不足");
                return "error3";
            }
            if (phone.length() != 11){
                logger.error("手机号格式错误");
                return "error4";
            }
            if (yibanProductEntity.getPrice() != need){
                logger.error("前后价格不一致。。。");
                return "error5";
            }
            HttpServletRequest request = ServletActionContext.getRequest();
            String access_token = request.getSession().getAttribute("access_token").toString();
            System.out.println(access_token);
            System.out.println(id);
            System.out.println(now);
            System.out.println(need);
            System.out.println(phone);
            String getUrl = AppContext.PAY_API + "?" + "access_token="
                    + access_token + "&pay=" + need + "&sign_back=" + AppContext.PAY_CALLBACK
                    + "&yb_userid=" + payUser;
            System.out.println("支付给的用户id：" + payUser);
            String payRes = HttpMethod.get(getUrl);
            System.out.println(payRes);
            JSONObject pay = JSON.parseObject(payRes);
            if (ERROR.equals(pay.get("status"))){
                logger.error("网薪支付链接生成失败");
                return "error6";
            }
            JSONObject info = pay.getJSONObject("info");
            System.out.println("````````````````````" + info.toJSONString());
            String user = request.getSession().getAttribute("user").toString();
            JSONObject userInfo = JSON.parseObject(user);
            orderService.saveOrder(id, info.getString("trade_id"),userInfo.getString("yb_userid")
                    ,userInfo.getString("yb_username"),userInfo.getString("yb_sex"),phone,access_token);
            System.out.println(info.getString("trade_id"));
            map.put("status","success");
            map.put("code","200");
            map.put("url",info.getString("sign_href"));
            System.out.println(map.get("sign_href"));
            subRes = JSON.toJSONString(map);
            return SUCCESS;
        }catch (Exception e){
            logger.error("提交获取出现异常：" + e.getMessage());
            logger.fatal("异常详细信息：" + DoUtil.getExceptionInfo(e));
            return ERROR;
        }
    }

    /**
     * getNeedMoney
     * @return needMoney
     */
    public String getNeedMoney() {
        return needMoney;
    }

    /**
     * setNeedMoney
     * @param needMoney needMoney
     */
    public void setNeedMoney(String needMoney) {
        this.needMoney = needMoney;
    }

    /**
     * getNowMoney
     * @return nowMoney
     */
    public String getNowMoney() {
        return nowMoney;
    }

    /**
     * setNowMoney
     * @param nowMoney nowMoney
     */
    public void setNowMoney(String nowMoney) {
        this.nowMoney = nowMoney;
    }

    /**
     * getpId
     * @return pId;
     */
    public String getpId() {
        return pId;
    }

    /**
     * setpId
     * @param pId pId
     */
    public void setpId(String pId) {
        this.pId = pId;
    }

    /**
     * setProductService
     * @param productService productService
     */
    public void setProductService(ProductServiceImpl productService) {
        this.productService = productService;
    }

    /**
     * getSubRes
     * @return subRes
     */
    public String getSubRes() {
        return subRes;
    }

    /**
     * setSubRes
     * @param subRes subRes
     */
    public void setSubRes(String subRes) {
        this.subRes = subRes;
    }

    /**
     * getPhone
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * setPhone
     * @param phone phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPayUser(String payUser) {
        this.payUser = payUser;
    }

    public String getPayUser() {
        return payUser;
    }

    public void setOrderService(IOrderService orderService) {
        this.orderService = orderService;
    }
}
