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

import java.io.IOException;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emfcloud.modelserver.emf.common.ValidationMapperModule;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ValidationResultModule extends SimpleModule {

	private static final long serialVersionUID = 1L;

	public ValidationResultModule() {
		addDeserializer(ValidationResult.class, new ValidationResultDeserializer());
	}

	public static class ValidationResultDeserializer extends JsonDeserializer<ValidationResult> {

		@Override
		public ValidationResult deserialize(final JsonParser p, final DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			JsonNode node = p.getCodec().readTree(p);
			String id = node.get(ValidationMapperModule.ID).asText();
			BasicDiagnostic diagnostic = p.getCodec().treeToValue(node, BasicDiagnostic.class);
			diagnostic.recomputeSeverity();
			return new ValidationResult(id, diagnostic);
		}

	}

}
