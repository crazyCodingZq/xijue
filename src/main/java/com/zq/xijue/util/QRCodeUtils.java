package com.zq.xijue.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhangQ
 * @Date: 2020/5/11 21:50
 */
public class QRCodeUtils {
    private final Logger logger = LoggerFactory.getLogger(QRCodeUtils.class);

    /**
     * 生成二维码
     *
     * @param codeUrl
     */
    public void getRCode(HttpServletResponse response, String codeUrl) {
        try {
            //生成二维码配置
            Map<EncodeHintType, Object> hints = new HashMap<>();
            //设置纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //编码类型
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 400, 400);
            OutputStream outputStream = response.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
        } catch (Exception e) {
            logger.error("QR code generate fail;Exception:", e);
        }

    }
}
