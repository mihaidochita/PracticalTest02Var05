package pdsd.systems.cs.pub.ro.practicaltest02var05.networkingthreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import pdsd.systems.cs.pub.ro.practicaltest02var05.general.Constants;
import pdsd.systems.cs.pub.ro.practicaltest02var05.general.Utilities;
import pdsd.systems.cs.pub.ro.practicaltest02var05.model.DateTime;
import pdsd.systems.cs.pub.ro.practicaltest02var05.model.StoredValue;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    private DateTime getDate() throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String pageSourceCode = httpClient.execute(httpGet, responseHandler);
        String[] val = pageSourceCode.split("[-T:+I]");
        System.out.println(pageSourceCode);
        DateTime dateTime = new DateTime(Integer.parseInt(val[0]), Integer.parseInt(val[1]), Integer.parseInt(val[2]), Integer.parseInt(val[3]), Integer.parseInt(val[4]), Integer.parseInt(val[5]));
        System.out.println(dateTime.toString());
        return dateTime;
    }


    @Override
    public void run() {
        if (socket != null) {
            try {
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
                    String method = bufferedReader.readLine();
                    if(method.equals("get")) {

                        String key = bufferedReader.readLine();
                        HashMap<String, StoredValue> data = serverThread.getData();
                        StoredValue storedValue = null;
                        String raspuns = null;
                        if (key != null && !key.isEmpty()) {
                            if (data.containsKey(key)) {
                                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");

                                storedValue = data.get(key);
                                if (getDate().toLong() - storedValue.getDate().toLong() < 60)
                                    raspuns = storedValue.getValue();
                                else
                                    raspuns = "none";
                                printWriter.println(raspuns);
                                printWriter.flush();
                            } else {
                                raspuns = "none";
                                printWriter.println(raspuns);
                                printWriter.flush();
                            }

                        } else {
                            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type)!");
                        }
                    }
                    else {
                        String key = bufferedReader.readLine();
                        String value = bufferedReader.readLine();
                        HashMap<String, StoredValue> data = serverThread.getData();
                        StoredValue storedValue = null;
                        String raspuns = null;
                        if (key != null && !key.isEmpty()) {
                            if (data.containsKey(key)) {
                                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");

                                storedValue = data.get(key);
                                raspuns = "modified";
                                storedValue.setDate(getDate());
                                storedValue.setValue(value);
                                serverThread.setData(key,storedValue);
                                printWriter.println(raspuns);
                                printWriter.flush();
                            } else {
                                raspuns = "inserted";
                                serverThread.setData(key,new StoredValue(value, getDate()));
                                printWriter.println(raspuns);
                                printWriter.flush();
                            }
                        }
                    }
                } else {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
                }
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        } else {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
        }
    }

}
