package com.adrar.skillforge.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lessons_seq")
    @SequenceGenerator(
        name = "lessons_seq",
        sequenceName = "lessons_seq_id",
        allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer position;

    @Column(length = 10000)
    private String content;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Lesson() {
    }

    public Lesson(String title, Integer position, String content) {
        this.title = title;
        this.position = position;
        this.content = content;
    }

    @PrePersist
    public void prePersist() {
        if (this.publicId == null) this.publicId = UUID.randomUUID();
        if (this.createdAt == null) this.createdAt = Instant.now();
        if (this.position == null) this.position = 1;
    }

    public Long getId() {
        return id;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
