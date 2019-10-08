package com.summer.demo.module.album.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import com.summer.helper.utils.Logs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class AlbumHelper {
    final String TAG = getClass().getSimpleName();
    Context context;
    ContentResolver cr;
    public static int MAX_SELECT = 0;

    HashMap<String, String> thumbnailList = new HashMap<>();

    HashMap<String, ImageBucket> bucketList = new HashMap<String, ImageBucket>();

    private static AlbumHelper instance;

    private AlbumHelper() {
    }

    public static AlbumHelper getHelper() {
        if (instance == null) {
            instance = new AlbumHelper();
        }
        return instance;
    }

    public void init(Context context) {
        if (this.context == null) {
            this.context = context;
            cr = context.getContentResolver();
        }
    }

    private void getThumbnail() {
        String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID,
                Thumbnails.DATA};
        Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
                null, null, null);
        getThumbnailColumnData(cursor);
    }

    private void getThumbnailColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int _id;
            int image_id;
            String image_path;
            int _idColumn = cur.getColumnIndex(Thumbnails._ID);
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

            do {
                // Get the field values
                _id = cur.getInt(_idColumn);
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
                thumbnailList.put("" + image_id, image_path);
            } while (cur.moveToNext());
        }
    }

    boolean hasBuildImagesBucketList = false;
    private static final String IS_LARGE_SIZE = " _size > ? or _size is null";
    private static final String BUCKET_GROUP_BY = ") GROUP BY  1,(2";
    void buildImagesBucketList() {
        long startTime = System.currentTimeMillis();
        getThumbnail();

        Logs.i("xia", "time:" + (System.currentTimeMillis() - startTime));

        String columns[] = new String[]{Media._ID, Media.BUCKET_ID, Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE, Media.SIZE, Media.BUCKET_DISPLAY_NAME};
        Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, IS_LARGE_SIZE + BUCKET_GROUP_BY,
                new String[]{"0"}, Media._ID + " desc");
        if (cur != null && cur.moveToFirst()) {
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
            int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
            int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
            int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
            int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
            int totalNum = cur.getCount();

            do {
                String _id = cur.getString(photoIDIndex);
                String name = cur.getString(photoNameIndex);
                String path = cur.getString(photoPathIndex);
                String title = cur.getString(photoTitleIndex);
                String size = cur.getString(photoSizeIndex);
                String bucketName = cur.getString(bucketDisplayNameIndex);
                String bucketId = cur.getString(bucketIdIndex);
                String picasaId = cur.getString(picasaIdIndex);

/*                Logs.i("xia", _id + ", bucketId: " + bucketId + ", picasaId: "
                        + picasaId + " name:" + name + " path:" + path
                        + " title: " + title + " size: " + size + " bucket: "
                        + bucketName + "---");*/

                ImageBucket bucket = bucketList.get(bucketId);
                if (bucket == null) {
                    bucket = new ImageBucket();
                    bucketList.put(bucketId, bucket);
                    bucket.imageList = new ArrayList<ImageItem>();
                    bucket.bucketName = bucketName;
                }
                bucket.count++;
                ImageItem imageItem = new ImageItem();
                imageItem.imageId = _id;
                imageItem.imagePath = path;
                imageItem.thumbnailPath = thumbnailList.get(_id);
                if (imageItem.thumbnailPath == null) {
                    imageItem.thumbnailPath = imageItem.imagePath;
                }
                bucket.imageList.add(imageItem);

            } while (cur.moveToNext());
        }

        hasBuildImagesBucketList = true;
    }


    public List<ImageBucket> getImagesBucketList(boolean refresh) {
        if (refresh || !hasBuildImagesBucketList) {
            buildImagesBucketList();
        }
        List<ImageBucket> tmpList = new ArrayList<ImageBucket>();
        Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, ImageBucket> entry = itr.next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }

}
