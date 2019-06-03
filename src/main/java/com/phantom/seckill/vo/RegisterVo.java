package com.phantom.seckill.vo;

import com.phantom.seckill.validator.isPhone;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class RegisterVo {
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @NotNull
    @isPhone
    private String phone;

    @NotNull
    @Length(min = 7)//长度必须大于7
    private String password;

    @NotNull
    private String nickname;
}
