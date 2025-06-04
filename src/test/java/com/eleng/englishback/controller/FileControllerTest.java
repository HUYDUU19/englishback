package com.eleng.englishback.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetImageWithPngExtension() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/images/test.png"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/png"));
    }

    @Test
    public void testGetImageWithJpgExtension() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/images/test.jpg"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/png"));
    }

    @Test
    public void testGetImageWithJpegExtension() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/images/test.jpeg"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/png"));
    }

    @Test
    public void testGetImageWithGifExtension() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/images/test.gif"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/gif"));
    }

    @Test
    public void testGetImageWithInvalidExtension() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/images/test.invalid"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/jpeg")); // default content type
    }

    @Test
    public void testGetNonExistentImage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/images/nonexistent.jpg"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAudioFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/audio/test.mp3"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "audio/mpeg"));
    }

    @Test
    public void testGetNonExistentAudio() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/audio/a1.mp3"))
                .andExpect(status().isNotFound());
    }
}
