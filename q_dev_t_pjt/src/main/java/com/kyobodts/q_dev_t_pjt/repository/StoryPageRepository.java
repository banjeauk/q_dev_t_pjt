package com.kyobodts.q_dev_t_pjt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kyobodts.q_dev_t_pjt.entity.StoryPage;

@Repository
public interface StoryPageRepository extends JpaRepository<StoryPage, Long> {
    List<StoryPage> findByStoryIdOrderByPageNumber(Long storyId);
}