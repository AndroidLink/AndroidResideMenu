package com.special.ResideMenuDemo;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by yangfeng on 14-7-23.
 */
public class VolleyImageUtils {
    private static final int CACHE_SIZE_DEFAULT = 10 * 1024 * 1024;

    private static int _cache_size;
    static {
        _cache_size = CACHE_SIZE_DEFAULT;
    }
    public static void setImageCacheSize(int size) {
        _cache_size = size;
    }

    private VolleyImageUtils() {
        // no instance
    }

    // volley code begin
    public static void bindUrlToImageView(String url, ImageView imageView) {
        bindUrlToImageView(url, imageView, 0, 0);
    }

    public static void bindUrlToImageView(String url, ImageView imageView, int defaultResId, int errorResId) {
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, defaultResId, errorResId);
        new ImageLoader(Volley.newRequestQueue(imageView.getContext().getApplicationContext()),
                new BitmapCache()).get(url, listener);
    }

    private static class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> mCache;
        private BitmapCache() {
            mCache = new LruCache<String, Bitmap>(_cache_size) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }
    }

    // volley code end
}
