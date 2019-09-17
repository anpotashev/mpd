package ru.net.arh.mpd.search.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SockJSResponse<T> {
    @JsonTypeId
    private String type;


    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type", visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = TreeItem.class, name = "FULL_TREE"),
            @JsonSubTypes.Type(value = Boolean.class, name = "CONNECTION_STATE")
    })
    private T payload;

}
