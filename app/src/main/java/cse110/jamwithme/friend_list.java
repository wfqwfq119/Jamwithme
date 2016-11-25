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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class friend_list extends AppCompatActivity {

    private ListView recent_find;
    private ListView friend_list;
    private ArrayList<friend_obj> friend_Array;
    private ArrayAdapter<friend_obj> friend_ArrayAdp;
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

        friend_ref = FirebaseDatabase.getInstance().getReference().child("users");

        friend_Array =  new ArrayList<friend_obj>();
        friend_ArrayAdp = new ArrayAdapter<friend_obj>(this,android.R.layout.simple_list_item_1,friend_Array);
        friend_list.setAdapter(friend_ArrayAdp);

        friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                friend_obj chat_to_friend = friend_ArrayAdp.getItem(position);
                firend_selected.putExtra("Uid",chat_to_friend.getUser_Uid());
                firend_selected.putExtra("name",chat_to_friend.getUser_name());
                startActivity(firend_selected);
            }
        });


        friend_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User_Uid = dataSnapshot.child("userId").toString();
                User_name = dataSnapshot.child("name").toString();
                friend_Array.add(new friend_obj(User_Uid,User_name));
                friend_ArrayAdp.notifyDataSetChanged();
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
