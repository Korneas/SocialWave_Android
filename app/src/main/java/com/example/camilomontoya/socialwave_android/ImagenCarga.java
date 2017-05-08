package com.example.camilomontoya.socialwave_android;

/**
 * Created by CamiloMontoya on 7/05/17.
 */

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidObjectException;

public class ImagenCarga {

    static int MAX_WIDTH = 1080;
    static int MAX_HEIGHT = 1080;
    Uri uri;
    ContentResolver resolver;
    String path;
    Matrix orientation;
    int storedHeight;
    int storedWidth;

    public ImagenCarga(Uri uri, ContentResolver resolver) {
        this.uri = uri;
        this.resolver = resolver;
    }

    private boolean getInformation() throws IOException {
        if (getInformationFromMediaDatabase())
            return true;

        if (getInformationFromFileSystem())
            return true;

        return false;
    }

    /* Soporte para la galeria y picasa */
    private boolean getInformationFromMediaDatabase() {
        String[] fields = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
        Cursor cursor = resolver.query(uri, fields, null, null, null);

        if (cursor == null)
            return false;

        cursor.moveToFirst();
        path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
        this.orientation = new Matrix();
        this.orientation.setRotate(orientation);
        cursor.close();

        return true;
    }

    /* Soporte para archivos en la nube como Dropbxo */
    private boolean getInformationFromFileSystem() throws IOException {
        path = uri.getPath();

        if (path == null)
            return false;

        ExifInterface exif = new ExifInterface(path);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        this.orientation = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                /* Identity matrix */
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                this.orientation.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                this.orientation.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                this.orientation.setScale(1, -1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                this.orientation.setRotate(90);
                this.orientation.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                this.orientation.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                this.orientation.setRotate(-90);
                this.orientation.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                this.orientation.setRotate(-90);
                break;
        }

        return true;
    }

    private boolean getStoredDimensions() throws IOException {
        InputStream input = resolver.openInputStream(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(resolver.openInputStream(uri), null, options);
        input.close();

        if (options.outHeight <= 0 || options.outWidth <= 0)
            return false;

        storedHeight = options.outHeight;
        storedWidth = options.outWidth;

        return true;
    }

    public Bitmap getBitmap() throws IOException {
        if (!getInformation())
            throw new FileNotFoundException();

        if (!getStoredDimensions())
            throw new InvalidObjectException(null);

        RectF rect = new RectF(0, 0, storedWidth, storedHeight);
        orientation.mapRect(rect);
        float width = (float) rect.width();
        float height = (float) rect.height();
        int subSample = 1;

        while (width > MAX_WIDTH || height > MAX_HEIGHT) {
            width /= 1.5;
            height /= 1.5;
            subSample *= 2;
        }

        if (width == 0 || height == 0)
            throw new InvalidObjectException(null);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = subSample;
        Bitmap subSampled = BitmapFactory.decodeStream(resolver.openInputStream(uri), null, options);

        Bitmap picture;
        if (!orientation.isIdentity()) {
            picture = Bitmap.createBitmap(subSampled, 0, 0, options.outWidth, options.outHeight,
                    orientation, false);
            subSampled.recycle();
        } else
            picture = subSampled;

        return picture;
    }
}
