package com.taskuser.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.Table;


@Data
@Table(name = "User")
@EntityListeners(AuditingEntityListener.class)

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserUpdateDto {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;


    public UserUpdateDto() {
    }

    public UserUpdateDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }


}