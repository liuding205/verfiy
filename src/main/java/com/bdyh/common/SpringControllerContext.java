package com.bdyh.common;

import org.springframework.web.util.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zheng on 2017/1/9.
 */
public class SpringControllerContext implements Serializable {
    private static ThreadLocal controllerContext = new ThreadLocal();

    private Map<Object,Object> context;

    private static final String REQUEST = "javax.servlet.http.HttpServletRequest";
    private static final String RESPONSE = "javax.servlet.http.HttpServletResponse";


    public SpringControllerContext(Map<Object, Object> context){
        this.context = context;
    }

    /**
     * 设置上下文
     * @param context
     */
    public static void setContext(SpringControllerContext context){
        controllerContext.set(context);
    }

    /**
     * 获取上下文
     * @return
     */
    public static SpringControllerContext getContext(){
        SpringControllerContext context = (SpringControllerContext)controllerContext.get();
        if(null == context){
            context = new SpringControllerContext(new HashMap<Object, Object>());
            setContext(context);
        }
        return context;
    }

    /**
     * 获取上下文map
     * @return
     */
    public Map<Object, Object> getContextMap(){
        return this.context;
    }

    /**
     * 设置上下文map
     * @param contextMap
     */
    public void setContextMap(Map<Object, Object> contextMap){
        getContext().context = contextMap;
    }

    /**
     * 获取Request或者Response
     * @param key
     * @return
     */
    public Object get(Object key) {
        return context.get(key);
    }

    /**
     * 设置Request或者Response
     * @param key
     * @param value
     */
    public void put(Object key, Object value) {
        context.put(key, value);
    }

    /**
     * 设置Request
     * @param request
     */
    public void setRequest(HttpServletRequest request){
        put(REQUEST,request);
    }

    /**
     * 获取Request
     * @return
     */
    public HttpServletRequest getRequest(){
        return (HttpServletRequest)get(REQUEST);
    }

    /**
     * 设置Response
     * @return
     */
    public HttpServletResponse getResponse(){
        return (HttpServletResponse)get(RESPONSE);
    }

    /**
     * 设置Response
     * @param response
     */
    public void setResponse(HttpServletResponse response){
        put(RESPONSE,response);
    }

    /**
     * 获取Session
     * @return
     */
    public HttpSession getSession(){
        return getRequest().getSession();
    }


    public ServletContext getApplication(){
        return getSession().getServletContext();
    }

    /**
     * 设置session的值
     * @param key
     * @param value
     */
    public void setSessionValue(String key,Object value,HttpServletRequest request){
        //HttpServletRequest request = SpringControllerContext.getContext().getRequest();
        WebUtils.setSessionAttribute(request, key, value);
    }

    /**
     * 获取session的值
     * @param key
     * @return
     */
    public Object getSessionValue(String key,HttpServletRequest request){
        //HttpServletRequest request = SpringControllerContext.getContext().getRequest();
        return WebUtils.getSessionAttribute(request, key);
    }


}
