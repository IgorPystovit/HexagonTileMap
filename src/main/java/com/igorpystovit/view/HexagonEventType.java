package com.igorpystovit.view;

import javafx.event.EventType;

public enum HexagonEventType {
    BEGIN_CLICKED("begin_clicked"),
    END_CLICKED("end_clicked");

    private EventType<?> eventType;

    HexagonEventType(String name) {
        this.eventType = new EventType<>(name);
    }

    public EventType<?> getEventType() {
        return eventType;
    }

    public void setEventType(EventType<?> eventType) {
        this.eventType = eventType;
    }
}
