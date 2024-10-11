package com.gdg.hackathon.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
public class Registrant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long studentId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String major;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    @Column(unique = true, nullable = true)
    private String githubId;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false, unique = true)
    private String email;

    public Registrant() {

    }

    public Registrant(Long studentId, String name, String major, String phoneNumber, Position position, String githubId, String teamName, String email) {
        this.studentId = studentId;
        this.name = name;
        this.major = major;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.githubId = githubId;
        this.teamName = teamName;
        this.email = email;
    }

}
