package com.littlepants.attack.attackweb.service.intf;
import com.littlepants.attack.attackweb.entity.User;
import com.littlepants.attack.attackweb.validation.PasswordNotCorrectException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    int addUser(User user);
    void addUser(List<User> users);
    List<User> queryAll();
    User queryById(User user);
    List<User> queryLikeNickName(String name);
    List<User> queryByNickName(String name);
    User getUserByUserName(String name);
    int count();
    int count(String condition,String value);
    void changeUserById(User user);
    void changeUserBy(User user,String column,Object val);
    int deleteById(User user);
    void deleteBy(String column,Object val);
    void deleteByIds(List<String> idList);
    void updateUserInfoByName(User user);
    void resetPasswordByName(User user);
    void changePasswordByName(User user) throws PasswordNotCorrectException;
    void changeGroupByName(User user);
}
