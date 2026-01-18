package com.embabel.template.ecommerce;

import com.embabel.agent.api.annotation.LlmTool;
import org.springframework.stereotype.Component;

@Component
public class RuleBook {

    @LlmTool(description = "Rules to decide whether an item is eligible for replacement or not")
    public String rules() {
        return """
                - Check whether the item is eatable or not.
                - Eatable items aren't eligible for replacement.
                - Non-eatable items are eligible for replacement.
                """;
    }
}
