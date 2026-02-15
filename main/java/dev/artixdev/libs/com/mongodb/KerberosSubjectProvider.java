package dev.artixdev.libs.com.mongodb;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.internal.Locks;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Logger;
import dev.artixdev.libs.com.mongodb.internal.diagnostics.logging.Loggers;
import dev.artixdev.libs.com.mongodb.lang.NonNull;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@ThreadSafe
public class KerberosSubjectProvider implements SubjectProvider {
   private static final Logger LOGGER = Loggers.getLogger("authenticator");
   private static final String TGT_PREFIX = "krbtgt/";
   private final ReentrantLock lock;
   private String loginContextName;
   private String fallbackLoginContextName;
   private Subject subject;

   public KerberosSubjectProvider() {
      this("com.sun.security.jgss.krb5.initiate", "com.sun.security.jgss.initiate");
   }

   public KerberosSubjectProvider(String loginContextName) {
      this(loginContextName, (String)null);
   }

   private KerberosSubjectProvider(String loginContextName, @Nullable String fallbackLoginContextName) {
      this.lock = new ReentrantLock();
      this.loginContextName = (String)Assertions.notNull("loginContextName", loginContextName);
      this.fallbackLoginContextName = fallbackLoginContextName;
   }

   @NonNull
   public Subject getSubject() throws LoginException {
      return (Subject)Locks.checkedWithInterruptibleLock(this.lock, () -> {
         if (this.subject == null || needNewSubject(this.subject)) {
            this.subject = this.createNewSubject();
         }

         return this.subject;
      });
   }

   private Subject createNewSubject() throws LoginException {
      LoginContext loginContext;
      try {
         LOGGER.debug(String.format("Creating LoginContext with name '%s'", this.loginContextName));
         loginContext = new LoginContext(this.loginContextName);
      } catch (LoginException e) {
         if (this.fallbackLoginContextName == null) {
            throw e;
         }

         LOGGER.debug(String.format("Creating LoginContext with fallback name '%s'", this.fallbackLoginContextName));
         loginContext = new LoginContext(this.fallbackLoginContextName);
         this.loginContextName = this.fallbackLoginContextName;
         this.fallbackLoginContextName = null;
      }

      loginContext.login();
      LOGGER.debug("Login successful");
      return loginContext.getSubject();
   }

   private static boolean needNewSubject(Subject subject) {
      Iterator var1 = subject.getPrivateCredentials(KerberosTicket.class).iterator();

      while(var1.hasNext()) {
         KerberosTicket cur = (KerberosTicket)var1.next();
         if (cur.getServer().getName().startsWith("krbtgt/")) {
            if (System.currentTimeMillis() > cur.getEndTime().getTime() - TimeUnit.MILLISECONDS.convert(5L, TimeUnit.MINUTES)) {
               LOGGER.info("The TGT is close to expiring. Time to reacquire.");
               return true;
            }
            break;
         }
      }

      return false;
   }
}
