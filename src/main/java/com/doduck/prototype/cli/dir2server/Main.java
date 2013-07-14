package com.doduck.prototype.cli.dir2server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.beust.jcommander.JCommander;
import com.doduck.prototype.cli.dir2server.cmd.Client2ServerCmd;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;


public class Main {

	private static final Log log = LogFactory.getLog(Main.class);
	public static String APP_KEY = "swlb71yeguncpzc";
	public static String APP_SECRET = "n12ad3sv4mja3bj";
	
	public static void main(String[] args) throws MalformedURLException, DropboxException, IOException, URISyntaxException, InterruptedException {
		
		Main main = new Main();
		
		Client2ServerCmd cmd = new Client2ServerCmd();
		new JCommander(cmd, args);
		
		main.mightSaveTokenCmd(cmd);
		main.mightUploadCmd(cmd);
		main.mightDownloadCmd(cmd);
	}

	private void mightDownloadCmd(Client2ServerCmd cmd) throws IOException, DropboxException, URISyntaxException, InterruptedException {
		if(cmd.getDownloadFile() == null){
			return;
		}
		
		if(cmd.getEmail() != null || cmd.getSmtp() != null || cmd.getLogin() != null || cmd.getPassword() != null){
			if( !(cmd.getEmail() != null && cmd.getSmtp() != null && cmd.getLogin() != null && cmd.getPassword() != null)){
				log.error("Arguments are missing. If you want to send an email at the end of the download you need to set all params:");
				log.error("--email, --smtp, --login, --password");
				return;
			}
		}
		
		this.downloadCmd(cmd);
	}

	private void downloadCmd(Client2ServerCmd cmd) throws IOException, DropboxException, URISyntaxException, InterruptedException {
		AccessTokenPair accessToken = this.requestTokenIfNeeded();
		new Client(APP_KEY, APP_SECRET, accessToken.key, accessToken.secret)
			.download(cmd.getDownloadFile(), cmd.getExcludeVersion(), cmd.getEmail(), cmd.getSmtp(), cmd.getLogin(), cmd.getPassword());
	}



	private void mightUploadCmd(Client2ServerCmd cmd) throws IOException, DropboxException, URISyntaxException {
		if(cmd.getUploadFile() == null){
			return;
		}
		
		this.uplaodCmd(cmd);
	}

	private void uplaodCmd(Client2ServerCmd cmd) throws IOException, DropboxException, URISyntaxException {
		AccessTokenPair accessToken = this.requestTokenIfNeeded();
		new Client(APP_KEY, APP_SECRET, accessToken.key, accessToken.secret).upload(cmd.getUploadFile());
	}

	private void mightSaveTokenCmd(Client2ServerCmd cmd) {
		if(cmd.getAccessKey() == null || cmd.getSecretKey() == null){
			return;
		}
		
		if(cmd.getAccessKey() != null && cmd.getSecretKey() == null){
			log.error("param --secretkey is also required.");
			return;
		}
		
		if(cmd.getAccessKey() == null && cmd.getSecretKey() != null){
			log.error("param --accessKey is also required.");
			return;
		}
		
		try {
			this.saveTokenCmd(cmd);
			log.info("Access secretkey & accessKey saved on this computer");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveTokenCmd(Client2ServerCmd cmd) throws IOException {
		AccessToken access = new AccessToken(APP_KEY, APP_SECRET);
		AccessTokenPair token = new AccessTokenPair(cmd.getAccessKey(), cmd.getSecretKey());
		access.saveDefaultToken(token);
	}
	
	
	private AccessTokenPair requestTokenIfNeeded() throws IOException, DropboxException, URISyntaxException {
		AccessToken access = new AccessToken(APP_KEY, APP_SECRET);
		AccessTokenPair accessToken = access.getDefaultToken();
		
		if(accessToken == null){
			AccessTokenPair token = access.request();
			access.saveDefaultToken(token);
			return token;
		}
		
		return accessToken;
	}
}