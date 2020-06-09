package com.summer.demo.module.album.util;

import java.io.Serializable;


public class ImageItem implements Serializable {
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    private boolean isSelected = false;
    private String videoPath;
    private int duration;
    boolean uploaded;

    private String createdTime;
    private int bepraiseCounts;
    private boolean hasThumb;
    private long id;
    private String img;
    private long userId;
    private int size;//图片或视频大小
    private String name;//媒体名称

    public ImageItem(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageItem(String img){
        this.imagePath = img;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getImagePath() {
        if(imagePath == null){
            return img;
        }
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public int getBepraiseCounts() {
        return bepraiseCounts;
    }

    public void setBepraiseCounts(int bepraiseCounts) {
        this.bepraiseCounts = bepraiseCounts;
    }

    public boolean isHasThumb() {
        return hasThumb;
    }

    public void setHasThumb(boolean hasThumb) {
        this.hasThumb = hasThumb;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
