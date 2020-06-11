package com.summer.demo.module.album.bean;

import com.summer.demo.module.album.listener.AlbumCallback;
import com.summer.demo.module.album.util.ImageItem;

import java.util.ArrayList;
import java.util.List;

public final class SelectOptions {
    private boolean isCrop;
    private int mCropWidth, mCropHeight;
    private AlbumCallback mCallback;
    private boolean hasCam;
    private int mSelectCount;
    private List<ImageItem> mSelectedImages;
    private boolean isVideoMode;
    private SelectAlumbType selectAlumbType;
    private SelectOptions() {

    }

    public SelectAlumbType getSelectAlumbType() {
        return selectAlumbType;
    }

    public void setSelectAlumbType(SelectAlumbType selectAlumbType) {
        this.selectAlumbType = selectAlumbType;
    }

    public boolean isVideoMode() {
        return isVideoMode;
    }
    public boolean isCrop() {
        return isCrop;
    }

    public int getCropWidth() {
        return mCropWidth;
    }

    public int getCropHeight() {
        return mCropHeight;
    }

    public AlbumCallback getCallback() {
        return mCallback;
    }

    public boolean isHasCam() {
        return hasCam;
    }

    public int getSelectCount() {
        return mSelectCount;
    }

    public List<ImageItem> getSelectedImages() {
        return mSelectedImages;
    }

    public static class Builder {
        private boolean isCrop;
        private int cropWidth, cropHeight;
        private AlbumCallback callback;
        private boolean hasCam;
        private boolean isVideoMode;
        private int selectCount;
        private List<ImageItem> selectedImages;
        private SelectAlumbType selectAlumbType;

        public Builder() {
            selectCount = 1;
            hasCam = true;
            selectedImages = new ArrayList<>();
        }

        public Builder setCrop(int cropWidth, int cropHeight) {
            if (cropWidth <= 0 || cropHeight <= 0)
                throw new IllegalArgumentException("cropWidth or cropHeight mast be greater than 0 ");
            this.isCrop = true;
            this.cropWidth = cropWidth;
            this.cropHeight = cropHeight;
            return this;
        }

        public Builder setCallback(AlbumCallback callback) {
            this.callback = callback;
            return this;
        }

        public Builder setVideoMode(boolean isVideo){
            this.isVideoMode = isVideo;
            return this;
        }

        public Builder setSlectAlbumType(SelectAlumbType type){
            this.selectAlumbType = type;
            return this;
        }


        public boolean isVideoMode() {
            return isVideoMode;
        }

        public Builder setHasCam(boolean hasCam) {
            this.hasCam = hasCam;
            return this;
        }

        public Builder setSelectCount(int selectCount) {
            if (selectCount <= 0)
                throw new IllegalArgumentException("selectCount mast be greater than 0 ");
            this.selectCount = selectCount;
            return this;
        }

        public Builder setSelectedImages(List<ImageItem> selectedImages) {
            if (selectedImages == null || selectedImages.size() == 0) return this;
            this.selectedImages.addAll(selectedImages);
            return this;
        }

        public SelectOptions build() {
            SelectOptions options = new SelectOptions();
            options.hasCam = hasCam;
            options.isCrop = isCrop;
            options.isVideoMode = isVideoMode;
            options.selectAlumbType = selectAlumbType;
            options.mCropHeight = cropHeight;
            options.mCropWidth = cropWidth;
            options.mCallback = callback;
            options.mSelectCount = selectCount;
            options.mSelectedImages = selectedImages;
            return options;
        }
    }



}
