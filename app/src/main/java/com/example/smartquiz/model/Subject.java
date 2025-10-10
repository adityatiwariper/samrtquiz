package com.example.smartquiz.model;

public   class Subject {
    private String id;
    private String name;
    private String icon;
    private int questionCount;

    public Subject(String id, String name, String icon, int questionCount) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.questionCount = questionCount;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getIcon() { return icon; }
    public int getQuestionCount() { return questionCount; }
}
