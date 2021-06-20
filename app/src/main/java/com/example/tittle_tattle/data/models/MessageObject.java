package com.example.tittle_tattle.data.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "messages")
public class MessageObject {
    @PrimaryKey(autoGenerate = true)
    @NonNull
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
    public MessageObject(@NonNull Long source,
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

    public MessageObject(@NonNull Long source,
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable @org.jetbrains.annotations.Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof MessageObject)) {
            return false;
        }

        MessageObject message = (MessageObject) obj;

        if ((!message.getSource().equals(this.source))
                || (!message.getContent().equals(this.content))
                || (!message.getTimestamp().equals(this.timestamp))
                || (!message.getTopic1().equals(this.topic1))) {
            return false;
        }

        if (message.getTopic2() == null) {
            if (this.topic2 != null) {
                return false;
            }
        } else {
            if (this.topic2 == null) {
                return false;
            }
        }

        if (message.getTopic3() == null) {
            return this.topic3 == null;
        } else {
            return this.topic3 != null;
        }
    }
}
