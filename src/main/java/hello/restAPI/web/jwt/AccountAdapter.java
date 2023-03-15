package hello.restAPI.web.jwt;

import hello.restAPI.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

public class AccountAdapter extends org.springframework.security.core.userdetails.User {
    private User user;

    public AccountAdapter(User user) {
        super(user.getUsername(), user.getPassword(), user.getAuthorities());
        this.user = user;
    }

    public User getAccount() {
        return this.user;
    }
}