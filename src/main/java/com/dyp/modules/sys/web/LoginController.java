package com.dyp.modules.sys.web;

import com.dyp.modules.sys.service.LoginService;
import com.dyp.modules.sys.service.SystemService;
import com.dyp.modules.sys.utils.RandomUtils;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.common.collect.Maps;
import com.dyp.common.config.Global;
import com.dyp.common.security.shiro.session.SessionDAO;
import com.dyp.common.servlet.ValidateCodeServlet;
import com.dyp.common.utils.CacheUtils;
import com.dyp.common.utils.CookieUtils;
import com.dyp.common.utils.IdGen;
import com.dyp.common.utils.StringUtils;
import com.dyp.common.web.BaseController;
import com.dyp.modules.sys.security.FormAuthenticationFilter;

import com.dyp.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登录Controller
 * @author ThinkGem
 * @version 2013-5-31
 */
//@Controller
public class LoginController extends BaseController{
	
	@Autowired
	private SessionDAO sessionDAO;

	@Autowired
	DefaultKaptcha defaultKaptcha;

	@Autowired
	SystemService systemService;

	@Autowired
	private LoginService loginService;

	private long verifyTTL = 60;//验证码过期时间60秒

	private String create16String()
	{
		return RandomUtils.generateString(16);
	}
//	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String toLogin(HttpServletRequest request, Model model) {
//        loginService.logout();
		String key = create16String();

		model.addAttribute("key",key);

		return "/sys/login";
	}
	
	/**
	 * 管理登录
	 */
//	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
       return "login";
	}


}
