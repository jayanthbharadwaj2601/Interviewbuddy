package com.interviewbuddy.interviewbuddy.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    public int id;
    @Column
    public String username;
    @Column
    public String password;
}
