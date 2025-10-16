package com.kyobodts.q_dev_t_pjt.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "stories")
public class Story {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String background;
    
    @Column(nullable = false)
    private String characters;
    
    @Column(nullable = false)
    private String theme;
    
    @Column(nullable = false)
    private String status = "IN_PROGRESS";
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
    private List<StoryPage> pages;
}