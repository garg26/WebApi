package com.neostencil.model.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.neostencil.common.enums.PostCategory;
import com.neostencil.common.enums.PostSubCategory;
import com.neostencil.common.enums.PublishStatus;
import com.neostencil.model.entities.Post;

public interface PostRepository extends JpaRepository<Post, String> {

  Post findByPostId(int id);

  Post findByPostSlug(String postSlug);

  @Query("select p from Post p where p.featured = 'true' and p.status like 'publish' order by p.updatedAt desc")
  List<Post> fetchFeaturedPosts();

  @Query("select p from Post p where p.popular = 'true' and p.status like 'publish' order by p.updatedAt desc")
  List<Post> fetchPopularPosts();

  @Query("select p from Post p where p.featured= 'true' and p.videoUrl is not null and p.status like 'publish' order by p.updatedAt desc")
  List<Post> fetchFeaturedVideoBlogs();

  Page<Post> findByStatusOrderByUpdatedAtDesc(PublishStatus status, Pageable pageable);

  Page<Post> findByStatusOrderByCreatedAtDesc(PublishStatus status, Pageable pageable);

  List<Post> findByStatus(PublishStatus status);

  List<Post> findByCategory(PostCategory category);

  List<Post> findByPostSubCategory(PostSubCategory subCategory);

  @Query("select p from Post p where p.type like 'FREE_PREP' order by p.updatedAt")
  List<Post> fetchFreePrep();

  @Query("select p from Post p where lower(p.tags) like :name and p.status = 'publish' order by p.createdAt desc")
  Page<Post> findOtherTagsByTag(@Param("name") String name, Pageable pageable);

  @Query("select p.tags from Post p where lower(p.tags) like :name and p.status='publish'")
  List<String> findOtherTagsByTag(@Param("name") String name);

}

