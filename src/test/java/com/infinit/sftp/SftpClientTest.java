package com.infinit.sftp;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.sftp.SftpSubsystem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class SftpClientTest {
    private SshServer sshd;
    private SftpClient test;
    
    private String server = "localhost";
    private int port = 22;
    private String login = "login";
    private String password = "testPassword";

    @Before
    public void setUp() throws IOException {
        // Init sftp server stuff
        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(port);
        sshd.setPasswordAuthenticator(new MyPasswordAuthenticator());
        sshd.setPublickeyAuthenticator(new MyPublickeyAuthenticator());
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
        sshd.setSubsystemFactories(Arrays.<NamedFactory<Command>>asList(new SftpSubsystem.Factory()));
        sshd.setCommandFactory(new ScpCommandFactory());

        sshd.start();
        
        // Init tested class
        test = new SftpClient();
        test.setServer(server);
        test.setPort(port);
        test.setLogin(login);
        test.setPassword(password);

        // Clean existing files from previous runs
        cleanFiles();
    }

    @After
    public void tearDown() throws InterruptedException {
        sshd.stop();

        // Clean existing files from previous runs
        cleanFiles();
    }

    @Test
    public void testUploadFile() throws Exception {
        test.connect();
        test.uploadFile("src/test/resources/upload.txt", "target/uploaded.txt");
        test.disconnect();

        File uploaded = new File("target/uploaded.txt");
        Assert.assertTrue(uploaded.exists());
    }

    @Test
    public void testRetrieveFile() throws Exception {
        test.connect();
        test.uploadFile("src/test/resources/upload.txt", "target/uploaded.txt");
        test.retrieveFile("target/uploaded.txt", "target/downloaded.txt");
        test.disconnect();
        
        File downloaded = new File("target/downloaded.txt");
        Assert.assertTrue(downloaded.exists());
    }
    
    private void cleanFiles() {
        File uploaded = new File("target/uploaded.txt");
        if (uploaded.exists()) {
            uploaded.delete();
        }

        File downloaded = new File("target/downloaded.txt");
        if (downloaded.exists()) {
            downloaded.delete();
        }
    }
}
