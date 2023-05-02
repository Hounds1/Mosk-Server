package team.mosk.api.server.domain.store.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import team.mosk.api.server.domain.auth.util.GivenAuth;
import team.mosk.api.server.global.security.principal.CustomUserDetails;

import static team.mosk.api.server.domain.auth.util.GivenAuth.*;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {
    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        CustomUserDetails userDetails = new CustomUserDetails(1L, GIVEN_EMAIL, "password");

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails, "password", null);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);

        return context;
    }
}
