package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.elasticsearch.search.aggregations.Aggregation;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ScriptedField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Do implements Serializable {

    private Long count;
    private String id;
    private Integer age;
    private Double salary;
    private Integer mark;
    private String state;
    private List<Integer> list = new ArrayList<>();

}
