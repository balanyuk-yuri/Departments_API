package com.ybal.dep.auth;

import com.ybal.dep.auth.User;
import com.ybal.dep.repo.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name).get();
        if (user == null) {
            throw new UsernameNotFoundException(name);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getName(), user.getPassword(), user.getRoles());
    }
}
