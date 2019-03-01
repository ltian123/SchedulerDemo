package cn.ltian.restful.service.impl;

import cn.ltian.restful.entity.User;
import cn.ltian.restful.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl implements UserService {

    public User getUser(Long id) {

        User u = new User();
        u.setId(1L);
        u.setName("小明");
        return u;
    }

    public boolean removeUser(Long id) {

        System.out.println("删除成功");
        return true;
    }

    public boolean addUser(User user) {
        System.out.println("添加成功");
        return true;
    }

    public boolean updateUser(User user) {
        System.out.println("更新成功");
        return true;
    }
}
