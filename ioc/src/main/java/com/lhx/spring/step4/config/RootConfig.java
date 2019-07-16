package com.lhx.spring.step4.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by lihuaixin on 2019/7/16 14:36
 */
@Configuration
@ComponentScan(value = {"com.lhx.spring.step4"})
@EnableAspectJAutoProxy
public class RootConfig {

}
