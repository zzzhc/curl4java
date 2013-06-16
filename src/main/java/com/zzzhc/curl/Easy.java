package com.zzzhc.curl;

import java.io.Closeable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class Easy implements Closeable {
	private static final String DEFAULT_CONTENT_CHARSET = "UTF-8";

	private static final CurlEasyLib lib = CurlEasyLib.INSTANCE;

	private Pointer handle;

	private List<String> headers = new ArrayList<String>();
	private curl_slist.ByReference headerList = null;

	private MemoryDataHandler headerHandler;
	private MemoryDataHandler contentHandler;

	private PointerByReference stringRef = new PointerByReference();

	public Easy() {
		handle = lib.curl_easy_init();
		headerHandler = new MemoryDataHandler();
		contentHandler = new MemoryDataHandler();
	}

	Pointer getHandle() {
		return handle;
	}

	public void setopt(int option, Object value) {
		lib.curl_easy_setopt(handle, option, value);
	}

	public int perform() {
		if (!headers.isEmpty()) {
			StringBuilder ss = new StringBuilder();
			for (String header : headers) {
				if (ss.length() > 0) {
					ss.append("\r\n");
				}
				ss.append(header);
			}
			// only add one curl_slist item, otherwise jvm will crash when
			// calling curl_slist_free_all
			headerList = lib.curl_slist_append(headerList, ss.toString());
			setopt(CurlOption.CURLOPT_HTTPHEADER, headerList);
		}

		setopt(CurlOption.CURLOPT_HEADERFUNCTION, headerHandler);
		setopt(CurlOption.CURLOPT_WRITEFUNCTION, contentHandler);

		return lib.curl_easy_perform(handle);
	}

	public int getinfo(int info, Object... args) {
		return lib.curl_easy_getinfo(handle, info, args);
	}

	public void reset() {
		lib.curl_easy_reset(handle);
		freeHeaderList();
		headerHandler.reset();
		contentHandler.reset();
	}

	private void freeHeaderList() {
		if (headerList != null) {
			lib.curl_slist_free_all(headerList);
			headerList = null;
		}
	}

	@Override
	public void close() {
		if (handle != null) {
			lib.curl_easy_cleanup(handle);
			handle = null;
			freeHeaderList();
		}
	}

	public void setMethod(String method) {
		if ("GET".equalsIgnoreCase(method)) {
			setopt(CurlOption.CURLOPT_HTTPGET, 1);
		} else if ("POST".equalsIgnoreCase(method)) {
			setopt(CurlOption.CURLOPT_HTTPPOST, 1);
		} else if ("HEAD".equalsIgnoreCase(method)) {
			setopt(CurlOption.CURLOPT_NOBODY, 1);
		} else if ("PUT".equalsIgnoreCase(method)) {
			setopt(CurlOption.CURLOPT_PUT, 1);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void setUrl(String url) {
		setopt(CurlOption.CURLOPT_URL, url);
	}

	public void setUserAgent(String userAgent) {
		setopt(CurlOption.CURLOPT_USERAGENT, userAgent);
	}

	public void setReferer(String referer) {
		setopt(CurlOption.CURLOPT_REFERER, referer);
	}

	public void setCookie(String cookie) {
		setopt(CurlOption.CURLOPT_COOKIE, cookie);
	}

	public void setAcceptEncoding(String encodings) {
		setopt(CurlOption.CURLOPT_ACCEPT_ENCODING, encodings);
	}

	public void addHeaders(List<String> headers) {
		for (String header : headers) {
			addHeader(header);
		}
	}

	public void addHeader(String header) {
		headers.add(header);
	}

	public void setPostData(String data) {
		setopt(CurlOption.CURLOPT_POSTFIELDS, data);
	}

	public String getContentType() {
		return getString(CurlInfo.CURLINFO_CONTENT_TYPE);
	}

	public String getContentCharset() {
		String contentType = getContentType();
		if (contentType == null) {
			return DEFAULT_CONTENT_CHARSET;
		}

		Pattern charsetPattern = Pattern.compile("charset=([\\w\\-_]+)",
				Pattern.CASE_INSENSITIVE);
		Matcher m = charsetPattern.matcher(contentType);
		if (m.find()) {
			return m.group(1);
		}
		return DEFAULT_CONTENT_CHARSET;
	}

	public String getBodyString() {
		try {
			return new String(getBody(), getContentCharset());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private String getString(int info) {
		getinfo(info, stringRef);
		Pointer value = stringRef.getValue();
		if (value == null) {
			return null;
		}
		return value.getString(0);
	}

	private int getInt(int info) {
		int[] ints = new int[1];
		getinfo(info, ints);
		return ints[0];
	}

	private long getLong(int info) {
		long[] longs = new long[1];
		getinfo(info, longs);
		return longs[0];
	}

	private double getDouble(int info) {
		double[] doubles = new double[1];
		getinfo(info, doubles);
		return doubles[0];
	}

	public Info getInfo() {
		Info info = new Info();

		info.url = getString(CurlInfo.CURLINFO_EFFECTIVE_URL);
		info.httpCode = getInt(CurlInfo.CURLINFO_RESPONSE_CODE);
		info.contentType = getString(CurlInfo.CURLINFO_CONTENT_TYPE);

		info.totalTime = getDouble(CurlInfo.CURLINFO_TOTAL_TIME);
		info.namelookupTime = getDouble(CurlInfo.CURLINFO_NAMELOOKUP_TIME);
		info.connectTime = getDouble(CurlInfo.CURLINFO_CONNECT_TIME);
		info.appconnectTime = getDouble(CurlInfo.CURLINFO_APPCONNECT_TIME);
		info.pretransferTime = getDouble(CurlInfo.CURLINFO_PRETRANSFER_TIME);
		info.starttransferTime = getDouble(CurlInfo.CURLINFO_STARTTRANSFER_TIME);
		info.redirectTime = getDouble(CurlInfo.CURLINFO_REDIRECT_TIME);

		info.redirectCount = getInt(CurlInfo.CURLINFO_REDIRECT_COUNT);
		info.redirectUrl = getString(CurlInfo.CURLINFO_REDIRECT_URL);

		info.sizeUpload = getDouble(CurlInfo.CURLINFO_SIZE_UPLOAD);
		info.sizeDownload = getDouble(CurlInfo.CURLINFO_SIZE_DOWNLOAD);
		info.speedDownload = getDouble(CurlInfo.CURLINFO_SPEED_DOWNLOAD);
		info.speedUpload = getDouble(CurlInfo.CURLINFO_SPEED_UPLOAD);

		info.headerSize = getLong(CurlInfo.CURLINFO_HEADER_SIZE);
		info.requestSize = getLong(CurlInfo.CURLINFO_REQUEST_SIZE);
		info.sslVerifyresult = getLong(CurlInfo.CURLINFO_SSL_VERIFYRESULT);
		info.filetime = getLong(CurlInfo.CURLINFO_FILETIME);

		info.contentLengthDownload = getLong(CurlInfo.CURLINFO_CONTENT_LENGTH_DOWNLOAD);
		info.contentLengthUpload = getLong(CurlInfo.CURLINFO_CONTENT_LENGTH_UPLOAD);

		info.primaryIp = getString(CurlInfo.CURLINFO_PRIMARY_IP);
		info.primaryPort = getInt(CurlInfo.CURLINFO_PRIMARY_PORT);

		info.localIp = getString(CurlInfo.CURLINFO_LOCAL_IP);
		info.localPort = getInt(CurlInfo.CURLINFO_LOCAL_PORT);

		return info;
	}

	public byte[] getHeaders() {
		return headerHandler.getData();
	}

	public byte[] getBody() {
		return contentHandler.getData();
	}

	public static void main(String[] args) throws Exception {
		Easy easy = new Easy();
		for (int i = 0; i < 10; i++) {
			easy.setopt(CurlOption.CURLOPT_VERBOSE, 0);
			easy.setopt(CurlOption.CURLOPT_URL, "http://www.baidu.com/");
			easy.setopt(CurlOption.CURLOPT_ACCEPT_ENCODING, "gzip");

			List<String> headers = Arrays.asList("A: 1\r\nB: 2\r\nC: 3"
					.split("\\r\\n"));
			easy.addHeaders(headers);

			easy.perform();

			System.out.println(easy.getInfo());
			System.out.println("headers: "
					+ new String(easy.getHeaders(), DEFAULT_CONTENT_CHARSET));
			System.out.println("body: " + easy.getBodyString());

			easy.reset();

			Runtime.getRuntime().gc();
		}
		easy.close();
	}
}
