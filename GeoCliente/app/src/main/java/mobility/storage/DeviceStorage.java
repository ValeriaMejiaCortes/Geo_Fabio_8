package mobility.storage;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class DeviceStorage {

    private Context context;

    public DeviceStorage(Context context) {
        this.context = context;
    }

    public void save(String fileName, String value) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(value);
            osw.flush();
            osw.close();
        } catch (IOException ex) {
        }
    }

    public String get(String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[100];
            String s = "";
            int charRead;
            while ((charRead = isr.read(inputBuffer)) > 0) {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                s += readString;
                inputBuffer = new char[100];
            }
            isr.close();
            return s;
        } catch (IOException ex) {
            return "";
        }
    }


}
