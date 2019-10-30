package org.feup.cmov.acmecustomer.interfaces;

public interface QRCodeInterface {
    String encode();

    /*private void generateQRCode() {
        QRCodeWriter writer = new QRCodeWriter();
        final static String CHARACTER_SET = "ISO-8859-1";

        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, CHARACTER_SET);

        try {
            BitMatrix bitMatrix = writer.encode("teste", BarcodeFormat.QR_CODE, 512, 512, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            //((ImageView) findViewById(R.id.img_result_qr)).setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }*/
}
