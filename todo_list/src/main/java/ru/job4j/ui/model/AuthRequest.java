package ru.job4j.ui.model;

public class AuthRequest {

    private String login;

    private String password;

    private AuthAction action;

    public AuthRequest() {
    }

    public AuthRequest(String login,
                       String password,
                       String actionName
    ) {
        this.login = login.strip();
        this.password = password;
        this.action = AuthAction.of(actionName.strip());
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public AuthAction getAction() {
        return action;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAction(String action) {
        this.action = AuthAction.of(action.strip());
    }

    public boolean isValid() {
        return !this.login.strip().isBlank()
                && !this.password.strip().isBlank()
                && this.action != AuthAction.DEFAULT;
    }
}
