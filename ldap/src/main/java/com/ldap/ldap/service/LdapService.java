package com.ldap.ldap.service;


import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;

@Service
public class LdapService {

    public void testConnection() {
        try {
            DirContext context = getLdapContext();
            System.out.println("✅ Successfully connected to LDAP");
            context.close();
        } catch (NamingException e) {
            System.out.println("❌ Failed to connect to LDAP: " + e.getMessage());
        }
    }

    private DirContext getLdapContext() throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();

        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:389");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=jami,dc=in");
        env.put(Context.SECURITY_CREDENTIALS, "jami");

        return new InitialDirContext(env);
    }

    public String createOrganizationalUnit(String ouName) {
        try {
            DirContext context = getLdapContext();

            // DN for the OU
            String dn = "ou=" + ouName + ",dc=jami,dc=in";

            // Set the attributes
            Attributes attributes = new BasicAttributes();
            Attribute objClass = new BasicAttribute("objectClass");
            objClass.add("top");
            objClass.add("organizationalUnit");
            attributes.put(objClass);
            attributes.put("ou", ouName);

            // Create entry
            context.createSubcontext(dn, attributes);
            context.close();

            return "OU '" + ouName + "' created successfully.";
        } catch (NamingException e) {
            e.printStackTrace();
            return "Error creating OU: " + e.getMessage();
        }
    }

    public String createUser(String username, String password, String ouName) {
        try {
            DirContext context = getLdapContext();

            // DN for the user
            String dn = "cn=" + username + ",ou=" + ouName + ",dc=jami,dc=in";

            // Set user attributes
            Attributes attrs = new BasicAttributes();
            Attribute objClass = new BasicAttribute("objectClass");
            objClass.add("top");
            objClass.add("person");
            objClass.add("organizationalPerson");
            objClass.add("inetOrgPerson");
            attrs.put(objClass);
            attrs.put("cn", username);
            attrs.put("sn", username); // Required for 'person'
            attrs.put("userPassword", password); // Plaintext or hashed

            // Create the user
            context.createSubcontext(dn, attrs);
            context.close();

            return "User '" + username + "' created successfully.";
        } catch (NamingException e) {
            e.printStackTrace();
            return "Error creating user: " + e.getMessage();
        }
    }

    public String createGroup(String groupName, String ouName) {
        try {
            DirContext context = getLdapContext();

            // DN for the group
            String dn = "cn=" + groupName + ",ou=" + ouName + ",dc=jami,dc=in";

            // Set group attributes
            Attributes attrs = new BasicAttributes();
            Attribute objClass = new BasicAttribute("objectClass");
            objClass.add("top");
            objClass.add("groupOfNames");
            attrs.put(objClass);
            attrs.put("cn", groupName);

            // 'groupOfNames' requires at least one 'member'
            // Add a dummy DN (should be real in production)
            attrs.put("member", "cn=dummy,ou=" + ouName + ",dc=jami,dc=in");

            // Create group
            context.createSubcontext(dn, attrs);
            context.close();

            return "Group '" + groupName + "' created successfully.";
        } catch (NamingException e) {
            e.printStackTrace();
            return "Error creating group: " + e.getMessage();
        }
    }

    public String addUserToGroup(String username, String userOu, String groupName, String groupOu) {
        try {
            DirContext context = getLdapContext();

            // DNs
            String userDN = "cn=" + username + ",ou=" + userOu + ",dc=jami,dc=in";
            String groupDN = "cn=" + groupName + ",ou=" + groupOu + ",dc=jami,dc=in";

            // Modify group to add member
            ModificationItem[] mods = new ModificationItem[1];
            Attribute mod = new BasicAttribute("member", userDN);
            mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, mod);

            context.modifyAttributes(groupDN, mods);
            context.close();

            return "User '" + username + "' added to group '" + groupName + "'.";
        } catch (NamingException e) {
            e.printStackTrace();
            return "Error adding user to group: " + e.getMessage();
        }
    }




}
