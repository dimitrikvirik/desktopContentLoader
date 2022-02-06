package com.sharedrive.desktopapp;

import com.sharedrive.desktopapp.lib.FXMLScanner;
import com.sharedrive.desktopapp.lib.SceneContext;
import com.sharedrive.desktopapp.lib.SceneContextHolder;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StageInitializer implements ApplicationListener<ChartApplication.StageReadyEvent> {

    private final FXMLScanner fxmlScanner;
    private final SceneContextHolder sceneContextHolder;

    @Override
    public void onApplicationEvent(ChartApplication.StageReadyEvent event) {
        fxmlScanner.scan("/view");
        SceneContext sceneContext = sceneContextHolder.getSceneContext("main");
        sceneContextHolder.setStage(event.getStage());

        Stage stage = event.getStage();
        Scene scene = sceneContext.getScene();
        stage.setScene(scene);
        stage.setTitle("R34FX");
//        Image image = new Image(Objects.requireNonNull(StageInitializer.class.getResourceAsStream("/desktopApp/img/app-icon.png")));
//        stage.getIcons().add(image);
        stage.setResizable(false);

        stage.show();
    }


}
