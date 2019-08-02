/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.dyp.modules.sys.dao;

import com.dyp.common.persistence.annotation.MyBatisDao;
import com.dyp.common.persistence.dao.TreeDao;
import com.dyp.modules.sys.entity.Office;


/**
 * 机构DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {
	
}
