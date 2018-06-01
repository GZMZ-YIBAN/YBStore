package web.action;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import entity.YibanProductEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import service.IProductService;
import utils.DoUtil;
import utils.redis.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * -----------------------------
 * Copyright © 2017, EchoCow
 * All rights reserved.
 *
 * @author EchoLZY
 * @version 2.0
 * @description 读取商品 <br/>
 * 使用 redis 做缓存，<br/>
 *      key ：   YBStore_product     固定字段<br/>
 *      value ： result              商品的 json 数据<br/>
 * @date 21:28 2018/4/14
 * @modified By EchoLZY
 *
 * -----------------------------
 */
public class ProductAction extends ActionSupport {
    /**
     * 日志
     */
    private static Logger logger = LogManager.getLogger(ProductAction.class.getName());
    /**
     * service
     */
    private IProductService productService;
    /**
     * 产品列表
     */
    private List<YibanProductEntity> list;
    /**
     * redis
     */
    private RedisUtil redisUtil;
    /**
     * result 返回 json
     */
    private String result;
    /**
     * 用户信息 json
     */
    private String user;

    /**
     * 读取商品信息的方法
     * @description
     * 可能出现的错误代码 <br/>
     *      502     商品获取出现异常<br/>
     *      503     用户信息获取出现异常<br/>
     * @return 结果
     */
    public String queryList(){
        logger.info("开始读取商品。。。。。。");
        try {
            if (redisUtil.exists("YBStore_product")) {
                logger.info("缓存中存在数据，读取缓存。。。");
                result = redisUtil.get("YBStore_product").toString();
            } else {
                logger.info("缓存中不存在数据，进入数据库读取。。。");
                list = productService.findAll();
                result = JSONObject.toJSON(list).toString();
            }
        } catch (Exception e){
            logger.error("商品获取出现异常：" + e.getMessage());
            logger.fatal("异常详细信息：" + DoUtil.getExceptionInfo(e));
            return "error1";
        }

        try {
            HttpServletRequest request = ServletActionContext.getRequest();
            JSONObject userJson = (JSONObject) request.getSession().getAttribute("user");
            user = userJson.toJSONString();
        } catch (Exception e){
            logger.error("用户信息获取出现异常：" + e.getMessage());
            logger.fatal("异常详细信息：" + DoUtil.getExceptionInfo(e));
            return "error2";
        }

        return SUCCESS;
    }

    public IProductService getProductService() {
        return productService;
    }

    public void setProductService(IProductService productService) {
        this.productService = productService;
    }

    public List<YibanProductEntity> getList() {
        return list;
    }

    public void setList(List<YibanProductEntity> list) {
        this.list = list;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
