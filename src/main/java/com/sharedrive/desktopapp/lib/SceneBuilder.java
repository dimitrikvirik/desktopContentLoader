package com.sharedrive.desktopapp.lib;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SceneBuilder {

    public static void build(File file) throws MalformedURLException {
        URL resource = file.toURI().toURL();

        FXMLLoader  fxmlLoader = loadSynchronously(resource);
        ThreadLocal<Object> presenterProperty =  new ThreadLocal<>();
        Controller controller = fxmlLoader.getController();
        presenterProperty.set(controller);

        Parent parent = fxmlLoader.getRoot();
        SceneContextHolder sceneContextHolder = BeanContext.getBean(SceneContextHolder.class);
        String str = file.getName();
        String fileName = str.substring(0, str.lastIndexOf("."));
        SceneContext scene = sceneContextHolder.createScene(fileName, parent);
        controller.setSceneContext(scene);

    }

    private static Object createControllerForType(Class<?> type) {
        return BeanContext.getBean(type);
    }

    private static FXMLLoader loadSynchronously(URL resource) throws IllegalStateException {

        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(SceneBuilder::createControllerForType);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot load Resource!", ex);
        }
        return loader;
    }


}
