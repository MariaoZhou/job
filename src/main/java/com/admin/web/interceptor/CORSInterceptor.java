package com.admin.web.interceptor;


import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * 跨域请求-拦截器
 */
public class CORSInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        inv.getController().getResponse().setContentType("text/html;charset=UTF-8");
        inv.getController().getResponse().addHeader("Access-Control-Allow-Origin", "*");
        inv.getController().getResponse().setHeader("Access-Control-Allow-Headers","Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type");
        inv.getController().getResponse().setHeader("Access-Control-Allow-Credentials", "true");
        inv.invoke();
    }
}
