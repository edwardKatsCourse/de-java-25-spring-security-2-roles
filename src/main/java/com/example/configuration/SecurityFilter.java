package com.example.configuration;

import com.example.entity.AccountSession;
import com.example.entity.RoleType;
import com.example.repository.AccountSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final AccountSessionRepository accountSessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }


        AccountSession accountSession = accountSessionRepository.findBySessionId(header);
        if (accountSession == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var role = accountSession.getAccount().getRole();
        var roles = role == RoleType.ADMIN ?
                List.of(RoleType.USER, RoleType.ADMIN)
                :
                List.of(RoleType.USER);

        // role, authority

        // Roles
        // USER         --> USER_ROLE       --> %_ROLE
        // USER_ROLE    --> USER_ROLE_ROLE  --> %_ROLE

        // Authority
        // USER         --> USER            --> %
        // USER_ROLE    --> USER_ROLE       --> %

        Authentication key = new UsernamePasswordAuthenticationToken(
                accountSession,

                null,
                roles.stream().map(x -> new SimpleGrantedAuthority(x.name())).toList()
                // Granted Authority
        );

        SecurityContextHolder
                .getContext()
                .setAuthentication(key);

        filterChain.doFilter(request, response);
    }
}
