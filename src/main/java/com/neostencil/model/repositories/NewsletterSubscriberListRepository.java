package com.neostencil.model.repositories;

import com.neostencil.model.entities.NewsletterSubscriberList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsletterSubscriberListRepository extends JpaRepository<NewsletterSubscriberList,Integer> {

  NewsletterSubscriberList findByName(String name);
}
