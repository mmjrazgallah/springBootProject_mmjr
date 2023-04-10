package com.sip.repositories;
import com.sip.entities.Provider;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sip.entities.Article;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article,Long> {
    @Query("FROM Article a WHERE a.provider.id = ?1")
    List<Article> findArticlesByProvider(long id);

    @Query("FROM Article a WHERE a.label LIKE %?1% OR a.description LIKE %?1% or a.provider.name LIKE %?1%")
    List<Article> findArticles(String mots);
}
