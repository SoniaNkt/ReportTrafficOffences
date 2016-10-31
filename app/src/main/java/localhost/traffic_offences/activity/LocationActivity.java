package localhost.traffic_offences.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import localhost.traffic_offences.R;
import localhost.traffic_offences.helper.SQLiteHandler;


public class LocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private SQLiteHandler db;

    RequestQueue requestQueue;
    String insertUrl = "http://192.168.137.1/android_login_api/insertReport.php";
    String mLatitude;
    String mLongitude;
    Button insert;

    String offence;
    String code;

    //image things
    private Button buttonChoose;
    //private Button buttonUpload;

    private ImageView imageView;

    private EditText editTextName;

    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    //private String UPLOAD_URL ="http://192.168.137.1/android_login_api/upload.php";

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    private RadioGroup radioGroup;
    //private RadioButton radioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        //mLatitude = (TextView)findViewById(R.id.latitude);
        //mLongitude = (TextView)findViewById(R.id.longitude);
        insert = (Button) findViewById(R.id.find_location_button);

        //image things
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        //buttonUpload = (Button) findViewById(R.id.buttonUpload);

        editTextName = (EditText) findViewById(R.id.editText);

        imageView  = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(chooseListener);
       // buttonUpload.setOnClickListener(uploadListener);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                offence = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + offence, Toast.LENGTH_LONG).show();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Over speeding");
        categories.add("Overlapping");
        categories.add("Driving without identification plates");
        categories.add("Driving on/through a pavement or footpath");
        categories.add("Obstruction causing inconvenience");
        categories.add("Boarding or alighting at an unauthorized stop");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        addListenerOnButton();
    }

    public void addListenerOnButton() {

         /* Initialize Radio Group and attach click handler */
        //radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupCodes);
        radioGroup.clearCheck();

        /* Attach CheckedChangeListener to radio group */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if(null!=rb && checkedId > -1){
                    //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
                    code = rb.getText().toString().trim();
                }

            }
        });

//        radioGroup = (RadioGroup) findViewById(R.id.radioGroupCodes);
        //btnDisplay = (Button) findViewById(R.id.btnDisplay);


                // get selected radio button from radioGroup
               // int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
               // radioButton = (RadioButton) findViewById(selectedId);

                //Toast.makeText(getApplicationContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
                //code = radioButton.getText().toString().trim();


    }

    //IMAGE

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

//    private void uploadImage(){
//        //Showing the progress dialog
//        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        //Disimissing the progress dialog
//                        loading.dismiss();
//                        //Showing toast message of the response
//                        Toast.makeText(LocationActivity.this, s , Toast.LENGTH_LONG).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        //Dismissing the progress dialog
//                        loading.dismiss();
//
//                        //Showing toast
//                        Toast.makeText(LocationActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
//                    }
//                }){
////            @Override
////            protected Map<String, String> getParams() throws AuthFailureError {
////                //Converting Bitmap to String
////                String image = getStringImage(bitmap);
////
////                //Getting Image Name
////                String name = editTextName.getText().toString().trim();
////
////                //Creating parameters
////                Map<String,String> params = new Hashtable<String, String>();
////
////                //Adding parameters
////                params.put(KEY_IMAGE, image);
////                params.put(KEY_NAME, name);
////
////                //returning parameters
////                return params;
////            }
//        };

//        //Creating a Request Queue
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        //Adding request to the queue
//        requestQueue.add(stringRequest);
//    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    public void onClick(View v) {
//
//        if(v == buttonChoose){
//            showFileChooser();
//        }
//
//        if(v == buttonUpload){
//            uploadImage();
//        }
//    }

    private View.OnClickListener chooseListener = new View.OnClickListener() {

        @Override

        public void onClick(View v) {

            showFileChooser();

        }

    };

//    private View.OnClickListener uploadListener = new View.OnClickListener() {
//
//        @Override
//
//        public void onClick(View v) {
//
//            uploadImage();
//
//        }
//
//    };

    //IMAGE



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            final double lat = lastLocation.getLatitude(), lng = lastLocation.getLongitude();

            Toast.makeText(getApplicationContext(), "Your coordinates are " + lat + lng,  Toast.LENGTH_SHORT).show();

            String units = "imperial";

            mLatitude = String.valueOf(lat);
            mLongitude = String.valueOf(lng);

            // SQLite database handler
            db = new SQLiteHandler(getApplicationContext());

            // Fetching user details from sqlite
            HashMap<String, String> user = db.getUserDetails();
            final String email = user.get("email");
            Log.d("EMAIL!!!!!", email);
            //Log.d("Offence!!!!!", offence);


            requestQueue = Volley.newRequestQueue(getApplicationContext());

            insert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(),"Your report has been successfully submitted",Toast.LENGTH_LONG).show();
                            //Log.d("Offence!!!!!", offence);

                            //System.out.println(response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //   Toast.makeText(getApplicationContext(),"FAIL"+error,Toast.LENGTH_LONG).show();
                            Log.d("VOLLEY ERROR",error.toString());
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            //Converting Bitmap to String
               String image = getStringImage(bitmap);
//
//                //Getting Image Name
               String name = editTextName.getText().toString().trim();

                            Map<String,String> parameters  = new HashMap<String, String>();
                            parameters.put("lat",String.valueOf(lat));
                            parameters.put("lng",String.valueOf(lng));
                            parameters.put("email",String.valueOf(email));
                            parameters.put("offence",String.valueOf(offence));
                            parameters.put(KEY_IMAGE, image);
                            parameters.put(KEY_NAME, name);
                            parameters.put("code", code);
                            Log.d("CODE!!!!!", code);
                            return parameters;
                        }
                    };

                    requestQueue.add(request);
                }

            });

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {

        googleApiClient.disconnect();
        super.onStop();

    }
}
