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
import android.widget.ImageView;
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


public class Register extends AppCompatActivity {

    private static final String URL = "http://192.168.1.46/androidfile/register.php";
    private ProgressDialog progressDialog;
    Button btnBrowse, btnRegister;
    TextView btnLogin;
    ImageView profileImg;
    EditText userNameTxt, emailTxt, phoneTxt, passwordTxt, confirmPasswordTxt;


    String username, email, pass, cnfrmPass;
    String phone,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnBrowse = findViewById(R.id.registerBrowseImageBtn);
        btnLogin = findViewById(R.id.RegisterLoginBtn);
        btnRegister = findViewById(R.id.registerSubmitBtn);

        profileImg = findViewById(R.id.registerProfileImage);

        userNameTxt = findViewById(R.id.registerUserNameTxt);
        emailTxt = findViewById(R.id.registerEmailTxt);
        phoneTxt = findViewById(R.id.registerPhoneTxt);
        passwordTxt = findViewById(R.id.registerPasswordTxt);
        confirmPasswordTxt = findViewById(R.id.registerConfirmPasswordTxt);
        progressDialog = new ProgressDialog(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                token=task.getResult().getToken();
                Log.d("myToken",token);//saad
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = userNameTxt.getText().toString().trim();
                email = emailTxt.getText().toString().trim();
               phone = phoneTxt.getText().toString().trim();
                pass = passwordTxt.getText().toString().trim();
                cnfrmPass = confirmPasswordTxt.getText().toString().trim();
                if(username.isEmpty())
                {
                    userNameTxt.setError("Name is required");
                    userNameTxt.requestFocus();
                    return;
                }
                if(email.isEmpty())
                {
                    emailTxt.setError("Email is required");
                    emailTxt.requestFocus();
                    return;
                }
                if(phone.isEmpty())
                {
                    phoneTxt.setError("Phone is required");
                    phoneTxt.requestFocus();
                    return;
                }
                if(pass.isEmpty())
                {
                    passwordTxt.setError("Password is required");
                    passwordTxt.requestFocus();
                    return;
                }
                if(cnfrmPass.isEmpty())
                {
                    confirmPasswordTxt.setError("Password is required");
                    confirmPasswordTxt.requestFocus();
                    return;
                }

                if (pass.compareTo(cnfrmPass) == 0) {

                    progressDialog.setMessage("Registering User....");
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
                                if(message.equals("success"))
                                {
                                    Toast.makeText(Register.this, "You have created an account successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(Register.this,Login.class);
                                    startActivity(intent);
                                }
                                else
                                    {
                                        Toast.makeText(Register.this, "Something went wrong ....! ", Toast.LENGTH_SHORT).show();

                                    }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.cancel();
                            Toast.makeText(Register.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params=new HashMap<String, String>();
                            params.put("firstname", username);
                            params.put("lastname", "");
                            params.put("address", "");
                            params.put("photo", "");
                            params.put("email", email);
                            params.put("phone_number", phone);
                            params.put("password", cnfrmPass);
                            params.put("token", token);
                            return params;
                        }
                    };

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(stringRequest);
                }
                else {
                    Toast.makeText(Register.this,"Password Do not Match", Toast.LENGTH_SHORT).show();;
                }
            }
        });
    }
}
