package com.project.hit.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Authcode {
    
    @Id
    private String email;

    private String code;
    
}
