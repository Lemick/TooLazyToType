package com.toolazytotype.quid.dto;

public class QuidAnswerDTO {
    private String label;
    private String title;

    public QuidAnswerDTO(String label, String title) {
        this.label = label;
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
