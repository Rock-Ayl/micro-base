package com.rock.micro.base.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 双 IP 拆解 额外工具类
 *
 * @Author ayl
 * @Date 2025-04-01
 */
public class DualIpExtraUtils {

    @Getter
    @Setter
    @ApiModel("双IP对象")
    public static class DualIP {

        @ApiModelProperty("ipv4地址")
        private String ipv4;

        @ApiModelProperty("ipv6地址")
        private String ipv6;

    }

    /**
     * 解析 双ip 方法
     *
     * @param request 用户请求
     * @return
     */
    public static DualIP getDualIp(HttpServletRequest request) {
        //初始化结果
        DualIP result = new DualIP();
        //解析ip
        String ip = getClientIp(request);
        //分别尝试组装
        result.setIpv4(extractIPv4(ip));
        result.setIpv6(extractIPv6(ip));
        //返回
        return result;
    }

    /**
     * 常见代理标识头（按优先级排序）
     */
    private static final String[] IP_HEADERS = {
            "X-Real-IP",
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    /**
     * 获取客户端 IPv4/IPv6 混合地址（自动处理代理和本地环回）
     *
     * @param request 用户请求
     * @return
     */
    private static String getClientIp(HttpServletRequest request) {

        //判空
        if (request == null) {
            //过
            return "";
        }

        /**
         * ✅ 第一步：检查代理头信息（解决 CDN/反向代理场景）
         */

        //循环
        for (String header : IP_HEADERS) {
            //获取各种header
            String ip = request.getHeader(header);
            //如果有效
            if (isValidIp(ip)) {
                //处理多级代理(取第一个有效IP)
                return parseProxyIp(ip);
            }
        }

        /**
         * ✅ 第二步：获取直连地址（支持 IPv4/IPv6）
         */

        //默认获取
        String remoteAddr = request.getRemoteAddr();
        //处理本地 IPv6 环回地址
        if ("0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            //返回
            return "127.0.0.1";
        }
        //默认结果
        return remoteAddr;
    }

    /**
     * 解析多级代理的 IP 链（如 X-Forwarded-For: client, proxy1, proxy2）
     *
     * @param ipChain ip 代理链
     * @return
     */
    private static String parseProxyIp(String ipChain) {
        //判空
        if (ipChain == null) {
            //过
            return "";
        }
        //如果存在
        if (ipChain.contains(",")) {
            //返回
            return Arrays.stream(ipChain.split(","))
                    .map(String::trim)
                    .filter(DualIpExtraUtils::isValidIp)
                    .findFirst()
                    .orElse("");
        }
        //默认
        return ipChain;
    }

    /**
     * 校验 Header 中 IP 格式有效性（兼容 IPv4/IPv6）
     */
    private static boolean isValidIp(String ip) {
        //效验
        return ip != null && ip.isEmpty() == false && "unknown".equalsIgnoreCase(ip) == false;
    }

    /**
     * 分离 IPv4 地址
     */
    private static String extractIPv4(String ip) {
        //判空
        if (ip == null) {
            //过
            return "";
        }
        //简单判断 IPv4
        if (ip.contains(".")) {
            //返回
            return ip;
        }
        //默认
        return "";
    }

    /**
     * 分离 IPv6 地址
     */
    private static String extractIPv6(String ip) {
        //判空
        if (ip == null) {
            //过
            return "";
        }
        //简单判断 IPv6
        if (ip.contains(":")) {
            //去除 IPv6 地址的方括号（如 [2001:db8::1] -> 2001:db8::1）
            return ip.replaceAll("[\\[\\]]", "");
        }
        //默认
        return "";
    }

}