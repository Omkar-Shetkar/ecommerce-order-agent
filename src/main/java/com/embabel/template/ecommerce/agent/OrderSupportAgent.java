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

    record ReplacementReport(String text) {}

    @AchievesGoal(
            description = "Customer representative decides whether item is replaceable or not")
    @Action
    ReplacementReport verifyReplacementRequest(UserInput userInput, Ai ai){
        return ai
                // Medium temperature for accuracy
                .withLlm(LlmOptions
                        .withAutoLlm()
                        .withTemperature(0.5)
                )
                .withPromptContributor(Personas.CUSTOMER_REPRESENTATIVE)
                .withToolObjects(List.of(ruleBook, inventory, orders))
                .createObject(String.format("""
                        Customer has requested for order replacement.
                        Check whether customer order is valid using orders.
                        If yes, check whether the item is replaceable using rule book and inventory.
                        # User input
                        %s
                        """,
                        userInput.getContent()
                ).trim(), ReplacementReport.class);
    }


}
