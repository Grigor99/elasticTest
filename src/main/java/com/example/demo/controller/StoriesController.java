package com.example.demo.controller;


import com.example.demo.model.Stories;
import com.example.demo.model.StoryDTo;
import com.example.demo.repo.StoriesRepo;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Log4j2
@RestController
@RequestMapping("/story")
public class StoriesController {
    Logger logger = LogManager.getLogger(StoriesController.class);

    @PostConstruct
    private void someLogs() {
        Stories stories = new Stories();
        stories.setPrice(5000);
        stories.setSalary(500D);
        storiesRepo.save(stories);
        Stories stories2 = new Stories();
        stories.setPrice(22000);
        stories.setSalary(5089890D);
        storiesRepo.save(stories);
        Stories stories3 = new Stories();
        stories.setPrice(505500);
        stories.setSalary(50099D);
        stories.setAge(66);
        storiesRepo.save(stories);
        logger.info("{\\\"story1\\\": " + stories + "}");
        logger.info("{\\\"story2\\\": " + stories2 + "}");
        logger.info("{\\\"story3\\\": " + stories3 + "}");
        logger.info("{\\\"message\\\": \\\"Hello World from Logback!\\\"}");
    }

    private StoriesRepo storiesRepo;
    private ElasticsearchOperations elasticsearchOperations;

    private RestHighLevelClient restHighLevelClient;

    public StoriesController(StoriesRepo storiesRepo, ElasticsearchOperations elasticsearchOperations, RestHighLevelClient restHighLevelClient) {
        this.storiesRepo = storiesRepo;
        this.elasticsearchOperations = elasticsearchOperations;
        this.restHighLevelClient = restHighLevelClient;
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody StoryDTo d) {

        Stories s = new Stories();
        s.setAge(d.getAge());
        s.setMark(d.getMark());
//s.setPrice(1);
        ;
        s.setSalary(d.getSalary());
        s.setState(d.getState());
        logger.info("{story:" + s + "}");

        return ResponseEntity.ok(storiesRepo.save(s));
    }

