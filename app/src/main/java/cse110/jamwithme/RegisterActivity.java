package cse110.jamwithme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private String username;

    private EditText Re_Email;
    private EditText Re_Instr;
    private EditText Re_Name;
    private EditText Re_Pass;
    private Button Re_Button;

    private ProgressDialog Re_Pro;

    private FirebaseAuth fAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Re_Pro = new ProgressDialog(RegisterActivity.this);

        fAuth = FirebaseAuth.getInstance();

        Re_Email = (EditText)findViewById(R.id.Register_Email);
        Re_Instr = (EditText)findViewById(R.id.Re_Instr);
        Re_Name = (EditText)findViewById(R.id.Re_Username);
        Re_Pass = (EditText)findViewById(R.id.Register_password);
        Re_Button = (Button)findViewById(R.id.Re_button);

        Re_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    //Adds created user to Firebase database
    private void addToDatabase(String userinst) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            // Not logged in, launch the Log In activity
            startActivity(new Intent(RegisterActivity.this,logina_ctivity.class));
        }

        //Create user object and places into database
        User newUser = new User();
        newUser.setName(username);
        mDatabase.child("users").child(user.getUid()).setValue(newUser);

        //TODO Assign user instrument values
    }

    private void registerUser(){
        String email = Re_Email.getText().toString().trim();
        String password = Re_Pass.getText().toString().trim();
        username = Re_Name.getText().toString();

        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this,"Email or password field is empty",Toast.LENGTH_LONG).show();
            return;
        }
        Re_Pro.setMessage("Registering User...");
        Re_Pro.show();

        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Re_Pro.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Registered Successfully",Toast.LENGTH_LONG).show();
                    addToDatabase(Re_Instr.getText().toString()); //Add user to database
                    startActivity(new Intent(RegisterActivity.this,camera.class));
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Cannot register, please try again",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
