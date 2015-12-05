package anka.niklas.ankaremote;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import Model.Model;


public class LauncherActivity extends Activity implements View.OnClickListener {

    Button connect_button;
    EditText ip_field;
    EditText port_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        connect_button = (Button)findViewById(R.id.Launcher_connectButton);
        ip_field = (EditText)findViewById(R.id.Launcher_ipadressField);
        port_field = (EditText)findViewById(R.id.Launcher_portField);
        connect_button.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Model m = ((MyApplication) this.getApplication()).getModel();
        m.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return false;
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

    @Override
    public void onClick(View v) {
        System.out.println("Click!");
        System.out.println("IP: " + ip_field.getText());
        System.out.println("port: " + port_field.getText());

        Model m = ((MyApplication) this.getApplication()).getModel();
        m.setIpAdress(ip_field.getText().toString());
        m.setPort(Integer.parseInt(port_field.getText().toString()));
        if(m.connect()) {
            System.out.println("Start new Activity");
            Intent in = new Intent(getApplicationContext(),MapsActivity.class);
            startActivity(in);
        }
        else {
            System.out.println("Failed!");
        }

    }
}
