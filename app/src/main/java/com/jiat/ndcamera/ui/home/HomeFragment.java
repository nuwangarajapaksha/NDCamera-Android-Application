package com.jiat.ndcamera.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jiat.ndcamera.MainActivity;
import com.jiat.ndcamera.R;
import com.jiat.ndcamera.adapter.ProductAdapter;
import com.jiat.ndcamera.adapter.SliderAdapter;
import com.jiat.ndcamera.model.Category;
import com.jiat.ndcamera.model.Product;
import com.jiat.ndcamera.model.SliderItem;
import com.jiat.ndcamera.ui.product.ProductDetailsFragment;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    private static final String TAG = "eshop";
    private FirebaseFirestore firebaseFirestore;
    private GridView productView;
    private List<Product> products;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseFirestore = FirebaseFirestore.getInstance();
        products = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity activity = (MainActivity) getActivity();
        activity.showBottomNavigationView(true);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SliderView sliderView = view.findViewById(R.id.imageSlider);
        SliderAdapter sliderAdapter = new SliderAdapter(view.getContext());
        sliderAdapter.addItem(new SliderItem("", "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/10479/Megasale_d.jpg"));
        sliderAdapter.addItem(new SliderItem("", "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/10298/Mid-Year-Super-Sale_Web.jpg"));
        sliderAdapter.addItem(new SliderItem("", "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/10264/Godox-Dealz_Web.jpg"));
        sliderAdapter.addItem(new SliderItem("", "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/10301/Clearance-Sale_Web.jpg"));
        sliderAdapter.addItem(new SliderItem("", "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/10447/DJI-Ronin-RS3_Web-II-%281%29.jpg"));
        sliderAdapter.addItem(new SliderItem("", "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/8946/Go-Pro-10_Web.jpg"));
        sliderAdapter.addItem(new SliderItem("", "https://s3-ap-southeast-1.amazonaws.com/media.cameralk.com/8941/Sirui_Web.jpg"));

        sliderView.setSliderAdapter(sliderAdapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setScrollTimeInSec(5);
        sliderView.startAutoCycle();


        productView = view.findViewById(R.id.home_product_list);

        ProductAdapter adapter = new ProductAdapter(view.getContext(), products);
        productView.setAdapter(adapter);


            firebaseFirestore.collection("products").whereEqualTo("category", "1").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                products.clear();
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    Product product = snapshot.toObject(Product.class);
                                    products.add(product);
                                }
                                adapter.notifyDataSetChanged();

                                if (task.getResult().isEmpty()) {
                                    productView.setVisibility(View.GONE);
                                    view.findViewById(R.id.home_no_product_text).setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });


        productView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Product product = products.get(i);
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, new ProductDetailsFragment(product));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        productView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {

//                    // load more products like pagination

//                    view.findViewById(R.id.home_message).setVisibility(View.VISIBLE);
//
//                    products.add(new Product("P1", "T Shirt", "asfadasd", "", 2200, "1"));
//                    products.add(new Product("P2", "Trouser", "asfadasd", "", 4000, "1"));
//                    products.add(new Product("P3", "Jeans", "asfadasd", "", 3200, "1"));
//                    products.add(new Product("P4", "Belt", "asfadasd", "", 1500, "1"));
//                    products.add(new Product("P5", "Short", "asfadasd", "", 2200, "1"));
//                    adapter.notifyDataSetChanged();
//
//
//                    view.findViewById(R.id.home_message).setVisibility(View.INVISIBLE);
                }

            }
        });

    }


}