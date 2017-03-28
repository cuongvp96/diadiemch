package c.top3haui.diadiemch;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by VanCuong on 23/03/2017.
 */

public class Activity_getAll extends AppCompatActivity {
    private String url = "http://vancuonghauistudio.96.lt/cuahang/get_all_cuahang.php";

    private RecyclerView recyclerView;
    private ArrayList<Items> arr = new ArrayList<Items>();
    Recycleview_adpater adpater;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loaddl();
    }

    public void loaddl() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //  data.setText(response.substring(0, 500));
//                    jsonStr = response.toString();
                showonListview(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());

            }
        });//neu dung json thi n tra ve oject luon
        requestQueue.add(strReq);
    }

    public void showonListview(String JsonObj) {

        try {
            JSONObject jsonObject = new JSONObject(JsonObj);
            JSONArray jsonArray = jsonObject.getJSONArray("cuahang");
            arr = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String ten = obj.getString("ten");
                String loai = obj.getString("loaigas");
                String motagia = obj.getString("motagia");
                String sdt = obj.getString("sdt");
                String chucuahang = obj.getString("chucuahang");
                String diadiem = obj.getString("diadiem");
                String latlng = obj.getString("latlng");
                Items im = new Items(ten,loai, motagia,sdt,chucuahang,diadiem, latlng);
                arr.add(im);
            }
            pDialog.dismiss();
            //do arraylist vao adapter
            adpater = new Recycleview_adpater(this, arr, new Recycleview_adpater.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
//                    Toast.makeText(getApplicationContext(), arr.get(position)., Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemLongClick(int position, View v) {

                }
            });
//            RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
            RecyclerView.LayoutManager mlayoutManager =new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mlayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adpater);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
