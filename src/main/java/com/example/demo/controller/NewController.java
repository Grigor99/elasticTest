package com.example.demo.controller;


import com.example.demo.mod.Cars;
import com.example.demo.mod.Dto;
import com.example.demo.mod.Response;
import com.example.demo.mod.Ships;
import com.example.demo.repo.CarRepo;
import com.example.demo.repo.ShipsRepo;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@RestController
@RequestMapping("/new")
public class NewController {

    private ElasticsearchOperations elasticsearchOperations;
    private CarRepo carRepo;
    private ShipsRepo shipsRepo;

    public NewController(ElasticsearchOperations elasticsearchOperations, CarRepo carRepo, ShipsRepo shipsRepo) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.carRepo = carRepo;
        this.shipsRepo = shipsRepo;
    }


    @PostMapping
    public ResponseEntity create(@RequestBody Dto dto) {
        Cars cars = new Cars();
        cars.setCompany(dto.getCompany());
        cars.setType("TYPE");
        cars.setYear(dto.getYear());
        cars.setYearFrom(dto.getYearFrom());
        cars.setYearTo(dto.getYearTo());
        Cars savedCars = carRepo.save(cars);


        Response response = new Response();
        response.setCompany(savedCars.getCompany());
        response.setYear(savedCars.getYear());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<SearchHit<Response>> get(@RequestParam String condition, @RequestParam Integer year) {
        QueryBuilder conditionBuilder = QueryBuilders
                .matchQuery("condition", condition);
        QueryBuilder yearBuilder = QueryBuilders
                .termQuery("year", year)
                .caseInsensitive(true);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders
                .boolQuery().should(conditionBuilder).should(yearBuilder);
        String[] includes = new String[3];
        includes[0] = "company";
        includes[1] = "year";
        includes[2] = "condition";
        String[] excludes = new String[0];
        Query query = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
//                .withSourceFilter(new FetchSourceFilter(includes, excludes))
                .build();

        return elasticsearchOperations.search(query, Response.class, IndexCoordinates.of("cars1", "ships1"))
                .getSearchHits();
    }

    @GetMapping("/all")
    public Object getAll() {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        ScriptField scriptedField = new ScriptField("price", new Script(ScriptType.INLINE, "painless",
                "int response = 0;" +
                        "if(doc['year'].value<1999&&doc['type'].value=='TYPE'){" +
                        "response += (10000*doc['yearFrom'].value+doc['yearTo'].value);" +
                        "}"+
                        "else if(doc['year'].value>1999&&doc['year'].value<2019){" +
                        "response += (30*doc['yearFrom'].value+doc['yearTo'].value);" +
                        "}"+
                        "else{" +
                        "response += (50*doc['yearFrom'].value+doc['yearTo'].value);" +
                        "}"
                , Collections.emptyMap()));
        String[] includes = new String[6];
        includes[0] = "company";
        includes[1] = "year";
        includes[2] = "yearFrom";
        includes[3] = "yearTo";
        includes[4] = "price";
        includes[5] = "type";
        String[] excludes = new String[0];
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withScriptField(scriptedField)
                .withSourceFilter(new FetchSourceFilter(includes, excludes))
                .build();
        return elasticsearchOperations.search(nativeSearchQuery, Cars.class, IndexCoordinates.of("cars5")).getSearchHits();
    }
}
