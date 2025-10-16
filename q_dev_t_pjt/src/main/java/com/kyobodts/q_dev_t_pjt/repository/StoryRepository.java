package com.kyobodts.q_dev_t_pjt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kyobodts.q_dev_t_pjt.entity.Story;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
}