package com.neostencil.rss;

import com.neostencil.base.BaseService;
import com.neostencil.model.entities.Post;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Content;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

@Component
public class RssFeedView extends AbstractRssFeedView {


  @Autowired
  private BaseService baseService;

  @Value("${neostencil.website.url}")
  private String websiteHomePageURL;

  @Override
  protected void buildFeedMetadata(Map<String, Object> model,
      Channel feed, HttpServletRequest request) {
    feed.setTitle("NeoStencil RSS Feed");
    feed.setDescription(" Prepare from India's best Coaching Institutes Live Online ");
    feed.setLink("https://neostencil.com/blog");
  }

  @Override
  protected List<Item> buildFeedItems(Map<String, Object> model,
      HttpServletRequest request, HttpServletResponse response) {

    Page<Post> posts = baseService.fetchPublishedPosts(0, 100, null, null);
    List<Item> items = new ArrayList<>();
    for(Post p: posts)
    {

      Item feedItem = new Item();
      feedItem.setTitle(p.getTitle());
//      feedItem.setAuthor(p.getCreatedBy());
      feedItem.setLink(websiteHomePageURL + "/" +  p.getPostSlug());
      feedItem.setPubDate(p.getUpdatedAt());

      Description d = new Description();
      d.setValue(p.getText().substring(0,500));
      feedItem.setDescription(d);
    //  Content c = new Content();
  //    c.setValue(p.getText());
//      feedItem.setContent(c);
      items.add(feedItem);
    }

    return items;
  }
}
