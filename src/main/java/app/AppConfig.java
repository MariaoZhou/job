package app;

import com.admin.web.interceptor.CORSInterceptor;
import com.baidu.ueditor.UeditorConfigKit;
import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.wxaapp.WxaConfig;
import com.jfinal.wxaapp.WxaConfigKit;
import com.rlax.framework.config.AppBaseConfig;
import com.rlax.framework.interceptor.Log4sInterceptor;
import com.rlax.framework.interceptor.SessionMessageInterceptor;
import com.rlax.framework.plugin.beetl.function.*;
import com.rlax.framework.plugin.reids.activerecord.ActiveRecordCache;
import com.rlax.framework.plugin.shiro.SessionHandler;
import com.rlax.framework.plugin.shiro.ShiroInterceptor4s;
import com.rlax.framework.support.cache.CacheExtKit;
import com.rlax.framework.support.file.FileManager;
import com.rlax.framework.support.file.FileManagerKit;
import com.rlax.framework.support.file.oss.AliyunOssFileManager;
import com.rlax.framework.support.sms.SmsSender;
import com.rlax.framework.support.sms.SmsSenderKit;
import com.rlax.framework.support.sms.aliyun.AliyunMessageServiceSmsSender;
import com.rlax.framework.support.ueditor.AliyunOssUeditorFileManager;
import com.rlax.framework.support.xss.XssHandler;
import net.dreamlu.controller.UeditorApiController;
import org.beetl.core.GroupTemplate;
import org.beetl.ext.jfinal3.JFinal3BeetlRenderFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 主配置文件
 * @author Rlax
 *
 */
public class AppConfig extends AppBaseConfig {

	@Override
	public void afterJFinalStarted() {
		PropKit.use(cfg);
		
		/** 阿里云OSS初始化 */
	    String endpoint = PropKit.get("file.oss.endpoint");
        String accessId = PropKit.get("file.oss.accessId");
        String accessKey = PropKit.get("file.oss.accessKey");
        String bucket = PropKit.get("file.oss.bucket");
        FileManager fileManager = new AliyunOssFileManager(endpoint, accessId, accessKey, bucket);
        FileManagerKit.add(fileManager, "jruc-files");
        FileManagerKit.use("jruc-files");
        
        /** 阿里云 SMS 初始化 */
        accessId = PropKit.get("sms.ms.aliyun.accessId");
        accessKey = PropKit.get("sms.ms.aliyun.accessKey");
        String mnsEndpoint = PropKit.get("sms.ms.aliyun.mnsEndpoint"); 
        String topic = PropKit.get("sms.ms.aliyun.topic"); 
        SmsSender smsSender = new AliyunMessageServiceSmsSender(accessId, accessKey, mnsEndpoint, topic);
        SmsSenderKit.add(smsSender, "jruc-sms");
        SmsSenderKit.use("jruc-sms");
        
        /** Ueditor 初始化 */
	    endpoint = PropKit.get("file.ueditor.endpoint");
        accessId = PropKit.get("file.ueditor.accessId");
        accessKey = PropKit.get("file.ueditor.accessKey");
        bucket = PropKit.get("file.ueditor.bucket");
        
	    UeditorConfigKit.setFileManager(new AliyunOssUeditorFileManager(endpoint, accessId, accessKey, bucket));
	    
	    /** 系统使用 ehcache 缓存 */
	    CacheExtKit.use(CacheExtKit.EHCACHE_TYPE);

	    /** 小程序 */

		WxaConfig wc = new WxaConfig();
		wc.setAppId(PropKit.get("wxaapp.appid"));
		wc.setAppSecret(PropKit.get("wxaapp.appSecret"));

		WxaConfigKit.setWxaConfig(wc);

		/** 公众号 订阅号*/

		ApiConfig ac = new ApiConfig();
		// 配置微信 API 相关参数
		ac.setToken(PropKit.get("weixin.token"));
		ac.setAppId(PropKit.get("weixin.appid"));
		ac.setAppSecret(PropKit.get("weixin.appSecret"));

        /** 华人老板公众号 服务号*/
        ApiConfig serviceAC = new ApiConfig();
        serviceAC.setToken(PropKit.get("service.token"));
        serviceAC.setAppId(PropKit.get("service.appid"));
        serviceAC.setAppSecret(PropKit.get("service.appSecret"));


        /** 途听公众号 服务号*/
        ApiConfig serviceTT = new ApiConfig();
        serviceTT.setToken(PropKit.get("tut.token"));
        serviceTT.setAppId(PropKit.get("tut.appid"));
        serviceTT.setAppSecret(PropKit.get("tut.appSecret"));

//		ApiConfigKit.putApiConfig(ac);
//		ApiConfigKit.putApiConfig(serviceAC);
		ApiConfigKit.putApiConfig(serviceTT);

	    //CacheExtKit.use(CacheExtKit.REDIS_TYPE);
	}

	@Override
	public void configMoreConstants(Constants me) {
		me.setError404View("error404.html");
		me.setError500View("error500.html");
		me.setError403View("error500.html");
		
        JFinal3BeetlRenderFactory rf = new JFinal3BeetlRenderFactory();
        rf.config();
        me.setRenderFactory(rf);
		
		GroupTemplate groupTemplate = rf.groupTemplate;
		
		/** 模版全局变量 */
		Map<String, Object> sharedVars = new HashMap<String, Object>();
		sharedVars.put("system_name", APP_NAME);
		sharedVars.put("viewPath", "/WEB-INF/views");
		groupTemplate.setSharedVars(sharedVars);
		
		/** 模版注册函数 */
		groupTemplate.registerFunction("value", new RequestAttrFunction());
		groupTemplate.registerFunction("selectlist", new KeyValueListFunction());
		groupTemplate.registerFunction("valuedesc", new ValueDescFunction());
		groupTemplate.registerFunction("timestamp2str", new TimeStampConvFunction());
		groupTemplate.registerFunction("menu", new MenuFunction());
		groupTemplate.registerFunction("breadcrumbs", new BreadCrumbsFunction());
		groupTemplate.registerFunctionPackage("so", ShiroFunction.class);
	}

	@Override
	public void configMoreHandlers(Handlers me) {
		me.add(new DruidStatViewHandler("/druid"));
		me.add(new SessionHandler());
		me.add(new XssHandler("admin"));
	}

	/**
	 * 拦截器 注册
	 * @param me
	 */
	@Override
	public void configMoreInterceptors(Interceptors me) {
		me.add(new SessionMessageInterceptor());
		me.add(new Log4sInterceptor());
		me.add(new ShiroInterceptor4s());
		me.add(new CORSInterceptor());
	}

	@Override
	public void configMorePlugins(Plugins me) {
		me.add(new EhCachePlugin());
		me.add(new QuartzPlugin("job.properties"));
		//me.add(new RedisPlugin("", ""));
		
		/** 接入kisso 可配合 shiro 也可单独使用 */
		//me.add(new KissoJfinalPlugin());
	}

	@Override
	public void configMoreRoutes(Routes me) {
		me.setBaseViewPath("/WEB-INF/views");
		me.add("/ueditor/api", UeditorApiController.class);
	}

	@Override
	public void configTablesMapping(String ds, ActiveRecordPlugin arp) {
		arp.setCache(new ActiveRecordCache());
	}
	
	@Override
	public void configMoreEngines(Engine me) {
		
	}

	public static void main(String[] args) {
		JFinal.start("src/main/webapp", 8899, "/", 5);
	}

}
