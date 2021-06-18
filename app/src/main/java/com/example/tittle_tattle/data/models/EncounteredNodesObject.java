package com.example.tittle_tattle.data.models;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "encountered_nodes")
public class EncounteredNodesObject {
    @PrimaryKey
    private final Long userId;

    @Embedded private ContactInfo contactInfo;

    public EncounteredNodesObject(Long userId, ContactInfo contactInfo) {
        this.userId = userId;
        this.contactInfo = contactInfo;
    }

    public Long getUserId() {
        return userId;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }
}
