package com.infinit.sftp;

import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;

import java.security.PublicKey;

public class MyPublickeyAuthenticator implements PublickeyAuthenticator {
    public boolean authenticate(String s, PublicKey publicKey, ServerSession serverSession) {
        return false;
    }
}
