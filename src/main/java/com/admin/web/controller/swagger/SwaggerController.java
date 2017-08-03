package com.admin.web.controller.swagger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.admin.web.swagger.annotation.Api;
import com.admin.web.swagger.annotation.ApiOperation;
import com.admin.web.swagger.annotation.Param;
import com.admin.web.swagger.annotation.Params;
import com.admin.web.swagger.model.SwaggerDoc;
import com.admin.web.swagger.model.SwaggerPath;
import com.admin.web.swagger.utils.ClassHelper;
import com.alibaba.fastjson.JSON;

import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;

/**
 * swagger
 *
 * @author lee
 * @version V1.0.0
 * @date 2017/7/7
 */
@ControllerBind(controllerKey = "/swagger")
public class SwaggerController extends Controller {

    public void index() {
        render("index.html");
    }
    
    public void api() {
        SwaggerDoc doc = new SwaggerDoc();
        Map<String, Map<String, SwaggerPath.ApiMethod>> paths = new HashMap<>();
        Map<String, String> classMap = Maps.newHashMap();
        Set<Class<?>> classSet = ClassHelper.getBeanClassSet();
        for (Class<?> cls : classSet) {
            if (cls.isAnnotationPresent(Api.class)) {
                Api api = cls.getAnnotation(Api.class);
                
                if (!classMap.containsKey(api.tag())) {
                    classMap.put(api.tag(), api.description());
                }
                
                Method[] methods = cls.getMethods();
                
                for (Method method : methods) {
                    Annotation[] annotations = method.getAnnotations();
                    SwaggerPath.ApiMethod apiMethod = new SwaggerPath.ApiMethod();
                    apiMethod.setOperationId("");
                    apiMethod.addProduce("application/json");
                    
                    for (Annotation annotation : annotations) {
                        Class<? extends Annotation> annotationType = annotation.annotationType();
                        if (ApiOperation.class == annotationType) {
                            ApiOperation apiOperation = (ApiOperation) annotation;
                            Map<String, SwaggerPath.ApiMethod> methodMap = new HashMap<>();
                            apiMethod.setSummary(apiOperation.description());
                            apiMethod.setDescription(apiOperation.description());
                            apiMethod.addTag(apiOperation.tag());
                            methodMap.put(apiOperation.httpMethod(), apiMethod);
                            paths.put(apiOperation.url(), methodMap);
                        } else if (Params.class == annotationType) {
                            Params apiOperation = (Params) annotation;
                            Param[] params = apiOperation.value();
                            for (Param apiParam : params) {
                                apiMethod.addParameter(new SwaggerPath.Parameter(apiParam.name(), apiParam.description(), apiParam.required(), apiParam.dataType(), apiParam.format(), apiParam.defaultValue()));
                            }
                        } else if (Param.class == annotationType) {
                            Param apiParam = (Param) annotation;
                            apiMethod.addParameter(new SwaggerPath.Parameter(apiParam.name(), apiParam.description(), apiParam.required(), apiParam.dataType(), apiParam.format(), apiParam.defaultValue()));
                        }
                    }
                }
            }
        }
        
        if (classMap.size() > 0) {
            for (String key : classMap.keySet()) {
                doc.addTags(new SwaggerDoc.TagBean(key, classMap.get(key)));
            }
        }
        doc.setPaths(paths);
        
        // swaggerUI 使用Java的关键字default作为默认值,此处将生成的JSON替换
        renderText(JSON.toJSONString(doc).replaceAll("\"defaultValue\"", "\"default\""));
        
    }
}
