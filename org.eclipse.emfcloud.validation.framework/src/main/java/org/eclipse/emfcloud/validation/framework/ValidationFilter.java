/*******************************************************************************
 * Copyright (c) 2020-2021 EclipseSource and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0, or the MIT License which is
 * available at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: EPL-2.0 OR MIT
 ******************************************************************************/
package org.eclipse.emfcloud.validation.framework;

public class ValidationFilter {

	private int code;
	private String source;

	public ValidationFilter(int code, String source) {
		this.code = code;
		this.source = source;
	}

	public int getCode() {
		return code;
	}

	public String getSource() {
		return source;
	}

	@Override
	public boolean equals(Object obj) {
		ValidationFilter other = (ValidationFilter) obj;
		if (code == other.code && source.equals(other.source)) {
			return true;
		}
		return false;
	}

}
