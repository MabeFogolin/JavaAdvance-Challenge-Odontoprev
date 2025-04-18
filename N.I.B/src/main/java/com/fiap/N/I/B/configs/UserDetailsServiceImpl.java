package com.fiap.N.I.B.configs;

import com.fiap.N.I.B.Repositories.UsuarioSecurityRepository;
import com.fiap.N.I.B.model.UsuarioSecurity;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    final UsuarioSecurityRepository userRepository;

    public UserDetailsServiceImpl(UsuarioSecurityRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioSecurity userModel = (UsuarioSecurity) userRepository.findByEmailUser(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        return new User(userModel.getNomeUser(), userModel.getPassword(), true, true, true,true, userModel.getAuthorities());
    }
}

