package io.github.rusyasoft.example.tour.elprit.elpritreview.model.type;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum ReviewPointType {
    ADD_FIRST_REVIEW("ADD_FIRST_REVIEW", 1),
    ADD_CONTENT("ADD_CONTENT", 1),
    ADD_PHOTO("ADD_PHOTO", 1),
    DELETE_CONTENT("DELETE_CONTENT", -1),
    DELETE_FIRST_REVIEW("DELETE_FIRST_REVIEW", -1),
    DELETE_PHOTO("DELETE_PHOTO", -1);

    private String code;
    private int pointVal;

    ReviewPointType(String code, int value) {
        this.code = code;
        this.pointVal = value;
    }

    @JsonValue
    public String getCode() {
        return this.code;
    }

    public int getPointVal() { return this.pointVal; }

    public static ReviewPointType fromString(String value) {
        for (ReviewPointType actionType : ReviewPointType.values()) {
            if (actionType.code.equalsIgnoreCase(value)) {
                return actionType;
            }
        }
        throw new IllegalArgumentException("No ReviewPointType with value " + value + " was found");
    }
}