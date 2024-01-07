package com.littlepants.attack.attackplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.littlepants.attack.attackplus.dao.RoleDao;
import com.littlepants.attack.attackplus.dao.UserDao;
import com.littlepants.attack.attackplus.dao.UserRoleDao;
import com.littlepants.attack.attackplus.entity.Role;
import com.littlepants.attack.attackplus.entity.User;
import com.littlepants.attack.attackplus.entity.UserRole;
import com.littlepants.attack.attackplus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @since 2023-04-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements UserService, UserDetailsService {
    private final UserRoleDao userRoleDao;
    private final RoleDao roleDao;

    public UserServiceImpl(UserRoleDao userRoleDao, RoleDao roleDao) {
        this.userRoleDao = userRoleDao;
        this.roleDao = roleDao;
    }

    @Override
    public boolean assignRole(User user, String roleName) {
        Role role = roleDao.selectOne(new QueryWrapper<Role>().eq("name",roleName));
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());
        if (userRoleDao.insert(userRole)==1)
            return true;
        else
            return userRoleDao.updateById(userRole)==1;
    }

    @Override
    public User getUserByAccount(String account) {
        User user = baseMapper.selectOne(new QueryWrapper<User>().eq("user_acct",account));
        UserRole userRole = userRoleDao.selectOne(new QueryWrapper<UserRole>().eq("user_id",user.getId()));
        List<Role> roles = roleDao.selectBatchIds(Collections.singletonList(userRole.getRoleId()));
        user.setRoles(roles);
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        User user = baseMapper.selectById(userId);
        UserRole userRole = userRoleDao.selectOne(new QueryWrapper<UserRole>().eq("user_id",userId));
        List<Role> roles = roleDao.selectBatchIds(Collections.singletonList(userRole.getRoleId()));
        user.setRoles(roles);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = baseMapper.selectList(new QueryWrapper<User>().eq("user_acct",username));
        if (users.isEmpty())
            throw new UsernameNotFoundException("账号不存在");
        User user = users.get(0);
        UserRole userRole = userRoleDao.selectOne(new QueryWrapper<UserRole>().eq("user_id",user.getId()));
        List<Role> roles = roleDao.selectBatchIds(Collections.singletonList(userRole.getRoleId()));
        user.setRoles(roles);
        return user;
    }
}
