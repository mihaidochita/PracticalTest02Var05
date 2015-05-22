package pdsd.systems.cs.pub.ro.practicaltest02var05.networkingthreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;
import android.widget.TextView;

import pdsd.systems.cs.pub.ro.practicaltest02var05.general.Constants;
import pdsd.systems.cs.pub.ro.practicaltest02var05.general.Utilities;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String key;
    private TextView dataTextView;
    private String method;
    private String value;
    private Socket socket;

    public ClientThread(
            String address,
            int port,
            String key,
            TextView data,
            String method,
            String value) {
        this.address = address;
        this.port = port;
        this.key = key;
        this.dataTextView = data;
        this.method = method;
        this.value=value;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader != null && printWriter != null) {
                printWriter.println(method);
                printWriter.flush();
                printWriter.println(key);
                printWriter.flush();
                printWriter.println(value);
                printWriter.flush();
                String data;
                while ((data = bufferedReader.readLine()) != null) {
                    final String finalizedWeatherInformation = data;
                    dataTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            dataTextView.setText(finalizedWeatherInformation + "\n");
                        }
                    });
                }
            } else {
                Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
            }
            socket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
