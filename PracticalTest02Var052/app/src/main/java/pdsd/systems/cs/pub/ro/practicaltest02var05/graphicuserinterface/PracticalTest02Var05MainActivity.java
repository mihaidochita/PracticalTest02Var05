package pdsd.systems.cs.pub.ro.practicaltest02var05.graphicuserinterface;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pdsd.systems.cs.pub.ro.practicaltest02var05.R;
import pdsd.systems.cs.pub.ro.practicaltest02var05.general.Constants;
import pdsd.systems.cs.pub.ro.practicaltest02var05.networkingthreads.ClientThread;
import pdsd.systems.cs.pub.ro.practicaltest02var05.networkingthreads.ServerThread;


public class PracticalTest02Var05MainActivity extends ActionBarActivity {
    // Server widgets
    private EditText serverPortEditText       = null;
    private Button connectButton            = null;

    // Client widgets
    private EditText     clientAddressEditText    = null;
    private EditText     clientPortEditText       = null;
    private EditText     keyText             = null;

    private EditText     valueText = null;
    private EditText    raspunsText = null;
    private Button get = null;
    private Button post= null;

    private ServerThread serverThread             = null;
    private ClientThread clientThread             = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Server port should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
            }

        }
    }

    private PostButtonClickListener postButtonClickListener = new PostButtonClickListener();
    private class PostButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort    = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }

            String key = keyText.getText().toString();
            String value= valueText.getText().toString();
            clientThread = new ClientThread(clientAddress,
                    Integer.parseInt(clientPort),
                    key,
                    raspunsText,
                    "post",
                    value);
            clientThread.start();
        }
    }

    private GetButtonOnClickListener getButtonOnClickListener = new GetButtonOnClickListener();
    private class GetButtonOnClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort    = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }

            String key = keyText.getText().toString();
            clientThread = new ClientThread(clientAddress,
                    Integer.parseInt(clientPort),
                    key,
                    raspunsText,
                    "get",
                    "");
            clientThread.start();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_var05_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        keyText = (EditText)findViewById(R.id.cheie);
        valueText = (EditText)findViewById(R.id.val);
        raspunsText = (EditText)findViewById(R.id.raspuns);
        post = (Button)findViewById(R.id.post);
        post.setOnClickListener(postButtonClickListener);
        get = (Button)findViewById(R.id.get);
        get.setOnClickListener(getButtonOnClickListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practical_test02_var05_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
