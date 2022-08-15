package com.example.study_lab.login;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.study_lab.R;

public class LoginFragment extends Fragment {
    private LoginViewModel loginViewModel;

    private EditText et_email;
    private EditText et_password;
    private Button bt_login;
    private Button bt_signup;

    private long pressedTime = 0;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (System.currentTimeMillis() > pressedTime + 2000) {
                    pressedTime = System.currentTimeMillis();
                    Toast.makeText(requireContext(), "Press once more to exit", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Exit the app", Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        et_email = view.findViewById(R.id.login_et_email);
        et_password = view.findViewById(R.id.login_et_password);
        bt_login = view.findViewById(R.id.login_bt_login);
        bt_signup = view.findViewById(R.id.login_bt_signUp);

        //region observer
        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(LoginFormState loginFormState) {
                if (loginFormState.getIdErrorMessage() != null) {
                    et_email.setError(loginFormState.getIdErrorMessage());
                }
                if (loginFormState.getPasswordErrorMessage() != null) {
                    et_password.setError(loginFormState.getPasswordErrorMessage());
                }
                bt_login.setEnabled(loginFormState.isFieldsValid());
            }
        });

        loginViewModel.getDoingWork().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isWorking) {
                if (isWorking == true) {
                    et_email.setEnabled(false);
                    et_password.setEnabled(false);
                    bt_login.setEnabled(false);
                    bt_signup.setEnabled(false);
                } else {
                    et_email.setEnabled(true);
                    et_password.setEnabled(true);
                    bt_signup.setEnabled(true);
                }
            }
        });


        loginViewModel.isLoggedIn().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoggedIn) {
                if (isLoggedIn) {
                    loginViewModel.setUser(et_email.getText().toString());
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    et_email.setText(null);
                    et_password.setText(null);
                }
            }
        });

        loginViewModel.doesUserSetSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean doseUserSetSuccess) {
                if (doseUserSetSuccess) {
                    LoginFragmentDirections.ActionLoginFragmentToQrCodeImageFragment action = LoginFragmentDirections.actionLoginFragmentToQrCodeImageFragment();
                    action.setUserId(et_email.getText().toString());
                    NavHostFragment.findNavController(LoginFragment.this).navigate(action);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "fail to set", Toast.LENGTH_SHORT).show();
                    et_email.setText(null);
                    et_password.setText(null);
                }
            }
        });
        //endregion

        //region Listener
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginViewModel.onLoginIdTextChanged(editable.toString());
            }
        });

        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginViewModel.onLoginPasswordTextChanged(editable.toString());
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.tryLogin(et_email.getText().toString(), et_password.getText().toString());
            }
        });

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_signupFragment);
            }
        });
        //endregion

    }
}