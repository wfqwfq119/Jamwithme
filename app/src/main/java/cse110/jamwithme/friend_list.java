package cse110.jamwithme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class friend_list extends CreateMenu {

    private ListView recent_find;
    private ListView friend_list;
    static ArrayList<friend_obj> friend_Array = new ArrayList<friend_obj>();;
    private ArrayAdapter<friend_obj> friend_ArrayAdp;
    private ArrayList<String> friend_Array_show;
    private ArrayAdapter<String> friend_showAdp;
    private DatabaseReference friend_ref;
    String User_Uid;
    String User_name;
    Intent firend_selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        firend_selected = new Intent(this,messagerAct.class);

        recent_find = (ListView)findViewById(R.id.recfind_list);
        friend_list = (ListView)findViewById(R.id.friend_list);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        friend_ref = myRef.child("users").child(user.getUid()).child("friends");

        if(friend_ref == null) {
            myRef.child("users").child(user.getUid()).setValue("friends");
        }

        //object array to contain user UID and user name
        friend_Array =  new ArrayList<friend_obj>();
        friend_ArrayAdp = new ArrayAdapter<friend_obj>(this,android.R.layout.simple_list_item_1,friend_Array);

        //String array to show user name
        friend_Array_show = new ArrayList<String>();
        friend_showAdp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,friend_Array_show);

        friend_list.setAdapter(friend_showAdp);

        friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                friend_obj chat_to_friend = friend_ArrayAdp.getItem(position);
                firend_selected.putExtra("Uid",chat_to_friend.getUser_Uid());
                firend_selected.putExtra("name",chat_to_friend.getUser_name());
                startActivity(firend_selected);
            }
        });

        final Context c = this;

        final DatabaseReference userref = myRef.child("users");

        DatabaseReference fref = myRef.child("users").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child("friends");

        final DatabaseWatcher d = new DatabaseWatcher(this);

        fref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Toast.makeText(c, "top level ds: " + dataSnapshot.getKey().toString(), Toast
                            .LENGTH_SHORT).show();
                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        Toast.makeText(c, "ds: " + ds.getKey().toString(), Toast.LENGTH_SHORT).show();
                        User_Uid = ds.getKey().toString();

                        User_name = ds.getValue().toString();
                        Toast.makeText(c, "array edits", Toast.LENGTH_SHORT).show();
                        Toast.makeText(c, "nam" + User_name, Toast.LENGTH_SHORT).show();
                        friend_Array_show.add(User_name);
                        friend_Array.add(new friend_obj(User_Uid, User_name));
                        friend_showAdp.notifyDataSetChanged();
                    }
                }
                else {
                    Toast.makeText(c, "Data fref doesn't exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        /*friend_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User_Uid = dataSnapshot.child("userId").toString();
                User_name = dataSnapshot.child("name").getValue().toString();
                //System.out.println(User_name);
                friend_Array_show.add(User_name);
                friend_Array.add(new friend_obj(User_Uid,User_name));
                friend_showAdp.notifyDataSetChanged();
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
        });*/
    }

}
