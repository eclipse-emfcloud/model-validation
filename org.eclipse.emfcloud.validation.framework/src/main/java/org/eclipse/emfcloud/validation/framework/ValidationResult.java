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

import org.eclipse.emf.common.util.BasicDiagnostic;

public class ValidationResult {

	private String identifier;
	private BasicDiagnostic diagnostic;

	public ValidationResult(String identifier, BasicDiagnostic diagnostic) {
		this.identifier = identifier;
		this.diagnostic = diagnostic;
	}

	public String getIdentifier() {
		return identifier;
	}

	public BasicDiagnostic getDiagnostic() {
		return diagnostic;
	}

}
