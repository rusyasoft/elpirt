package io.github.rusyasoft.example.tour.elprit.elpritreview.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.type.ActionType;
import io.github.rusyasoft.example.tour.elprit.elpritreview.model.type.EventType;
import lombok.Data;

import java.util.List;

@Data
@JsonSerialize
public class EventParam {
    private EventType type;     //REVIEW
    private ActionType action;      //ADD, MOD, DELETE
    private String reviewId;    //240a0658-dc5f-4878-9381-ebb7b2667772
    private String content;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;
}