    @GetMapping("/all")
    public ResponseEntity<?> all() {
        QueryBuilder queryBuilder = matchAllQuery();
        Query query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();
        return ResponseEntity.ok(elasticsearchOperations.search(query, Stories.class, IndexCoordinates.of("long_stories")).stream().map(SearchHit::getContent).collect(Collectors.toList()));
    }


//        PutStoredScriptRequest request = new PutStoredScriptRequest();
//        request.id("new_eko");
//
//        request.content(new BytesArray(
//                "{\n" +
//                        "\"script\": {\n" +
//                        "\"lang\": \"painless\",\n" +
//                        "\"source\": \"stories/doc['salary'].value\"" +
//                        "}\n" +
//                        "}\n"
//        ), XContentType.JSON);
//        AcknowledgedResponse putStoredScriptResponse = restHighLevelClient.putScript(request, RequestOptions.DEFAULT);
//
//        GetStoredScriptRequest request1 = new GetStoredScriptRequest("new_eko");
//
//        GetStoredScriptResponse getResponse = restHighLevelClient.getScript(request1, RequestOptions.DEFAULT);
//
//        StoredScriptSource storedScriptSource = getResponse.getSource();
//
//        Script script = new Script("{\n" +
//                "  \"query\": {\n" +
//                "    \"bool\": {\n" +
//                "      \"filter\": {\n" +
//                "        \"script\": {\n" +
//                "          \"script\": \"\"\"\n" +
//                "            double amount = doc['salary'].value;\n" +
//                "            if (doc['salary'].value == 4) {\n" +
//                "              amount *= -1;\n" +
//                "            }\n" +
//                "            return amount < 10;\n" +
//                "          \"\"\"\n" +
//                "        }\n" +
//                "      }\n" +
//                "    }\n" +
//                "  }\n" +
//                "}");

//        Script script = new Script("{\n" +
//                "  \"script_fields\": {\n" +
//                "    \"my_doubled_field\": {\n" +
//                "      \"script\": { \n" +
//                "        \"source\": \"doc['salary'].value * params['multiplier']\", \n" +
//                "        \"params\": {\n" +
//                "          \"multiplier\": 5\n" +
//                "        }\n" +
//                "      }\n" +
//                "    }\n" +
//                "  }\n" +
//                "}");
//        Script script = new Script("params['_source']['innerId']==doc['innerId'].value");
//
//        ScriptQueryBuilder queryBuilder = QueryBuilders
//                .scriptQuery(script);
//
//
//        NativeSearchQuery query = new NativeSearchQueryBuilder()
//                .withQuery(queryBuilder)
//
//                .build();
//        MaxAggregationBuilder maxAggregationBuilder = AggregationBuilders
//                .max("max_of_age").field("age");
//
//        QueryBuilder queryBuilder = QueryBuilders
//                .matchAllQuery();
//        Query query = new NativeSearchQueryBuilder()
//                .withQuery(queryBuilder)
//                .addAggregation(maxAggregationBuilder)
//                .build();
////
//        SearchHits<Stories>
//                searchHits = elasticsearchOperations.search(query, Stories.class
//                , IndexCoordinates.of("long_stories"));

//  .scriptQuery(new Script("    { \"script\": { \n" +
//                        "        \"source\": \"doc['salary'].value * params['multiplier']\", \n" +
//                        "        \"params\": {\n" +
//                        "          \"multiplier\": 2\n" +
//                        "        }\n" +
//                        "      }},doc['salary'].value>5"));
//               .scriptQuery(new Script("doc['age'].value == doc['salary'].value"));
//                .scriptQuery(new Script("double amount = doc['salary'].value ;" +
//                        "return amount >1 "));

//        ScriptQueryBuilder scriptQueryBuilder
//                = QueryBuilders
//
//                .scriptQuery(new Script("{\n" +
//                        "    \"aggs\": {\n" +
//                        "        \"total_price\": {\n" +
//                        "            \"sum\": {\n" +
//                        "                \"script\": {\n" +
//                        "                    \"lang\": \"painless\",\n" +
//                        "                    \"inline\": \"doc['salary'].value * doc['age'].value\"\n" +
//                        "                }\n" +
//                        "            }\n" +
//                        "        }\n" +
//                        "    }\n" +
//                        "}"));


//
//        TermsAggregationBuilder subAggregation = AggregationBuilders
//
//                .terms("prices")
//
//                .script(new Script(ScriptType.INLINE, "painless",
//                        "doc['age'].value"
//                        , Collections.emptyMap()));
//        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
//                .withQuery(matchAllQuery())
//                .addAggregation(subAggregation)
//
//                .build();


//        TermsAggregationBuilder subAggregation = AggregationBuilders
//                .terms("aggName")
//                .script(new Script(ScriptType.INLINE, "painless",
//
//                        "int result =0;" +
//                                "for(int i =0;i< 5;i++){" +
//                                "result+=doc['age'].value" +
//                                "};" +
//                                "return result;"
//                        , Collections.emptyMap()));
//        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
//                .addAggregation(subAggregation)
//                .build();
//
//            Query query = new NativeSearchQueryBuilder()                       // we build a Elasticsearch native query
//                    .addAggregation(terms("price").field("age").size(10)) // add the aggregation
//                    .withQuery(QueryBuilders.matchAllQuery())   // add the query part
//                    .build();
//
//            SearchHits<Stories> searchHits = elasticsearchOperations.search(query, Stories.class);  // send it of and get the result


