package com.dyp.modules.sys.web;

import com.dyp.common.persistence.Page;
import com.dyp.common.web.BaseController;
import com.dyp.modules.sys.entity.Log;
import com.dyp.modules.sys.service.LogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping(value = "/sys/log")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;

	@RequestMapping(value="/list")
	public String list()
	{
		return "/sys/logList";
	}
	
	@RequiresPermissions("sys:log:view")
	@RequestMapping(value = {""})
	public String list(Log log, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Log> page = logService.findPage(new Page<Log>(request, response), log); 
        model.addAttribute("page", page);
		return "modules/sys/logList";
	}

}
