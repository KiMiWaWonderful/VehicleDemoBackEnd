package com.example.demo.mapper;

import com.example.demo.pojo.Trace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraceRepository extends MongoRepository<Trace, String> {
}
