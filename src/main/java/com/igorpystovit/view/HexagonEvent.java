package com.igorpystovit.view;

import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HexagonEvent<T> extends Event {
    private T eventPayload;

    public HexagonEvent(EventType<? extends javafx.event.Event> eventType, T eventPayload) {
        super(eventType);
        this.eventPayload = eventPayload;
    }
}
