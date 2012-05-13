package org.whiskeysierra.gestureremote.servermanagment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import org.whiskeysierra.R;
import org.whiskeysierra.gestureremote.servermanagment.data.SettingsDAO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: afeldmann
 * Date: 11.05.12
 * Time: 21:11
 * To change this template use File | Settings | File Templates.
 */
public class NewServerActivity extends Activity {
    private SettingsDAO dao;
    private EditText text;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newserver);
        dao = new SettingsDAO(this);
        dao.open();

    }
    public void resetForm(View view){
        hideKeyboard(view);
        text = (EditText)findViewById(R.id.svr_name);
        text.setText("");
        text = (EditText)findViewById(R.id.svr_port);
        text.setText("");
        text = (EditText)findViewById(R.id.svr_host);
        text.setText("");
    }

    public void saveNewServer(View view){
        String message = "";
        text = (EditText)findViewById(R.id.svr_host);
        String host = text.getText().toString();
        text = (EditText)findViewById(R.id.svr_port);
        String port = text.getText().toString();
        text = (EditText)findViewById(R.id.svr_name);
        String name = text.getText().toString();
        if(!host.matches("(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)")){
            message = "Please input a valid address";
        }else if(port.length() == 0 || Integer.valueOf(port) > 65536){
            message = "Please input a valid port";
        }else if(name.length() == 0){
            message = "Please input a valid name";
        }else{
            dao.createSetting(host,Integer.parseInt(port),name);
            resetForm(view);
            message = "Server saved";
        }
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();

    }
    public void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

    }

    @Override
    protected void onResume() {
        dao.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dao.close();
        super.onPause();
    }


}
