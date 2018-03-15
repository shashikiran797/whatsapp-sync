package com.boatman.sync;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private ListView contactsListView;
    private ArrayList<String> contactsList;
    static int CONTACTS_PERMISSION_CODE = 1;
    //Send button
    private Button uploadBtn;
    private EditText urlEditText;
    private TextView message;
    private ProgressBar progressBar;
    String[] temp = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing the views
        if (!isContactPermissionGranted()) {
            requestReadAndSendSmsPermission();
        }
        contactsListView = (ListView) findViewById(R.id.contactsList);
        uploadBtn = (Button) findViewById(R.id.buttonSend);
        urlEditText = (EditText) findViewById(R.id.urlEditText);
        message = findViewById(R.id.message);
        progressBar = findViewById(R.id.progressbar);
        urlEditText.setText(LocalStorageService.getString(this, "url"));
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadContacts();
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        contactsList = ContactAccessor.getContacts(this, "com.whatsapp");
        message.setText("Total Whatsapp contacts found:" + contactsList.size());
        progressBar.setVisibility(View.GONE);
        if (contactsList.size() <= 0) {
            showToast("No contacts found");
        } else {
            temp = new String[contactsList.size()];
            temp = contactsList.toArray(temp);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, temp);
            contactsListView.setAdapter(adapter);
            uploadContacts();
        }
    }


    /**
     * Check if we have SMS permission
     */
    public boolean isContactPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request runtime SMS permission
     */
    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
            // https://developer.android.com/training/permissions/requesting.html
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACTS_PERMISSION_CODE);
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_LONG)
                .show();
    }

    private void uploadContacts() {
        progressBar.setVisibility(View.VISIBLE);
        uploadBtn.setVisibility(View.GONE);
        String url = urlEditText.getText().toString();
        LocalStorageService.putString(this, "url", url);
        Response.Listener<JSONObject> onSuccess = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressBar.setVisibility(View.GONE);
                uploadBtn.setVisibility(View.VISIBLE);
                Log.d(TAG, "onResponse: " + jsonObject.toString());
                showToast("Upload success");
            }

        };
        Response.ErrorListener onFailure = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                progressBar.setVisibility(View.GONE);
                uploadBtn.setVisibility(View.VISIBLE);
                Log.d("Error.Response", error.toString());
                showToast("Upload failed");
            }
        };
        JSONObject requestObj = new JSONObject();
        JSONArray newArray = new JSONArray();
        for (String str : temp) {
            newArray.put(str);
        }
        try {
            requestObj.put("contacts", newArray);
        } catch (JSONException e) {
            showToast("json exception");
            e.printStackTrace();
        }
        HttpService.postCall(this, url, requestObj,
                onSuccess, onFailure);

    }
}
