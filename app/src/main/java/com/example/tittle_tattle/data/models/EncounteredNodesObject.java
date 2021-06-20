package com.example.tittle_tattle.data.models;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "encountered_nodes")
public class EncounteredNodesObject {
    @PrimaryKey
    @NonNull
    private final Long userId;

    @Embedded
    @NonNull
    private ContactInfo contactInfo;

    public EncounteredNodesObject(@NotNull Long userId, @NotNull ContactInfo contactInfo) {
        this.userId = userId;
        this.contactInfo = contactInfo;
    }

    @NotNull
    public Long getUserId() {
        return userId;
    }

    @NotNull
    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(@NotNull ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }
}
