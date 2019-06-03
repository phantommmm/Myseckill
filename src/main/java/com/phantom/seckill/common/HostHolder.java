package com.phantom.seckill.common;

import com.phantom.seckill.entity.User;
import org.springframework.stereotype.Component;

/**
 * 当前线程的唯一User
 */
@Component
public class HostHolder {

    private final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public User getUser() {
        return threadLocal.get();
    }

    public void setUser(User user) {
        threadLocal.set(user);
    }

    public void clear(){
        threadLocal.remove();
    }
}
