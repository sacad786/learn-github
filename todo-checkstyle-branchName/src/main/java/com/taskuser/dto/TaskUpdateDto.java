package com.taskuser.dto;

import lombok.Data;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data

@Table(name="Task")
@EntityListeners(AuditingEntityListener.class)

public class TaskUpdateDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("descriptionOfTask")
    private String descriptionOfTask;

    @JsonProperty("dateTime")
    private String dateTime;

    public TaskUpdateDto() {
    }

    public TaskUpdateDto(String name, String descriptionOfTask, String dateTime){
        this.name = name;
        this.descriptionOfTask = descriptionOfTask;
        this.dateTime = dateTime;

    }
}

