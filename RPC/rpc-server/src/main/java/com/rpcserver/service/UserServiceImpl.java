package com.rpcserver.service;

import com.rpcapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public String saveUser(String name) {
        log.info("Api Begin SaveUser:" + name);
        return "Save User Success";
    }

    @Override
    public String getUserId(String name, Long userId) {
        return name + "的用户id是:" + userId;
    }
}
