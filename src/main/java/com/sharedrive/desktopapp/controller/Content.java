package com.sharedrive.desktopapp.controller;

import lombok.Data;

import java.util.UUID;

@Data
public class Content {

    private ContentType type;

    private String name = UUID.randomUUID().toString();

    private String Url;

    private String source;

}
