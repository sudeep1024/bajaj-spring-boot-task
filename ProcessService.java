package com.bajaj.hiring.service;

import com.bajaj.hiring.dto.WebhookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProcessService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    public void executeProcess() {
        try {
            // Step 1: Generate Webhook
            System.out.println("Generating Webhook...");
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("name", "Sudeep");
            requestBody.put("regNo", "22BCE2751");
            requestBody.put("email", "sudeep@example.com");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<WebhookResponse> response = restTemplate.postForEntity(GENERATE_WEBHOOK_URL, request,
                    WebhookResponse.class);
            WebhookResponse webhookResponse = response.getBody();

            if (webhookResponse != null) {
                String webhookUrl = webhookResponse.getWebhookUrl();
                String accessToken = webhookResponse.getAccessToken();

                System.out.println("Webhook URL: " + webhookUrl);
                System.out.println("Access Token: " + accessToken);

                // Step 2: Submit Solution
                submitSolution(webhookUrl, accessToken);
            } else {
                System.err.println("Failed to get webhook response.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitSolution(String webhookUrl, String accessToken) {
        System.out.println("Submitting Solution...");

        // SQL Query for Question 1 (Odd RegNo)
        String sqlQuery = "SELECT t.DEPARTMENT_NAME, t.SALARY, t.EMPLOYEE_NAME, t.AGE " +
                "FROM ( " +
                "    SELECT " +
                "        d.DEPARTMENT_NAME, " +
                "        SUM(p.AMOUNT) AS SALARY, " +
                "        CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS EMPLOYEE_NAME, " +
                "        TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
                "        RANK() OVER (PARTITION BY d.DEPARTMENT_ID ORDER BY SUM(p.AMOUNT) DESC) as rnk " +
                "    FROM EMPLOYEE e " +
                "    JOIN DEPARTMENT d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID " +
                "    JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID " +
                "    WHERE DAY(p.PAYMENT_TIME) <> 1 " +
                "    GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME, e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, e.DOB " +
                ") t " +
                "WHERE t.rnk = 1";

        Map<String, String> solutionBody = new HashMap<>();
        solutionBody.put("finalQuery", sqlQuery);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken); // As per instructions: <accessToken>

        HttpEntity<Map<String, String>> request = new HttpEntity<>(solutionBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, request, String.class);
            System.out.println("Solution Submitted. Response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Error submitting solution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
