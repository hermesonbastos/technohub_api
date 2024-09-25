package com.ufc.web.dto;

import java.io.Serializable;

public record AuthDTO(String email, String password) implements Serializable {}
