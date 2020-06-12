package com.example.hydrofarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Forgetpassword extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    Button forgetBtn;
    private static final String URL = "http://192.168.1.46/androidfile/forgetpassword.php";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        progressDialog = new ProgressDialog(this);
        setview();
    }

    private void setview()
    {
        email=findViewById(R.id.forgetEmailTxt);
        forgetBtn=findViewById(R.id.forgetSubmitBtn);
        forgetBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
     switch (v.getId())
     {
         case R.id.forgetSubmitBtn:
             resetEmail();
             break;
     }
    }

    private void resetEmail()
    {
        final String mail=email.getText().toString().trim();
        if(mail.isEmpty())
        {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        progressDialog.setMessage("Logging User....");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                try {
                    progressDialog.cancel();
                    Log.d("forget",response);
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String message=jsonObject.getString("code");
                    if(message.equals("success"))
                    {
                        Toast.makeText(Forgetpassword.this, "Check your email for reset password", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Forgetpassword.this,Login.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(Forgetpassword.this, "Something went wrong ....! ", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(Forgetpassword.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("email", mail);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(Forgetpassword.this);
        rQueue.add(stringRequest);


    }
}