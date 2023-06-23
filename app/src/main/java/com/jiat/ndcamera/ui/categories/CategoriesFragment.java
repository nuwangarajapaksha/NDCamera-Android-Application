package com.jiat.ndcamera.ui.categories;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jiat.ndcamera.MainActivity;
import com.jiat.ndcamera.R;
import com.jiat.ndcamera.adapter.CategoryAdapter;
import com.jiat.ndcamera.model.Category;

import java.util.ArrayList;
import java.util.List;


public class CategoriesFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private List<Category> categoryList;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        categoryList = new ArrayList<>();

        //insertSampleData();

    }

    private void insertSampleData() {

        CollectionReference reference = firebaseFirestore.collection("categories");

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("1", "Digital Cameras", "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/3115/conversions/canon_eos_r5_mirrorless_digital-thumb.jpg"));
        categories.add(new Category("2", "Pro Video", "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/4120/conversions/panasonic_hc_x1000_rk_ultra_hd_1409737026_1077993-thumb.jpg"));
        categories.add(new Category("3", "Lenses", "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/3117/conversions/Canon_2519A003_85mm_f_1_8_USM_Autofocus_1266925629_12182-%281%29-thumb.jpg"));
        categories.add(new Category("4", "Drones", "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/5866/conversions/drone-thumb.jpg"));
        categories.add(new Category("5", "Tripods", "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/1573/conversions/oben_at_3565_bz_217t_at_3565_aluminum_tripod_with_1538585177_1412819-thumb.jpg"));
        categories.add(new Category("6", "Accessories", "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/5445/conversions/Accessories-thumb.jpg"));

        for(Category c : categories){
            reference.add(c);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        MainActivity activity =(MainActivity) getActivity();
        activity.showBottomNavigationView(true);

        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.categories_categories_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        CategoryAdapter adapter = new CategoryAdapter(categoryList, view.getContext());
        recyclerView.setAdapter(adapter);

        firebaseFirestore.collection("categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    categoryList.clear();
                    for(QueryDocumentSnapshot snapshot : task.getResult()){
                        Category category = snapshot.toObject(Category.class);
                        categoryList.add(category);
                    }

                    adapter.notifyDataSetChanged();

                }
            }
        });





    }
}