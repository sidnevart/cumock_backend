package com.example.cumock.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pvp_contest")
public class PvpContest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;
    private UUID player1Id;
    private UUID player2Id;
    private Long problem1Id;
    private Long problem2Id;

    private UUID winnerId;
    private int player1Score;
    private int player2Score;
    private int player1Attempts;
    private int player2Attempts;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @Enumerated(EnumType.STRING)
    private ContestStatus status;
}
