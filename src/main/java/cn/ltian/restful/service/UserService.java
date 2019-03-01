package cn.ltian.restful.service;

import cn.ltian.restful.entity.User;

public interface UserService {

    /**
     * 根据id  查询user
     * @param id
     * @return
     */
    public User getUser(Long id);

    /**
     * 根据id ，删除user
     * @param id
     * @return
     */
    public boolean removeUser(Long id);

    /**
     * 增加用户
     * @param user
     * @return
     */
    public boolean addUser(User user);

    /**
     * 根据id，更新用户user
     * @param user
     * @return
     */
    public boolean updateUser(User user);
}
