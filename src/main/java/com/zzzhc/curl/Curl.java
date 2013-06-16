package com.zzzhc.curl;

/**
 * see http://curl.haxx.se/libcurl/c/
 */
public class Curl {
	private Curl() {
	}

	public static String version() {
		return CurlEasyLib.INSTANCE.curl_version();
	}

	public Easy get(String url) {
		Easy easy = new Easy();
		easy.setUrl(url);
		easy.setAcceptEncoding("gzip");
		
		easy.perform();

		return easy;
	}

}
