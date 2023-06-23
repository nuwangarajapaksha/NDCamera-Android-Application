package com.jiat.ndcamera.ui.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jiat.ndcamera.MainActivity;
import com.jiat.ndcamera.R;
import com.jiat.ndcamera.adapter.ProductAdapter;
import com.jiat.ndcamera.model.Category;
import com.jiat.ndcamera.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProductFragment extends Fragment {
    private static final String TAG = "eshop";
    private FirebaseFirestore firebaseFirestore;
    private GridView productView;
    private Category category;
    private List<Product> products;

    public ProductFragment(Category category) {
        this.category = category;
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseFirestore = FirebaseFirestore.getInstance();
        products = new ArrayList<>();


        //insertSampleData();

    }


    private void insertSampleData() {
        CollectionReference products = firebaseFirestore.collection("products");

        List<Product> productsList = new ArrayList<>();
        productsList.add(new Product("P9", "Panasonic AG-UX90 UHD 4K Professional Camcorder", "For a wide range of high-resolution and professional options when mobile or in the studio, use the Panasonic AG-UX90 UHD 4K Camcorder for recording up to UHD 3840 x 2160 video and 8-mode gamma. The AG-UX90 features a large, 1.0-type MOS sensor that creates a dramatic bokeh with its wide-angle lens, and its focal length with 15x optical zoom ranges from 25.4 to 367.5mm. The camera's 5-axis Hybrid Image Stabilizer provides anti-shake correction for smooth movement, and the 3-ring lens design allows for precision iris/focus/zoom manual control. The shutter system functions in three modes—System, Slow, and Syncro Scan—which allow for shutter speeds from 1/60 to 1/8000 per second. The three-step variable gain switch allows for greater control over brightness and shutter. ", "P9", 549500, "2"));
        productsList.add(new Product("P10", "Sony PXW-Z90T 4K HDR XDCAM with Fast Hybrid AF Camcorder", "The Sony PXW-Z90 4K HDR XDCAM camcorder offers phase-detection autofocus (AF) and HDR capabilities in a compact, palm-style body. Suitable for corporate events, broadcast news, and television production, the PXW-Z90 camcorder features a stacked 1\" Exmor RS CMOS sensor with UHD 4K (3840 x 2160) resolution, a 12x Zeiss optical zoom, an OLED viewfinder, and 3.5\" touchscreen LCD monitor", "P10", 842500, "2"));
        productsList.add(new Product("P11", "Sigma 35mm f/1.4 DG HSM Art Lens for Nikon F", "An optically advanced wide-angle prime, the Nikon F 35mm f/1.4 DG HSM Art Lens from Sigma mixes a comfortable focal length with an especially fast design. The bright f/1.4 maximum aperture helps to achieve shallow depth of field and selective focus effects, and also suits working in difficult lighting conditions. The optical layout incorporates both low", "P11", 339500, "3"));
        productsList.add(new Product("P12", "Sigma 12-24mm f/4 DG HSM Art Lens for Nikon F", "Covering a useful variety of ultra-wide focal lengths, the Nikon F-mount Sigma 12-24mm f/4 DG HSM is an Art-series zoom, characterized by its advanced design to greatly reduce distortion for a high degree of sharpness. A series of low dispersion and aspherical elements minimize both chromatic and spherical aberrations throughout the zoom range", "P12", 392500, "3"));
        productsList.add(new Product("P13", "DJI Mavic 3 Cine Premium Combo", "Geared towards professional content creation, the Mavic 3 Cine Premium Combo from DJI adds the ability to use the Apple ProRes 422 HQ codec, and it bumps the internal storage from 8GB to a monster 1TB SSD. Outside of that, the Cine Premium sports the same impressive dual cameras in the drone's 3-axis gimbal. ", "P13", 1939500, "4"));
        productsList.add(new Product("P14", "Ryze Tech Tello Quadcopter", "Built in partnership with Ryze Tech, the DJI Tello Quadcopter (2019) is designed to be a fun and educational quadcopter drone. The 2019 edition features a larger and more robust form factor and includes a micro-USB cable.", "P14", 45500, "4"));
        productsList.add(new Product("P15", "Yunteng VCT-680RM Aluminum Tripod", "Tripod, high-quality aluminum alloy and are selected through special surface treatment, produced by a rigorous examination, lightweight and sturdy and easy to carry. And able to adapt to the crude complex environment of the terrain.", "P15", 9000, "5"));
        productsList.add(new Product("P16", "Sirui Traveler 7A Aluminium Tripod with E-10 ball head", "_____", "P16", 32500, "5"));
        productsList.add(new Product("P17", "Nikon EN-EL 18c Rechargeable Lithium-Ion Battery", "The Nikon EN-EL 18c Rechargeable Battery has a capacity of 2500mAh, an output of 10.8V, and 27Wh. Also, the battery can charge within the MH-26a or MH-26aAK chargers, and it can be used as a replacement battery for the Nikon D5, D4S, and the D4.", "P17", 75500, "6"));
        productsList.add(new Product("P18", "Sony VG-C4EM Vertical Grip", "Dedicated to the Sony Alpha a7R IV and a9 II mirrorless cameras, the VG-C4EM Vertical Grip offers both extended battery and a more comfortable grip when shooting in the vertical orientation.", "P18", 91000, "6"));


        for (Product p : productsList) {
            products.add(p);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        MainActivity activity = (MainActivity) getActivity();
        activity.showBottomNavigationView(false);

        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productView = view.findViewById(R.id.product_product_list);

        ProductAdapter adapter = new ProductAdapter(view.getContext(), products);
        productView.setAdapter(adapter);


        if (this.category != null) {
            firebaseFirestore.collection("products").whereEqualTo("category", this.category.getId()).get()
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

                                if (task.getResult().isEmpty()){
                                    productView.setVisibility(View.GONE);
                                    view.findViewById(R.id.product_no_product_text).setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
        }


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

//                    view.findViewById(R.id.product_message).setVisibility(View.VISIBLE);
//
//                    products.add(new Product("P1", "T Shirt", "asfadasd", "", 2200, "1"));
//                    products.add(new Product("P2", "Trouser", "asfadasd", "", 4000, "1"));
//                    products.add(new Product("P3", "Jeans", "asfadasd", "", 3200, "1"));
//                    products.add(new Product("P4", "Belt", "asfadasd", "", 1500, "1"));
//                    products.add(new Product("P5", "Short", "asfadasd", "", 2200, "1"));
//                    adapter.notifyDataSetChanged();
//
//
//                    view.findViewById(R.id.product_message).setVisibility(View.INVISIBLE);
                }

            }
        });


    }
}