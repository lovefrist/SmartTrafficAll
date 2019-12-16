package com.lenovo.smarttraffic.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * 自己的工具栏
 * @author asus
 */
public class MyUtil {
    /**
     * 二维码实现
     * @param param 二维码内容
     * @param width 二维码图片宽度
     * @param height 二维码图片高度
     * @return BitMap
     */
    public static Bitmap qRCode(String param, int width, int height){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>(1);
        /* 要自定义长宽 */
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix encode = null;
        try {
            encode = qrCodeWriter.encode(param, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "qRCode: "+encode);
        int[] colors = new int[width * height];
        //利用for循环将要表示的信息写出来
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (encode.get(i, j)) {
                    colors[i * width + j] = Color.BLACK;
                } else {
                    colors[i * width + j] = Color.WHITE;
                }
            }
        }

        return Bitmap.createBitmap(colors, width, height, Bitmap.Config.RGB_565);
    }

    /**
     * 生成简单二维码
     *
     * @param context                字符串内容
     * @param width                  二维码宽度
     * @param height                 二维码高度
     * @param character_set          编码方式（一般使用UTF-8）
     * @param error_correction_level 容错率 L：7% M：15% Q：25% H：35%
     * @param margin                 空白边距（二维码与边框的空白区域）
     * @param color_black            黑色色块
     * @param color_white            白色色块
     * @return BitMap
     */
   public static   Bitmap createBitmap(String context, int width, int height, String character_set, String error_correction_level, String margin, int color_black, int color_white) {
        try {
            /**二维吗的相关设置*/
            if (context == null) {
                return null;
            }
            if (width < 0 || height < 0) {
                return null;
            }
            HashMap<EncodeHintType, String> hashtable = new HashMap<>(1);
            //字符转码格式设置
            if (!TextUtils.isEmpty(character_set)) {
                hashtable.put(EncodeHintType.CHARACTER_SET, "utf-8");
            }
//        //容错率设置
//        if (!TextUtils.isEmpty(error_correction_level)) {
//            hashtable.put(EncodeHintType.ERROR_CORRECTION, error_correction_level);
//        }
//        //空白间距设置
//        if (!TextUtils.isEmpty(margin)) {
//            hashtable.put(EncodeHintType.MARGIN, margin);
//        }

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象 */
            BitMatrix bitMatrix = qrCodeWriter.encode(context, BarcodeFormat.QR_CODE, width, height, hashtable);
            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值 */
            int[] pixels = new int[width*height];
            for (int y=0;y<height;y++){
                for (int x =0;x<width;x++){
                    if (bitMatrix.get(x,y)){
                        pixels[y*width+x] = color_black;
                    }else {
                        pixels[y*width+x] = color_white;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {

            e.printStackTrace();
            return null;
        }
    }
}
