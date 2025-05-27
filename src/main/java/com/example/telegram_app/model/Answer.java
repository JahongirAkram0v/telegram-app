package com.example.telegram_app.model;

import java.util.Map;

public record Answer(String method, Map<String, Object> body) {}
