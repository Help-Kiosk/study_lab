package com.example.study_lab.login;

public class LoginFormState {
    private final String idErrorMessage;
    private final String passwordErrorMessage;
    private final boolean isFieldsValid;

    public LoginFormState(String usernameErrorMessage, String passwordErrorMessage, boolean isFieldsValid) {
        this.idErrorMessage = usernameErrorMessage;
        this.passwordErrorMessage = passwordErrorMessage;
        this.isFieldsValid = isFieldsValid;
    }

    public String getIdErrorMessage() {
        return idErrorMessage;
    }

    public String getPasswordErrorMessage() {
        return passwordErrorMessage;
    }

    public boolean isFieldsValid() {
        return isFieldsValid;
    }
}
