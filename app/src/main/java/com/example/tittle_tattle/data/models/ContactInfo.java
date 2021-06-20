package com.example.tittle_tattle.data.models;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

public class ContactInfo {
    @NonNull
    private int contacts;
    @NonNull
    private long duration;
    @NonNull
    private long lastEncounterTime;

    @Ignore
    public ContactInfo(long lastEncounterTime) {
        this.contacts = 1;
        this.duration = 0;
        this.lastEncounterTime = lastEncounterTime;
    }

    public ContactInfo(int contacts, long duration, long lastEncounterTime) {
        this.contacts = contacts;
        this.duration = duration;
        this.lastEncounterTime = lastEncounterTime;
    }

    public int getContacts() {
        return contacts;
    }

    public void incrementContacts() {
        this.contacts = this.contacts + 1;
    }

    public void setContacts(int contacts) {
        this.contacts = contacts;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getLastEncounterTime() {
        return lastEncounterTime;
    }

    public void setLastEncounterTime(long lastEncounterTime) {
        this.lastEncounterTime = lastEncounterTime;
    }
}
