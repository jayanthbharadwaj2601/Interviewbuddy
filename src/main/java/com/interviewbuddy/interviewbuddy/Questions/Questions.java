package com.interviewbuddy.interviewbuddy.Questions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
@Entity
public class Questions {
    @Id
    @Lob
    public String question;
    @Column
    public int userid;
}
