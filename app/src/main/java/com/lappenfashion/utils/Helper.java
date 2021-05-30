package com.lappenfashion.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.Window;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.lappenfashion.R;

import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

import okhttp3.ResponseBody;


public class Helper {

    private static Dialog dialog;

    public static Dialog showLoader(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.loader_view);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public static void dismissLoader() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static void showTost(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String getErrorBodyMessage(Context context, ResponseBody responseBody) {
        try {
            JSONObject jObjError = new JSONObject(responseBody.string());
            String message = jObjError.getString("message");
            return message;
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return "";
    }

}
