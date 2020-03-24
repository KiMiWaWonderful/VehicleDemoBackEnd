package com.example.demo.serviceImpl;

import com.example.demo.pojo.Fence;
import com.example.demo.service.FenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FenceServiceImpl implements FenceService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Fence> getAllFence() {
        return mongoTemplate.findAll(Fence.class, "fence");
    }

    @Override
    public void addFence(Fence fence) {
        mongoTemplate.insert(fence, "fence");
    }

    @Override
    public Fence getFenceById(String id) {
        return mongoTemplate.findById(id, Fence.class);
    }

    @Override
    public void deleteFenceById(String id) {

        Query query = Query.query(Criteria.where("id").is(id));

        mongoTemplate.remove(query, Fence.class);
    }
}
