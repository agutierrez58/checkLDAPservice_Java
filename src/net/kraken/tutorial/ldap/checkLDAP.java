
package net.kraken.tutorial.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


/*
 * Fixeu-vos que el nom del package, segueix una estructura conceptual similar
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
 * */
public class checkLDAP {

  /**
   * @param args
   */
  public static void main(String[] args) {

		  
    //Setup the environment to login as 'Directory Manager'
    String rootDN = "cn=admin";
    String rootPWD = "admin";
    
    Hashtable<String, String> environment = new Hashtable<String, String>();
    environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    environment.put(Context.PROVIDER_URL, "ldap://localhost:389/cn=admin,dc=kraken,dc=net");
   // environment.put(Context.SECURITY_AUTHENTICATION, "simple");
   // environment.put(Context.SECURITY_PRINCIPAL, rootDN);
   // environment.put(Context.SECURITY_CREDENTIALS, rootPWD);

    DirContext dirContext = null;
    NamingEnumeration<?> results = null;
    
    try {
      dirContext = new InitialDirContext(environment);

      /**
       * Retrieve the specific attributes 
       */
      SearchControls controls = new SearchControls();
      controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      controls.setReturningAttributes(new String [] { 
          "nsRole", 
          "userPassword",
                "uid",
                "objectClass",
                "givenName",
                "sn",
                "cn"
      });

      //Get entries having objectclass=person
      String filter = "(objectclass=person)";
      results = dirContext.search("", filter, controls);  
      
      while (results.hasMore()) {
        SearchResult searchResult = (SearchResult) results.next();
        Attributes attributes = searchResult.getAttributes();
        
        NamingEnumeration<? extends Attribute> attrs = attributes.getAll();

        while (attrs.hasMore()) {
          System.out.println(attrs.next());
        }

        //Password string depends on LDAP password policy
        Attribute pwd = attributes.get("userPassword");
        System.out.println("=> userPassword : " + new String((byte[])pwd.get()));
        System.out.println();
      }

    } catch (NameNotFoundException e) {
      e.printStackTrace();
    } catch (NamingException e) {
      e.printStackTrace();
    } finally {
      if (results != null) {
        try {
          results.close();
        } catch (Exception e) {
        }
      }

      if (dirContext != null) {
        try {
          dirContext.close();
        } catch (Exception e) {
        }
      }
    }

  }

}
