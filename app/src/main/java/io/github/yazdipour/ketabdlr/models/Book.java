package io.github.yazdipour.ketabdlr.models;

import android.annotation.SuppressLint;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.github.yazdipour.ketabdlr.services.Api;

public class Book {

    @SerializedName("id")
    @Expose
    private Integer Id = 0;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("time-to-read")
    @Expose
    private String timeToRead;
    @SerializedName("file-path")
    @Expose
    private String filePath;
    @SerializedName("cookie")
    @Expose
    private String cookie;
    @SerializedName("sha1")
    @Expose
    private String sha1;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getTimeToRead() {
        return timeToRead;
    }

    public void setTimeToRead(String timeToRead) {
        this.timeToRead = timeToRead;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return Api.BASE_URL + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover != null && cover.charAt(0) == '/' ? "http:" + cover : cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDownloadUrl() {
        return "https://ketab.io/book/get/" + getId();
    }

    public String getAmazonUrl() {
        return "https://www.amazon.com/s?k=";
    }

    @SuppressLint("DefaultLocale")
    public String getFileName() {
        try {
            return String.format("%s.%s", getName().replaceAll("\\W+", "_"), getType());
        } catch (Exception e) {
            return String.format("%d.%s", System.currentTimeMillis(), getType());
        }
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}