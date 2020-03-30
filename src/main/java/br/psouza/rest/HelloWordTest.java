package br.psouza.rest;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class HelloWordTest {

	@Test
	public void testHelloWord() {

		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");

		System.out.println(response.getBody().asString());
		System.out.println("Status Code: " + response.getStatusCode());		
		ValidatableResponse validate = response.then();
		validate.statusCode(200);

	}

}
