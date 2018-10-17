package com.jfeat;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * AM Web程序启动类
 *
 * @author Admin
 * @date 2017-05-21 9:43
 */
public class AmServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AmApplication.class);
    }

}
