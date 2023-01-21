package com.f83260.foodwaste.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceFragmentCompat;

import com.f83260.foodwaste.R;
import com.f83260.foodwaste.data.AuthDataSource;
import com.f83260.foodwaste.data.Result;
import com.f83260.foodwaste.data.UserRepository;
import com.f83260.foodwaste.data.model.LoggedInUser;
import com.f83260.foodwaste.databinding.SettingsActivityBinding;
import com.f83260.foodwaste.ui.common.dto.UserDto;
import com.f83260.foodwaste.ui.login.LoginActivity;
import com.f83260.foodwaste.ui.settings.dto.EditProfileFormDto;

public class SettingsActivity extends AppCompatActivity {
    private SettingsActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EditProfileFormDto dto = new EditProfileFormDto(binding);

        // prefil the form
        this.initForm(dto);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // edit profile button
        binding.saveProfile.setOnClickListener(event ->{
            UserDto userDto = new UserDto(binding.profileForm);
            Result updateRes = UserRepository.getInstance(new AuthDataSource()).updateProfile(userDto);

            if (updateRes instanceof Result.Success){
                Toast.makeText(getApplicationContext(), "Profile updated successfully!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to update profile!", Toast.LENGTH_LONG).show();
            }
        });
        
        // logout action
        binding.logoutBtn.setOnClickListener(event ->{
            UserRepository.getInstance(new AuthDataSource()).logout();
            Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(), "Logged out successfully!", Toast.LENGTH_LONG).show();
        });

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

    private void initForm(EditProfileFormDto form){
        LoggedInUser user = UserRepository.getInstance(new AuthDataSource()).loggedUser();
        form.getFirstNameEditText().setText(user.getFirstName());
        form.getLastNameEditText().setText(user.getLastName());
        form.getPhoneNameEditText().setText(user.getPhone());
        form.getUsernameEditText().setText(user.getDisplayName());

        // make email readonly
        form.getUsernameEditText().setEnabled(false);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}