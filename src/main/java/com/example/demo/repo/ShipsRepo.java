package com.example.demo.repo;

import com.example.demo.mod.Ships;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ShipsRepo extends ElasticsearchRepository<Ships,String > {
}
