/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package hello.portlet.filter.customize.jsp;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.portlet.RenderResponse;
import javax.portlet.filter.RenderResponseWrapper;

/**
 * @author István András Dézsi
 */
public class BufferedRenderResponseWrapper extends RenderResponseWrapper {

	public BufferedRenderResponseWrapper(RenderResponse response) {
		super(response);

		charWriter = new CharArrayWriter();
	}

	public OutputStream getOutputStream() throws IOException {
		if (getWriterCalled) {
			throw new IllegalStateException("getWriter already called");
		}

		getOutputStreamCalled = true;

		return super.getPortletOutputStream();
	}

	public PrintWriter getWriter() throws IOException {
		if (writer != null) {
			return writer;
		}

		if (getOutputStreamCalled) {
			throw new IllegalStateException("getOutputStream already called");
		}

		getWriterCalled = true;

		writer = new PrintWriter(charWriter);

		return writer;
	}

	public String toString() {
		String s = null;

		if (writer != null) {
			s = charWriter.toString();
		}

		return s;
	}

	protected CharArrayWriter charWriter;
	protected boolean getOutputStreamCalled;
	protected boolean getWriterCalled;
	protected PrintWriter writer;

}