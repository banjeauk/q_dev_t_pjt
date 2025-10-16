package com.kyobodts.q_dev_t_pjt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyobodts.q_dev_t_pjt.entity.Story;
import com.kyobodts.q_dev_t_pjt.entity.StoryPage;
import com.kyobodts.q_dev_t_pjt.repository.StoryPageRepository;
import com.kyobodts.q_dev_t_pjt.repository.StoryRepository;

@Service
public class StoryService {
    
    @Autowired
    private StoryRepository storyRepository;
    
    @Autowired
    private StoryPageRepository storyPageRepository;
    
    @Autowired
    private AIService aiService;
    
    public Story createStory(String background, String characters, String theme) {
        Story story = new Story();
        story.setBackground(background);
        story.setCharacters(characters);
        story.setTheme(theme);
        story.setTitle(aiService.generateTitle(background, characters, theme));
        
        return storyRepository.save(story);
    }
    
    public synchronized List<StoryPage> generateCompleteStory(Long storyId) {
        // 이미 생성된 페이지가 있는지 확인
        List<StoryPage> existingPages = storyPageRepository.findByStoryIdOrderByPageNumber(storyId);
        if (existingPages.size() >= 8) {
            return existingPages;
        }
        
        Story story = storyRepository.findById(storyId).orElse(null);
        if (story == null) {
            throw new RuntimeException("Story not found with id: " + storyId);
        }
        
        List<StoryPage> pages = new ArrayList<>(existingPages);
        
        // 남은 페이지만 생성
        for (int pageNumber = existingPages.size() + 1; pageNumber <= 8; pageNumber++) {
            String content = aiService.generateStoryContent(story, pages, pageNumber, null);
            String imagePrompt = aiService.generateImagePrompt(content, story);
            String imageUrl = aiService.generateImage(imagePrompt);
            
            StoryPage page = new StoryPage();
            page.setStory(story);
            page.setPageNumber(pageNumber);
            page.setContent(content);
            page.setImagePrompt(imagePrompt);
            if (imageUrl != null) {
                page.setImagePath(imageUrl);
            }
            
            StoryPage savedPage = storyPageRepository.save(page);
            pages.add(savedPage);
            
            System.out.println("[Story] Generated page " + pageNumber + "/8");
        }
        
        return pages;
    }
    
    public StoryPage generateNextPage(Long storyId, int pageNumber, String previousChoice) {
        // 기존 페이지가 있으면 반환
        List<StoryPage> existingPages = storyPageRepository.findByStoryIdOrderByPageNumber(storyId);
        StoryPage existingPage = existingPages.stream()
            .filter(p -> p.getPageNumber() == pageNumber)
            .findFirst()
            .orElse(null);
            
        if (existingPage != null) {
            return existingPage;
        }
        
        // 없으면 전체 스토리 생성
        List<StoryPage> allPages = generateCompleteStory(storyId);
        return allPages.stream()
            .filter(p -> p.getPageNumber() == pageNumber)
            .findFirst()
            .orElse(null);
    }
    
    public void selectChoice(Long pageId, String choice) {
        StoryPage page = storyPageRepository.findById(pageId).orElse(null);
        if (page != null) {
            page.setSelectedChoice(choice);
            storyPageRepository.save(page);
        }
    }
    
    public List<StoryPage> getStoryPages(Long storyId) {
        return storyPageRepository.findByStoryIdOrderByPageNumber(storyId);
    }
    
    public List<Story> getAllStories() {
        return storyRepository.findAll();
    }
    
    public void deleteStory(Long storyId) {
        storyRepository.deleteById(storyId);
    }
}