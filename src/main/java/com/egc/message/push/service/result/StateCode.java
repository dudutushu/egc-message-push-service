package com.egc.message.push.service.result;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StateCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String message;

}
