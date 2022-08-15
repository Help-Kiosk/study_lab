package com.example.study_lab.login;

public class SignupFormState {
    private final String idErrorMessage;
    private final String passwordErrorMessage;
    private final String passwordCheckErrorMessage;
    private final String nameErrorMessage;
    private final boolean fieldsValid;

    public SignupFormState(String usernameErrorMessage, String passwordErrorMessage, String passwordCheckErrorMessage,
                           String displayNameErrorMessage, boolean isFieldsValid) {
        this.idErrorMessage = usernameErrorMessage;
        this.passwordErrorMessage = passwordErrorMessage;
        this.passwordCheckErrorMessage = passwordCheckErrorMessage;
        this.nameErrorMessage = displayNameErrorMessage;
        this.fieldsValid = isFieldsValid;
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

    public String getNameErrorMessage() {
        return nameErrorMessage;
    }

    public boolean isFieldsValid() {
        return fieldsValid;
    }
}
