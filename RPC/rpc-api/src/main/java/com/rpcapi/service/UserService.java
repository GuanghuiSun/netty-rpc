package com.rpcapi.service;

public interface UserService {

    String saveUser(String name);

    String getUserId(String name, Long userId);
}
