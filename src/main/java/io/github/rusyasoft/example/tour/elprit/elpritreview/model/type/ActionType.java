package io.github.rusyasoft.example.tour.elprit.elpritreview.model.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum ActionType {
    ADD("ADD"),
    MOD("MOD"),
    DELETE("DELETE");

    private String code;

    ActionType(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return this.code;
    }

    public static ActionType fromString(String value) {
        for (ActionType actionType : ActionType.values()) {
            if (actionType.code.equalsIgnoreCase(value)) {
                return actionType;
            }
        }
        throw new IllegalArgumentException("No ActionType with value " + value + " was found");
    }
}