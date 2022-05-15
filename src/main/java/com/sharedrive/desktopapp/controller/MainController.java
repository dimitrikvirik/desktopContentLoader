package com.sharedrive.desktopapp.controller;

import com.sharedrive.desktopapp.ChartApplication;
import com.sharedrive.desktopapp.lib.BeanContext;
import com.sharedrive.desktopapp.lib.Controller;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MainController extends Controller {


    public Button nextContentButton;
    public Button prevContentButton;
    public Label contentIndex;
    public TextField contentIndexSelect;
    public TextField contentTag;
    public Button saveContentButton;

    public MediaView contentVideo;
    public ImageView contentImage;
    public Button sourceButton;

    private ContentLoader contentLoader;
    private HostServices hostServices;


    @FXML
    public void initialize() throws IOException {
        contentLoader = new Rule34ContentLoader("output/", contentVideo, contentImage, contentIndex, 420, Collections.emptyList());
        Hyperlink hyperlink = new Hyperlink("https://rule34.xxx/index.php?page=post&s=list&tags=%2Brating%3Aexplicit");
        hostServices = BeanContext.getBean(ChartApplication.class).getHostServices();
    }

    public void selectTag(KeyEvent keyEvent) throws IOException {
        final List<String> tags = new ArrayList<>(Arrays.stream(contentTag.getCharacters().toString().split(" ")).toList());

        String blacklist= "-comic -furry -fur -my_little_pony -hyper_penis -horse -censored -horse_penis -extra_penises -roblox -pregnant -smelly -obese -eating -fat -burp -boxers -shitting -soiling -anthro -penis_piercing -muscular -fart -multi_penis -gigantic_breasts -muscular_futanari -huge_balls -peeing -wide_hips";
        tags.addAll(Arrays.stream(blacklist.split(" ")).toList());

        contentLoader = new Rule34ContentLoader("output/", contentVideo, contentImage, contentIndex, 420, tags);
    }


    public void nextContent(MouseEvent mouseEvent) {
        contentLoader.nextContent();
    }

    public void prevContent(MouseEvent mouseEvent) {
        contentLoader.prevContent();
    }


    public void jumpContent(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            contentLoader.jumpToContent(Integer.parseInt(contentIndexSelect.getCharacters().toString()));
        }
    }


    public void saveContent(MouseEvent mouseEvent) {
        contentLoader.saveContent();
    }

    public void getSource(MouseEvent mouseEvent) {
        hostServices.showDocument(contentLoader.getCurrentContent().getSource());
    }
}
