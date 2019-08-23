package com.dyp.modules.sys.security;

import com.dyp.modules.sys.dao.UserDao;
import com.dyp.modules.sys.entity.User;
import com.dyp.modules.sys.utils.EncryptUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
* 这个使用component是因为实际工程中会引入DAO层对象，而DAO层对像一般都是由Spring框架做管理的
* */
@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("-------权限认证方法--------");
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        List<SysPermission> sysPermissions = userRepository.findUserRolePermissionByUserName(username);
//        Set<String> stringSet = new HashSet<>();
//
//        for(SysPermission sysPermission : sysPermissions)
//        {
//            stringSet.add(sysPermission.getPermission());
//        }
//        info.setStringPermissions(stringSet);

        return info;
    }

    /**
     * 这里可以注入userService,为了方便演示，我就写死了帐号了密码 * private UserService userService; * <p> * 获取即将需要认证的信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("-------身份认证方法--------");

        String userName = (String) authenticationToken.getPrincipal();

        if (userName == null) {
            throw new AccountException("用户名不正确");
        }

        User loginUser = userDao.getByLoginName(new User("", userName));
        String encryptPwd = EncryptUtils.getPasswordPart(loginUser.getPassword());
        String salt = EncryptUtils.getSaltPart(loginUser.getPassword());

        return new SimpleAuthenticationInfo(userName, encryptPwd, ByteSource.Util.bytes(salt), getName());
    }

}
