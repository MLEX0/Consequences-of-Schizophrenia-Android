package com.example.yarizhizaremake.model;
//Класс, по которому строиться массив Лора в игре


public class LoreClass {
    public String TextDialog;
    public int PageNumber;
    public String PagePath;
    public String PersonageName;

    public LoreClass(String textDialog, int pageNumber, String pagePath, String personageName) {
        TextDialog = textDialog;
        PageNumber = pageNumber;
        PagePath = pagePath;
        PersonageName = personageName;
    }

    public String getTextDialog() {
        return TextDialog;
    }

    public void setTextDialog(String textDialog) {
        TextDialog = textDialog;
    }

    public int getPageNumber() {
        return PageNumber;
    }

    public void setPageNumber(int pageNumber) {
        PageNumber = pageNumber;
    }

    public String getPagePath() {
        return PagePath;
    }

    public void setPagePath(String pagePath) {
        PagePath = pagePath;
    }

    public String getPersonageName() {
        return PersonageName;
    }

    public void setPersonageName(String personageName) {
        PersonageName = personageName;
    }
}

