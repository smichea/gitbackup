package org.manaty.gitbackup;

import java.util.List;

public class Organisation {
    private String name;
    private String token;
    private String username;
    private String password;
    private List<String> gitRepoUrls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getGitRepoUrls() {
        return gitRepoUrls;
    }

    public void setGitRepoUrls(List<String> gitRepoUrls) {
        this.gitRepoUrls = gitRepoUrls;
    }
}
