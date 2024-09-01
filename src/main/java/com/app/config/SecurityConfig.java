package com.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

// Configurar seguridad basica
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permite trabajar con spring security con anotaciones
public class SecurityConfig {

//    @Autowired
//    AuthenticationConfiguration authenticationConfiguration;

    // Configurar security filter chain
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        /**
//         * Si no hay condiciones aca la peticion pasara sin problemas
//         */
//
//        // Esto trabaja con el patron builder
//        return httpSecurity
//                // deshabilitando esta proteccion, no se necesita
//                .csrf(crsf -> crsf.disable())
//                // Se usa cuando nos logueamos con usuario y password, con tokens es diferente
//                .httpBasic(Customizer.withDefaults())
//                // Trabajar sesiones sin estado
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(http -> {
//                    // Si hace match con alguna de estas URL
//
//                    // Configurar los endpoints publicos
//                    http.requestMatchers(HttpMethod.GET,  "/auth/hello").permitAll();
//
//                    // Configurar los endpoints privados
//                    // Tiene que tener autorizacion de lectura
//                    http.requestMatchers(HttpMethod.GET, "/auth/hello-secured").hasAuthority("CREATE");
//
//                    // Cualquier otro request diferente a estos se denegara el acceso, se puede usar denyAll() o authenticated()
//
//                    // Configurar el resto de endpoints - NO ESPECIFICADOS
//                    // denyAll(): Si no especifico en los endpoins publicos o privados rechazara todo lo que no se especifique, el denyAll es mas seguro, mas restrictivo (recomendado)
//                    // authenticated(): Cualquier otro debo estar autenticado
//                    http.anyRequest().authenticated();
//
//                })
//                .build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        /**
         * Si no hay condiciones aca la peticion pasara sin problemas
         */

        // Esto trabaja con el patron builder
        return httpSecurity
                // deshabilitando esta proteccion, no se necesita
                .csrf(crsf -> crsf.disable())
                // Se usa cuando nos logueamos con usuario y password, con tokens es diferente
                .httpBasic(Customizer.withDefaults())
                // Trabajar sesiones sin estado
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService());
        return provider;
    }

    // Un rol puede tener muchos permisos y muchos permisos pueden tener varios roles, este usuario tendra permisos para leer y escribir (authorities)
    @Bean
    public UserDetailsService  userDetailService() {
        // Crear un usuario en memoria para pruebas

        // Tambien podemos devolver una lista de usuarios
        List<UserDetails> userDetailsList = new ArrayList<>();

        userDetailsList.add(User.withUsername("angel")
                .password("1234")
                .roles("ADMIN")
                .authorities("READ", "CREATE")
                .build());

        userDetailsList.add(User.withUsername("gustavo")
                .password("1234")
                .roles("USER")
                .authorities("READ")
                .build());

        return new InMemoryUserDetailsManager(userDetailsList);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // NoOpPasswordEncoder solo para pruebas
        // en Prod es BcryptPasswordEncoder
        return NoOpPasswordEncoder.getInstance();
    }

}
