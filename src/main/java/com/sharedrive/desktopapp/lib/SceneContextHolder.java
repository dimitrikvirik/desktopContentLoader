package com.sharedrive.desktopapp.lib;


import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SceneContextHolder {

    private final Map<String, SceneContext> sceneContextList = new HashMap<>();
    public Stage stage;

    public SceneContext createScene(String name, Parent parent) {
        SceneContext sceneContext = new SceneContext(name, parent);
        sceneContextList.put(name, sceneContext);
        return sceneContext;
    }

    public SceneContext getSceneContext(String name) {
        return sceneContextList.get(name);
    }

    public void switchScene(String name) {
        stage.setScene(getSceneContext(name).getScene());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
