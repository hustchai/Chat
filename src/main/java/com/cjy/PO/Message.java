package com.cjy.PO;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by jychai on 16/7/23.
 */
@Data
public class Message implements Serializable {
    private String username;
    private String content;
}
