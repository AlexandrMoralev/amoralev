package ru.job4j.ui.dto;

public class AuthRequest {

    private String phone;

    private String password;

    private AuthAction action;

    public AuthRequest() {
    }

    public AuthRequest(String login,
                       String password,
                       String actionName
    ) {
        this.phone = login.strip();
        this.password = password;
        this.action = AuthAction.of(actionName.strip());
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public AuthAction getAction() {
        return action;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAction(String action) {
        this.action = AuthAction.of(action.strip());
    }

    public boolean isValid() {
        return !this.phone.strip().isBlank()
                && !this.password.strip().isBlank()
                && this.action != AuthAction.UNKNOWN;
    }
}
