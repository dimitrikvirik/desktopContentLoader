package com.sharedrive.desktopapp.lib;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FXMLScanner {



    public  void scan(String rootPath) {

        List<File> files = new ArrayList<>();
        listf(FXMLScanner.class.getResource(rootPath).getPath(), files);
        for (File file : files) {
            try {
                SceneBuilder.build(file);
            }catch (MalformedURLException e){
                return;
            }
        }
    }
    private  void listf(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile() && file.getAbsolutePath().endsWith(".fxml")) {
                    files.add(file);

                } else if (file.isDirectory()) {
                    listf(file.getAbsolutePath(), files);
                }
            }
    }


}
