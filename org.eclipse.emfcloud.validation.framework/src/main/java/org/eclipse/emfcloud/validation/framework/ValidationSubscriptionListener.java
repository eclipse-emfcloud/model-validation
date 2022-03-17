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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emfcloud.modelserver.client.ModelServerNotification;
import org.eclipse.emfcloud.modelserver.client.Response;
import org.eclipse.emfcloud.modelserver.client.SubscriptionListener;
import org.eclipse.emfcloud.modelserver.emf.common.ValidationMapperModule;
import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ValidationSubscriptionListener implements SubscriptionListener {

	private final ValidationFramework framework;
	private final String modelUri;

	private static Logger LOG = LogManager.getLogger(ValidationSubscriptionListener.class);

	public ValidationSubscriptionListener(final ValidationFramework framework, final String modelUri) {
		this.framework = framework;
		this.modelUri = modelUri;
	}

	@Override
	public void onOpen(final Response<String> response) {
		try {
			this.framework.validate();
		} catch (Exception e) {
			LOG.error("Could not automatically validate " + modelUri);
			e.printStackTrace();
		}
	}

	@Override
	public void onClosing(final int code, @NotNull final String reason) {
	}

	@Override
	public void onFailure(final Throwable t) {
		t.printStackTrace();
	}

	@Override
	public void onClosed(final int code, @NotNull final String reason) {
	}

	@Override
	public void onFailure(final Throwable t, final Response<String> response) {
	}

	@Override
	public void onNotification(final ModelServerNotification notification) {
		if (notification.getType().equals("validationResult")) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.registerModule(new ValidationMapperModule());
				mapper.registerModule(new ValidationResultModule());
				this.framework.updateRecentValidationResult(this.framework.jsonToValidationResultList(mapper,
						mapper.readTree(notification.getData().get())));
			} catch (IOException e) {
				LOG.error("Recieving update from Modelserver failed for " + modelUri);
				e.printStackTrace();
			}
		}
	}
}
