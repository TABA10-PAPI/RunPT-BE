package com.runpt.back.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RunningToAIResponseDto {
    
    private String message;
    
    @JsonProperty("updated_skill")
    private UpdatedSkill updatedSkill;
    
    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdatedSkill {
        @JsonProperty("avg_pace_sec")
        private Double avgPaceSec;
        
        @JsonProperty("max_distance")
        private Double maxDistance;
        
        @JsonProperty("weekly_distance")
        private Double weeklyDistance;
        
        @JsonProperty("training_load")
        private Double trainingLoad;
        
        @JsonProperty("fatigue_level")
        private Double fatigueLevel;
        
        @JsonProperty("consistency_score")
        private Double consistencyScore;
    }
}

