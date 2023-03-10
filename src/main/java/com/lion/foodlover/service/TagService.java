package com.lion.foodlover.service;

import com.lion.foodlover.dao.TagDao;
import com.lion.foodlover.entity.Post;
import com.lion.foodlover.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TagService {
    private final TagDao tagDao;

    @Autowired
    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public void appendTag(int tagId, int postId) {
        tagDao.addTag(tagId, postId);
    }

    public void removeTag(int tagId, int postId) {
        tagDao.removeTag(tagId, postId);
    }

    public Set<Post> getAllPosts(int tagId){
        return tagDao.getAllPostsByTagId(tagId);
    }

    public Set<Tag> getAllTags() {
        Set<Tag> tags = tagDao.getAllTags();
        for (Tag tag : tags) {
            tag.setPosts(null);
        }
        return tags;
    }
}
