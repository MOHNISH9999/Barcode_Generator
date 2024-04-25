package com.example.user.barcode_generator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Objects;

public
class MainActivity extends AppCompatActivity {


    EditText editText;
    ImageView imageViewCode128,imageViewQRCode,imageViewEAN13;
    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        EdgeToEdge.enable ( this );
        setContentView ( R.layout.activity_main );
        ViewCompat.setOnApplyWindowInsetsListener ( findViewById ( R.id.main ) ,
                ( v , insets ) -> {
                    Insets systemBars = insets.getInsets ( WindowInsetsCompat.Type.systemBars ( ) );
                    v.setPadding ( systemBars.left ,
                            systemBars.top ,
                            systemBars.right ,
                            systemBars.bottom );
                    return insets;
                } );

        editText = findViewById(R.id.editText);
        imageViewCode128 = findViewById(R.id.imageViewCode128);
        imageViewQRCode = findViewById(R.id.imageViewQRCode);
//        imageViewEAN13 = findViewById(R.id.imageViewEAN13);

        // Generate and display barcode when text changes
        editText.addTextChangedListener(new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                generateBarcode(s.toString());
            }

            @Override
            public void afterTextChanged( Editable s) {}
        });
    }

    private void generateBarcode(String text) {
        if (text.isEmpty()) {
            // Clear all ImageViews if text is empty
            imageViewCode128.setImageBitmap(null);
            imageViewQRCode.setImageBitmap(null);
            imageViewEAN13.setImageBitmap(null);
            return;
        }

        try {
            // Generate CODE_128 barcode
            BitMatrix bitMatrixCode128 = new MultiFormatWriter().encode(
                    text,
                    BarcodeFormat.CODE_128,
                    500,
                    200);
            imageViewCode128.setImageBitmap(renderBitmap(bitMatrixCode128));

            // Generate QR_CODE barcode
            BitMatrix bitMatrixQRCode = new MultiFormatWriter().encode(
                    text,
                    BarcodeFormat.QR_CODE,
                    500,
                    500);
            imageViewQRCode.setImageBitmap(renderBitmap(bitMatrixQRCode));

//            // Validate input text length for EAN_13 format
//            if (text.matches("\\d{12,13}")) {
//                BitMatrix bitMatrixEAN13 = new MultiFormatWriter().encode(
//                        text,
//                        BarcodeFormat.EAN_13,
//                        500,
//                        200);
//                imageViewEAN13.setImageBitmap(renderBitmap(bitMatrixEAN13));
//            } else {
//                imageViewEAN13.setImageBitmap(null);
//            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private Bitmap renderBitmap(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }
}