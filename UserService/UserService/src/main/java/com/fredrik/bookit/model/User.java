package com.fredrik.bookit.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appuser")
public class User {

	@Id
    private String userid;

    private String name;

    private String email;    
    
    private String role;
    
}