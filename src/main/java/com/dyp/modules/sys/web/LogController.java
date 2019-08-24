package com.dyp.modules.sys.web;

import com.dyp.common.persistence.Page;
import com.dyp.common.web.BaseController;
import com.dyp.modules.sys.entity.Log;
import com.dyp.modules.sys.service.LogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


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
	
//	@RequiresPermissions("sys:log:view")
	@RequestMapping(value = {""})
	@ResponseBody
	public Object list(Log log, HttpServletRequest request, HttpServletResponse response) {
		int pageSize = 10;
		try {
			pageSize =  Integer.parseInt(request.getParameter("pageSize"));
		}catch (Exception e)
		{
			e.printStackTrace();
		}

		int pageNumber=0 ;
		try {
			pageNumber =  Integer.parseInt(request.getParameter("pageNumber"))-1;
		}catch (Exception e)
		{
			e.printStackTrace();
		}


        Page<Log> page = logService.findPage(new Page<Log>(request, response), log);

		Map<String, Object> map = new HashMap<>();
		map.put("total", page.getList().size());
		map.put("rows", page.getList());
        return map;
	}

}
