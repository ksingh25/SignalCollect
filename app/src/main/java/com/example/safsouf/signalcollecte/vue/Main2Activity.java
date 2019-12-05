package com.example.safsouf.signalcollecte.vue;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.safsouf.signalcollecte.R;
import com.example.safsouf.signalcollecte.modele.CompleteAddress;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.os.Build.MANUFACTURER;
import static android.os.Build.MODEL;
import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;
import static android.widget.Toast.*;

public class

Main2Activity extends AppCompatActivity {




    TelephonyManager mTelephonyManager;
    MyPhoneStateListener mPhoneStatelistener;
    String mSignalStrength = "";
    TextView txt_etat;

    //LTE Info
    TextView rsrp;
    TextView rsrq;
    TextView rssi;
    TextView cqi;
    TextView SS;

    //device info
    TextView dn;
    TextView dm;
    TextView dv;

    Button btn_save;

    //Cell info
    TextView cellIDTextView;
    TextView cellMccTextView;
    TextView cellMncTextView;
    TextView cellPciTextView;
    TextView cellTacTextView;

    View view;

    List<CellInfo> cellInfoList;
    int cellSig, cellID, cellMcc, cellMnc, cellPci, cellTac = 0;

    private FusedLocationProviderClient mFusedLocationClient;

    String fileName = "InfoSave.csv";

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initComponent();
        initPhoneManager();
        getDeviceÌnfo();
        getCellInfo();
        getLocation();

        btn_save = (Button) findViewById(R.id.button2);


        btn_save.setOnClickListener(new View.OnClickListener() {

            public void onClick(View pView) {
                try {
                    saveFile( fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void saveFile(String file){

        String fileName ="InfoSave.csv";
        String data = txt_etat.getText().toString() + "," +
                rsrp.getText().toString() + "," +
                rsrq.getText().toString() + "," +
                cqi.getText().toString() + "," +
                SS.getText().toString() + "," +
                dn.getText().toString() + "," +
                dm.getText().toString() + "," +
                dv.getText().toString() +
                cellIDTextView.getText().toString()+","+
                cellMccTextView.getText().toString()+","+
                cellMncTextView.getText().toString()+","+
                cellPciTextView.getText().toString()+","+
                cellTacTextView.getText().toString()+"\n";
        try{
            FileOutputStream fos = openFileOutput( file, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(Main2Activity.this,"Error saving file",Toast.LENGTH_SHORT).show();}

    }
    

    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            mSignalStrength = signalStrength.toString();
            String[] parts = mSignalStrength.split(" ");

            if (mTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE)
                txt_etat.setText("Conected to LTE");
            else
                txt_etat.setText("Not conected to LTE");

            //LteSignalStrength PART 8
            SS.setText(convertToDbm(parts[8]));

            //RSRP PART 9
            rsrp.setText(convertToDbm(parts[9]));

            //RSRQPART 10
            rsrq.setText(convertToDbm(parts[10]));

            //RSSNR PART 11
            rssi.setText(convertToDbm(parts[11]));

            //CQI  PART 12
            cqi.setText(convertToDbm(parts[12]));
        }


        private String convertToDbm(String value) {
            return String.valueOf(Integer.parseInt(value) - 140);
        }


        @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void onCellInfoChanged(List<CellInfo> cellInfoList) {
            super.onCellInfoChanged(cellInfoList);
            getCellInfo(cellInfoList);
        }
    }

    private void getDeviceÌnfo() {
        dn.setText(MANUFACTURER);
        dm.setText(MODEL);
        dv.setText("Android " + VERSION.RELEASE);
    }

    //
    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    public void getCellInfo(List<CellInfo> cellInfoList) {
        try {
            for (CellInfo cellInfo : cellInfoList) {
                if (cellInfo instanceof CellInfoLte) {
                    // cast to CellInfoLte and call all the CellInfoLte methods you need
                    // gets RSRP cell signal strength:
                    cellSig = ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();

                    // Gets the LTE cell indentity: (returns 28-bit Cell Identity, Integer.MAX_VALUE if unknown)
                    cellID = ((CellInfoLte) cellInfo).getCellIdentity().getCi();
                    cellIDTextView.setText(String.valueOf(cellID));
                    Log.d("CellID ------->", String.valueOf(cellID)) ;

                    // Gets the LTE MCC: (returns 3-digit Mobile Country Code, 0..999, Integer.MAX_VALUE if unknown)
                    cellMcc = ((CellInfoLte) cellInfo).getCellIdentity().getMcc();
                    cellMccTextView.setText(String.valueOf(cellMcc));
                    Log.d("MCC ------------------>", String.valueOf(cellMcc)) ;


                    // Gets theLTE MNC: (returns 2 or 3-digit Mobile Network Code, 0..999, Integer.MAX_VALUE if unknown)
                    cellMnc = ((CellInfoLte) cellInfo).getCellIdentity().getMnc();
                    cellMncTextView.setText(String.valueOf(cellMnc));

                    // Gets the LTE PCI: (returns Physical Cell Id 0..503, Integer.MAX_VALUE if unknown)
                    cellPci = ((CellInfoLte) cellInfo).getCellIdentity().getPci();
                    cellPciTextView.setText(String.valueOf(cellPci));

                    // Gets the LTE TAC: (returns 16-bit Tracking Area Code, Integer.MAX_VALUE if unknown)
                    cellTac = ((CellInfoLte) cellInfo).getCellIdentity().getTac();
                    cellTacTextView.setText(String.valueOf(cellTac));

                }

            }
        } catch (Exception e) {
            Log.d("SignalStrength", "++++++++++++++++++++++ null array spot 2: " + e);
        }
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR1)
    private void getCellInfo() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            cellInfoList = mTelephonyManager.getAllCellInfo();
            getCellInfo(cellInfoList);
        } catch (Exception e) {
            Log.d("SignalStrength", "+++++++++++++++++++++++++++++++++++++++++ null array spot 1: " + e);
        }
    }

    private void initComponent() {
        txt_etat = (TextView) findViewById(R.id.state);
        rsrp = (TextView) findViewById(R.id.rsrp);
        rsrq = (TextView) findViewById(R.id.rsrq);
        rssi = (TextView) findViewById(R.id.rssi);
        cqi = (TextView) findViewById(R.id.cqi);
        SS = (TextView) findViewById(R.id.ss);

        btn_save = (Button) findViewById(R.id.button2);

        dn = (TextView) findViewById(R.id.dn);
        dm = (TextView) findViewById(R.id.dm);
        dv = (TextView) findViewById(R.id.dv);

        cellIDTextView = (TextView) findViewById(R.id.cellIDTextView);
        cellMccTextView = (TextView) findViewById(R.id.cellMccTextView);
        cellMncTextView = (TextView) findViewById(R.id.cellMncTextView);
        cellPciTextView = (TextView) findViewById(R.id.cellPciTextView);
        cellTacTextView = (TextView) findViewById(R.id.cellTacTextView);
    }

    private void initPhoneManager() {
        mPhoneStatelistener = new MyPhoneStateListener();
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    private CompleteAddress getCompleteAddress(double longitude, double latitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();

        return new CompleteAddress(address, city, state, country, postalCode);

    }

    private void getLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            try {
                                CompleteAddress ca = getCompleteAddress(location.getLongitude(), location.getLatitude());

                                Log.d("completeadress---->", ca.toString());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}

