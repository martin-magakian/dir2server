package com.doduck.prototype.cli.dir2server;

import java.io.IOException;

import com.dropbox.client2.ProgressListener;

public class PrintProgressListener extends ProgressListener {

	@Override
	public void onProgress(long bytes, long total) {
        String anim = "|/-\\";
        int x = (int) (bytes * 1.0 / total * 100);
        String data = "\r" + anim.charAt(x % anim.length()) + " " + x + "%\r";
        try {
            System.out.write(data.getBytes());
        } catch (IOException e) {
            // do nothing, be quite if something wrong happens
        }
	}
}
