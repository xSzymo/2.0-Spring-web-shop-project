package com.shop.others;

import com.shop.configuration.ApplicationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ShowPictures {

    @RequestMapping(value = "/getImage/{imageName}", method = RequestMethod.GET)
    @ResponseBody
    public byte[] getImage(@PathVariable String imageName, HttpServletRequest request) throws IOException {
        String rpath;
        rpath = ApplicationProperties.PICTURE_PATH + imageName;
        Path path = Paths.get(rpath);
        byte[] data = Files.readAllBytes(path);
        return data;
    }
}
