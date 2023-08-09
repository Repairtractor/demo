package com.lcc.minispring.context;

import com.lcc.minispring.context.event.ApplicationEvent;

public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);
}
