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

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.OSDetector;
import com.liferay.portal.util.PropsValues;

import hello.portlet.constants.HelloWorldPortletKeys;

import java.io.File;
import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.PortletFilter;
import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.RenderResponseWrapper;

import jodd.io.FileUtil;

import org.osgi.service.component.annotations.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author István András Dézsi
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + HelloWorldPortletKeys.HELLO_WORLD,
	service = PortletFilter.class
)
public class HelloWorldRenderFilter implements RenderFilter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(
			RenderRequest request, RenderResponse response, FilterChain chain)
		throws IOException, PortletException {

		RenderResponseWrapper renderResponseWrapper =
			new BufferedRenderResponseWrapper(response);

		chain.doFilter(request, renderResponseWrapper);

		String text = renderResponseWrapper.toString();

		System.out.println(text);

		if (text != null) {
			if ((text != StringPool.BLANK) &&
				(_startup_notes != StringPool.BLANK)) {

				text = text + "\n<p>" + _startup_notes + "</p>\n";
			}

			response.getWriter(
			).write(
				text
			);
		}
	}

	@Override
	public void init(FilterConfig config) throws PortletException {
		String fileName = "startupnotes";

		if (OSDetector.isWindows()) {
			fileName += ".txt";
		}

		String filePath = StringBundler.concat(_DATA_DIR, fileName);

		File file = new File(filePath);

		if (file.exists()) {
			try {
				_startup_notes = FileUtil.readString(file);
			}
			catch (IOException ioe) {
				if (_log.isWarnEnabled()) {
					_log.warn("Could not read the startup notes file!");
				}
			}
		}
		else {
			if (_log.isWarnEnabled()) {
				_log.warn("Could not locate the startup notes file!");
			}
		}
	}

	private static final String _DATA_DIR = StringBundler.concat(
		PropsValues.LIFERAY_HOME, StringPool.SLASH, "data", StringPool.SLASH);

	private static final Logger _log = LoggerFactory.getLogger(
		HelloWorldRenderFilter.class);

	private static String _startup_notes = StringPool.BLANK;

}