package com.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
// A nivel de clase valida el authenticated() o denyAll()
// Por defecto niega el acceso a todo, a menos que se le indique lo contrario
@PreAuthorize("denyAll()")
public class TestAuthController {

     @GetMapping("/hello")
     // Este endpoint es publico para todo el mundo
     @PreAuthorize("permitAll()")
     public String hello() {
         return "Hello World";
     }

     @GetMapping("/hello-secured")
     // Debe tener permisos de lectura
     @PreAuthorize("hasAuthority('READ')")
     public String helloSecured() {
         return "Hello World Secured";
     }

     @GetMapping("/hello-secured2")
     @PreAuthorize("hasAuthority('CREATE')")
     public String helloSecured2() {
         return "Hello World Secured2";
     }
}