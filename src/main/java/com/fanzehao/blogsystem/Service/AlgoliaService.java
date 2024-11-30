package com.fanzehao.blogsystem.Service;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;

import com.fanzehao.blogsystem.model.AlgoliaRecord;
import com.fanzehao.blogsystem.model.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AlgoliaService {
    private final SearchClient client;
    private final String indexName = "articles";

    public AlgoliaService(
            @Value("${algolia.application-id}") String applicationId,
            @Value("${algolia.api-key}") String apiKey
    ) {
        this.client = DefaultSearchClient.create(applicationId, apiKey);
    }

    public void saveArticle(Article article) {
        try {
            SearchIndex<AlgoliaRecord> index = client.initIndex(indexName, AlgoliaRecord.class);
            AlgoliaRecord record = AlgoliaRecord.fromArticle(article);
            index.saveObject(record).waitTask();
            log.info("Article indexed in Algolia: {}", article.getId());
        } catch (Exception e) {
            log.error("Error indexing article: {}", e.getMessage());
        }
    }

    public void deleteArticle(Long id) {
        try {
            SearchIndex<AlgoliaRecord> index = client.initIndex(indexName, AlgoliaRecord.class);
            index.deleteObject(id.toString()).waitTask();
            log.info("Article deleted from Algolia: {}", id);
        } catch (Exception e) {
            log.error("Error deleting article: {}", e.getMessage());
        }
    }

    public void reindexAll(List<Article> articles) {
        try {
            SearchIndex<AlgoliaRecord> index = client.initIndex(indexName, AlgoliaRecord.class);
            List<AlgoliaRecord> records = articles.stream()
                    .map(AlgoliaRecord::fromArticle)
                    .collect(Collectors.toList());
            index.saveObjects(records).waitTask();
            log.info("Reindexed {} articles", articles.size());
        } catch (Exception e) {
            log.error("Error reindexing articles: {}", e.getMessage());
        }
    }
}