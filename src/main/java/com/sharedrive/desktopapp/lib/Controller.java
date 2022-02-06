package com.sharedrive.desktopapp.lib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public abstract class Controller {
    protected SceneContext sceneContext;
    @Autowired
    protected  SceneContextHolder sceneContextHolder;

    public void setSceneContext(SceneContext sceneContext) {
        this.sceneContext = sceneContext;
    }
}
