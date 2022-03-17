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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emfcloud.jackson.module.EMFModule;
import org.eclipse.emfcloud.modelserver.client.ModelServerClientApiV1;
import org.eclipse.emfcloud.modelserver.client.ModelServerNotification;
import org.eclipse.emfcloud.modelserver.client.Response;
import org.eclipse.emfcloud.modelserver.emf.common.EMFFacetConstraints;
import org.eclipse.emfcloud.modelserver.emf.common.JsonResponse;
import org.eclipse.emfcloud.modelserver.emf.common.ValidationMapperModule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.ResponseBody;

public class ValidationFrameworkTest {

	ValidationFramework framework;

	ModelServerClientApiV1<EObject> mockAPI;

	@Before
	public void setup() {
		mockAPI = Mockito.mock(ModelServerClientApiV1.class);
		Mockito.when(mockAPI.getValidationConstraints("test")).thenReturn(mockConstraintList());
		Mockito.when(mockAPI.validate("test")).thenReturn(mockValidation());
		ValidationResultChangeListener changeListener = new ValidationResultChangeListener() {
			public void changed(java.util.List<ValidationResult> newResult) {
			}
		};
		framework = new ValidationFramework("test", mockAPI, changeListener);
	}

	@Test
	public void testConstraintList() throws MalformedURLException {
		// Retrieve Constraint List
		framework.getConstraintList();

		// Check Constraint List
		assertNull(framework.getInputValidationMap().get("Employee").get("tasks").getEnumeration());
		assertTrue(framework.getInputValidationMap().get("Employee").get("tasks").getWhiteSpace() == 2);
	}

	@Test
	public void testValidation() throws IOException, InterruptedException, ExecutionException {
		// Validate Model
		framework.validate();
		// Check Result
		assertTrue(framework.getRecentValidationResult().size() == 2);
	}

	@Test
	public void testSubscription() {
		// Subscribe
		ValidationSubscriptionListener listener = new ValidationSubscriptionListener(framework, "test");
		// Simulate OnOpen
		listener.onOpen(null);
		// Check ValidationResult
		assertTrue(framework.getRecentValidationResult().size() == 2);
		// Simulate Notification
		listener.onNotification(new ModelServerNotification("validationResult", Optional
				.of(getDiagnostic("src/test/java/org/eclipse/emfcloud/validation/framework/emptyTest.ecore").toString())));
		// Check ValidationResult
		assertTrue(framework.getRecentValidationResult().size() == 0);
	}

	@Test
	public void testFilter() throws IOException, InterruptedException, ExecutionException {
		// Validate
		framework.validate();
		// Check Result
		assertTrue(framework.getRecentValidationResult().size() == 2);
		// Add Filter
		framework.addValidationFilter(List.of(new ValidationFilter(29, "org.eclipse.emf.ecore.model")));
		// Check Result
		assertTrue(framework.getRecentValidationResult().size() == 1);
		// Remove Filter
		framework.removeValidationFilter(List.of(new ValidationFilter(29, "org.eclipse.emf.ecore.model")));
		// Check Result
		assertTrue(framework.getRecentValidationResult().size() == 2);
		// Toggle Filter
		framework.toggleValidationFilter(new ValidationFilter(6, "org.eclipse.emf.ecore.model"));
		// Check Result
		assertTrue(framework.getRecentValidationResult().size() == 1);
	}

	private CompletableFuture<Response<String>> mockConstraintList() {
		ObjectMapper mapper = EMFModule.setupDefaultMapper();
		Map<String, Map<String, EMFFacetConstraints>> constraintMap = new HashMap<>();
		Map<String, Object> mockFacets = new HashMap<String, Object>();
		mockFacets.put(EMFFacetConstraints.WHITESPACE, 2);
		mockFacets.put(EMFFacetConstraints.ENUMERATION, List.of());
		mockFacets.put(EMFFacetConstraints.PATTERN, List.of());
		mockFacets.put(EMFFacetConstraints.LENGTH, -1);
		mockFacets.put(EMFFacetConstraints.MINLENGTH, -1);
		mockFacets.put(EMFFacetConstraints.MAXLENGTH, -1);
		mockFacets.put(EMFFacetConstraints.TOTALDIGITS, -1);
		mockFacets.put(EMFFacetConstraints.FRACTIONDIGITS, -1);
		mockFacets.put(EMFFacetConstraints.MININCLUSIVE, null);
		mockFacets.put(EMFFacetConstraints.MAXINCLUSIVE, null);
		mockFacets.put(EMFFacetConstraints.MINEXCLUSIVE, null);
		mockFacets.put(EMFFacetConstraints.MAXEXCLUSIVE, null);
		EMFFacetConstraints emfFacetConstraints = new EMFFacetConstraints(mockFacets);
		Map<String, EMFFacetConstraints> innerMap = new HashMap<>();
		innerMap.putIfAbsent("tasks", emfFacetConstraints);
		constraintMap.put("Employee", innerMap);
		JsonNode body = JsonResponse.success(mapper.valueToTree(constraintMap));

		return CompletableFuture.completedFuture(getResponse("validation/constraints", body));
	}

	private CompletableFuture<Response<String>> mockValidation() {
		return CompletableFuture.completedFuture(getResponse("validation", JsonResponse
				.validationResult(getDiagnostic("src/test/java/org/eclipse/emfcloud/validation/framework/test.ecore"))));
	}

	private JsonNode getDiagnostic(String path) {
		ObjectMapper mapper = EMFModule.setupDefaultMapper();
		// Load Resource
		ResourceSet set = new ResourceSetImpl();
		set.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		URI uri = URI.createFileURI(path);
		Resource res = set.getResource(uri, true);

		// Register Serialization
		mapper.registerModule(new ValidationMapperModule(res));

		// Create Diagnostic
		BasicDiagnostic diagnostic = Diagnostician.INSTANCE.createDefaultDiagnostic(res.getContents().get(0));
		Diagnostician.INSTANCE.validate(res.getContents().get(0), diagnostic,
				Diagnostician.INSTANCE.createDefaultContext());

		return mapper.valueToTree(diagnostic);
	}

	private Response<String> getResponse(String url, JsonNode body) {
		okhttp3.Response.Builder httpBuilder = new okhttp3.Response.Builder();
		httpBuilder.request(new okhttp3.Request.Builder().url("http://localhost:8081/api/v1/" + url + "/test").build())
				.protocol(Protocol.HTTP_1_1).code(200).message("success")
				.body(ResponseBody.create(body.toString(), MediaType.get("application/json")));
		return new Response<String>(httpBuilder.build());
	}

}
