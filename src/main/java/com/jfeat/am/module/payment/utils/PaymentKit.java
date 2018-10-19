package com.jfeat.am.module.payment.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.utils.Charsets;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author jackyhuang
 * @date 2018/10/18
 */
public class PaymentKit {

    /**
     * 组装签名的字段
     * @param params 参数
     * @param urlEncoder 是否urlEncoder
     * @return String
     */
    public static String packageSign(Map<String, String> params, boolean urlEncoder) {
        // 先将参数以其参数名的字典序升序进行排序
        TreeMap<String, String> sortedParams = new TreeMap<>(params);
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> param : sortedParams.entrySet()) {
            String value = param.getValue();
            if (StrKit.isBlank(value)) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            sb.append(param.getKey()).append("=");
            if (urlEncoder) {
                try { value = urlEncode(value); } catch (UnsupportedEncodingException e) {}
            }
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * urlEncode
     * @param src 微信参数
     * @return String
     * @throws UnsupportedEncodingException 编码错误
     */
    public static String urlEncode(String src) throws UnsupportedEncodingException {
        return URLEncoder.encode(src, Charsets.UTF_8.name()).replace("+", "%20");
    }

    /**
     * 生成签名
     * @param params 参数
     * @param appCode 应用密钥
     * @return sign
     */
    public static String createSign(Map<String, String> params, String appCode) {
        // 生成签名前先去除sign
        params.remove("sign");
        String stringA = packageSign(params, false);
        String stringSignTemp = stringA + "&key=" + appCode;
        return HashKit.md5(stringSignTemp).toUpperCase();
    }

    /**
     * 检查签名
     * @param params
     * @param appCode
     * @return
     */
    public static boolean verifySign(Map<String, String> params, String appCode){
        String sign = params.get("sign");
        String localSign = createSign(params, appCode);
        return sign.equals(localSign);
    }

    public static Map<String, String> convertToMap(Object obj) {
        return JSONObject.parseObject(JSONObject.toJSONString(obj), new TypeReference<Map<String, String>>(){});
    }

    /**
     * 2.10 => 210
     * @param price
     * @return
     */
    public static String convertPrice(double price) {
        return String.valueOf(BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(100)).intValue());
    }

}
