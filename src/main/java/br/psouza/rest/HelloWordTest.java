package br.psouza.rest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
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
	
	@Test
	public void outraFormaDeGet() {
		//pega o caminho e valida se o retorno do status code é igual a 200 (tudo na mesma linha)
		get("http://restapi.wcaquino.me/ola").then().statusCode(200);
		
		//realizando get no estilo de escrita Gherkin		
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
			.statusCode(200);
	}
	
	@Test
	public void deveConhecerValidacaoComMatchers() {
		
		//Matchers é uma classe que faz parte do pacote Hamcrest
		
		assertThat("Maria", Matchers.is("Maria"));
		assertThat(128, Matchers.is(128));
		assertThat(151, Matchers.greaterThan(150));
		
		List<Integer> impares = Arrays.asList(1,3,5,7,9);
		//utilizando métodos Matchers importados estaticamente
		assertThat(impares, hasSize(5));
		assertThat(impares, contains(1,3,5,7,9));
		assertThat(impares,	containsInAnyOrder(9,7,1,3,5));
		assertThat(impares, hasItem(3));
		
		
		assertThat("Maria", not("João"));
	}
	
	@Test
	public void deveValidarOBody() {
		
		given()
		.when()
			.get("http://restapi.wcaquino.me/ola")
		.then()
			.statusCode(200)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(not(nullValue()));
		
	}
}
