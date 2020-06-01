package com.groupchatback.controller;

import com.groupchatback.pojo.request.Request;
import com.groupchatback.pojo.response.Response;
import com.groupchatback.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/profilepic")
    public Response getProfilePic() throws Exception {
        byte[] byteArr = this.imageService.getProfilePic();
        String mimeType = this.imageService.getProfilePicMimeType();

        Response response = new Response();
        response.add("byteArrBase64", byteArr);
        response.add("mimeType", mimeType);

        return response;
    }

    @PostMapping("/profilepic")
    public ResponseEntity<?> postProfilePic(@NonNull @RequestBody Request request) throws Exception {
        String base64Str = (String)request.get("byteArrBase64");
        String mimeType = (String)request.get("mimeType");

        this.imageService.postProfilePic(base64Str, mimeType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/profilepic")
    public ResponseEntity<?> deleteProfilePic() throws Exception {
        this.imageService.deleteProfilePic();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/defaultpic")
    public Response getDefaultPic() throws Exception {
        byte[] byteArr = this.imageService.getDefaultPic();
        String mimeType = this.imageService.getDefaultPicMimeType();

        Response response = new Response();
        response.add("byteArrBase64", byteArr);
        response.add("mimeType", mimeType);

        return response;
    }

    @GetMapping("/hasprofilepic")
    public Response hasProfilePic() {
        boolean hasProfilePic = this.imageService.hasProfilePic();

        Response response = new Response();
        response.add("hasProfilePic", hasProfilePic);

        return response;
    }

    @GetMapping("/icons")
    public Response getIcons() throws Exception {
        Map<String, byte[]> iconLookup = this.imageService.getIcons();
        String mimeType = this.imageService.getIconMimeType();

        Response response = new Response();
        response.add("byteArrBase64s", iconLookup);
        response.add("mimeType", mimeType);

        return response;
    }

}
