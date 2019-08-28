package io.github.rusyasoft.example.tour.elprit.elpritreview.model.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum  EventType {
    REVIEW("REVIEW"),
    NONE("NONE");

    String code;

    EventType(String code) {
        this.code = code;
    }

    @JsonValue
    public String value() {
        return this.code;
    }

    public static EventType fromString(String value) {
        return EventType.valueOf(value);
    }
}
