package com.f83260.foodwaste.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.f83260.foodwaste.MainActivity;
import com.f83260.foodwaste.R;
import com.f83260.foodwaste.common.SharedPreferenceManager;
import com.f83260.foodwaste.databinding.ActivityLoginBinding;
import com.f83260.foodwaste.databinding.ActivityRegisterBinding;
import com.f83260.foodwaste.service.UserService;
import com.f83260.foodwaste.ui.login.LoggedInUserView;
import com.f83260.foodwaste.ui.login.LoginActivity;

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

        final EditText firstNameEditText = binding.firstName;
        final EditText lastNameEditText = binding.lastName;
        final EditText phoneNameEditText = binding.phone;
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;

        // TODO: Add for the others
        registerViewModel.getRegisterFormState().observe(this, registerFormState -> {
            if (registerFormState == null) {
                return;
            }
            registerButton.setEnabled(registerFormState.isDataValid());

            if (registerFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(registerFormState.getUsernameError()));
            }
            if (registerFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(registerFormState.getPasswordError()));
            }
        });

        registerViewModel.getRegisterResult().observe(this, registerResult -> {
            if (registerResult == null) {
                return;
            }

            loadingProgressBar.setVisibility(View.GONE);

            if (registerResult.getError() != null) {
                showRegisterFailed(registerResult.getError());
            }
            if (registerResult.getSuccess() != null) {
                SharedPreferenceManager
                        .setUserName(RegisterActivity.this, registerResult.getSuccess().getDisplayName());
                updateUiWithUser(registerResult.getSuccess());
            }
            setResult(Activity.RESULT_OK);

            //Complete and destroy register activity once successful
            finish();
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
                registerViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);


        // clicked on the 'register' button
        registerButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Hello world", Toast.LENGTH_LONG).show();

            registerViewModel.register(firstNameEditText.getText().toString(),
                    lastNameEditText.getText().toString(),
                    phoneNameEditText.getText().toString(),
                    usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());

        });
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
}