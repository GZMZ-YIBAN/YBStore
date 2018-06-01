package web.interceptor;


import cn.yiban.open.common.User;
import cn.yiban.util.AESDecoder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import utils.AppContext;
import utils.DoUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录拦截器
 * <p>阻止未登录用户</p>
 *
 * @author EchoLZY
 * @date 2018-2-12 20:15:11
 */
public class LoginInterceptor extends MethodFilterInterceptor {
    /**
     * 日志
     */
    private static Logger logger = LogManager.getLogger(LoginInterceptor.class.getName());

    /**
     * 拦截器
     * @param actionInvocation 拦截
     * @return 结果
     */
    @Override
    protected String doIntercept(ActionInvocation actionInvocation) {
        logger.info("登录检验拦截器开启。。。。");
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
        String verify = request.getParameter(AppContext.KEY_VERIFY_REQUEST);
        System.out.println(verify);
        System.out.println(StringUtils.isNotEmpty(verify));
        if (StringUtils.isNotEmpty(verify)){
            try {
                System.out.println(verify);
                JSONObject json = JSON.parseObject((AESDecoder.dec(verify,AppContext.APP_SEC,
                        AppContext.APP_ID)).trim());
                String visit_oauth = json.getString("visit_oauth");
                if ("false".equals(visit_oauth)){
                    logger.info("当前用户未授权，返回登录页面。。。。。。");
                    request.getSession().setAttribute("tip","当前用户未授权");
                    return "reLogin";
                }
                logger.info("登录成功，获取用户信息");
                AppContext.ACCESS_TOKEN = JSON.parseObject(visit_oauth.trim()).getString("access_token");
                request.getSession().setAttribute("access_token", AppContext.ACCESS_TOKEN);
                User user = new User(AppContext.ACCESS_TOKEN);
                JSONObject userInfo = JSON.parseObject(user.me().trim()).getJSONObject("info");
                logger.info("当前用户信息：" + userInfo.toJSONString());
                logger.info("当前授权信息：" + AppContext.ACCESS_TOKEN);
                request.getSession().setAttribute("user", userInfo);
                request.getSession().setAttribute("access_token",AppContext.ACCESS_TOKEN);
                logger.info("拦截器放行");
                return actionInvocation.invoke();
            } catch (Exception e) {
                logger.info("拦截器未放行");
                logger.fatal("登录异常：" + e.getMessage());
                logger.fatal("异常详细信息：" + DoUtil.getExceptionInfo(e));
                return "errorLogin";
            }
        }else {
            logger.info("未登录，返回登录页面。。。。。");
            return "reLogin";
        }
    }

}
