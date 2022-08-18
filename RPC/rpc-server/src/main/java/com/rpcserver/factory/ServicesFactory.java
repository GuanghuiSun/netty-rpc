package com.rpcserver.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServicesFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static <T> T getService(Class<T> interfaceClass) {
        return applicationContext.getBean(interfaceClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServicesFactory.applicationContext = applicationContext;
    }
}
