package ir.hmnedi.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class Play extends AppCompatActivity {

    private Boolean flag = false, flagStart = true;
    private Button btnTry, btnAction, btnData;
    private TextView txtStatus;
    private ImageView imgO1, imgO2, imgO3, imgO4, imgC1, imgC2, imgC3, imgC4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        txtStatus = findViewById(R.id.txtStatus);
        btnAction = findViewById(R.id.btnStartAction);
        btnData = findViewById(R.id.btnData);
        btnTry = findViewById(R.id.btnTryYpurself);
        btnTry.setEnabled(false);
        btnAction.setBackgroundColor(Color.parseColor("#80FF00"));
        btnData.setBackgroundColor(Color.LTGRAY);
        btnData.setEnabled(false);

        imgO1 = findViewById(R.id.imgFinO1);
        imgO2 = findViewById(R.id.imgFinO2);
        imgO3 = findViewById(R.id.imgFinO3);
        imgO4 = findViewById(R.id.imgFinO4);
        imgC1 = findViewById(R.id.imgFinC1);
        imgC2 = findViewById(R.id.imgFinC2);
        imgC3 = findViewById(R.id.imgFinC3);
        imgC4 = findViewById(R.id.imgFinC4);

        imgC1.setVisibility(View.INVISIBLE);
        imgC2.setVisibility(View.INVISIBLE);
        imgC3.setVisibility(View.INVISIBLE);
        imgC4.setVisibility(View.INVISIBLE);
        imgO1.setVisibility(View.VISIBLE);
        imgO2.setVisibility(View.VISIBLE);
        imgO3.setVisibility(View.VISIBLE);
        imgO4.setVisibility(View.VISIBLE);

    }

    public void try_yourself(View view) {
        if (flag){
            makeJasonReq("http://192.168.4.1/get-finger?fn1=open&fn2=try");
        } else {
            makeJasonReq("http://192.168.4.1/get-finger?fn1=try&fn2=try");
        }
        btnData.setEnabled(true);
        txtStatus.setText("Action started with lower speed...");
        txtStatus.setTextColor(Color.parseColor("#F7FE2E"));
    }

    public void startAction(View view) {
        if (flagStart) {
            btnTry.setEnabled(true);
            if (flag){
                makeJasonReq("http://192.168.4.1/get-finger?fn1=open&fn2=close");
            } else {
                makeJasonReq("http://192.168.4.1/get-finger?fn1=close&fn2=close");
            }
            txtStatus.setText("Action started...");
            txtStatus.setTextColor(Color.parseColor("#9AFE2E"));

            btnAction.setText("Stop");
            btnAction.setBackgroundColor(Color.RED);
            flagStart = false;
        } else {
            btnTry.setEnabled(false);
            makeJasonReq("http://192.168.4.1/get-finger?fn1=open&fn2=open");
            txtStatus.setText("Action stopped.\nwaiting for user to start.");
            txtStatus.setTextColor(Color.parseColor("#6E6E6E"));
            btnAction.setText("Start Action");
            btnAction.setBackgroundColor(Color.parseColor("#80FF00"));
            imgC2.setVisibility(View.INVISIBLE);
            imgC1.setVisibility(View.INVISIBLE);
            imgO1.setVisibility(View.VISIBLE);
            imgO2.setVisibility(View.VISIBLE);
            imgC3.setVisibility(View.INVISIBLE);
            imgC4.setVisibility(View.INVISIBLE);
            imgO3.setVisibility(View.VISIBLE);
            imgO4.setVisibility(View.VISIBLE);

            flagStart = true;
        }
    }

    private void showMeData() {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        String url = "http://192.168.4.1/res/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "fn1 = Correct\nfn2 = Correct", Toast.LENGTH_LONG).show();

                        // todo: the number doesn't appear
                            /*String jsonData = response.getString("message");
                            Toast.makeText(getApplicationContext(), jsonData, Toast.LENGTH_LONG).show();*/

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "error req" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void newPattern(View view) {
        // TODO: make it random and all fingers

        Random rand = new Random();
        int n = rand.nextInt(50);
        if (n%2 == 0){
            flag = true;

            // todo: make a function to set the rest or all
            imgC2.setVisibility(View.VISIBLE);
            imgO2.setVisibility(View.INVISIBLE);
            imgO1.setVisibility(View.VISIBLE);
            imgC1.setVisibility(View.INVISIBLE);
            imgC3.setVisibility(View.INVISIBLE);
            imgC4.setVisibility(View.INVISIBLE);
            imgO3.setVisibility(View.VISIBLE);
            imgO4.setVisibility(View.VISIBLE);
        } else {
            flag = false;
            imgC2.setVisibility(View.VISIBLE);
            imgC1.setVisibility(View.VISIBLE);
            imgO1.setVisibility(View.INVISIBLE);
            imgO2.setVisibility(View.INVISIBLE);
            imgC3.setVisibility(View.INVISIBLE);
            imgC4.setVisibility(View.INVISIBLE);
            imgO3.setVisibility(View.VISIBLE);
            imgO4.setVisibility(View.VISIBLE);
        }
        txtStatus.setText("New pattern generated.");
        txtStatus.setTextColor(Color.parseColor("#CC2EFA"));
    }

    private void makeJasonReq(String url){
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //txtStatus.setText("pattern is playing...");
                        //txtStatus.setTextColor(Color.parseColor("#FE2E9A"));
                        Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mQueue.add(request);
    }

    public void data(View view) {
        Toast.makeText(getApplicationContext(), "fn1 = Correct\nfn2 = Correct", Toast.LENGTH_LONG).show();

    }
}