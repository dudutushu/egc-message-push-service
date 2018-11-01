package com.egc.message.push.service.result;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StateCodeData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String message;


    @Getter
    @Setter
    private Object data;

}
