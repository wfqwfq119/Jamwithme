package cse110.jamwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Storm Quark on 11/29/2016.
 * This is a class that can be extended from an activity which creates the menu for the context.
 */

public class CreateMenu extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.log_out:
                mAuth.signOut();
                startActivity(new Intent(this,logina_ctivity.class));
                break;
            case R.id.action_settings:
                //We never made a settings thing
                break;
            case R.id.navi_disprofile:
                startActivity(new Intent(this,ProfileDisplay.class));
                break;
            case R.id.navi_friend:
                startActivity(new Intent(this,friend_list.class));
                break;
            case R.id.matching:
                try {
                    Intent match = new Intent(this, MatchingDisplay.class);
                    match.putExtra("updated", "false");
                    startActivity(match);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.delete_acct:
                Toast.makeText(this, "Please verify account!", Toast.LENGTH_SHORT).show();
                try{
                    startActivity(new Intent(this, DeleteAccountActivity.class));
                }catch(Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
