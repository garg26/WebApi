package com.neostencil.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ns_user_examsegment")
public class UserExamSegment implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_examsegment_seq")
    @SequenceGenerator(name = "user_examsegment_seq", sequenceName = "user_examsegment_seq", allocationSize = 1)
    @Field(name = "userExamSegmentValueId", index = Index.YES, analyze = Analyze.YES, store = Store.YES)
    private int id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "exam_category")
    private String examCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getExamCategory() {
        return examCategory;
    }

    public void setExamCategory(String examCategory) {
        this.examCategory = examCategory;
    }

}
