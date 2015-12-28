package com.bufferworks.simpletodo.entity;

import com.orm.SugarRecord;

import java.util.Date;

public class Item extends SugarRecord<Item> {

    private String title;
    private String notes;
    private Priority priority = Priority.LOW;
    private Status status = Status.TODO;
    private Date created;

    public Item() {

    }

    public Item(final String title, final String notes, final Priority priority, final Status status) {
        this.title = title;
        this.notes = notes;
        this.priority = priority;
        this.status = status;
        created = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
