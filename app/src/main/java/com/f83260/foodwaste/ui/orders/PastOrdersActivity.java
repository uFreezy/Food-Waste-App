package com.f83260.foodwaste.ui.orders;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.f83260.foodwaste.PastOrderDetails;
import com.f83260.foodwaste.R;
import com.f83260.foodwaste.data.AuthDataSource;
import com.f83260.foodwaste.data.DataRepository;
import com.f83260.foodwaste.data.UserRepository;
import com.f83260.foodwaste.data.model.Opportunity;
import com.f83260.foodwaste.data.model.Store;
import com.f83260.foodwaste.databinding.ActivityPastOrdersBinding;

import java.util.List;

public class PastOrdersActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPastOrdersBinding binding;

    private DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.dataRepository = DataRepository.getInstance(getApplicationContext());

        binding = ActivityPastOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        List<Opportunity> opps = this.dataRepository
                .getOpportunitiesForUser(UserRepository.getInstance(new AuthDataSource()).loggedUser().getUserId());

        FragmentManager fm = getSupportFragmentManager();

        for (Opportunity opp : opps){
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            Store store = this.dataRepository.getStoreById(opp.getStoreId());
            Fragment fr = PastOrderDetails.newInstance(opp.getProductName(), "1", store.getName());
            fragmentTransaction.add(R.id.past_orders_list, fr, null);
            fragmentTransaction.commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // define the behaviour for the back arrow
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}