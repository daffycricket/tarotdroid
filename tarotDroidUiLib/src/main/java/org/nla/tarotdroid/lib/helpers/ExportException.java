package org.nla.tarotdroid.lib.helpers;

@SuppressWarnings("serial")
public class ExportException extends Exception {

	public ExportException() {
		super();
	}

	public ExportException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ExportException(String detailMessage) {
		super(detailMessage);
	}

	public ExportException(Throwable throwable) {
		super(throwable);
	}
}
