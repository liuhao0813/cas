package com.digierp.it.cas.component;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义账户登录
 */
@Component
@Setter(onMethod_ = {@Autowired})
public class CasUserDetailsService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String root = passwordEncoder.encode("root");
        return User.withUsername("root").password(root).roles("ADMIN").build();
    }
}
