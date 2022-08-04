package com.example.structure.userdetails;

import com.example.structure.userclient.UserClient;
import com.example.structure.userclient.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<UserClient> _user = userRepository.findUserClientByEmail(username);
        if (!_user.isPresent() || _user.get() == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new MyUserDetails(_user.get());
    }

}