package com.incodegroup.test.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
    private String id;
    private String email;
    private String password;
    private String name;
}
