package com.doduck.prototype.cli.dir2server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.WebAuthSession;

public class Client {

	private static final Log log = LogFactory.getLog(Client.class);

	private String appKey;
	private String appSecret;
	private String accessKey;
	private String accessSecret;
	DropboxAPI<WebAuthSession> sourceClient;

	public Client(String appKey, String appSecret, String accessKey,
			String accessSecret) {
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.accessKey = accessKey;
		this.accessSecret = accessSecret;

		sourceClient = createSourceClient();
	}

	private DropboxAPI<WebAuthSession> createSourceClient() {
		AppKeyPair pair = new AppKeyPair(this.appKey, this.appSecret);
		AccessTokenPair sourceAccess = new AccessTokenPair(this.accessKey,
				this.accessSecret);

		WebAuthSession session = new WebAuthSession(pair,
				Session.AccessType.APP_FOLDER, sourceAccess);
		DropboxAPI<WebAuthSession> sourceClient = new DropboxAPI<WebAuthSession>(
				session);

		return sourceClient;
	}

	public void upload(String fileName) {
		String fileNameSimplify = this.simplifyFileName(fileName);

		File f = new File(fileName);
		if (!f.exists() || !f.isFile()) {
			log.error("File " + fileName + " not found");
			System.exit(-1);
			return;
		}

		Entry oldFileMeta = getCurrentFileMeta(fileNameSimplify);
		if (oldFileMeta != null) {
			log.info("File exist on the server with revision: "
					+ oldFileMeta.rev);
		}

		String parentRevision = (oldFileMeta == null) ? null : oldFileMeta.rev;
		try {
			displayCommandAndAskToContinu(fileName, parentRevision);

			sourceClient.putFile(fileNameSimplify,
					new FileInputStream(fileName), f.length(), parentRevision,
					new PrintProgressListener());

		} catch (Exception e) {
			log.error("Uploading file '" + fileName + "' failed!", e);
			return;
		}
		log.info("Uploading file '" + fileName + "' was sucessful!");
	}

	private void displayCommandAndAskToContinu(String dropboxFileName, String parentRevision) throws IOException {
		System.out.println();
		System.out.println("You have to run on the server (Only once):");
		System.out.println("java -jar [JAR_NAME] --accessKey "+accessKey+" --secretKey "+accessKey);
		System.out.println();

		String cmdLean = "java -jar [JAR_NAME] --download " + dropboxFileName;
		if (parentRevision != null)
			cmdLean += " -x " + parentRevision;
		System.out
				.println("Run the following command on the server. It will start the download the file as soon as the upload finish");
		System.out.println(cmdLean);
		System.out.println();

		String cmdWithEmail = cmdLean
				+ " --email 'your@email.com' --smtp 'your.smtp.com' --login 'your login' --password 'your password'";
		System.out
				.println("You can also get notify by email when the server finish to download the file.");
		System.out.println(cmdWithEmail);

		System.out.println();
		System.out.println();

		boolean askAgain = true;
		do {

			System.out.println("Start upload ?  Y/N :");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String yn = br.readLine();

			if (yn.toLowerCase().startsWith("n")) {
				System.exit(0);
			}

			if (yn.toLowerCase().startsWith("y")) {
				askAgain = false;
			}
		} while (askAgain);
	}

	private Entry getCurrentFileMeta(String dropboxFileName) {
		try {
			return sourceClient.metadata("/" + dropboxFileName, 1, null, false,
					null);
		} catch (DropboxException e) {
			// log.error(e);
		}
		return null;
	}

	private String simplifyFileName(String fileName) {
		if (fileName.lastIndexOf('/') != -1) {
			fileName = fileName.substring(fileName.lastIndexOf('/') + 1,
					fileName.length());
		}
		return fileName;
	}

	public void download(String fileName, String excludeRevision, String email,
			String smtp, String login, String password)
			throws InterruptedException {

		String fileNameSimplify = this.simplifyFileName(fileName);

		boolean readyToDownload = false;
		Entry currentFileMeta = null;
		while (readyToDownload == false) {

			currentFileMeta = getCurrentFileMeta(fileNameSimplify);

			if (currentFileMeta != null) {
				if (excludeRevision == null || currentFileMeta.rev == null) {
					readyToDownload = true;
				} else if (!currentFileMeta.rev.equals(excludeRevision)) {
					System.out.println(currentFileMeta.rev);
					System.out.println(excludeRevision);
					readyToDownload = true;
				}
			}

			if (readyToDownload == false) {
				System.out
						.println("File not uploaded yet. Retry in 10 seconds...");
				Thread.sleep(1000 * 10);
			}
		}

		try {

			sourceClient.getFile(fileNameSimplify, null, new FileOutputStream(
					fileNameSimplify), new PrintProgressListener());

		} catch (Exception e) {
			log.error("downloading file '" + fileName + "' failed!", e);
			return;
		}
		log.info("Downloading file '" + fileName + "' was sucessful!");

		if (email != null) {
			try {
				sendEmailSucess(fileName, email, smtp, login, password);
				log.info("email send");
			} catch (EmailException e) {
				e.printStackTrace();
				log.warn("Download finish but UNABLE to send the finish email. Check your configuration");
			}
		}
	}

	private void sendEmailSucess(String fileName, String senderEmail,
			String smtp, String login, String password) throws EmailException {
		Email email = new SimpleEmail();
		email.setHostName(smtp);
		email.setSmtpPort(465);
		email.setAuthenticator(new DefaultAuthenticator(login, password));
		email.setSSLOnConnect(true);
		email.setFrom(senderEmail);
		email.setSubject(fileName + " finish to download");
		email.setMsg("This is a test mail ... :-)");
		email.addTo(senderEmail);
		email.send();
	}

}
