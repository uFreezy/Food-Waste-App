package com.f83260.foodwaste.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProvider;

import com.f83260.foodwaste.databinding.ActivityRegisterBinding;
import com.f83260.foodwaste.ui.MainActivity;
import com.f83260.foodwaste.R;
import com.f83260.foodwaste.ui.login.LoggedInUserView;
import com.f83260.foodwaste.ui.register.dto.RegistrationFromDto;

public class RegisterActivity extends AppCompatActivity {
    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);

        RegistrationFromDto form = new RegistrationFromDto(binding);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        registerViewModel.getRegisterFormState().observe(this, registerFormState -> {
            if (registerFormState == null) {
                return;
            }
            form.getRegisterButton().setEnabled(registerFormState.isDataValid());

            this.digestErrors(registerFormState, form);
        });

        registerViewModel.getRegisterResult().observe(this, registerResult -> {
            if (registerResult == null) {
                return;
            }

            form.getLoadingProgressBar().setVisibility(View.GONE);

            if (registerResult.getError() != null) {
                showRegisterFailed(registerResult.getError());
            }else if (registerResult.getSuccess() != null) {
                updateUiWithUser(registerResult.getSuccess());

                setResult(Activity.RESULT_OK);

                //Complete and destroy register activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.registerDataChanged(form);
            }
        };
        form.getUsernameEditText().addTextChangedListener(afterTextChangedListener);
        form.getPasswordEditText().addTextChangedListener(afterTextChangedListener);

        // clicked on the 'register' button
        form.getRegisterButton().setOnClickListener(v -> {
            form.getLoadingProgressBar().setVisibility(View.VISIBLE);

            registerViewModel.register(form.getFirstNameEditText().getText().toString(),
                    form.getLastNameEditText().getText().toString(),
                    form.getPhoneNameEditText().getText().toString(),
                    form.getUsernameEditText().getText().toString(),
                    form.getPasswordEditText().getText().toString());

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

    private void showRegisterFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();

        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i);

        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void digestErrors(RegisterFormState formState, RegistrationFromDto form ){
        if (formState.getFirstNameError() != null){
            form.getFirstNameEditText().setError(getString(formState.getFirstNameError()));
        }
        if (formState.getLastNameError() != null){
            form.getLastNameEditText().setError(getString(formState.getLastNameError()));
        }
        if (formState.getUsernameError() != null) {
            form.getUsernameEditText().setError(getString(formState.getUsernameError()));
        }
        if (formState.getPasswordError() != null) {
            form.getPasswordEditText().setError(getString(formState.getPasswordError()));
        }
    }
}