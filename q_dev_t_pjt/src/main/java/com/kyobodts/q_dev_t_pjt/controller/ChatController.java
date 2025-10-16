package com.kyobodts.q_dev_t_pjt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChatController {

    @GetMapping("/chat")
    public String chatPage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", username);
        return "chat";
    }

    @PostMapping("/chat")
    public String chat(@RequestParam("message") String message,
                      HttpSession session,
                      Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        // 간단한 AI 응답 (실제 AI 연동은 API 키 필요)
        String aiResponse = "AI: " + message + "에 대한 응답입니다.";

        model.addAttribute("username", username);
        model.addAttribute("userMessage", message);
        model.addAttribute("aiResponse", aiResponse);
        return "chat";
    }
}