package com.neostencil.model.repositories;

import com.neostencil.model.entities.NewsletterSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsletterSubscriberRepository extends JpaRepository<NewsletterSubscriber,Integer> {

  NewsletterSubscriber findByEmailId(String emailId);

}
