package com.fanzehao.blogsystem.Service;

import com.fanzehao.blogsystem.model.Article;
import com.fanzehao.blogsystem.model.Tag;
import com.fanzehao.blogsystem.repository.ArticleRepository;
import com.fanzehao.blogsystem.repository.TagRepository;
import com.fanzehao.blogsystem.response.PageResponse;
import com.fanzehao.blogsystem.response.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;


@Service
public class TagService {
    private static final Logger logger = LoggerFactory.getLogger(TagService.class);
    private final TagRepository tagRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Map<Long, Long> getTagCounts() {
        List<Object[]> objects = articleRepository.countByTags();
        Map<Long, Long> tagCounts = new HashMap<>();
        for (Object[] object : objects) {
            tagCounts.put((Long) object[0], (Long) object[1]);
        }
        return tagCounts;
    }
    public Result<?> getTagsByPage(String name, Integer page, Integer pageSize) {
        try {
            Pageable pageable = PageRequest.of(page - 1, pageSize);
            System.out.println(name);
            Page<Tag> paginatedTagsByName = tagRepository.findPaginatedTagsByName(name, pageable);
            PageResponse<Tag> tagPageResponse = new PageResponse<>(paginatedTagsByName.getContent(), paginatedTagsByName.getTotalElements());

            return Result.success(tagPageResponse);
        }
        catch (Exception e) {
            logger.error("获取标签失败", e);
            return Result.fail("获取标签失败");

        }
    }
    public Result<?> getAllTags() {
        try {
            List<Tag> tags = tagRepository.findAll();
            return Result.success(tags);
        }
        catch (Exception e) {
            logger.error("获取所有标签失败", e);
            return Result.fail("获取所有标签失败");
        }

    }
    public Tag findByName(String name) {
        // 查找标签没有也没事，目的是为了避免重复添加标签
        Optional<Tag> optionalTag = Optional.ofNullable(tagRepository.findByName(name));
        //存在就返回标签，不存在就返回null
        return optionalTag.orElse(null);

    }
    @Transactional
    public Tag saveTag(String tag) {
        try {
            Tag tagObj = new Tag();
            tagObj.setName(tag);
            return tagRepository.save(tagObj);
        }
        catch (Exception e)  {
            logger.error("保存标签失败", e);
            return null;
        }


    }
    @Transactional
    public Result<?> addTag(Tag tag) {
        try {
            return Result.success(tagRepository.save(tag));
        }
        catch (Exception e) {
            logger.error("添加标签失败", e);
            return Result.fail("添加标签失败");
        }
    }
    @Transactional
    public Result<?> updateTag(Long id, Tag tag) {
        String message = "更新标签失败";
        try {
            Optional<Tag> byIdTag = tagRepository.findById(id);
            if (!byIdTag.isPresent()) {
                return Result.fail("标签不存在");
            }
            Tag tag1 = byIdTag.get();
            tag1.setName(tag.getName());
            return Result.success(tagRepository.save(tag1));
        }
        catch (Exception e) {
            logger.error(message, e);
            return Result.fail(message);
        }
    }
    @Transactional
    public Result<?> deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("标签不存在"));

        // 获取所有关联该标签的文章
        Set<Article> articles = articleRepository.findByTagsSetContains(tag);

        // 从所有文章中移除该标签
        articles.forEach(article -> article.getTagsSet().remove(tag));
        articleRepository.saveAll(articles); // 更新文章关联

        // 现在可以安全删除标签
        tagRepository.delete(tag);
        return Result.success("删除标签成功");

    }

    @Transactional
    public Set<Tag> initTags(Set<Tag> tags,Object tagsObject) {

        //如果直接强制强化List<String>会有警告，所以需要判断一下
        if (tagsObject instanceof List<?>) {
            List<?> tagsList = (List<?>) tagsObject;
            for (Object tagObject : tagsList) {
                if (tagObject instanceof String) {
                    String tagName = (String) tagObject;
                    Tag tag = findByName(tagName);
                    if (tag == null) {
                        tag = saveTag(tagName);
                    }
                    tags.add(tag);
                }
            }
        }
        return tags;
    }


}
