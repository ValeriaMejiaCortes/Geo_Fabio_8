package mobility.storage;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class ExternalStorage {

    private Context context;

    public ExternalStorage(Context context) {
        this.context = context;
    }

    public void save(String folder, String fileName, String value) {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/" + folder);
            myDir.mkdirs();
            String fname = fileName;
            File file = new File(myDir, fname);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(value.getBytes());
            fos.close();
        } catch (Exception e) {
        }
    }

    public String get(String folder, String fileName) {
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/" + folder);
            myDir.mkdirs();
            String fname = fileName;
            File file = new File(myDir, fname);
            FileInputStream fIn = new FileInputStream(file);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow;
            }
            myReader.close();
            return aBuffer;
        } catch (Exception e) {
            return "";
        }
    }
}
