package cse110.jamwithme;

import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Intent;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class messagerAct extends CreateMenu {
    private ArrayList<String> text_list;
    private ListView mes_list;
    private DatabaseReference mRootRef;
    private DatabaseReference mChildRef;
    private ArrayAdapter<String> mes_adp;
    private Button mes_senter;
    private message_model my_Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager);
        text_list = new ArrayList<String>();
        mes_list = (ListView) findViewById(R.id.message_List);
        mes_senter = (Button)findViewById(R.id.mes_sent);
        mes_adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, text_list);
        mes_list.setAdapter(mes_adp);

        String User_Uid = getIntent().getStringExtra("Uid");

        mRootRef = FirebaseDatabase.getInstance().getReference().getRoot();
        mChildRef = FirebaseDatabase.getInstance().getReference("/users/"+User_Uid+"/Message");

        mes_senter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = (EditText)findViewById(R.id.mes_Text);
                my_Message = new message_model(input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                        FirebaseAuth.getInstance().getCurrentUser().getUid());
                mRootRef.child("Message").push().setValue(my_Message);
                mChildRef.push().setValue(my_Message.getMessageText());
                input.setText("");
            }
        });

        mChildRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String message = dataSnapshot.getValue(String.class);
                    Log.v("E_CHILD_ADDED", message);
                    text_list.add(message);
                    mes_adp.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
