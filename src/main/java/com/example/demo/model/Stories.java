package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ScriptedField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "long_stories")
public class Stories implements Serializable {

    @Id
    private String id;
    @Field(type = FieldType.Integer)
    @ScriptedField
    private Integer age;
    @Field(type = FieldType.Double)
    @ScriptedField
    private Double salary;
    @Field(type = FieldType.Integer)
    private Integer mark;
    @Field(type = FieldType.Keyword)
    private String state;
    @Field(type = FieldType.Integer)
    @ScriptedField
    private Integer price;



}
