package com.edme.processingCenter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class AuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
        if(realmAccess != null && realmAccess.containsKey("roles")) {
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");

            // Логируем роли
            log.debug("JWT claims: {}", source.getClaims());
            log.info("Пользователь: {}, Роли: {}", source.getSubject(), roles);

//            return ((Collection<String>) realmAccess.get("roles")).stream()
//                    .map(role -> "ROLE_" + role)
//                    .map(SimpleGrantedAuthority::new)
//                    .collect(Collectors.toList());

            return roles.stream()
                    .map(role -> "ROLE_" + role)
                    //.map(role -> "ROLE_" +  role.toUpperCase())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        log.warn("Пользователь: {}, не имеет roles в realm_access", source.getSubject());
        return Collections.emptyList();
    }
}
