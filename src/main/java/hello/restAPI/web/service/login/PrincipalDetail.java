package hello.restAPI.web.service.login;//package com.example.KimJunHyuk.web.service.login;
//
//import com.example.KimJunHyuk.domain.user.User;
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//@Getter
//public class PrincipalDetail implements UserDetails {
//
//    private User user;
//
//    public PrincipalDetail(User user) {
//        this.user = user;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> collection = new ArrayList<>();
//        collection.add(() -> {
//            return "" + user.getId();
//        });
//        return collection;
//    }
//
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getAccountId();
//    }
//    //계정이 만료되지 않았는지 리턴
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//    //계정이 잠겨있는지 리턴 (true: 잠기지 않음)
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//    //비밀번호가 만료되지 않았는지 리턴 (true:만료)
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//    //계정이 활성화 상태인지 리턴(true:활성화)
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
