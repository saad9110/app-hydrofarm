package com.example.hydrofarm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Button btnLogin;
    TextView btnRegister,forgetpasswordBtn;
    EditText emailTxt, passwordTxt;
    private static final String URL = "http://192.168.1.46/androidfile/login.php";
    private ProgressDialog progressDialog;
    String email,password,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                token=task.getResult().getToken();
                Log.d("myToken",token);//saad
            }
        });
        btnLogin = findViewById(R.id.loginSubmitBtn);
        btnRegister = findViewById(R.id.loginRegisterBtn);
        forgetpasswordBtn = findViewById(R.id.forgetpasswordBtn);
        forgetpasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forget=new Intent(Login.this,Forgetpassword.class);
                startActivity(forget);
            }
        });

        emailTxt = findViewById(R.id.loginEmailTxt);
        passwordTxt = findViewById(R.id.loginPasswordTxt);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Login.this, MainActivity.class);
//                startActivity(intent);
                login();

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(Login.this);
        String message=sharedPreferences.getString("message","");
        if(message.equals("login"))
        {
            Intent intent=new Intent(Login.this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void login()
    {


        email=emailTxt.getText().toString().trim();
        password=passwordTxt.getText().toString().trim();
        if(email.isEmpty())
        {
            emailTxt.setError("Email is required");
            emailTxt.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            passwordTxt.setError("Password is required");
            passwordTxt.requestFocus();
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
                    Log.d("register",response);
                    JSONArray jsonArray=new JSONArray(response);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String message=jsonObject.getString("code");
                    String id=jsonObject.getString("userid");
                    if(message.equals("login_successful"))
                    {
                        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(Login.this);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("id",id);
                        editor.putString("message","login");
                        editor.apply();
                        editor.commit();
                        Toast.makeText(Login.this, "Login successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Login.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(Login.this, "Something went wrong ....! ", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("token", token);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(Login.this);
        rQueue.add(stringRequest);
    }
}
