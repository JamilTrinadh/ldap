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

    @PostMapping("/user")
    public String createUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String ou) {
        return ldapService.createUser(username, password, ou);
    }

    @PostMapping("/group")
    public String createGroup(
            @RequestParam String groupName,
            @RequestParam String ou) {
        return ldapService.createGroup(groupName, ou);
    }

    @PostMapping("/group/add-user")
    public String addUserToGroup(
            @RequestParam String username,
            @RequestParam String userOu,
            @RequestParam String groupName,
            @RequestParam String groupOu) {
        return ldapService.addUserToGroup(username, userOu, groupName, groupOu);
    }





}
