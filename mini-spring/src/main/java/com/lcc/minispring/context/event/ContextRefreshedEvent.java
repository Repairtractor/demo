package com.lcc.minispring.context.event;

import com.lcc.minispring.context.ApplicationContext;

public class ContextRefreshedEvent extends ApplicationContextEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }

}
