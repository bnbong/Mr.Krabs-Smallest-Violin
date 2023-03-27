package com.bnbong.mrkrabsmallestviolin.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class MusicController {

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // read and save file
            byte[] bytes = file.getBytes();
            Path path = Paths.get(file.getOriginalFilename());
            Files.write(path, bytes);

            // // applying violin filter
            // InputStream inputStream = new FileInputStream(path.toString());
            // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // byte[] buffer = new byte[1024];
            // int bytesRead;
            // while ((bytesRead = inputStream.read(buffer)) != -1) {
            //     for (int i = 0; i < bytesRead; i += 2) {
            //         buffer[i] = (byte) Math.min(127, Math.max(-128, buffer[i] * (1 - (i * 1.0 / bytesRead))));
            //     }
            //     byteArrayOutputStream.write(buffer, 0, bytesRead);
            // }
            // inputStream.close();
            
            // // save file
            // Files.write(path, byteArrayOutputStream.toByteArray());

            // play uploaded music
            return "<audio src=\"/play/" + file.getOriginalFilename() + "\" controls>";

        } catch (IOException e) {
            e.printStackTrace();
            return "failed to upload mp3 file.";
        }
    }

    @GetMapping("/play/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Path path = Paths.get(filename);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resource);
    }

}