    //        ScriptedMetricAggregationBuilder sumAggregationBuilder = AggregationBuilders
//                .scriptedMetric("script_test").initScript(script);
//        AggregationBuilders.scriptedMetric().
//        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
////                .withQuery(matchAllQuery())
//                .addAggregation(sumAggregationBuilder)
//                .build();
//
    @GetMapping("/script")
    public Object getI() {

        TermsAggregationBuilder subAggregation = AggregationBuilders
                .terms("price")
                .script(new Script(ScriptType.INLINE, "painless",

                        "            int total = 0;\n" +
                                "            for (int i = 0; i < 5; ++i) {\n" +
                                "              total += doc['age'].value;\n" +
                                "            }\n" +
                                "            return total ;\n"
                        , Collections.emptyMap()));
//        ScriptQueryBuilder scriptedField = QueryBuilders
//                .scriptQuery(new Script(ScriptType.INLINE, "painless",


//                        , Collections.emptyMap()));


        ScriptField scriptedField1 = new ScriptField("price", new Script(ScriptType.INLINE, "painless",

                "            int total = 0;\n" +
                        "            for (int i = 0; i < 5; ++i) {\n" +
                        "              total += doc['age'].value*doc['salary'].value;\n" +
                        "            }\n" +
                        "            return total ;\n"
                , Collections.emptyMap()));

        String[] includes = new String[4];
        includes[0] = "salary";
        includes[1] = "mark";
        includes[2] = "state";
        includes[3] = "age";
        String[] excludes = new String[0];


        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())

                .withScriptField(scriptedField1)
                .withSourceFilter(new FetchSourceFilter(includes, excludes))
                .build();
        SearchHits<Stories> searchHits = elasticsearchOperations.search(nativeSearchQuery, Stories.class, IndexCoordinates.of("long_stories"));  // send it of and get the result

        return searchHits.getSearchHits();
    }
}

//        SearchHits<Stories>
//                searchHits = elasticsearchOperations.search(nativeSearchQuery, Stories.class
//                , IndexCoordinates.of("long_stories"));
//        List<Aggregation> list = searchHits.getAggregations().asList();
//        for (Aggregation aggregation : list) {
//            aggregation.getMetadata();
//        }
//        System.out.println(searchHits.getTotalHits());3


//    public void putMappingJSONSoure() throws ExecutionException, InterruptedException {
//        PutMappingRequest putMappingRequest = new PutMappingRequest("long_storie");
//        putMappingRequest.type("doc")
//                .source("{\n" +
//                        "      \"properties\": {\n" +
//                        "        \"blogId\": {\n" +
//                        "          \"type\": \"integer\"\n" +
//                        "        },\n" +
//                        "        \"isGuestPost\": {\n" +
//                        "          \"type\": \"boolean\"\n" +
//                        "        },\n" +
//                        "        \"voteCount\": {\n" +
//                        "          \"type\": \"integer\"\n" +
//                        "        },\n" +
//                        "        \"createdAt\": {\n" +
//                        "          \"type\": \"date\"\n" +
//                        "        }\n" +
//                        "      }\n" +
//                        "}", XContentType.JSON);
//        AcknowledgedResponse acknowledgedResponse = elasticsearchOperations.putMapping(putMappingRequest).get();
//        System.out.println("Put Mapping response : " + acknowledgedResponse.isAcknowledged());



/*

Expression	Description
doc['field_name'].value

The value of the field, as a double

doc['field_name'].empty

A boolean indicating if the field has no values within the doc.

doc['field_name'].length

The number of values in this document.

doc['field_name'].min()

The minimum value of the field in this document.

doc['field_name'].max()

The maximum value of the field in this document.

doc['field_name'].median()

The median value of the field in this document.

doc['field_name'].avg()

The average of the values in this document.

doc['field_name'].sum()

The sum of the values in this document.
 */



