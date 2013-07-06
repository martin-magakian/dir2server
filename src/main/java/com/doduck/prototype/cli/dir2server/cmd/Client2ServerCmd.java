package com.doduck.prototype.cli.dir2server.cmd;

import com.beust.jcommander.Parameter;

public class Client2ServerCmd {

	/* save token */
	@Parameter(names = {"--accessKey", "--accesskey"}, description = "accessKey to save")
	private String accessKey;
	
	@Parameter(names = {"--secretKey", "--secretkey"}, description = "secretKey to save")
	private String secretKey;
	
	
	
	/* upload */
	@Parameter(names = "--upload", description = "File to upload")
	private String uploadFile;
	
	
	
	/* downlaod */
	@Parameter(names = "--download", description = "File to download")
	private String downloadFile;
	
	@Parameter(names = {"--excludeVersion", "-x" }, description = "exclude a version when downloading a file. It's used when an upload need to overwrite an old file so we don't download the old file.")
	private String excludeVersion;
	
	@Parameter(names = "--email", validateWith = EmailValide.class ,  description = "email to send email from/to")
	private String email;
	  
	@Parameter(names = "--smtp", description = "smtp to use to send email")
	private String smtp;
	
	@Parameter(names = "--login", description = "login to use to send email")
	private String login;
	
	@Parameter(names = "--password", password = true, description = "password to use to send email")
	private String password;

	
	
	/*
	 * Getter / Setter
	 * 
	 */
	
	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(String uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(String downloadFile) {
		this.downloadFile = downloadFile;
	}

	public String getExcludeVersion() {
		return excludeVersion;
	}

	public void setExcludeVersion(String excludeVersion) {
		this.excludeVersion = excludeVersion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSmtp() {
		return smtp;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	

}
