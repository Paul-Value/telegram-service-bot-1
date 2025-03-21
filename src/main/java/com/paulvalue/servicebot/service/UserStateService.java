package com.paulvalue.servicebot.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserStateService {
    private final Map<Long, String> userStates = new ConcurrentHashMap<>();

    public void setUserState(Long userId, String state) {
        userStates.put(userId, state);
    }

    public String getCurrentState(Long userId) {
        return userStates.getOrDefault(userId, "DEFAULT");
    }
}
