package com.example.study_lab.login;

public class SignupFormState {
    private final String idErrorMessage;
    private final String passwordErrorMessage;
    private final String passwordCheckErrorMessage;
    private final String displayNameErrorMessage;
    private final boolean isFieldsValid;

    public SignupFormState(String usernameErrorMessage, String passwordErrorMessage, String passwordCheckErrorMessage, String displayNameErrorMessage, boolean isFieldsValid) {
        this.idErrorMessage = usernameErrorMessage;
        this.passwordErrorMessage = passwordErrorMessage;
        this.passwordCheckErrorMessage = passwordCheckErrorMessage;
        this.displayNameErrorMessage = displayNameErrorMessage;
        this.isFieldsValid = isFieldsValid;
    }

    public String getIdErrorMessage() {
        return idErrorMessage;
    }

    public String getPasswordErrorMessage() {
        return passwordErrorMessage;
    }

    public String getPasswordCheckErrorMessage() {
        return passwordCheckErrorMessage;
    }

    public String getDisplayNameErrorMessage() {
        return displayNameErrorMessage;
    }

    public boolean isFieldsValid() {
        return isFieldsValid;
    }
}
