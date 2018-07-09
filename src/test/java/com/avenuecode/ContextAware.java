package com.avenuecode;

import com.avenuecode.model.ContextProvider;
import org.springframework.context.annotation.Bean;

public class ContextAware {

    @Bean
    public ContextProvider contextProvider() {
        return new ContextProvider();
    }
}
