package com.kyobodts.q_dev_t_pjt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kyobodts.q_dev_t_pjt.entity.Story;
import com.kyobodts.q_dev_t_pjt.entity.StoryPage;
import com.kyobodts.q_dev_t_pjt.service.StoryService;

@Controller
public class StoryController {
    
    @Autowired
    private StoryService storyService;
    
    @GetMapping("/story/create")
    public String createStoryForm() {
        return "story/create";
    }
    
    @PostMapping("/story/create")
    public String createStory(@RequestParam("background") String background,
                             @RequestParam("characters") String characters,
                             @RequestParam("theme") String theme) {
        Story story = storyService.createStory(background, characters, theme);
        return "redirect:/story/" + story.getId() + "/loading";
    }
    
    @GetMapping("/story/{storyId}/page/{pageNumber}")
    public String showStoryPage(@PathVariable("storyId") Long storyId,
                               @PathVariable("pageNumber") int pageNumber,
                               @RequestParam(value = "choice", required = false) String choice,
                               Model model) {
        
        List<StoryPage> existingPages = storyService.getStoryPages(storyId);
        StoryPage currentPage = existingPages.stream()
            .filter(p -> p.getPageNumber() == pageNumber)
            .findFirst()
            .orElse(null);
        
        if (currentPage == null) {
            currentPage = storyService.generateNextPage(storyId, pageNumber, choice);
        }
        
        model.addAttribute("page", currentPage);
        model.addAttribute("storyId", storyId);
        model.addAttribute("isLastPage", pageNumber == 8);
        
        return "story/page";
    }
    
    @GetMapping("/story/{storyId}/loading")
    public String showLoadingPage(@PathVariable("storyId") Long storyId, Model model) {
        model.addAttribute("storyId", storyId);
        
        // 백그라운드에서 전체 스토리 생성
        new Thread(() -> {
            try {
                storyService.generateCompleteStory(storyId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        
        return "story/loading";
    }
    
    @PostMapping("/story/{storyId}/page/{pageNumber}/choice")
    public String selectChoice(@PathVariable("storyId") Long storyId,
                              @PathVariable("pageNumber") int pageNumber,
                              @RequestParam("pageId") Long pageId,
                              @RequestParam("choice") String choice) {
        
        storyService.selectChoice(pageId, choice);
        
        if (pageNumber < 8) {
            try {
                String encodedChoice = java.net.URLEncoder.encode(choice, "UTF-8");
                return "redirect:/story/" + storyId + "/loading/" + (pageNumber + 1) + "?choice=" + encodedChoice;
            } catch (Exception e) {
                return "redirect:/story/" + storyId + "/loading/" + (pageNumber + 1);
            }
        } else {
            return "redirect:/story/" + storyId + "/complete";
        }
    }
    
    @GetMapping("/story/{storyId}/complete")
    public String completeStory(@PathVariable("storyId") Long storyId, Model model) {
        List<StoryPage> pages = storyService.getStoryPages(storyId);
        
        // 동화가 완성되지 않았으면 로딩 페이지로 되돌리기
        if (pages.size() < 8) {
            return "redirect:/story/" + storyId + "/loading";
        }
        
        model.addAttribute("pages", pages);
        return "story/complete";
    }
    
    @GetMapping("/story/list")
    public String listStories(Model model) {
        List<Story> stories = storyService.getAllStories();
        model.addAttribute("stories", stories);
        return "story/list";
    }
    
    @GetMapping("/story/{storyId}/delete")
    public String deleteStory(@PathVariable("storyId") Long storyId) {
        storyService.deleteStory(storyId);
        return "redirect:/story/list";
    }
}