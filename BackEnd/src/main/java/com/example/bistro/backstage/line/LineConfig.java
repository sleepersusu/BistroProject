package com.example.bistro.backstage.line;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.bot.client.LineMessagingClient;

@Configuration
public class LineConfig {
    @Bean
    public LineMessagingClient lineMessagingClient(
            @Value("${line.bot.channel-token}") String channelToken) {
        return LineMessagingClient.builder(channelToken).build();
    }
}
