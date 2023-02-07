package com.example.projectauth.security;

import com.example.projectauth.model.Member;
import com.example.projectauth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find " + username));
        log.info("UserDetailsServiceImpl loadUserByUsername");
        if (findMember != null) {
            log.info("userdetails객체 생성 후 전달");
            return UserDetailsImpl.builder()
                    .username(findMember.getUsername())
                    .password(findMember.getPassword())
                    .build();
        }
        return null;
    }
}