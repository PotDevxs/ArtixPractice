package dev.artixdev.libs.com.mongodb;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@ThreadSafe
public interface SubjectProvider {
   @Nullable
   Subject getSubject() throws LoginException;
}
