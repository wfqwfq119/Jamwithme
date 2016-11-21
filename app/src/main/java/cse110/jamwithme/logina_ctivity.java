package cse110.jamwithme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class logina_ctivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText Login_name;
    private EditText Login_Pass;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logina_ctivity);

        Login_name = (EditText)findViewById(R.id.Login_name);
        Login_Pass = (EditText)findViewById(R.id.Login_pass);
        Button Login_button = (Button)findViewById(R.id.Login_botton);
        final TextView register_link = (TextView)findViewById(R.id.Login_tv);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    //finish();
                    startActivity(new Intent(logina_ctivity.this, UserProfileActivity.class));
                }
            }
        };

        register_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(logina_ctivity.this,RegisterActivity.class);
                logina_ctivity.this.startActivity(registerIntent);
            }
        });
        Login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mAuth.signOut();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void startSignIn(){
        String username = Login_name.getText().toString();
        String password = Login_Pass.getText().toString();
        if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            Toast.makeText(logina_ctivity.this, "Fields are empty", Toast.LENGTH_LONG).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(logina_ctivity.this, "Sign In problem", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
