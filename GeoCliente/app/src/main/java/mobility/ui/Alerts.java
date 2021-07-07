package mobility.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class Alerts {

    private Context context;

    public Alerts(Context context) {
        this.context = context;
    }

    public void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public ProgressDialog initProgressDialog(String title, String message, ProgressDialog pd) {
        pd = ProgressDialog.show(context, title, message, true, false);
        return pd;
    }

    public void closeProgressDialog(ProgressDialog pd) {
        try {
            pd.dismiss();
        } catch (Exception e) {
        }
    }
}
