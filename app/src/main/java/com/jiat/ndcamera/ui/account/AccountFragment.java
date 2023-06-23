package com.jiat.ndcamera.ui.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jiat.ndcamera.MainActivity;
import com.jiat.ndcamera.R;
import com.jiat.ndcamera.model.User;
import com.jiat.ndcamera.ui.profile.ProfileFragment;

public class AccountFragment extends Fragment {

    private TextView textViewName, textViewEmail;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    public AccountFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewName = view.findViewById(R.id.account_username);
        textViewEmail = view.findViewById(R.id.account_user_email);

        updateUI(firebaseAuth.getCurrentUser());

        view.findViewById(R.id.account_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ProfileFragment());
            }
        });

    }

    private void updateUI(FirebaseUser currentUser) {
        firebaseFirestore.collection("users").whereEqualTo("id",currentUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                User user = snapshot.toObject(User.class);
                                textViewName.setText(user.getName());
                                textViewEmail.setText(user.getEmail());
//                                    Glide.with(MainActivity.this).load(R.drawable.ic_baseline_profile_24).circleCrop().override(100, 100).into(imageViewProfile);
                            }

                        }
                    }
                });
    }

    public void loadFragment(Fragment fragment){
        FragmentManager supportFragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack("account");
        fragmentTransaction.commit();

//        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
    }



}