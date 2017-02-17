package net.kraken.tutorial.ldap;

import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/* 
 * Tutorial recomanable
 * ====================
 * JNDI objectes serialitzables
 * https://www.youtube.com/watch?v=NrxaJaKpnlg
 * 
 * JNDI - LDAP
 * https://www.youtube.com/watch?v=5GXwMURbIk4
 * 
 * 
 * LDAP Estructura
 * ===============
 * 
 * 
 * */
public class LDAPCheck {
	
	private static void mostraNodeLDAP(DirContext context, String searchBindLDAP) throws NamingException {
		// A parti
		NamingEnumeration<Binding> enumeration = context.listBindings( searchBindLDAP );
		 while (enumeration.hasMore()){
			 System.out.println(enumeration.next().getName());
		 }
	}
	
	public static void main(String[] args) throws NamingException {
		
		 Hashtable<String, String> environment = new Hashtable<String, String>();
		 environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		 environment.put(Context.PROVIDER_URL, "ldap://portatil.kraken.net:389/dc=kraken,dc=net");
		 
		 DirContext context = new InitialDirContext(environment);
		 
		 mostraNodeLDAP(context,"");
		
		 mostraNodeLDAP(context,"ou=persones");
			
		
	}

	

}

/*
 * 
 * * Fixeu-vos que el nom del package, segueix una estructura conceptual similar
 * al DNS.
 * 
 * Seguin la pràctica 4, imaginem que el nostre localhost local és el kraken.net
 * (un nom adient pel package pensant en microserveis seria per exemple : 
 * 		net.kraken.tutorial ... )
 * 
 * CONFIGURACIÓ
 * ============
 * Per configuració de l'entorn:
 * 		Botó dret (a la perspectiva de Java), "Project Facets", i seleccionar "Java", 
 * 		doncs únicament requerim . 
 * 
 * Recordeu que quan debugueu, podeu configurar les tecles. Per exemple, el debugar seria "F6":
    Open Preferences
    General > Keys
    Type filter text of "step "
    Change Step Into, Step Over and Step Out to "In Windows" instead of "Debugging" in the When dropdown.



 * Webgrafia
 * http://bethecoder.com/applications/tutorials/java/ldap/how-to-query-password-attribute-of-ldap-entry.html
 * 
 * http://stackoverflow.com/questions/12317205/ldap-authentication-using-java
 * http://stackoverflow.com/questions/6302870/java-ldap-authentication-using-username-and-password
 * 
 * Escencial:
 * http://stackoverflow.com/questions/23689964/javax-naming-authenticationexception-ldap-error-code-49-invalid-credentials

 * 
 * 
 * */
