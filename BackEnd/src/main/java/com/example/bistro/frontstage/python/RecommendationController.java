package com.example.bistro.frontstage.python;

import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.menu.MenuService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Value("${python.script.path}")
    private String pythonScriptPath;

    @Autowired
    private MenuService menuService;

    //postman ok
    @GetMapping("/user/{memberId}")
    public ResponseEntity<?> getRecommendations(@PathVariable Integer memberId) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "C:/Users/User/Desktop/app/BistroProject/BackEnd/src/main/python/venv/Scripts/python.exe",
                    pythonScriptPath + "/main.py",
                    memberId.toString()
            );
            processBuilder.environment().put("PYTHONIOENCODING", "utf-8");

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "UTF-8")
            );
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), "UTF-8")
            );

            //error
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                String jsonOutput = output.toString().trim();

                if (!jsonOutput.startsWith("{") || !jsonOutput.endsWith("}")) {
                    return ResponseEntity.status(500).body("Invalid JSON output from Python script: " + jsonOutput);
                }

                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(jsonOutput);

                // 返回 JSON
                return ResponseEntity.ok(rootNode);
            } else {
                return ResponseEntity.status(500)
                        .body("Python script execution failed:\n" + errorOutput.toString());
            }

        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body("Error starting Python process: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/menu/findByName")
    public ResponseEntity<?> findMenuByName(@RequestParam String productName) {
        try {
            // 去除菜單名稱中的首尾空格,並轉為小寫
            String trimmedName = productName.trim().toLowerCase();
            Menu menu = (Menu) menuService.findMenuByNameLikeForPython(trimmedName);
            if (menu != null) {
                return ResponseEntity.ok(menu);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }




//test用
    //    @GetMapping("/user23/{memberId}")
    //    public ResponseEntity<?> getRecommendations232(@PathVariable Integer memberId) {
    //        Map<String, Object> response = new HashMap<>();
    //        response.put("status", "success");
    //        response.put("message", "推薦生成成功");
    //        response.put("recommendations", List.of(
    //                Map.of("menuId", 1, "productName", "熔岩巧克力蛋糕", "score", 4.5)
    //        ));
    //        return ResponseEntity.ok(response);
    //    }

}
