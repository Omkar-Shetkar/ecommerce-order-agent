package com.embabel.template.ecommerce.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.Ai;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.prompt.persona.Persona;
import com.embabel.common.ai.model.LlmOptions;
import com.embabel.template.ecommerce.Inventory;
import com.embabel.template.ecommerce.Orders;
import com.embabel.template.ecommerce.RuleBook;

import java.util.List;

abstract class Personas {
    static final Persona CUSTOMER_REPRESENTATIVE = Persona.create(
            "Customer Representative",
            "Customer Representative",
            "Professional and helpful",
            "Help customer on his/her order replacement"
    );

    static final Persona STORE_REPRESENTATIVE = Persona.create(
            "Store Representative",
            "Store Representative",
            "Professional and precise",
            "Help Customer Representative with order replacement delivery"
    );
}

@Agent(description = "Support agent to check replacement of items and schedule delivery of replacement item")
public class OrderSupportAgent {

    private final RuleBook ruleBook;
    private final Orders orders;
    private final Inventory inventory;

    public OrderSupportAgent(RuleBook ruleBook, Orders orders, Inventory inventory) {
        this.ruleBook = ruleBook;
        this.orders = orders;
        this.inventory = inventory;
    }

    public record ReplacementReport(String text) {
    }

    public record StockReport(String text) {
    }

    public record DeliveryReport(String text) {
    }

    public record CustomerConfirmationReport(String text) {
    }

    @Action
    public ReplacementReport replacementReport(UserInput userInput, Ai ai) {
        return ai.withAutoLlm()
                .withToolObjects(List.of(ruleBook, inventory, orders))
                .createObject(String.format("""
                        Verify eligibility for: %s
                        """, userInput.getContent()), ReplacementReport.class);
    }

    @Action
    StockReport stockReport(ReplacementReport replacementReport, Ai ai) {
        return ai
                .withAutoLlm()
                .withPromptContributor(Personas.STORE_REPRESENTATIVE)
                .withToolObjects(List.of(ruleBook, inventory, orders))
                .createObject("Check stock for: " + replacementReport.text(), StockReport.class);
    }

    @Action
    DeliveryReport deliveryReport(StockReport stockReport, Ai ai) {
        return ai
                .withAutoLlm()
                .withPromptContributor(Personas.STORE_REPRESENTATIVE)
                .withToolObjects(List.of(ruleBook, inventory, orders))
                .createObject("Confirm delivery details: " + stockReport.text(), DeliveryReport.class);
    }

    @AchievesGoal(description = "Checked for eligibility, stock, delivery schedule")
    @Action
    CustomerConfirmationReport customerConfirmationReport(DeliveryReport deliveryReport, Ai ai) {
        return ai
                .withAutoLlm()
                .withPromptContributor(Personas.CUSTOMER_REPRESENTATIVE)
                .createObject(String.format("Compose a reply mail to customer: " + deliveryReport.text())
                        .trim(), CustomerConfirmationReport.class);
    }


}
