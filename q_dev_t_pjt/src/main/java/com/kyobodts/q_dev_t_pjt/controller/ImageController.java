package com.kyobodts.q_dev_t_pjt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kyobodts.q_dev_t_pjt.service.AIService;

@Controller
public class ImageController {
    
    @Autowired
    private AIService aiService;
    
    @GetMapping("/image-test")
    public String imageTestPage() {
        return "image-test";
    }
    
    @PostMapping("/image-test")
    public String generateTestImage(@RequestParam("prompt") String prompt, Model model) {
        System.out.println("[ImageController] Received prompt: " + prompt);
        
        try {
            String imageUrl = aiService.generateImage(prompt);
            model.addAttribute("prompt", prompt);
            model.addAttribute("imageUrl", imageUrl);
            model.addAttribute("success", imageUrl != null);
            
            System.out.println("[ImageController] Generated image URL: " + (imageUrl != null ? "SUCCESS" : "FAILED"));
        } catch (Exception e) {
            System.out.println("[ImageController] Error: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
        }
        
        return "image-test";
    }
}