package com.doduck.prototype.cli.dir2server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.RequestTokenPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.WebAuthSession.WebAuthInfo;

public class AccessToken {

	private String appKey;
	private String appSecret;

	public AccessToken(String appKey, String appSecret) {
		this.appKey = appKey;
		this.appSecret = appSecret;
	}

	public AccessTokenPair request() throws DropboxException, MalformedURLException, IOException, URISyntaxException {
        AppKeyPair appKeys = new AppKeyPair(this.appKey, this.appSecret);
        WebAuthSession session = new WebAuthSession(appKeys, AccessType.APP_FOLDER);
        WebAuthInfo authInfo = session.getAuthInfo();

        RequestTokenPair pair = authInfo.requestTokenPair;
        String url = authInfo.url;

        System.out.println("Accept the app by openning your browser at this URL:");
        System.out.println(url);
        System.out.println("THEN press any key:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		br.readLine();
        
        try {
            session.retrieveWebAccessToken(pair);
        } catch (Exception e) {
            System.out.println("authentication fail with exception:" + e);
        }

        AccessTokenPair tokens = session.getAccessTokenPair();

        return tokens;
	}

	public void saveDefaultToken(AccessTokenPair token) throws IOException {

		String filePath = getPersistantFilePath();
		
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		bw.write(token.key);
		bw.newLine();
		bw.write(token.secret);
		bw.newLine();
		bw.close();
	}

	private String getPersistantFilePath() {
		String dir = System.getProperty("user.home") + System.getProperty("file.separator");
		String filePath = dir + ".dir2Server";
		return filePath;
	}

	public AccessTokenPair getDefaultToken(){
		String filePath = getPersistantFilePath();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
			String key = br.readLine();
			String secret = br.readLine();
			
			return new AccessTokenPair(key, secret);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
			return null;
		}finally{
			if(br!= null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}

}
