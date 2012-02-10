package com.infinit.sftp;

import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.session.ServerSession;

/**
 * Very basic PasswordAuthenticator used for unit tests.
 */
public class MyPasswordAuthenticator implements PasswordAuthenticator {

    public boolean authenticate(String username, String password, ServerSession session) {
        boolean retour = false;

        if ("login".equals(username) && "testPassword".equals(password)) {
            retour = true;
        }

        return retour;
    }
}
