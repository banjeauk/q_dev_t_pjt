package com.kyobodts.q_dev_t_pjt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kyobodts.q_dev_t_pjt.entity.Story;
import com.kyobodts.q_dev_t_pjt.entity.StoryPage;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class AIService {
    
    private final Gson gson = new Gson();
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${aws.bedrock.bearer.token}")
    private String bearerToken;
    
    @Value("${aws.region}")
    private String region;
    
    @Value("${aws.bedrock.text.model}")
    private String textModel;
    
    private boolean useAWS = true;
    
    public String generateTitle(String background, String characters, String theme) {
        return String.format("%s의 모험", characters.split(",")[0].trim());
    }
    
    public String generateStoryContent(Story story, List<StoryPage> existingPages, int pageNumber, String previousChoice) {
        if (useAWS && bearerToken != null && !bearerToken.isEmpty()) {
            try {
                return generateWithBedrock(story, existingPages, pageNumber, previousChoice);
            } catch (Exception e) {
                return generateSampleContent(pageNumber, story, previousChoice);
            }
        }
        return generateSampleContent(pageNumber, story, previousChoice);
    }
    
    private String generateWithBedrock(Story story, List<StoryPage> existingPages, int pageNumber, String previousChoice) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("어린이를 위한 동화를 작성해주세요.\n");
        prompt.append("배경: ").append(story.getBackground()).append("\n");
        prompt.append("주인공: ").append(story.getCharacters()).append("\n");
        prompt.append("주제: ").append(story.getTheme()).append("\n");
        prompt.append("현재 페이지: ").append(pageNumber).append("/8\n\n");
        
        if (previousChoice != null) {
            prompt.append("이전 선택: ").append(previousChoice).append("\n\n");
        }
        
        if (!existingPages.isEmpty()) {
            prompt.append("지금까지의 이야기:\n");
            for (StoryPage page : existingPages) {
                prompt.append(page.getPageNumber()).append(". ").append(page.getContent()).append("\n");
            }
            prompt.append("\n");
        }
        
        prompt.append("다음 페이지 내용을 1-2문장으로 간결하게 작성해주세요. 30자 이내로 써주세요.");
        
        System.out.println("[AI] Story Generation Prompt: " + prompt.toString());
        
        JsonObject textGenerationConfig = new JsonObject();
        textGenerationConfig.addProperty("maxTokenCount", 150);
        textGenerationConfig.addProperty("temperature", 0.4);
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("inputText", prompt.toString());
        requestBody.add("textGenerationConfig", textGenerationConfig);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        
        HttpEntity<String> entity = new HttpEntity<>(gson.toJson(requestBody), headers);
        
        String url = String.format("https://bedrock-runtime.%s.amazonaws.com/model/%s/invoke", region, textModel);
        
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            String.class
        );
        
        JsonObject jsonResponse = gson.fromJson(response.getBody(), JsonObject.class);
        return jsonResponse.getAsJsonArray("results").get(0).getAsJsonObject().get("outputText").getAsString().trim();
    }
    
    private String generateSampleContent(int pageNumber, Story story, String previousChoice) {
        String mainCharacter = story.getCharacters().split(",")[0].trim();
        
        switch (pageNumber) {
            case 1:
                return mainCharacter + "는 모험을 시작했어요.";
            case 2:
                return mainCharacter + "는 반짝이는 보석을 발견했어요.";
            case 3:
                return mainCharacter + "는 친근한 동물을 만났어요.";
            case 4:
                return mainCharacter + "는 커다란 바위를 마주쳤어요.";
            case 5:
                return mainCharacter + "는 친구들과 함께 놀았어요.";
            case 6:
                return mainCharacter + "는 마법의 열쇠를 찾았어요.";
            case 7:
                return mainCharacter + "는 마지막 도전을 극복했어요.";
            case 8:
                return mainCharacter + "는 집으로 돌아갔어요.";
            default:
                return mainCharacter + "의 모험이 계속되어요.";
        }
    }
    
    public String[] generateChoices(String content) {
        if (content.contains("보석") || content.contains("반짝")) {
            return new String[]{"보석을 조심스럽게 만져보기", "보석의 힘을 느껴보기", "보석을 가방에 넣기", "보석을 원래 자리에 두기"};
        } else if (content.contains("동물") || content.contains("토끼") || content.contains("새")) {
            return new String[]{"동물과 대화 시도하기", "동물을 따라가기", "동물에게 인사하기", "동물과 함께 놀기"};
        } else if (content.contains("위험") || content.contains("어둠") || content.contains("무서")) {
            return new String[]{"용기를 내어 앞으로 가기", "친구들과 함께 행동하기", "안전한 곳 찾기", "큰 소리로 도움 요청하기"};
        } else if (content.contains("퍼즐") || content.contains("수수께끼") || content.contains("문")) {
            return new String[]{"차근차근 생각해보기", "단서를 더 찾아보기", "다른 방법 시도하기", "친구들과 의논하기"};
        } else if (content.contains("마법") || content.contains("신비")) {
            return new String[]{"마법의 힘 사용해보기", "마법 아이템 찾기", "마법 주문 외우기", "마법사에게 도움 요청하기"};
        } else if (content.contains("친구") || content.contains("도움")) {
            return new String[]{"친구들과 계획 세우기", "서로 격려하며 가기", "역할을 나누어 행동하기", "함께 노래하며 가기"};
        } else {
            String[][] defaultChoices = {
                {"신중하게 앞으로 가기", "주변을 자세히 관찰하기", "잠시 휴식하며 생각하기", "다른 경로 탐색하기"},
                {"용감하게 계속 진행하기", "흥미로운 것 찾아보기", "안전을 확인하며 가기", "새로운 발견하기"},
                {"모험을 계속하기", "주위 소리에 귀 기울이기", "발걸음을 조심하며 가기", "길을 바꿔서 가기"}
            };
            return defaultChoices[(int)(Math.random() * defaultChoices.length)];
        }
    }
    
    public String generateImagePrompt(String content, Story story) {
        // 동화 내용을 짧게 요약
        String summary = summarizeStoryContent(content, story);
        
        // 요약된 내용을 영어로 번역
        String englishPrompt = translateSummaryToEnglish(summary);
        
        System.out.println("[AI] Story Content: " + content);
        System.out.println("[AI] Korean Summary: " + summary);
        System.out.println("[AI] English Image Prompt: " + englishPrompt);
        
        return englishPrompt;
    }
    
    private String summarizeStoryContent(String content, Story story) {
        String character = story.getCharacters().split(",")[0].trim();
        String background = story.getBackground();
        
        // 동물 종류만 추출
        String simpleCharacter = "동물";
        if (character.contains("붕어") || character.contains("물고기")) simpleCharacter = "물고기";
        else if (character.contains("토끼")) simpleCharacter = "토끼";
        else if (character.contains("고양이")) simpleCharacter = "고양이";
        else if (character.contains("개")) simpleCharacter = "개";
        else if (character.contains("새")) simpleCharacter = "새";
        else if (character.contains("곰")) simpleCharacter = "곰";
        
        StringBuilder summary = new StringBuilder();
        summary.append(simpleCharacter).append("가 ").append(background).append("에서 ");
        
        // 내용 기반 장면 분류
        if (content.contains("보석") || content.contains("반짝")) {
            summary.append("보석을 발견하는 장면");
        } else if (content.contains("동물") || content.contains("만났") || content.contains("친근한")) {
            summary.append("동물 친구를 만나는 장면");
        } else if (content.contains("바위") || content.contains("마주쳤")) {
            summary.append("바위 앞에 서 있는 장면");
        } else if (content.contains("친구들") || content.contains("함께") || content.contains("놀았")) {
            summary.append("친구들과 노는 장면");
        } else if (content.contains("마법") || content.contains("열쇠") || content.contains("찾았")) {
            summary.append("마법 열쇠를 찾는 장면");
        } else if (content.contains("도전") || content.contains("극복") || content.contains("마지막")) {
            summary.append("도전을 극복하는 장면");
        } else if (content.contains("집") || content.contains("돌아갔")) {
            summary.append("집으로 돌아가는 장면");
        } else if (content.contains("모험") || content.contains("시작")) {
            summary.append("모험을 시작하는 장면");
        } else {
            summary.append("모험 중인 장면");
        }
        
        return summary.toString();
    }
    
    private String translateSummaryToEnglish(String koreanSummary) {
        // AWS 번역이 불안정하므로 간단 번역만 사용
        return translateSummarySimple(koreanSummary);
    }
    
    private String translateWithBedrock(String koreanSummary) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("다음 한국어 동화 장면 설명을 영어 이미지 생성 프롬프트로 번역해주세요:\n");
        prompt.append(koreanSummary).append("\n\n");
        prompt.append("어린이 동화책 스타일의 따뜻하고 밝은 이미지 프롬프트로 만들어주세요. 영어로만 답변해주세요.");
        
        JsonObject textGenerationConfig = new JsonObject();
        textGenerationConfig.addProperty("maxTokenCount", 80);
        textGenerationConfig.addProperty("temperature", 0.3);
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("inputText", prompt.toString());
        requestBody.add("textGenerationConfig", textGenerationConfig);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        
        HttpEntity<String> entity = new HttpEntity<>(gson.toJson(requestBody), headers);
        
        String url = String.format("https://bedrock-runtime.%s.amazonaws.com/model/%s/invoke", region, textModel);
        
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            String.class
        );
        
        JsonObject jsonResponse = gson.fromJson(response.getBody(), JsonObject.class);
        String translatedPrompt = jsonResponse.getAsJsonArray("results").get(0).getAsJsonObject().get("outputText").getAsString().trim();
        
        return translatedPrompt.length() > 300 ? translatedPrompt.substring(0, 300) : translatedPrompt;
    }
    
    private String translateSummarySimple(String koreanSummary) {
        // 동물 종류만 추출 (이름 없이)
        String character = "cute animal";
        if (koreanSummary.contains("물고기")) character = "cute fish";
        else if (koreanSummary.contains("토끼")) character = "cute rabbit";
        else if (koreanSummary.contains("고양이")) character = "adorable cat";
        else if (koreanSummary.contains("개")) character = "friendly dog";
        else if (koreanSummary.contains("새")) character = "colorful bird";
        else if (koreanSummary.contains("곰")) character = "gentle bear";
        else if (koreanSummary.contains("원숭이")) character = "cute monkey";
        else if (koreanSummary.contains("매미")) character = "cute cicada";
        
        // 배경 추출
        String background = "magical place";
        if (koreanSummary.contains("바다")) background = "underwater ocean";
        else if (koreanSummary.contains("우주")) background = "space";
        else if (koreanSummary.contains("숲")) background = "forest";
        else if (koreanSummary.contains("산")) background = "mountain";
        else if (koreanSummary.contains("마을")) background = "village";
        else if (koreanSummary.contains("성")) background = "castle";
        
        // 액션 추출
        String action = "on adventure";
        if (koreanSummary.contains("보석") || koreanSummary.contains("발견")) action = "discovering sparkling gem";
        else if (koreanSummary.contains("동물") || koreanSummary.contains("친구") || koreanSummary.contains("만나")) action = "meeting friendly animal";
        else if (koreanSummary.contains("바위") || koreanSummary.contains("마주쳤")) action = "facing big rock obstacle";
        else if (koreanSummary.contains("노는") || koreanSummary.contains("함께")) action = "playing with friends";
        else if (koreanSummary.contains("마법") || koreanSummary.contains("열쇠") || koreanSummary.contains("찾는")) action = "finding magic key";
        else if (koreanSummary.contains("도전") || koreanSummary.contains("극복")) action = "overcoming final challenge";
        else if (koreanSummary.contains("집") || koreanSummary.contains("돌아가")) action = "returning home happily";
        else if (koreanSummary.contains("모험") || koreanSummary.contains("시작")) action = "starting new adventure";
        
        return String.format("%s %s in %s, children's storybook illustration, consistent art style, warm bright colors, same character design", 
                           character, action, background);
    }
    
    private String translateBackground(String background) {
        if (background.contains("우주")) return "space";
        if (background.contains("바다")) return "ocean";
        if (background.contains("숲")) return "forest";
        if (background.contains("산")) return "mountain";
        if (background.contains("마을")) return "village";
        if (background.contains("성")) return "castle";
        if (background.contains("사막")) return "desert";
        if (background.contains("도시")) return "city";
        return background;
    }
    
    private String extractMainCharacter(String characters) {
        String mainChar = characters.split(",")[0].trim();
        if (mainChar.contains("토끼")) return "rabbit";
        if (mainChar.contains("고양이")) return "cat";
        if (mainChar.contains("개")) return "dog";
        if (mainChar.contains("새")) return "bird";
        if (mainChar.contains("물고기")) return "fish";
        if (mainChar.contains("곰")) return "bear";
        if (mainChar.contains("원숭이")) return "monkey";
        if (mainChar.contains("매미")) return "cicada";
        return "character";
    }
    

    
    public String generateImage(String prompt) {
        if (useAWS && bearerToken != null && !bearerToken.isEmpty()) {
            try {
                return generateImageWithTitan(prompt);
            } catch (Exception e) {
                System.err.println("[AI] Image generation failed: " + e.getMessage());
                return generateTestImage(prompt);
            }
        } else {
            System.out.println("[AI] AWS Bearer Token is not configured. Using test image.");
            return generateTestImage(prompt);
        }
    }
    
    private String generateTestImage(String prompt) {
        // 간단한 SVG 이미지 생성
        String svgContent = String.format(
            "<svg width='400' height='300' xmlns='http://www.w3.org/2000/svg'>" +
            "<rect width='100%%' height='100%%' fill='%s'/>" +
            "<text x='50%%' y='40%%' font-family='Arial' font-size='16' text-anchor='middle' fill='white'>🎨</text>" +
            "<text x='50%%' y='60%%' font-family='Arial' font-size='12' text-anchor='middle' fill='white'>%s</text>" +
            "</svg>",
            getRandomColor(),
            prompt.length() > 30 ? prompt.substring(0, 30) + "..." : prompt
        );
        
        // SVG를 Base64로 인코딩
        String base64Svg = java.util.Base64.getEncoder().encodeToString(svgContent.getBytes());
        return "data:image/svg+xml;base64," + base64Svg;
    }
    
    private String getRandomColor() {
        String[] colors = {"#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4", "#FFEAA7", "#DDA0DD", "#98D8C8"};
        return colors[(int)(Math.random() * colors.length)];
    }
    
    private String translatePromptToEnglish(String koreanPrompt) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("다음 한국어 프롬프트를 영어로 번역해주세요:\n");
        prompt.append(koreanPrompt).append("\n\n");
        prompt.append("영어 이미지 프롬프트만 생성해주세요.");
        
        System.out.println("[AI] Korean Prompt Translation Request: " + prompt.toString());
        
        JsonObject textGenerationConfig = new JsonObject();
        textGenerationConfig.addProperty("maxTokenCount", 100);
        textGenerationConfig.addProperty("temperature", 0.3);
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("inputText", prompt.toString());
        requestBody.add("textGenerationConfig", textGenerationConfig);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        
        HttpEntity<String> entity = new HttpEntity<>(gson.toJson(requestBody), headers);
        
        String url = String.format("https://bedrock-runtime.%s.amazonaws.com/model/%s/invoke", region, textModel);
        
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            String.class
        );
        
        JsonObject jsonResponse = gson.fromJson(response.getBody(), JsonObject.class);
        String translatedPrompt = jsonResponse.getAsJsonArray("results").get(0).getAsJsonObject().get("outputText").getAsString().trim();
        
        System.out.println("[AI] Translated English Prompt: " + translatedPrompt);
        
        return translatedPrompt.length() > 500 ? translatedPrompt.substring(0, 500) : translatedPrompt;
    }
    
    private String generateImageWithTitan(String prompt) {
        System.out.println("[AI] Starting image generation with prompt: " + prompt);
        
        JsonObject taskConfig = new JsonObject();
        taskConfig.addProperty("text", prompt);
        
        JsonObject imageGenerationConfig = new JsonObject();
        imageGenerationConfig.addProperty("numberOfImages", 1);
        imageGenerationConfig.addProperty("height", 1024);
        imageGenerationConfig.addProperty("width", 1024);
        imageGenerationConfig.addProperty("cfgScale", 8.0);
        imageGenerationConfig.addProperty("seed", (int)(Math.random() * 1000000));
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("taskType", "TEXT_IMAGE");
        requestBody.add("textToImageParams", taskConfig);
        requestBody.add("imageGenerationConfig", imageGenerationConfig);
        
        System.out.println("[AI] Image request body: " + gson.toJson(requestBody));
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + bearerToken);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        
        HttpEntity<String> entity = new HttpEntity<>(gson.toJson(requestBody), headers);
        
        String url = String.format("https://bedrock-runtime.%s.amazonaws.com/model/amazon.titan-image-generator-v1/invoke", region);
        System.out.println("[AI] Image generation URL: " + url);
        
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            String.class
        );
        
        System.out.println("[AI] Image response status: " + response.getStatusCode());
        System.out.println("[AI] Image response body length: " + response.getBody().length());
        
        JsonObject jsonResponse = gson.fromJson(response.getBody(), JsonObject.class);
        String base64Image = jsonResponse.getAsJsonArray("images").get(0).getAsString();
        System.out.println("[AI] Generated image base64 length: " + base64Image.length());
        return "data:image/png;base64," + base64Image;
    }

}