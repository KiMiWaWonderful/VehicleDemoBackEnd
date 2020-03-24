package com.example.demo.service;

import com.example.demo.pojo.Fence;

import java.util.List;

public interface FenceService {
    List<Fence> getAllFence();

    void addFence(Fence fence);

    Fence getFenceById(String id);

    void deleteFenceById(String id);
}
