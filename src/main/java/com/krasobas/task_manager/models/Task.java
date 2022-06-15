package com.krasobas.task_manager.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.DataFormatException;

public class Task {
    private static int ID_GENERATOR = 0;
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, d MMMM 'at' HH:mm").withLocale(Locale.ENGLISH);
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");
    private LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    private int id;
    private boolean status = false;

    @NotEmpty(message = "Task title should not be empty.")
    @Size(min=2, max=50, message = "Task title should be between 2 and 50 characters.")
    private String title;

    @NotEmpty(message = "Task content should not be empty.")
    @Size(min=2, max=500, message = "Task content should be between 2 and 500 characters.")
    private String content;

    public Task() {
        this.id = ++ID_GENERATOR;
    }

    public Task(String title, String content) {
        this.title = title;
        this.content = content;
        this.id = ++ID_GENERATOR;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated() {
        StringBuilder date = new StringBuilder();
        if (created.toLocalDate().isEqual(LocalDate.now())) {
            date.append("Today at ").append(created.format(TIME_FORMATTER));
        } else if (created.toLocalDate().isEqual(LocalDate.now().minusDays(1))) {
            date.append("Yesterday at ").append(created.format(TIME_FORMATTER));
        } else {
            date.append(created.format(DATE_TIME_FORMATTER));
        }
        return date.toString();
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus() {
        status = !status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task item = (Task) o;
        return id == item.id && Objects.equals(title, item.title)
                            && Objects.equals(created, item.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, created);
    }

    @Override
    public String toString() {
        return String.format("id: %s, name: %s, created: %s", id, title, DATE_TIME_FORMATTER.format(created));
    }
}