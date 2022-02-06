package com.sharedrive.desktopapp.controller;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Rule34ContentLoader extends ContentLoader {

    private static final String RULE34_URL = "https://rule34.xxx/";

    private static final int PAGE_SIZE = 42;

    private final List<String> tags;

    public Rule34ContentLoader(String saveContentPath, MediaView videoContent, ImageView imageContent, Label contentIndex, int cacheLimit, List<String> tags) {
        super(saveContentPath, videoContent, imageContent, contentIndex, cacheLimit);
        this.tags = tags;
    }

    private final Map<Integer, Element> elementMap = new HashMap<>();


    private int getPid(int page) {
        return (page - 1) * PAGE_SIZE;
    }

    private void loadContent(Element element, Content content) {
        final String href = element.attr("href");
        content.setSource(RULE34_URL + href);
        try {
            final Document document = Jsoup.connect(content.getSource()).get();
            Element image = document.selectFirst("#image");
            if (image != null) {
                content.setType(ContentType.IMAGE);

                content.setUrl(image.attr("src"));
            } else {
                Element video = document.selectFirst("#gelcomVideoContainer");
                if (video == null) {
                    content.setType(ContentType.UNKNOWN);
                } else {
                    final String attr = Objects.requireNonNull(video.selectFirst("source")).attr("src");
                    content.setType(ContentType.VIDEO);
                    content.setUrl(attr);
                }
            }
        } catch (IOException e) {

            throw new RuntimeException(e.getMessage() + " " + href);
        }
    }


    //lazy loading
    private Map<Integer, Content> getPageContent(int index) {
        try {
            int currentPage = index / PAGE_SIZE + 1;

            String url = RULE34_URL + "/index.php?page=post&s=list&tags=" + String.join("%20", tags) + "&pid=" + getPid(currentPage);
            final Document document = Jsoup.connect(url).get();
            final Elements elements = document.select("a[href*=\"s=view\"]");
            final int pid = getPid(currentPage);
            elementMap.putAll(IntStream.range(pid, getPid(currentPage + 1))
                    .boxed()
                    .collect(Collectors.toMap(i -> i, i -> elements.get(i - pid))));


            return IntStream.range(pid, getPid(currentPage + 1))
                    .boxed()
                    .collect(Collectors.toMap(i -> i, i -> new Content()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected Map<Integer, Content> getPageContent() {
        return getPageContent(getCurrentIndex());
    }

    @Override
    protected void loadContent() {
        loadCache();
        loadContent(elementMap.get(getCurrentIndex()), getCachedContent().get(getCurrentIndex()));
        super.loadContent();
    }


}
