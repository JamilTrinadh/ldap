package com.ldap.ldap.controller;


import com.ldap.ldap.service.LdapService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ldap")
public class LdapController {

    private final LdapService ldapService;

    public LdapController(LdapService ldapService) {
        this.ldapService = ldapService;
    }

    @PostMapping("/ou")
    public String createOU(@RequestParam String name) {
        return ldapService.createOrganizationalUnit(name);
    }
}
