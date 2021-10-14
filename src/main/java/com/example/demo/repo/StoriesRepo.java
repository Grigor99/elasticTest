package com.example.demo.repo;

import com.example.demo.model.Stories;
import org.elasticsearch.script.Script;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoriesRepo extends ElasticsearchRepository<Stories,String> {
}
