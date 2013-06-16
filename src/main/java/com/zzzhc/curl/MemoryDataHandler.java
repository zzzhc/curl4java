package com.zzzhc.curl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.sun.jna.Pointer;

public class MemoryDataHandler implements CurlEasyLib.DataHandler {

	private ByteArrayOutputStream data = new ByteArrayOutputStream();

	@Override
	public int invoke(Pointer contents, int size, int nmemb, Pointer userp) {
		int total = size * nmemb;
		byte[] data = contents.getByteArray(0, total);
		try {
			this.data.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data.length;
	}

	public byte[] getData() {
		return data.toByteArray();
	}

	public String toString(String charset) throws UnsupportedEncodingException {
		return data.toString(charset);
	}
	
	public void reset() {
		data.reset();
	}
}
