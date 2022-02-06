package com.sharedrive.desktopapp.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public abstract class ContentLoader {

    private final String saveContentPath;
    private final MediaView videoContent;
    private final ImageView imageContent;
    private final int cacheLimit;

    private final SimpleIntegerProperty currentIndex = new SimpleIntegerProperty(0);
    private final Map<Integer, Content> cachedContent = new HashMap<>();

    private Content currentContent;

    public ContentLoader(String saveContentPath, MediaView videoContent, ImageView imageContent, Label contentIndex, int cacheLimit) {
        this.saveContentPath = saveContentPath;
        this.videoContent = videoContent;
        this.imageContent = imageContent;
        this.cacheLimit = cacheLimit;

        contentIndex.textProperty().bind(currentIndex.asString());
    }

    protected abstract Map<Integer, Content> getPageContent();

    protected void loadCache() {
        if (cachedContent.keySet().size() > cacheLimit) {
            cachedContent.clear();
        }

        if (!cachedContent.containsKey(getCurrentIndex())) {
            cachedContent.putAll(getPageContent());
        }
    }

    protected void loadContent() {


        currentContent = cachedContent.get(getCurrentIndex());

        videoContent.setVisible(false);
        if (videoContent.getMediaPlayer() != null) {
            videoContent.getMediaPlayer().stop();
            videoContent.setMediaPlayer(null);
        }

        imageContent.setVisible(false);
        imageContent.setImage(null);
        if (currentContent.getType().equals(ContentType.IMAGE)) {
            imageContent.setVisible(true);
            final Image image = new Image(currentContent.getUrl());
            imageContent.setImage(image);
        }
        if (currentContent.getType().equals(ContentType.VIDEO)) {
            videoContent.setVisible(true);
            final Media media = new Media(currentContent.getUrl());
            final MediaPlayer mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

            videoContent.setMediaPlayer(mediaPlayer);
        }
    }

    public void saveContent() {
        new Thread(() -> {
            try {
                String ext = currentContent.getUrl().substring(currentContent.getUrl().lastIndexOf(".")).split("\\?(?!\\?)")[0];
                URL url = new URL(currentContent.getUrl());
                try (InputStream in = url.openStream()) {
                    Files.copy(in, Paths.get(saveContentPath + currentContent.getName() + ext));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void nextContent() {
        currentIndex.set(currentIndex.get() + 1);
        loadContent();

    }

    public void prevContent() {
        if (currentIndex.get() > 0) {
            currentIndex.set(currentIndex.get() - 1);
            loadContent();
        }
    }

    public void jumpToContent(int index) {
        currentIndex.set(index);
        loadContent();
    }

    public int getCurrentIndex() {
        return currentIndex.get();
    }

    public Map<Integer, Content> getCachedContent() {
        return cachedContent;
    }

    public Content getCurrentContent() {
        return currentContent;
    }
}
