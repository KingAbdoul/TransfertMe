package com.example.transfertme;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

public class ImageUtils {

    public static Bitmap getRoundedRectBitmap(Bitmap bitmap) {
        int targetWidth = bitmap.getWidth();
        int targetHeight = bitmap.getHeight();
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        RectF rectF = new RectF(0, 0, targetWidth, targetHeight);
        float cornerRadius = convertDpToPixel(30); // Convertir 10dp en pixels
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

        return targetBitmap;
    }

    private static float convertDpToPixel(float dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return dp * density;
    }
}
