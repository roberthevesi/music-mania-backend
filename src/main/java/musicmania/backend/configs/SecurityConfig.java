package musicmania.backend.configs;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.time.Duration;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private final RsaKeyProperties rsaKeyProperties;


    public SecurityConfig(RsaKeyProperties rsaKeyProperties) {
        this.rsaKeyProperties = rsaKeyProperties;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/**"))
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(new AntPathRequestMatcher("/api/users/register"),
                                        new AntPathRequestMatcher("/api/users/sendForgotPasswordCode"),
                                        new AntPathRequestMatcher("/api/users/verifyForgotPasswordCode"),
                                        new AntPathRequestMatcher("/api/users/setNewPassword"),
                                        new AntPathRequestMatcher("/api/users/sendNewUserCode"),
                                        new AntPathRequestMatcher("/api/users/verifyNewUserCode"),
                                        new AntPathRequestMatcher("/api/users/get-token")).permitAll() // Permit access to /register and /token for everyone
                                .anyRequest().authenticated()) // All other requests need authentication
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(userDetailsService)
                .authenticationProvider(daoAuthenticationProvider(userDetailsService, passwordEncoder))
                .build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

//    @Bean
//    JwtDecoder jwtDecoder(){
//        return NimbusJwtDecoder.withPublicKey(rsaKeyProperties.publicKey()).build();
//    }

    @Bean
    public JwtDecoder jwtDecoder(RsaKeyProperties rsaKeyProperties) {
        JWK jwk = new RSAKey.Builder(rsaKeyProperties.publicKey()).privateKey(rsaKeyProperties.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyProperties.publicKey()).build();

        // Create a default JWT validator with standard checks (including expiration)
        OAuth2TokenValidator<Jwt> defaultValidator = JwtValidators.createDefault();

        // Create a custom timestamp validator with some allowed clock skew
        JwtTimestampValidator timestampValidator = new JwtTimestampValidator(Duration.ofMinutes(1));

        // Combine the validators
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<Jwt>(defaultValidator, timestampValidator);

        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }

    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(rsaKeyProperties.publicKey()).privateKey(rsaKeyProperties.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
