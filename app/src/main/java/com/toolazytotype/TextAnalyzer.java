package com.toolazytotype;

public abstract class TextAnalyzer {

    protected Action action;

    public TextAnalyzer(Action action) {
        this.action = action;
    }

    public abstract void analyze(String text);

}
