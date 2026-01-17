package com.embabel.template.ecommerce;

import com.embabel.agent.api.annotation.LlmTool;
import org.springframework.stereotype.Component;

@Component
public class RuleBook {

    @LlmTool(description = "Rules to decide whether an item is eligible for replacement or not")
    public String rules() {
        return """
                - Consumable items aren't eligible for replacement.
                - All other items are eligible for replacement.
                """;
    }
}
