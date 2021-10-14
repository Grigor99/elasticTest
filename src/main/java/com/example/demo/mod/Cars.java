package com.example.demo.mod;


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

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "cars5")
public class Cars implements Serializable {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String company;

    @Field(type = FieldType.Integer)
    private Integer year;

    @Field(type = FieldType.Integer)
    private Integer yearFrom;

    @Field(type = FieldType.Integer)
    private Integer yearTo;

    @Field(type = FieldType.Keyword)
    private String type;

    @ScriptedField
    @Field(type = FieldType.Integer)
    private Integer price;

}
