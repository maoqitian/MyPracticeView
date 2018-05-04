package mao.com.mycustomview.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by maoqitian on 2016/7/15.
 */
public class ToastUtil {
    public static void showInfo(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}
