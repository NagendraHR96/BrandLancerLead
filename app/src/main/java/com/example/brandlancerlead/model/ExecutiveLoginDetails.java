package com.example.brandlancerlead.model;

import java.io.Serializable;

public class ExecutiveLoginDetails implements Serializable {
    private String ExecutiveId;
    private String ExecutiveName;
    private String Password;
    private String Hint;

    public String getHint() {
        return Hint;
    }

    public void setHint(String hint) {
        Hint = hint;
    }

    public String getExecutiveId() {
        return ExecutiveId;
    }

    public void setExecutiveId(String executiveId) {
        ExecutiveId = executiveId;
    }

    public String getExecutiveName() {
        return ExecutiveName;
    }

    public void setExecutiveName(String executiveName) {
        ExecutiveName = executiveName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
