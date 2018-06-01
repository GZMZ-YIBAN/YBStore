package utils;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 常用工具类
 * @author EchoLZY
 * @date 2018-2-13 10:21:15
 */
public class DoUtil {
    private static String info;
    private static StringWriter errorsWriter = new StringWriter();

    /**
     * 异常打印
     * @param e 捕获异常
     * @return 异常信息
     */
    public static String getExceptionInfo (Exception e){
        e.printStackTrace(new PrintWriter(errorsWriter));
        return errorsWriter.toString();
    }

    /**
     * 短信参数生成
     * @param phone 电话号码
     * @param p 商品名称
     * @return post 参数集合
     */
    public static Map<String,String> setPar(String phone,String p){
        Map<String,String> map = new HashMap<String, String>();
        map.put("appid","22015");
        map.put("to",phone);
        map.put("project","hAagF1");
        map.put("vars","{" +
                "\"p\"" + ":" + "\"" + p +"\"" +
                "}");
        map.put("signature","ddc1bdf23979ba5410e04a1ff9489a6a");
        return map;
    }



}
