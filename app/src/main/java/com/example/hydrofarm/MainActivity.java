package com.example.hydrofarm;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hydrofarm.Adpter.ViewProduct;
import com.example.hydrofarm.Model.ViewProductData;
import com.example.hydrofarm.Model.ViewProductModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv_view_product;
    private static final String URL = "http://192.168.1.46/androidfile/viewproduct.php";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        rv_view_product=findViewById(R.id.recyclerViewToViewProductss);
        rv_view_product.setLayoutManager(new GridLayoutManager(this,2));
        rv_view_product.setHasFixedSize(true);
        getAllProduct();
        Intent intent = new Intent();

        String manufacturer = android.os.Build.MANUFACTURER;

        switch (manufacturer) {

            case "xiaomi":
                intent.setComponent(new ComponentName("com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                break;
            case "oppo":
                intent.setComponent(new ComponentName("com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity"));

                break;
            case "vivo":
                intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                break;
        }

        List<ResolveInfo> arrayList =  getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);

        if (arrayList.size() > 0) {
            startActivity(intent);
        }
    }

    private void getAllProduct()
    {
        progressDialog.setMessage("Registering User....");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {


                    progressDialog.cancel();
                    Log.d("register",response);
                    String data="{ data: "+response+"}";
                Gson gson=new Gson();
                ViewProductModel model=new ViewProductModel();
             model =gson.fromJson(data, ViewProductModel.class);
             List<ViewProductData> dataList=new ArrayList<>();
             dataList=model.getData();
             rv_view_product.setAdapter(new ViewProduct(dataList,MainActivity.this));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        ;

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(stringRequest);
    }
}
