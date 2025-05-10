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

}
