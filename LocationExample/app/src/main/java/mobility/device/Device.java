package mobility.device;

import android.content.Context;
import android.os.Build;

public class Device {
    private Context context;

    public Device(Context context) {
        this.context = context;
    }

    /**
     * Método utilizado para obtener las especificaciones del dispositivo
     *
     * @return, Array con la información del dispositivo
     * [0] => Marca (Fabricante)
     * [1] => Modelo (Modelo especifico definido por la marca)
     * [2] => Versión del dispositivo
     * [3] => Sistema Operativo (Versión)
     */
    public String[] deviceInfo() {
        String[] device = {
                Build.MANUFACTURER,
                Build.MODEL,
                Build.DEVICE,
                Build.VERSION.RELEASE};
        return device;
    }
}