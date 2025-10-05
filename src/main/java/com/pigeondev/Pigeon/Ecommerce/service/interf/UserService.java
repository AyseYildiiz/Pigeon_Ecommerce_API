package com.pigeondev.Pigeon.Ecommerce.service.interf;

import com.pigeondev.Pigeon.Ecommerce.dto.LoginRequest;
import com.pigeondev.Pigeon.Ecommerce.dto.Response;
import com.pigeondev.Pigeon.Ecommerce.dto.UserDto;
import com.pigeondev.Pigeon.Ecommerce.entity.User;

public interface UserService {

    Response registerUser(UserDto registrationRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    User getLoginUser();

    Response getUserInfoAndOrderHistory();
}
