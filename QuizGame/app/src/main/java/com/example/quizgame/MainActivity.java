package com.example.quizgame;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.quizgame.Common.Common;
import com.example.quizgame.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    EditText userName, pass; //for sign in
    EditText newUserName, newPass, newEmail; //for sign up


    FirebaseDatabase databaseF;
    DatabaseReference usersData;

    Button btnSignUp, btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = (EditText)findViewById(R.id.userName);
        pass=(EditText)findViewById(R.id.password);

        //firebase part
        databaseF = FirebaseDatabase.getInstance();
        usersData= databaseF.getReference("Users");

        btnSignUp=(Button)findViewById(R.id.btnSignUp);
        btnSignIn= (Button)findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(userName.getText().toString(),
                        pass.getText().toString() );
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpDialog();
            }


        });

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String savedUser = prefs.getString("name", null);
        String savedPass = prefs.getString("password", null);
        if(savedUser != null && savedPass != null){
            userName.setText(savedUser);
            pass.setText(savedPass);
        }

    }

    private void signUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please fill!");

        LayoutInflater inflater =this.getLayoutInflater();
        View sign_up_layout=inflater.inflate(R.layout.sign_up_layout,null);

        newUserName=(EditText)sign_up_layout.findViewById(R.id.newUserName);
        newEmail=(EditText)sign_up_layout.findViewById(R.id.newEmail);
        newPass=(EditText)sign_up_layout.findViewById(R.id.newPassword);


        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final User users = new User(newUserName.getText().toString(),
                        newPass.getText().toString(),
                        newEmail.getText().toString());
                usersData.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("signin","asd");
                        if(dataSnapshot.child(users.getUserName()).exists())
                            Toast.makeText(MainActivity.this, "This user already exists!", Toast.LENGTH_SHORT).show();
                        else{
                            usersData.child(users.getUserName()).setValue(users);
                            Toast.makeText(MainActivity.this, "Successful registration!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("signin","error");
                    }
                });
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
    
    private void signIn(final String userN, final String password) {
        usersData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userN).exists())
                {
                    Log.d("signin","asd");
                    if(!userN.isEmpty()){
                        User logIn = dataSnapshot.child(userN).getValue(User.class);
                        if(logIn.getPassword().equals(password)) {
                             //Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("name",userN);
                            editor.putString("password",password);
                            editor.apply();

                            Intent homeActivity=new Intent(MainActivity.this,Home.class);
                            startActivity(homeActivity);
                            Common.currentUser1=logIn;
                            finish();

                        }
                        else{
                            Toast.makeText(MainActivity.this, "The password is incorrect!", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("name",null);
                            editor.putString("password",null);
                            editor.apply();
                        }

                    }else{
                        Toast.makeText(MainActivity.this, "Enter your username, please.", Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                    Toast.makeText(MainActivity.this, "The user doesn't exist!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError dataErr) {

            }
        });
        usersData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userN).exists())
                {
                    if(!userN.isEmpty()){
                        User logIn = dataSnapshot.child(userN).getValue(User.class);
                        if(logIn.getPassword().equals(password)) {
                            Common.currentUser1=logIn;
                             Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(MainActivity.this, "The password is incorrect!", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(MainActivity.this, "Enter your username, please.", Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                    Toast.makeText(MainActivity.this, "The user doesn't exist!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError dataErr) {

            }
        });
    }


}



