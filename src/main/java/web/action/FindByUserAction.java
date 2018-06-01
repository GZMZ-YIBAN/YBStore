package web.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import entity.YibanProductGetEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import service.IUserService;
import utils.DoUtil;
import utils.MD5;
import utils.redis.RedisUtil;

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
 * <p>
 * -----------------------------
 * @description 当前用户订单查询 <br/>
 * 使用 redis 做缓存，<br/>
 *      key     ybUserid    可变字段：当前用户id         <br/>
 *      Value   byUser      通过id查询到的列表json数据    <br/>
 * @date 18:02 2018/4/15
 * @modified By EchoLZY
 */
public class FindByUserAction extends ActionSupport {
    /**
     * 日志
     */
    private static Logger logger = LogManager.getLogger(FindByUserAction.class.getName());
    /**
     * service
     */
    private IUserService userService;
    /**
     * redis
     */
    private RedisUtil redisUtil;
    /**
     * json 返回
     */
    private String productUserList;
    /**
     * 产品id
     */
    private String pId;

    /**
     * 产品信息
     */
    private String product;
    /**
     * 管理员密码
     */
    private String admin;
    /**
     * 接受到的密码
     */
    private String password;


    /**
     * 通过当前用户获取已购买的订单
     * @description
     * 可能出现的错误代码 <br/>
     *      611     获取用户的id失败
     * @return 结果
     */
    @Override
    public String execute() {
        HttpServletRequest request = ServletActionContext.getRequest();
        JSONObject userJson = (JSONObject) request.getSession().getAttribute("user");
        String ybUserid = userJson.getString("yb_userid");
        logger.info("获取用户id" + ybUserid);
        if (redisUtil.exists(ybUserid)){
            logger.info("缓存中存在用户数据，从缓存中获取。。。");
            productUserList = redisUtil.get(ybUserid).toString();
        } else {
            logger.info("缓存中不存在用户数据，从数据库获取。。。");
            if (StringUtils.isNotEmpty(ybUserid)){
                DetachedCriteria detachedCriteria = DetachedCriteria.forClass(YibanProductGetEntity.class);
                detachedCriteria.add(Restrictions.eq("yibanId",ybUserid));
                productUserList = userService.findByUser(ybUserid,detachedCriteria);
            } else {
                logger.error("获取用户id失败。。。");
                return ERROR;
            }
        }
        return SUCCESS;
    }

    /**
     * 查找用户需要兑换的当前商品
     * @return 结果
     * @description
     * 可能出现的错误代码 <br/>
     *      612     获取商品出现异常
     */
    public String findProduct(){
        try {
            Integer id = Integer.parseInt(pId);
            YibanProductGetEntity yibanProductGetEntity = userService.findById(id);
            String proJson = JSON.toJSONString(yibanProductGetEntity);
            Map<String,String> map = new HashMap<String, String>();
            map.put("code","200");
            map.put("product",proJson);
            product = JSON.toJSONString(map);
            return SUCCESS;
        } catch (Exception e){
            logger.error("获取商品出现异常！！");
            logger.fatal("异常详细信息：" + DoUtil.getExceptionInfo(e));
            return "error1";
        }
    }

    /**
     * 商品更改状态
     * @description
     * 可能出现的错误代码: <br/>
     *      613     密码错误 <br/>
     *      614     商品状态更改出现异常 <br/>
     * @return 结果
     */
    public String submit(){
        try {
            String pw = MD5.getHash(password, "SHA");
            System.out.println("前台传过来的密码" + password);
            System.out.println("加密后" + pw);
            System.out.println("当前管理员密码" + admin);
            if (StringUtils.isEmpty(pw) || !pw.equals(admin)) {
                return ERROR;
            }
            Integer id = Integer.parseInt(pId);
            userService.updateProduct(id);
            return SUCCESS;
        }catch (Exception e){
            logger.error("获取商品出现异常！！");
            logger.fatal("异常详细信息：" + DoUtil.getExceptionInfo(e));
            return "error1";
        }

    }


    public String getProductUserList() {
        return productUserList;
    }

    public void setProductUserList(String productUserList) {
        this.productUserList = productUserList;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getAdmin() {
        return admin;
    }
}

