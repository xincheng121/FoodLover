package com.lion.foodlover.Controller;

import com.lion.foodlover.entity.Tag;
import com.lion.foodlover.service.PostService;
import com.lion.foodlover.service.TagService;
import com.lion.foodlover.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
@Controller
public class TagController {
    private final PostService postService;
    private final TagService tagService;

    private final TokenService tokenService;

    @Autowired
    public TagController(PostService postService, TagService tagService, TokenService tokenService) {
        this.postService = postService;
        this.tagService = tagService;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = {"/post/{postID}/addTag/{tagID}"}, method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void addTag(@PathVariable("postID") int postId, @PathVariable("tagID") int tagId,
                       @RequestHeader("Authorization") String token, HttpServletResponse response) {
        if (!tokenService.verify(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        String username = tokenService.getUsernameFromToken(token);
        if (!postService.verifyPost(postId, username)){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        tagService.appendTag(tagId, postId);
    }

    @RequestMapping(value = {"/post/{postID}/removeTag/{tagID}"}, method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void removeTag(@PathVariable("postID") int postId, @PathVariable("tagID") int tagId,
                          @RequestHeader("Authorization") String token, HttpServletResponse response) {
        if (!tokenService.verify(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        String username = tokenService.getUsernameFromToken(token);
        if (!postService.verifyPost(postId, username)){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        tagService.removeTag(tagId, postId);
    }

    @RequestMapping(value = {"/tags"}, method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Set<Tag> getAllTags() {
        return tagService.getAllTags();
    }
}
