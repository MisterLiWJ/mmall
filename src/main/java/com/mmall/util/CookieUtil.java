package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018-06-02.
 */
@Slf4j
public class CookieUtil {
    //把cookie写在指定的几级域名下面
    private final static String COOKIE_DU_MAIN=".lidawang.com";
    //cookie名称
    private final static String COOKIE_NAME="mall_login_token";

    /**
     dumain设置与域名的关系
     a:A.happymall.com          cookie.dumain=A.happymall.com;path="/"
     b:A.happymall.com          cookie.dumain=B.happymall.com;path="/"
     c:A.happymall.com          cookie.dumain=C.happymall.com;path="/"
     d:A.happymall.com          cookie.dumain=D.happymall.com;path="/"
     e:A.happymall.com          cookie.dumain=E.happymall.com;path="/"
     列如X:dumain=".happymall.com"他能拿到所有的cookie
        如果dumain="a.happymall.com"他等于A.happymall.com


     */


    /**
     * 写入cookie
     * @param response
     * @param token
     */
    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie cookie=new Cookie(COOKIE_NAME,token);
        cookie.setDomain(COOKIE_DU_MAIN);
        //设置在根目录,如果设置为test就表示在test目录或者子目录才能获取到
        cookie.setPath("/");
        //设置客户端不可见为true,避免脚本攻击
//        cookie.setHttpOnly(true);
        //设置cookie有效期单位秒,设置-1为永久的
        //如果maxAge不设置的话这个cookie不会写入硬盘,只会存在于内存,只在当前页面有效
        //设置为一年的
        cookie.setMaxAge(60*60*24*365);
        log.info("write CookieName:{},CookieValue:{}",cookie.getName(),cookie.getValue());
        response.addCookie(cookie);
    }

    /**
     * 读取cookie
     * @param request
     * @return
     */
    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cookies=request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                log.info("read cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
                if(StringUtils.equals(cookie.getName(),COOKIE_NAME)){
                    log.info("return cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 删除COOKIE
     * @param request
     * @param response
     */
    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie cookie[]=request.getCookies();
        if(cookie!=null){
            for(Cookie cookie1:cookie){
                if(StringUtils.equals(cookie1.getName(),COOKIE_NAME)){
                    cookie1.setDomain(COOKIE_DU_MAIN);
                    cookie1.setPath("/");
                    //设置为0代表删除Cookie
                    cookie1.setMaxAge(0);
                    log.info("del cookieName:{},cookieValue:{}",cookie1.getName(),cookie1.getValue());
                    response.addCookie(cookie1);
                    return;
                }
            }
        }
    }


}