/*

import com.dwelow.common.dto.AddressSearch;
import com.dwelow.common.dto.LatLong;
import com.dwelow.common.dto.SortablePageRequest;
import com.dwelow.common.dto.property.PropertySearch;
import com.dwelow.common.enums.FileType;
import com.dwelow.common.enums.property.PropertySort;
import com.dwelow.common.enums.property.PropertyStatus;
import com.dwelow.common.elasticlayer.filter.NullFilter;
import com.dwelow.common.elasticlayer.searchItems.mainsearch.PropertyData;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.Script;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.elasticsearch.index.query.QueryBuilders.geoPolygonQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

public class SearchRepositoryImpl extends SecondarySearchHandler implements SearchRepository {

    private NullFilter nullFilter;
    private ElasticsearchOperations elasticsearchOperations;

    public SearchRepositoryImpl(NullFilter nullFilter, ElasticsearchOperations elasticsearchOperations) {
        this.nullFilter = nullFilter;
        this.elasticsearchOperations = elasticsearchOperations;
    }


    @Override
    public List<PropertyData> getAll(SortablePageRequest<PropertySort> pageable, PropertySearch propertySearch) throws IOException {
        QueryBuilder propertyTypeBuilder = null;
        if (!CollectionUtils.isEmpty(propertySearch.getPropertyType())) {
            propertyTypeBuilder = QueryBuilders
                            .termsQuery("propertyPropertyType", propertySearch.getPropertyType());
        }

        QueryBuilder propertyIdsBuilder = null;
        if (!CollectionUtils.isEmpty(propertySearch.getPropertyIds())) {
            propertyIdsBuilder = QueryBuilders
                            .termsQuery("propertyId", propertySearch.getPropertyIds());
        }

        QueryBuilder propertyParentIdsBuilder = null;
        if (!CollectionUtils.isEmpty(propertySearch.getMainPropertyIds())) {
            propertyParentIdsBuilder = QueryBuilders
                            .termsQuery("propertyParentId", propertySearch.getMainPropertyIds());
        }
        QueryBuilder statusBuilder = null;

        statusBuilder = QueryBuilders
                        .termQuery("propertyStatus", PropertyStatus.PUBLISHED);

        // address search region

        // file
        QueryBuilder fileScoreBuilder =  QueryBuilders.termQuery("filesOrderNumber", 0);
        QueryBuilder fileTypeBuilder = QueryBuilders.termQuery("filesFileType", FileType.PROPERTY_IMAGE);



        //end of file
        AddressSearch addressDTO = propertySearch.getAddress();
        QueryBuilder addressStreetBuilder = null;
        QueryBuilder addressDistrictBuilder = null;
        QueryBuilder addressNeighborhoodBuilder = null;
        QueryBuilder addressZipCodeBuilder = null;
        QueryBuilder addressNumberBuilder = null;

        QueryBuilder addressCityBuilder = null;
        QueryBuilder addressStateBuilder = null;
        if (addressDTO != null) {
            if (addressDTO.getAddressLine() != null) {
                addressCityBuilder = matchQuery("addressCity", addressDTO.getAddressLine()).fuzziness(Fuzziness.TWO);
                addressStateBuilder = matchQuery("addressState", addressDTO.getAddressLine()).fuzziness(Fuzziness.TWO);
                addressZipCodeBuilder = matchQuery("addressZipCode", addressDTO.getAddressLine()).fuzziness(Fuzziness.TWO);
            } else {

                if (addressDTO.getStreet() != null) {
                    addressStreetBuilder = matchQuery("addressStreet", addressDTO.getStreet()).fuzziness(Fuzziness.TWO);
                }
                if (addressDTO.getDistrict() != null) {
                    addressDistrictBuilder = matchQuery("addressDistrict", addressDTO.getDistrict()).fuzziness(Fuzziness.TWO);
                }
                if (addressDTO.getNeighborhood() != null) {
                    addressNeighborhoodBuilder = matchQuery("addressNeighborhood", addressDTO.getNeighborhood()).fuzziness(Fuzziness.TWO);
                }
                if (addressDTO.getZipCode() != null) {
                    addressZipCodeBuilder = matchQuery("addressZipCode", addressDTO.getZipCode()).fuzziness(Fuzziness.TWO);
                }
                if (addressDTO.getNumber() != null) {
                    addressNumberBuilder = matchQuery("addressNumber", addressDTO.getNumber()).fuzziness(Fuzziness.TWO);
                }
            }
        }

        // the end of address search

        // near search beginning

        QueryBuilder nearDistanceBuilder = null;
        if (propertySearch.getNearLatitude() != null &&
                propertySearch.getNearLongitude() != null
                && propertySearch.getRadius() != null) {
            nearDistanceBuilder = QueryBuilders
                                    .geoDistanceQuery("addressLocation").point(propertySearch.getNearLatitude()
                                            , propertySearch.getNearLongitude()).distance(propertySearch.getRadius(), DistanceUnit.KILOMETERS);

        }


        //near search end

        // polygon search beginning
        List<QueryBuilder> geoQueryBuilders = new ArrayList<>();

        List<List<LatLong>> searchPolygons = propertySearch.getPolygons();
        if (!CollectionUtils.isEmpty(searchPolygons)) {

            for (List<LatLong> points : searchPolygons) {
                if (!CollectionUtils.isEmpty(points)) {
                    List<org.elasticsearch.common.geo.GeoPoint> geoPoints = new ArrayList<>();
                    for (LatLong latLong : points) {
                        if (latLong != null) {
                            geoPoints.add(new org.elasticsearch.common.geo.GeoPoint(latLong.getLatitude(), latLong.getLongitude()));
                        }
                    }
                    QueryBuilder queryBuilder = QueryBuilders
                                    .geoPolygonQuery("addressLocation", geoPoints);
                    geoQueryBuilders.add(queryBuilder);
                }
            }
        }


        // the end of the polygon search

        // beds search beginning

        BoolQueryBuilder bedsBuilder = null;
        if (!CollectionUtils.isEmpty(propertySearch.getBeds())) {
            QueryBuilder bedsMaxBuilder = null;
            QueryBuilder bedsMinBuilder = null;
            List<QueryBuilder> beds = new ArrayList<>();
            bedsMaxBuilder = QueryBuilders
                                    .rangeQuery("propertyBedsMaxValue").from(propertySearch.getBeds().stream().findFirst().get(), true);
            bedsMinBuilder = QueryBuilders
                                    .rangeQuery("propertyBedsMinValue").to(propertySearch.getBeds().stream().findFirst().get(), true);
            beds.add(bedsMinBuilder);
            beds.add(bedsMaxBuilder);
            if (!CollectionUtils.isEmpty(beds)) {
                bedsBuilder = QueryBuilders.boolQuery();
                for (QueryBuilder queryBuilder : beds) {
                    bedsBuilder.must(queryBuilder);
                }
            }


        }

        // beds end


        // baths search beginning

        BoolQueryBuilder bathsBuilder = null;
        if (!CollectionUtils.isEmpty(propertySearch.getBaths())) {
            QueryBuilder bathsMaxBuilder = null;
            QueryBuilder bathsMinBuilder = null;
            List<QueryBuilder> baths = new ArrayList<>();
            bathsMaxBuilder = QueryBuilders
                                    .rangeQuery("propertyBathsMaxValue").from(propertySearch.getBaths().stream().findFirst().get());
            bathsMinBuilder = QueryBuilders
                                    .rangeQuery("propertyBathsMinValue").to(propertySearch.getBaths().stream().findFirst().get());
            baths.add(bathsMaxBuilder);
            baths.add(bathsMinBuilder);
            if (!CollectionUtils.isEmpty(baths)) {
                bathsBuilder = QueryBuilders.boolQuery();
                for (QueryBuilder queryBuilder : baths) {
                    bathsBuilder.must(queryBuilder);
                }
            }
        }

        // baths end

        //square feet search beginning
        QueryBuilder unitSquareFeetBuilder = null;
        if (!isNull(propertySearch.getMinArea()) || !isNull(propertySearch.getMaxArea())) {
            Number min = nonNull(propertySearch.getMinArea()) ? propertySearch.getMinArea() : 0;
            Number max = nonNull(propertySearch.getMaxArea()) ? propertySearch.getMaxArea() : Integer.MAX_VALUE;
            unitSquareFeetBuilder =
                    QueryBuilders
                            .nestedQuery("propertyUnitList", QueryBuilders
                                            .rangeQuery("propertyUnitList.squareFeet").from(min, true).to(max, true)
                                    , ScoreMode.Avg);
        }


        // the  end of the square feet search


        // the amenity option id beginning
        Collection<Integer> amenities = propertySearch.getAmenityOptionIds();
        QueryBuilder propAmenityOptionBuilder = null;
        if (nonNull(amenities) && !amenities.isEmpty()) {
            propAmenityOptionBuilder
                    = QueryBuilders
                    .nestedQuery("property2AmenityOptions", QueryBuilders
                            .termsQuery("property2AmenityOptions.amenityOptionId", amenities), ScoreMode.Avg);
        }
        // end of prop amenity option region

        //keyword


        BoolQueryBuilder keyBoolBuilder = null;

        if (!CollectionUtils.isEmpty(propertySearch.getKeywords())) {
            List<QueryBuilder> keyWordBuilders = new ArrayList<>();
            for (String key : propertySearch.getKeywords()) {
                QueryBuilder keywordBuilder = QueryBuilders
                                .matchQuery("propertyAmenityOptions", key).fuzziness(Fuzziness.TWO);
                keyWordBuilders.add(keywordBuilder);
            }
            if (!CollectionUtils.isEmpty(keyWordBuilders)) {
                keyBoolBuilder = QueryBuilders.boolQuery();
                for (QueryBuilder queryBuilder : keyWordBuilders) {
                    if (queryBuilder != null) {
                        keyBoolBuilder.should(queryBuilder);
                    }
                }
            }
        }
        //move in filter beginning

        QueryBuilder isAvailableBuilderReturn = getIsAvailable();

        BoolQueryBuilder moveInBuilder = null;

        if (propertySearch.getMoveFrom() != null && propertySearch.getMoveTo() != null) {
            QueryBuilder availableBuilder = QueryBuilders

                    .nestedQuery("propertyUnitList", QueryBuilders
                            .rangeQuery("propertyUnitList.availableFrom")
                            .from(propertySearch.getMoveFrom(), true)
                            .to(propertySearch.getMoveTo(), true), ScoreMode.Avg);
            List<QueryBuilder> queryBuilderList = new ArrayList<>();
            if (availableBuilder != null) {
                queryBuilderList.add(availableBuilder);
            }
            if (isAvailableBuilderReturn != null) {
                queryBuilderList.add(isAvailableBuilderReturn);
            }
            if (!CollectionUtils.isEmpty(queryBuilderList)) {
                moveInBuilder = QueryBuilders.boolQuery();
                for (QueryBuilder queryBuilder : queryBuilderList) {
                    moveInBuilder.must(queryBuilder);
                }
            }

        } else if (propertySearch.getMoveFrom() != null && propertySearch.getMoveTo() == null) {
            QueryBuilder availableBuilder = QueryBuilders

                    .nestedQuery("propertyUnitList", QueryBuilders
                            .rangeQuery("propertyUnitList.availableFrom")
                            .gte(propertySearch.getMoveFrom()), ScoreMode.Avg);

            List<QueryBuilder> queryBuilderList = new ArrayList<>();
            if (availableBuilder != null) {
                queryBuilderList.add(availableBuilder);
            }
            if (isAvailableBuilderReturn != null) {
                queryBuilderList.add(isAvailableBuilderReturn);
            }
            if (!CollectionUtils.isEmpty(queryBuilderList)) {
                moveInBuilder = QueryBuilders.boolQuery();
                for (QueryBuilder queryBuilder : queryBuilderList) {
                    moveInBuilder.must(queryBuilder);
                }
            }

        } else if (propertySearch.getMoveFrom() == null && propertySearch.getMoveTo() != null) {
            QueryBuilder availableBuilder = QueryBuilders

                    .nestedQuery("propertyUnitList", QueryBuilders
                            .rangeQuery("propertyUnitList.availableFrom")
                            .lte(propertySearch.getMoveTo()), ScoreMode.Avg);


            List<QueryBuilder> queryBuilderList = new ArrayList<>();
            if (availableBuilder != null) {
                queryBuilderList.add(availableBuilder);
            }
            if (isAvailableBuilderReturn != null) {
                queryBuilderList.add(isAvailableBuilderReturn);
            }
            if (!CollectionUtils.isEmpty(queryBuilderList)) {
                moveInBuilder = QueryBuilders.boolQuery();
                for (QueryBuilder queryBuilder : queryBuilderList) {
                    moveInBuilder.must(queryBuilder);
                }
            }

        }

        // move in filter end


        //lease duration start
        QueryBuilder minLeaseBuilder = null;
        QueryBuilder maxLeaseBuilder = null;

        if (!isNull(propertySearch.getMinLeaseDuration()) || !isNull(propertySearch.getMaxLeaseDuration())) {

            Number min = nonNull(propertySearch.getMinLeaseDuration()) ? propertySearch.getMinLeaseDuration() : 0;
            Number max = nonNull(propertySearch.getMaxLeaseDuration()) ? propertySearch.getMaxLeaseDuration() : Integer.MAX_VALUE;

            minLeaseBuilder = QueryBuilders
                                    .rangeQuery("propertyMinLease").from(min, true)
                                    .to(max, true);
            maxLeaseBuilder = QueryBuilders
                                    .rangeQuery("propertyMaxLease").from(min, true)
                                    .to(max, true);
        }


        //the end of lease duration


        //the beginning of filtering null builders


        List<QueryBuilder> geoQueryBuildersList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(geoQueryBuilders)) {
            for (QueryBuilder queryBuilder : geoQueryBuilders) {
                if (queryBuilder != null) {
                    geoQueryBuildersList.add(queryBuilder);
                }
            }
        }


        List<QueryBuilder> queryBuilderList = new ArrayList<>();
        queryBuilderList.addAll(Arrays.asList(propertyTypeBuilder, propertyIdsBuilder, propertyParentIdsBuilder,
                addressStreetBuilder, addressDistrictBuilder, addressNeighborhoodBuilder, addressZipCodeBuilder,
                addressNumberBuilder, addressCityBuilder, addressStateBuilder,
                nearDistanceBuilder, bedsBuilder, bathsBuilder, unitSquareFeetBuilder
                ,fileScoreBuilder,fileTypeBuilder, propAmenityOptionBuilder, statusBuilder, moveInBuilder, minLeaseBuilder, maxLeaseBuilder, keyBoolBuilder));


        // the end of null filtering

        List<QueryBuilder> nonNullBuilders = nullFilter.filterBuilders(queryBuilderList);


        BoolQueryBuilder boolQueryBuilder = null;
        if (!CollectionUtils.isEmpty(nonNullBuilders)) {
            boolQueryBuilder = QueryBuilders.boolQuery();
            for (QueryBuilder queryBuilder : nonNullBuilders) {
                boolQueryBuilder.must(queryBuilder);
            }
        }
        BoolQueryBuilder geoPolygonBoolQueryBuilder = null;

        if (!CollectionUtils.isEmpty(geoQueryBuildersList)) {
            geoPolygonBoolQueryBuilder = QueryBuilders.boolQuery();
            for (QueryBuilder queryBuilder : geoQueryBuildersList) {
                geoPolygonBoolQueryBuilder.should(queryBuilder);
            }
        }

        BoolQueryBuilder finalBuilder = QueryBuilders.boolQuery();
        if (boolQueryBuilder != null) {
            finalBuilder.must(boolQueryBuilder);
        }
        if (geoPolygonBoolQueryBuilder != null) {
            finalBuilder.must(geoPolygonBoolQueryBuilder);

        }


        Pageable pageRequest = null;
        if (pageable.getPage() != null && pageable.getSize() != null) {

            Sort sort = Sort.by("propertyCreatedAt");
            if (pageable.getSortDirection() != null) {
                if (pageable.getSortDirection().name().equals("DESC")) {
                    sort.descending();
                } else {
                    sort.ascending();
                }
            } else {
                sort.ascending();
            }


            pageRequest =
                    PageRequest.of(pageable.getPage(), pageable.getSize(), sort);
        }
        Query resultQuery = null;
        if (pageRequest != null) {
            resultQuery = new NativeSearchQueryBuilder()
                    .withQuery(finalBuilder)
                    .withPageable(pageRequest)
                    .build();
        } else {
            assert geoPolygonBoolQueryBuilder != null;
            resultQuery = new NativeSearchQueryBuilder()
                    .withQuery(finalBuilder)
                    .build();
        }


        SearchHits<PropertyData> searchHits = elasticsearchOperations
                .search(resultQuery, PropertyData.class, IndexCoordinates.of("property_data"));

        return searchHits.stream().

                map(SearchHit::getContent).

                collect(Collectors.toList());
    }

    @Override
    public void get() {

        Script script = new Script("{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"filter\": {\n" +
                "        \"script\": {\n" +
                "          \"script\": \"\"\"\n" +
                "            double amount = doc['property.catPrice'].value;\n" +
                "            if (doc['property.catPrice'].value == 4) {\n" +
                "              amount *= -1;\n" +
                "            }\n" +
                "            return amount < 10;\n" +
                "          \"\"\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");

//        Script script = new Script("{\n" +
//                "  \"script_fields\": {\n" +
//                "    \"my_doubled_field\": {\n" +
//                "      \"script\": { \n" +
//                "        \"source\": \"doc['salary'].value * params['multiplier']\", \n" +
//                "        \"params\": {\n" +
//                "          \"multiplier\": 5\n" +
//                "        }\n" +
//                "      }\n" +
//                "    }\n" +
//                "  }\n" +
//                "}");

//        ScriptQueryBuilder queryBuilder = QueryBuilders
//                .scriptQuery(script);
//
//
//        NativeSearchQuery query = new NativeSearchQueryBuilder()
//                .withFilter(queryBuilder)
//
//                .build();
//
//        SearchHits<Stories>
//                searchHits = elasticsearchOperations.search(query, Stories.class
//                , IndexCoordinates.of("long_stories"));

        ScriptQueryBuilder a
                = QueryBuilders
                .scriptQuery(script);

        NativeSearchQuery query1 = new NativeSearchQueryBuilder()
                .withFilter(a)

                .build();

        SearchHits<PropertyData>
                searchHits = elasticsearchOperations.search(query1, PropertyData.class
                , IndexCoordinates.of("property_data"));
//searchHits.stream().iterator().next().getContent().getProperty().getAddress().getLocation()

    }


}

 */

/*
package com.dwelow.common.elasticlayer.searchItems.mainsearch;


import com.dwelow.common.elasticlayer.searchItems.*;
import com.dwelow.common.enums.FileType;
import com.dwelow.common.enums.ImageSizeEnum;
import com.dwelow.common.enums.State;
import com.dwelow.common.enums.property.CommissionCategory;
import com.dwelow.common.enums.property.PropertyStatus;
import com.dwelow.common.enums.property.PropertyType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vladmihalcea.hibernate.type.range.Range;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "property_data")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyData {
    @Id
    private String id;

    //property
    @Field(type = FieldType.Text)
    private String propertyId;

    @Field(type = FieldType.Text)
    private String propertyUnitNumber;

    @Field(type = FieldType.Keyword)
    private PropertyType propertyPropertyType;

    @Field(type = FieldType.Keyword)
    private PropertyStatus propertyStatus = PropertyStatus.DRAFT;

    @Field(type = FieldType.Text)
    private String propertyName;

    @Field(type = FieldType.Text)
    private String propertyDescription;

    @Field
    private Map<String, String> propertyBusinessHours;

    @Field(type = FieldType.Nested)
    private List<String> propertyCustomAmenities;

 */