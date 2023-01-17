package com.f83260.foodwaste;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f83260.foodwaste.databinding.ActivityMainBinding;
import com.f83260.foodwaste.databinding.FragmentPastOrderDetailsBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PastOrderDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastOrderDetails extends Fragment {
    private static final String ITEM_NAME = "itemName";
    private static final String QUANTITY = "quantity";
    private static final String STORE_NAME = "storeName";

    private FragmentPastOrderDetailsBinding binding;


    public PastOrderDetails() {
        // Required empty public constructor
    }
    public static PastOrderDetails newInstance(String itemName, String quantity, String storeName) {
        PastOrderDetails fragment = new PastOrderDetails();
        Bundle args = new Bundle();
        args.putString(ITEM_NAME, itemName);
        args.putString(QUANTITY, quantity);
        args.putString(STORE_NAME, storeName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentPastOrderDetailsBinding.inflate(getLayoutInflater());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View vw = inflater.inflate(R.layout.fragment_past_order_details, container, false);

        if (getArguments() != null) {
            TextView itemName = vw.findViewById(R.id.itemName);
            TextView quantity = vw.findViewById(R.id.quantity);
            TextView storeName = vw.findViewById(R.id.storeName);

            itemName.setText("Product: " + getArguments().getString(ITEM_NAME));
            quantity.setText("Quantity: " + getArguments().getString(QUANTITY));
            storeName.setText("Store: " + getArguments().getString(STORE_NAME));

        }

        return vw;
    }
}