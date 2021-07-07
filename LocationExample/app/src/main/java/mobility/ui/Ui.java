package mobility.ui;

import android.content.Context;
import android.widget.Button;

import androidx.core.content.ContextCompat;


public class Ui {

    private Context context;

    public Ui(Context context) {
        this.context = context;
    }

    public void changeButtonColor(Button button, int color) {
        button.setBackgroundColor(ContextCompat.getColor(context, color));
    }
}