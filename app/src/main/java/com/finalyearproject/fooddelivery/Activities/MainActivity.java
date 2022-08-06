package com.finalyearproject.fooddelivery.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.finalyearproject.fooddelivery.R;
import com.finalyearproject.fooddelivery.Singletones.FoodDelivery;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button customerLogin;
    private Button supplierLogin;
    private Button driverLogin;
    private FoodDelivery foodDelivery;

    private static final int CUSTOMER_SIGN_IN = 100;
    private static final int SUPPLIER_SIGN_IN = 200;
    private static final int DRIVER_SIGN_IN = 300;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private Intent signInIntent;
    private Task<GoogleSignInAccount> task;
    private GoogleSignInAccount account;
    private AuthCredential credential;

    private Class activityClass;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            account = task.getResult(ApiException.class);
            credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                switch (requestCode) {
                                    case CUSTOMER_SIGN_IN:
                                        activityClass = CustomerActivity.class;
                                        foodDelivery.setUserType("Customer");
                                        break;
                                    case SUPPLIER_SIGN_IN:
                                        activityClass = SupplierActivity.class;
                                        foodDelivery.setUserType("Supplier");
                                        break;
                                    case DRIVER_SIGN_IN:
                                        activityClass = DriverActivity.class;
                                        foodDelivery.setUserType("Driver");
                                        break;
                                }
                                startActivity(new Intent(MainActivity.this, activityClass));
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customerLogin = findViewById(R.id.customer_login);
        supplierLogin = findViewById(R.id.supplier_login);
        driverLogin = findViewById(R.id.driver_login);
        foodDelivery = FoodDelivery.getInstance();

        customerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, CUSTOMER_SIGN_IN);
            }
        });
        supplierLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, SUPPLIER_SIGN_IN);
            }
        });
        driverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, DRIVER_SIGN_IN);
            }
        });

        Dexter.withContext(MainActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken("417820360031-g59r42ndcg8cts7tr9mlihugfal18rm2.apps.googleusercontent.com")
                                .requestEmail()
                                .build();

                        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
                        mAuth = FirebaseAuth.getInstance();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();
    }
}