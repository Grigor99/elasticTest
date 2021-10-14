package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ObjectEnr {
    @Field(type = FieldType.Text)
    private String surname;
}
