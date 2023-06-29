package com.picpay.card.core.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    @JsonIgnore
    private int status;
    private String description;
    private List<String> descriptions;

}