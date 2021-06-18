package com.example.tittle_tattle.data.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "messages")
public class Message {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @NonNull
    private final Long source;

    @NonNull
    private final String content;

    @NonNull
    private final Integer topic1;

    private final Integer topic2;

    private final Integer topic3;

    @NonNull
    private final Long timestamp;

    @Ignore
    public Message(@NonNull Long source,
                   @NonNull String content,
                   @NonNull Integer topic1,
                   @Nullable Integer topic2,
                   @Nullable Integer topic3) {
        this.content = content;
        this.source = source;
        this.topic1 = topic1;
        this.topic2 = topic2;
        this.topic3 = topic3;
        this.timestamp = System.currentTimeMillis();
    }

    public Message(@NonNull Long source,
                   @NonNull String content,
                   @NonNull Integer topic1,
                   @Nullable Integer topic2,
                   @Nullable Integer topic3,
                   @NonNull Long timestamp) {
        this.content = content;
        this.source = source;
        this.topic1 = topic1;
        this.topic2 = topic2;
        this.topic3 = topic3;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @NonNull
    public Long getSource() {
        return source;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    @NonNull
    public Integer getTopic1() {
        return topic1;
    }

    public Integer getTopic2() {
        return topic2;
    }

    public Integer getTopic3() {
        return topic3;
    }

    @NotNull
    public Long getTimestamp() {
        return timestamp;
    }
}
