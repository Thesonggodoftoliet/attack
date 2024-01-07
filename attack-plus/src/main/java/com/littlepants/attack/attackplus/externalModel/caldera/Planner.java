package com.littlepants.attack.attackplus.externalModel.caldera;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/7/29
 */
@Data
public class Planner {
    private boolean allow_repeatable_abilities=false;
    private String module="plugins.stockpile.app.atomic";
    private List<String> ignore_enforcement_modules=new ArrayList<>();
    private String plugin="stockpile";
    private Map<String,Object> params=new HashMap<>();
    private String description= """
            During each phase of the operation, the atomic planner iterates through each agent and sends the next
            available ability it thinks that agent can complete. This decision is based on the agent matching the operating
            system (execution platform) of the ability and the ability command having no unsatisfied variables.
            The planner then waits for each agent to complete its command before determining the subsequent abilities.
            The abilities are processed in the order set by each agent's atomic ordering.
            For instance, if agent A has atomic ordering (A1, A2, A3) and agent B has atomic ordering (B1, B2, B3), then
            the planner would send (A1, B1) in the first phase, then (A2, B2), etc.
            """;
    private String id="aaa7c857-37a0-4c4a-85f7-4e9f7f30e31a";
    private List<Fact> stopping_conditions=new ArrayList<>();
    private String name="atomic";
}
