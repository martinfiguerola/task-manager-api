package com.martin.taskmanager.exception;

import lombok.*;
import java.util.List;


@NoArgsConstructor
@Setter @Getter
public class ErrorResponse {
    private Integer status;
    private String error;
    private List<String> messages;
}
