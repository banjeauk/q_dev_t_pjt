package com.kyobodts.q_dev_t_pjt.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "chat_history")
public class ChatHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 1000)
    private String userMessage;
    
    @Column(nullable = false, length = 2000)
    private String aiResponse;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}