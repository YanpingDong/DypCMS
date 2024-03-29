package com.dyp.modules.sys.web;

import com.dyp.modules.sys.model.LoginResult;
import com.dyp.modules.sys.service.LoginService;
import com.dyp.modules.sys.service.SystemService;
import com.dyp.modules.sys.utils.AesUtils;
import com.dyp.modules.sys.utils.RandomUtils;
import com.google.code.kaptcha.impl.DefaultKaptcha;

import com.dyp.common.security.shiro.session.SessionDAO;
import com.dyp.common.web.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/sys")
public class LoginController extends BaseController{
	
//	@Autowired
//	private SessionDAO sessionDAO;

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

	private class menue{
		private String name;
		private String url;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

	}
	@GetMapping(value = "/login")
	public String toLogin(HttpServletRequest request, Model model) {
		loginService.logout();
		String key = create16String();

		model.addAttribute("key",key);

		return "/sys/login";
	}
	@GetMapping({"/", "/index"})
	public String index(HttpServletRequest request, Model model) {
//        List<SysPermission> sysPermissions = userRepository.findUserRolePermissionByUserName("admin");

		String userName = request.getParameter("userName");

		Map<String, List<menue>> menues = new HashMap<>();
		List<menue> subMenues = new ArrayList<>();
		menue ulistMenue = new menue();
		ulistMenue.setName("用户管理");
		ulistMenue.setUrl("/sys/user/ulist");

		menue rlistMenue = new menue();
		rlistMenue.setName("角色管理");
		rlistMenue.setUrl("/sys/role/rlist");

		menue pwdChgMenue = new menue();
		pwdChgMenue.setName("修改密码");
		pwdChgMenue.setUrl("/sys/user/toChangePassword");
		subMenues.add(ulistMenue);
		subMenues.add(rlistMenue);
		subMenues.add(pwdChgMenue);


		List<menue> subMenues1 = new ArrayList<>();
		menue loglistMenue = new menue();
		loglistMenue.setName("日志查看");
		loglistMenue.setUrl("/sys/log/list");
		subMenues1.add(loglistMenue);


		if("admin".equalsIgnoreCase(userName))
		{
			menues.put("系统管理",subMenues);
			menues.put("日志管理",subMenues1);
		}
		else
		{
			menues.put("日志管理",subMenues1);
		}

		model.addAttribute("menues", menues);
		return "/sys/index";
	}
	@GetMapping("/403")
	public String unauthorizedRole() {
		System.out.println("------没有权限-------");
		return "/sys/403";
	}

	@GetMapping("/getVerifyCode")
	public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		byte[] bytesCaptchaImg = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();

		try {
			// 生产验证码字符串并保存到session中
			String createText = defaultKaptcha.createText();
			request.getSession().setAttribute("verifyCode", createText);
			request.getSession().setAttribute("verifyCodeTTL", System.currentTimeMillis());
			// 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
			BufferedImage bufferedImage = defaultKaptcha.createImage(createText);
			ImageIO.write(bufferedImage, "jpg", jpegOutputStream);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
		bytesCaptchaImg = jpegOutputStream.toByteArray();
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(bytesCaptchaImg);
		responseOutputStream.flush();
		responseOutputStream.close();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> login(HttpServletRequest request) throws Exception {
		System.out.println("login()");
		Map<String, Object> map = new HashMap<>();
		String userName = request.getParameter("userName");
		String encryptedPassword = request.getParameter("password");
		String key = request.getParameter("key");

		String verifyCode = request.getParameter("verifyCode");
		String rightCode = (String) request.getSession().getAttribute("verifyCode");
		String rightCode1 = (String) request.getSession().getAttribute("code");
		Long verifyCodeTTL = (Long) request.getSession().getAttribute("verifyCodeTTL");

		String password = AesUtils.decrypt(encryptedPassword,key);
//        String password = encryptedPassword;
		Long currentMillis = System.currentTimeMillis();
		if (rightCode == null || verifyCodeTTL == null) {
			map.put("msg", "请刷新图片，输入验证码！");
			map.put("userName", userName);
			map.put("password", password);
			map.put("success",false);
			map.put("url","/sys/login");
			return map;
		}
		Long expiredTime = (currentMillis - verifyCodeTTL) / 1000;
		if (expiredTime > this.verifyTTL) {
			map.put("msg", "验证码过期，请刷新图片重新输入！");
			map.put("userName", userName);
			map.put("password", password);
			map.put("success",false);
			map.put("url","/login");
			return map;
		}

		if (!verifyCode.equalsIgnoreCase(rightCode)) {
			map.put("msg", "验证码错误，请刷新图片重新输入！");
			map.put("userName", userName);
			map.put("password", password);
			map.put("success",false);
			map.put("url","/sys/login");
			return map;
		}

		LoginResult loginResult = loginService.login(userName, password);
		if (loginResult.isLogin()) {
			map.put("userName", userName);
//            SysLog sysLog = LogFactory.createSysLog("登录","登录成功");
//            logService.writeLog(sysLog);
			map.put("success",true);
			map.put("url","/sys/index");
			return map;

		} else {
			map.put("msg", loginResult.getResult());
			map.put("userName", userName);
			map.put("password", password);
			map.put("success",false);
			map.put("url","/sys/login");
			return map;

		}

	}

}
