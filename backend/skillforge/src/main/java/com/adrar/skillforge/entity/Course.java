package com.adrar.skillforge.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courses_seq")
    @SequenceGenerator(
        name = "courses_seq",
        sequenceName = "courses_seq_id",
        allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Column(nullable = false)
    private String title;

    @Column(length = 4000)
    private String description;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<Module> modules = new ArrayList<>();

    protected Course() {
    }

    public Course(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @PrePersist
    public void prePersist() {
        if (this.publicId == null) this.publicId = UUID.randomUUID();
        if (this.createdAt == null) this.createdAt = Instant.now();
        if (this.modules == null) this.modules = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void addModule(Module module) {
        if (module == null) return;
        module.setCourse(this);
        this.modules.add(module);
    }

    public void removeModule(Module module) {
        if (module == null) return;
        this.modules.remove(module);
        module.setCourse(null);
    }
}
