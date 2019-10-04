package com.example.demo.repo;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, orphanRemoval = true)
    Set<Channel> channel;

    @CreationTimestamp
    Timestamp createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Channel> getChannel() {
        return channel;
    }

    public void setChannel(Set<Channel> channel) {
        this.channel = channel;
    }
}
