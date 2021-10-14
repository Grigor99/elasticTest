package com.example.demo.repo;

import com.example.demo.mod.Cars;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CarRepo extends ElasticsearchRepository<Cars,String > {
}
