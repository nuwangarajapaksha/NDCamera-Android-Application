package com.jiat.ndcamera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jiat.ndcamera.model.User;
import com.jiat.ndcamera.ui.account.AccountFragment;
import com.jiat.ndcamera.ui.cart.CartFragment;
import com.jiat.ndcamera.ui.categories.CategoriesFragment;
import com.jiat.ndcamera.ui.home.HomeFragment;
import com.jiat.ndcamera.ui.message.MessageFragment;
import com.jiat.ndcamera.ui.orders.OrdersFragment;
import com.jiat.ndcamera.ui.profile.ProfileFragment;
import com.jiat.ndcamera.ui.wishlist.WishlistFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        NavigationBarView.OnItemSelectedListener {

    private static final String TAG = "ndcamera";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        updateUI(firebaseAuth.getCurrentUser());


        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this);

        loadFragment(new HomeFragment(),false);






    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.side_nav_profile).setVisible(true);
            menu.findItem(R.id.side_nav_orders).setVisible(true);
            menu.findItem(R.id.side_nav_wishlist).setVisible(true);
            menu.findItem(R.id.side_nav_message).setVisible(true);

            menu.findItem(R.id.side_nav_signin).setVisible(false);
            menu.findItem(R.id.side_nav_signout).setVisible(true);

            View headerView = navigationView.getHeaderView(0);
            ImageView imageViewProfile = headerView.findViewById(R.id.profilePic);
            TextView textViewName = headerView.findViewById(R.id.userName);
            TextView textViewEmail = headerView.findViewById(R.id.userEmail);

            imageViewProfile.setVisibility(View.VISIBLE);
            textViewName.setVisibility(View.VISIBLE);
            textViewEmail.setVisibility(View.VISIBLE);

            headerView.findViewById(R.id.header_title).setVisibility(View.GONE);


            firebaseFirestore.collection("users").whereEqualTo("id",currentUser.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot snapshot : task.getResult()){
                                    User user = snapshot.toObject(User.class);
                                    textViewName.setText(user.getName());
                                    textViewEmail.setText(user.getEmail());
                                    Glide.with(MainActivity.this).load(R.drawable.ic_baseline_profile_24).circleCrop().override(100, 100).into(imageViewProfile);
                                }

                            }
                        }
                    });

        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            int entryCount = getSupportFragmentManager().getBackStackEntryCount();
            if (entryCount > 0){
                getSupportFragmentManager().popBackStack();
            }else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show();
        switch (item.getItemId()){
            case R.id.side_nav_home:
            case R.id.bottom_nav_home:
                loadFragment(new HomeFragment(),false);
                break;
            case R.id.side_nav_profile:
            case R.id.bottom_nav_profile:
                if (firebaseAuth.getCurrentUser() != null){
                    loadFragment(new AccountFragment(),false);
                }else {
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                }
                break;
            case R.id.bottom_nav_cart:
                if (firebaseAuth.getCurrentUser() != null){
                    loadFragment(new CartFragment(),false);
                }else {
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                }
                break;
            case R.id.bottom_nav_categories:
                loadFragment(new CategoriesFragment(),true);
                break;
            case R.id.side_nav_orders:
                loadFragment(new OrdersFragment(),false);
                break;
            case R.id.side_nav_wishlist:
                loadFragment(new WishlistFragment(),false);
                break;
            case R.id.side_nav_message:
                loadFragment(new MessageFragment(),false);
                break;
            case R.id.side_nav_signin:
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.side_nav_signout:
                firebaseAuth.signOut();
                finish();
                startActivity(getIntent());
                break;
        }
        return true;
    }

    public void loadFragment(Fragment fragment,boolean addToBackStack){
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        if (addToBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

//        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
    }

    public void showBottomNavigationView(boolean show){
        if (show){
            bottomNavigationView.setVisibility(View.VISIBLE);
        }else{
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

}