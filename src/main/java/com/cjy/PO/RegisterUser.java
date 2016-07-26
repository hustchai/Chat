package com.cjy.PO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by jychai on 16/7/1.
 */

@Data
@AllArgsConstructor
public class RegisterUser implements Serializable {
    private String username;
    private String password;
}
