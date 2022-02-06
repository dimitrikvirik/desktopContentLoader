package com.sharedrive.desktopapp.lib;

import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.Getter;


@Getter
public  class SceneContext {

    private final String name;
    private final Scene scene;

    public SceneContext(String name, Parent parent) {
        this.name = name;
        this.scene = new Scene(parent);
    }


}