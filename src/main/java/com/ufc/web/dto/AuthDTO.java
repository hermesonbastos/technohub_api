package com.ufc.web.dto;

import java.io.Serializable;

public record AuthDTO(String username, String password) implements Serializable {}
