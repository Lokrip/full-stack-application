package ru.slava.manager_app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.slava.manager_app.entity.Authority;
import ru.slava.manager_app.repository.SelmagUserRepository;

@Service
public class SelmagUserDetailService implements UserDetailsService {
    private final SelmagUserRepository selmagUserRepository;

    @Autowired
    public SelmagUserDetailService(SelmagUserRepository selmagUserRepository) {
        this.selmagUserRepository = selmagUserRepository;
    }

    @Override
    //Аннотация @Transactional(readOnly = true) используется для того, чтобы указать, что данный метод работает с базой данных, но не изменяет её. В данном случае, метод loadUserByUsername помечен как транзакционный, при этом указано, что операция является только для чтения.
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.selmagUserRepository.findByUsername(username)
            .map(user -> User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAutorities().stream()
                    .map(Authority::getAutority)
                    .map(SimpleGrantedAuthority::new)
                    .toList())
                .build())
            .orElseThrow(
                () -> new UsernameNotFoundException("User %s not found".formatted(username))
            );
    }

}
