package com.dyp;

import com.dyp.common.security.shiro.session.SessionManager;
import com.dyp.modules.sys.security.SystemAuthorizingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        /*
         * 使用Shiro内置过滤器实现页面拦截：拦截url链接请求
         *
         * shiro内置过滤器，可以实现权限相关的拦截器
         * 		常用的过滤器：
         * 			anon:无需认证（登录）可以访问
         * 			authc:必须认证才可以访问
         * 			user:如果使用rememberMe的功能可以直接访问  （记住用户和密码）
         * 			perms:该资源必须得到资源权限才可以访问		（密码验证）
         * 			role:该资源必须得到角色权限才可以访问		（VIP会员）
         *
         */
        //创建集合——充作拦截器集合
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        /*
         * 单个url拦截，
         */
/*		filterChainDefinitionMap.put("/add", "authc");
		filterChainDefinitionMap.put("/update", "authc");*/
        //url放行
        filterChainDefinitionMap.put("/static/**", "anon");
        //放行login.html页面,即放行login请求
        filterChainDefinitionMap.put("/login", "anon");

        //授权过滤器		注意：当前授权拦截后，shiro会自动跳转到默认的未授权页面
        filterChainDefinitionMap.put("/add", "perms[user:add]");	//perms[user:add]中user:add是授权的自定义字符串

        //批量url拦截
        filterChainDefinitionMap.put("/*", "authc");
        /*
         * shiro拦截器拦截成功后，会返回一个默认的地址login.jsp
         * 		可以自定义修改调整的登录页面
         */
        shiroFilterFactoryBean.setLoginUrl("/toLogin");


        //设置未授权的提示页面
        shiroFilterFactoryBean.setUnauthorizedUrl("noAuth");


        //设置拦截器map集合
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getSecurityManager()
    {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        DefaultWebSessionManager sessionManager = new SessionManager();
        AuthorizingRealm realm  = new SystemAuthorizingRealm();
        return securityManager;
    }

}