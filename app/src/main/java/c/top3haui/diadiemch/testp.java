package c.top3haui.diadiemch;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class testp extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    EditText edTen, edMotagia, edDiadiem, edlatlng;
    Button btn_create, btn_getAll, btn_getLocation;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edTen = (EditText) findViewById(R.id.edTen);
        edMotagia = (EditText) findViewById(R.id.edMota);
        edDiadiem = (EditText) findViewById(R.id.edDiadiem);
        edlatlng = (EditText) findViewById(R.id.edLatlng);
        btn_create = (Button) findViewById(R.id.btn_save);
        btn_getAll = (Button) findViewById(R.id.btn_getAll);
        btn_getLocation = (Button) findViewById(R.id.btn_getlocation);
        buildGoogleApiClient();
        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23) {
                   askpr();
                }else
                    getLocation();
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edTen.getText().toString().isEmpty())
                    edTen.setError("Không dc trống");
                else if (edDiadiem.toString().isEmpty())
                    edDiadiem.setError("Không dc trống");
                else {
                    Toast.makeText(getApplicationContext(), "Chờ tý, đang thêm...", Toast.LENGTH_SHORT).show();
                    callAddVolley();
                }
            }
        });
        btn_getAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(testp.this,Activity_getAll.class);
                startActivity(i);
            }
        });
    }

    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askpr() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(testp.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_CONTACTS, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void getLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Location location = mLastLocation;

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String lat_lon = latitude + "," + longitude;
            edlatlng.setText(lat_lon);
            getJsonLocation(lat_lon);
        } else
            Toast.makeText(getApplicationContext(), "Reset lại gps xem, ko thì chạy lại app", Toast.LENGTH_SHORT).show();


    }

    public void getJsonLocation(String lat_lon) {

        try {
            final RequestQueue queue = Volley.newRequestQueue(this);
            Log.i("url_city", urlSearch(lat_lon));
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSearch(lat_lon), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //  Log.i("result", response);
                    readPositions(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.activity_main), "Internet Lỗi!", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                }
            });
            queue.add(stringRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void readPositions(String response) {
        String diadiem = "";
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray arr = obj.getJSONArray("results");


            JSONObject item2 = arr.getJSONObject(0);
            String name_location = item2.getString("formatted_address");
            String[] itm = name_location.split(",");
            for (int j = 0; j < itm.length - 1; j++) {
                if (j < itm.length - 2)
                    diadiem += itm[j].toString() + ", ";
                else
                    diadiem += itm[j].toString();
            }
            edDiadiem.setText(diadiem);


        } catch (Exception e) {
            Log.e("loi", e.getMessage());
        }
    }

    public synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void callAddVolley() {
        String url = "http://vancuonghauistudio.96.lt/cuahang/create_cuahang.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Post", response);
                //phan tich json
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.activity_main), "Thêm thành công!", Snackbar.LENGTH_LONG);

                snackbar.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Add new ", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ten", edTen.getText().toString());
                params.put("motagia", edMotagia.getText().toString());
                params.put("diadiem", edDiadiem.getText().toString());
                params.put("latlng", edlatlng.getText().toString());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    public String urlSearch(String location) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + location + "&key=AIzaSyAeiVSzPzN5jj79LzA6hmeXMjmbD9Ij-yI";
        return url;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
