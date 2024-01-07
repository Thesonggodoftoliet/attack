package com.littlepants.attack.attackweb.service.implement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.littlepants.attack.attackweb.entity.PolicyGroup;
import com.littlepants.attack.attackweb.entity.User;
import com.littlepants.attack.attackweb.mapper.PolicyMapper;
import com.littlepants.attack.attackweb.mapper.UserMapper;
import com.littlepants.attack.attackweb.service.intf.UserService;
import com.littlepants.attack.attackweb.util.RandomPassword;
import com.littlepants.attack.attackweb.util.TimeUtils;
import com.littlepants.attack.attackweb.validation.PasswordNotCorrectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PolicyMapper policyMapper;

    public UserServiceImpl(UserMapper userMapper, PolicyMapper policyMapper) {
        this.userMapper = userMapper;
        this.policyMapper = policyMapper;
    }

    public int addUser(User user) {
        try {
            //密码加密
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String password = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(password);
            return userMapper.insert(user);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void addUser(List<User> users) {
        for (User user : users) {
            addUser(user);
        }
    }

    public List<User> queryAll() {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            if (user.getGroupId() == 1)
                user.setGroup("管理员");
            else if (user.getGroupId() == 2)
                user.setGroup("数据记录员");
            else
                user.setGroup("数据分析员");
        }
        return users;
    }

    public User queryById(User user) {
        User sqluser = userMapper.selectById(user.getId());
        if (sqluser.getGroupId() == 1)
            sqluser.setGroup("管理员");
        else if (sqluser.getGroupId() == 2)
            sqluser.setGroup("数据记录员");
        else
            sqluser.setGroup("数据分析员");
        return sqluser;
    }

    public List<User> queryLikeNickName(String name) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.like("nickname", name);
        return getUsers(userQueryWrapper);
    }

    public List<User> queryByNickName(String name) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("nickname", name);
        return getUsers(userQueryWrapper);
    }

    @Override
    public User getUserByUserName(String name) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", name);
        return getUsers(userQueryWrapper).get(0);
    }

    private List<User> getUsers(QueryWrapper<User> userQueryWrapper) {
        List<User> users = userMapper.selectList(userQueryWrapper);
        for (User user : users) {
            if (user.getGroupId() == 1)
                user.setGroup("管理员");
            else if (user.getGroupId() == 2)
                user.setGroup("数据记录员");
            else
                user.setGroup("数据分析员");
        }
        return users;
    }

    public int count() {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        return Math.toIntExact(userMapper.selectCount(userQueryWrapper));
    }

    @Override
    public int count(String condition, String value) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq(condition, value);
        return Math.toIntExact(userMapper.selectCount(userQueryWrapper));
    }

    public void changeUserById(User user) {
        userMapper.updateById(user);
    }

    public void changeUserBy(User user, String column, Object val) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq(column, val);
        userMapper.update(user, userQueryWrapper);
    }

    public int deleteById(User user) {
        return userMapper.deleteById(user.getId());
    }

    public void deleteBy(String column, Object val) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq(column, val);
        userMapper.delete(userQueryWrapper);
    }

    public void deleteByIds(List<String> idList) {
        userMapper.deleteBatchIds(idList);
    }

    @Override
    public void updateUserInfoByName(User user) {
        User olderUser = getUserByUserName(user.getUsername());
        if (user.getNickName() != null)
            olderUser.setNickName(user.getNickName());
        if (user.getEmail() != null)
            olderUser.setEmail(user.getEmail());
        if (user.getPhone() != null)
            olderUser.setPhone(user.getPhone());
        userMapper.updateById(olderUser);
    }

    @Override
    public void resetPasswordByName(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(user.getResetPassword());
        User oldUser = getUserByUserName(user.getUsername());
        oldUser.setPassword(password);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        oldUser.setLastPasswordChange(timestamp);
        changeUserById(oldUser);
    }

    @Override
    public void changePasswordByName(User user) throws PasswordNotCorrectException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User oldUser = getUserByUserName(user.getUsername());
        if (!encoder.matches(user.getPassword(), oldUser.getPassword()))
            throw new PasswordNotCorrectException();
        String password = encoder.encode(user.getResetPassword());
        oldUser.setPassword(password);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        oldUser.setLastPasswordChange(timestamp);
        changeUserById(oldUser);
    }

    @Override
    public void changeGroupByName(User user) {
        User olderUser = getUserByUserName(user.getUsername());
        olderUser.setGroupId(user.getGroupId());
        changeUserById(olderUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        if (userMapper.selectList(userQueryWrapper).isEmpty()) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        User user = userMapper.selectList(userQueryWrapper).get(0);
        PolicyGroup policyGroup = policyMapper.selectById(user.getGroupId());
        List<PolicyGroup> policyServices = new ArrayList<>();
        policyServices.add(policyGroup);
        user.setGroup_id(policyServices);
        if (user.getGroupId() == 1)
            user.setGroup("管理员");
        else if (user.getGroupId() == 2)
            user.setGroup("数据记录员");
        else
            user.setGroup("数据分析员");
        user.setLastLogin(TimeUtils.getTimeString());
        changeUserBy(user, "id", user.getId());
        return user;
    }
}
