package com.ttn.bootcamp.project.ecommerce.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AppUser implements UserDetails {

    private final String username;
    private final String password;
    List<GrantedAuthorityImpl> grantedAuthorities;
    private final boolean isEnabled;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;

    public AppUser(String username, String password, List<GrantedAuthorityImpl> grantedAuthorities, boolean isEnabled, boolean isAccountNonLocked, boolean isCredentialsNonExpired) {
        this.username = username;
        this.password = password;
        this.grantedAuthorities = grantedAuthorities;
        this.isEnabled = isEnabled;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", grantedAuthorities=" + grantedAuthorities +
                ", isEnabled=" + isEnabled +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", isCredentialsNonExpired=" + isCredentialsNonExpired +
                '}';
    }
}
