package io.github.rusyasoft.example.tour.elprit.elpritreview.model.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum  EventType {
    REVIEW("REVIEW"),
    NONE("NONE");

    private String code;

    EventType(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return this.code;
    }

    public static EventType fromString(String value) {
        for (EventType eventType : EventType.values()) {
            if (eventType.code.equalsIgnoreCase(value)) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("No EventType with value " + value + " was found");
    }
}
