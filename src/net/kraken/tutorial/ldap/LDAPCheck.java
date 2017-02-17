package net.kraken.tutorial.ldap;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

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
	URL portatil.kraken.net
	+ Root DSE
		+ dc=kraken,dc=net
			+ cn=admin
		+ ou=persones
			+ uid=zipi
			+ uid=zape
  
 * */
public class LDAPCheck {

	private static Boolean checkLoginPassword(String DN_uidPersona, String password) {

		Hashtable<String, String> ldapEnv = new Hashtable<String, String>();
		ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		ldapEnv.put(Context.PROVIDER_URL, "LDAP://portatil.kraken.net:389"); // hard-coded
		ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
		//ldapEnv.put(Context.SECURITY_PRINCIPAL, "uid=" + uid_name + ",ou=persones,dc=kraken,dc=net");
		ldapEnv.put(Context.SECURITY_PRINCIPAL, DN_uidPersona);
		ldapEnv.put(Context.SECURITY_CREDENTIALS, password);
		Boolean loginExit = false;
		try {
			DirContext ldapContext = new InitialLdapContext(ldapEnv, null);
			System.out.println("Password Correcte!");
			loginExit = true;
		} catch (Exception e) {
			System.out.println("Login incorrecte");
			// System.out.println(" bind error: " + e);
			// e.printStackTrace();
		}
		return loginExit;
	}
	

	private static void mostraNodeLDAP(DirContext context, String searchBindLDAP) throws NamingException {
		// A parti
		NamingEnumeration<Binding> enumeration = context.listBindings(searchBindLDAP);
		while (enumeration.hasMore()) {
			System.out.println(enumeration.next().getName());
		}
	}

	public static void main(String[] args) throws NamingException {

		Hashtable<String, String> environment = new Hashtable<String, String>();
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		// Canviar en funcio de la configuracio que hagiu creat
		environment.put(Context.PROVIDER_URL, "ldap://portatil.kraken.net:389/dc=kraken,dc=net"); // hard-coded

		DirContext context = new InitialDirContext(environment);

		// Visitant els Nodes
		System.out.println("Visitant nodes en general");
		mostraNodeLDAP(context, "");
		System.out.println("Visitant nodes de la ou=persones");
		mostraNodeLDAP(context, "ou=persones");
		System.out.println("");
		
		// Recuperant atributs
		System.out.println("Recuperant atributs d'un DN");
		Attributes attrs = context.getAttributes("uid=zape,ou=persones");
		System.out.println("Surname: " + attrs.get("sn").get());
		System.out.println("Common name : " + attrs.get("cn").get());
		System.out.println("");

		// Chequejem si un node te el password correcte
		// Recordar que podriem parsejar : zipi@persones.kraken.net 
		// ... i transformar-ho : uid=zipi,ou=persones,dc=kraken,dc=net
		System.out.println("Chequejant password d'un DN");
		if (checkLoginPassword("uid=zipi,ou=persones,dc=kraken,dc=net", "zipi")) {
			System.out.println("Password OK!\n");
		} else {
			System.out.println("Password ERRONI!\n");
		}
		// Check passowrd Erroni ..
		System.out.println("Chequejant password d'un DN");
		if (checkLoginPassword("uid=zipi,ou=persones,dc=kraken,dc=net", "zipiibadpassword")) {
			System.out.println("Password OK!\n");
		} else {
			System.out.println("Password ERRONI!\n");
		}
		
	}

}

/*
 * 
 * * Fixeu-vos que el nom del package, segueix una estructura conceptual similar
 * al DNS.
 * 
 * Seguin la pràctica 4, imaginem que el nostre localhost local és el kraken.net
 * (un nom adient pel package pensant en microserveis seria per exemple :
 * net.kraken.tutorial ... )
 * 
 * CONFIGURACIÓ ============ Per configuració de l'entorn: Botó dret (a la
 * perspectiva de Java), "Project Facets", i seleccionar "Java", doncs únicament
 * requerim .
 * 
 * Recordeu que quan debugueu, podeu configurar les tecles. Per exemple, el
 * debugar seria "F6": Open Preferences General > Keys Type filter text of
 * "step " Change Step Into, Step Over and Step Out to "In Windows" instead of
 * "Debugging" in the When dropdown.
 * 
 * 
 * 
 * Webgrafia
 * http://bethecoder.com/applications/tutorials/java/ldap/how-to-query-password-
 * attribute-of-ldap-entry.html
 * 
 * http://stackoverflow.com/questions/12317205/ldap-authentication-using-java
 * http://stackoverflow.com/questions/6302870/java-ldap-authentication-using-
 * username-and-password
 * 
 * Escencial: http://stackoverflow.com/questions/23689964/javax-naming-
 * authenticationexception-ldap-error-code-49-invalid-credentials
 * 
 * 
 * 
 */
