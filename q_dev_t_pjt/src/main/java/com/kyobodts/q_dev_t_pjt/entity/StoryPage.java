package com.kyobodts.q_dev_t_pjt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "story_pages")
public class StoryPage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;
    
    @Column(nullable = false)
    private Integer pageNumber;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column
    private String imagePrompt;
    
    @Column(columnDefinition = "TEXT")
    private String imagePath;
    
    @Column
    private String choice1;
    
    @Column
    private String choice2;
    
    @Column
    private String choice3;
    
    @Column
    private String choice4;
    
    @Column
    private String selectedChoice;
}