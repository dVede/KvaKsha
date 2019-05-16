package com.example.registration;

import android.content.Intent;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;




public class SlideMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActionBarDrawerToggle mToggle;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) { //TODO: replace placeholders in header.xml

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_menu);
        DrawerLayout mDrawerLayout = findViewById(R.id.slide_menu);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();



        updateInfo();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.profile);
        }
    }

    public void updateInfo(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("/users/" + firebaseUser.getUid());
        Log.d("Heh", "////" + reference.toString());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                final TextView username = findViewById(R.id.textView1);
                final TextView email = findViewById(R.id.textView2);
                username.setText(user.getUsername());
                email.setText(user.getEmail());
                ImageView imageCircle = findViewById(R.id.profile_photo);
                Log.d("URL", "////" + reference.toString());
                Picasso.get().load(user.getProfileImageUrl()).into(imageCircle);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id){
            case R.id.profile:
                setTitle("Profile");
                ProfileFragment fragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction() .replace(R.id.fragment_container, fragment).commit();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(SlideMenu.this, LoginIn.class);
                startActivity(intent);
                finish();
                break;
            case R.id.private_messages:
                Intent intentPM = new Intent(SlideMenu.this, EnterPrivateMessage.class);
                startActivity(intentPM);
                break;
            case R.id.chatroom_messages:
                Intent intentCM = new Intent(SlideMenu.this, EnterChatroomMessage.class);
                startActivity(intentCM);
                break;
            case R.id.create_chatroom:
                Intent intentCC = new Intent(SlideMenu.this, CreateChatroom.class);
                startActivity(intentCC);
                break;
            default:
        }
        DrawerLayout drawer = findViewById(R.id.slide_menu);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.slide_menu);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}