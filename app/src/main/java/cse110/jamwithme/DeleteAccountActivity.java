package cse110.jamwithme;

/**
 * Created by Storm Quark on 11/20/2016.
 * This class allows the user to delete their account. It asks the user
 * to enter the correct login information to confirm they want to delete
 * the account.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeleteAccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        final EditText useremail = (EditText)findViewById(R.id.Login_email);
        final EditText userpass = (EditText)findViewById(R.id.Login_pass);
        Button delete_button = (Button)findViewById(R.id.delete_button);

        mAuth = FirebaseAuth.getInstance();

        final DatabaseWatcher d = new DatabaseWatcher(this);

        //After user presses button to delete
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pull data from fields
                String ue = useremail.getText().toString().trim();
                String up = userpass.getText().toString().trim();
                if(TextUtils.isEmpty(ue)||TextUtils.isEmpty(up)) {
                    System.out.println("Empty field");
                    Toast.makeText(DeleteAccountActivity.this,"Email or password field is empty",Toast.LENGTH_LONG)
                            .show();
                }
                final FirebaseUser user = mAuth.getCurrentUser();

                //re-authenticate credential
                AuthCredential credential = EmailAuthProvider.getCredential(ue, up);

                // check credentials and then delete
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                d.deleteUserFromDatabase();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(DeleteAccountActivity.this, "User " +
                                                            "deleted!", Toast
                                                            .LENGTH_LONG)
                                                            .show();
                                                    startActivity(new Intent(DeleteAccountActivity
                                                            .this, logina_ctivity.class));
                                                }
                                                else {
                                                    Toast.makeText(DeleteAccountActivity.this,
                                                            "Could not delete user!", Toast
                                                            .LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        });
            }
        });
    }
}
