package br.psouza.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJsonPath {

	@Test
	public void deveVerificarPrimeiroNivel() {

		given()
		.when()
			.get("http://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name", containsString("João"))
			.body("age", greaterThan(18));

	}
	
	@Test
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/1");
		
		//path
		assertEquals(1, response.path("id"));
		assertEquals("João da Silva", response.path("name"));	
		
		//jsonpath
		JsonPath jpath = new JsonPath(response.asString());
		assertEquals(1, jpath.getInt("id"));
		assertEquals("João da Silva", jpath.get("name"));
		assertEquals(30, jpath.getInt("age"));
		
		//from
		int id = JsonPath.from(response.asString()).getInt("id");
		String name = JsonPath.from(response.asString()).getString("name");
		int age = JsonPath.from(response.asString()).getInt("age");
		assertEquals(1, id);
		assertEquals("João da Silva", name);
		assertEquals(30, age);
		
	}

	@Test
	public void deveVerificarSegundoNivel() {

		given()
				.when()
					.get("http://restapi.wcaquino.me/users/2")
				.then()
					.statusCode(200)
					.body("name", containsString("Joaquina"))
					.body("endereco.rua", containsString("Rua dos bobos"));

	}

	@Test
	public void deveVerificarListaSimples(){

		given()
				.when()
					.get("http://restapi.wcaquino.me/users/3")
				.then()
					.statusCode(200)
					.body("filhos", hasSize(2))
					.body("filhos[0].name", is("Zezinho"))
					.body("filhos[1].name", is("Luizinho"))
					.body("filhos.name", hasItem("Zezinho"))
					.body("filhos.name", hasItems("Zezinho", "Luizinho"))
		;
	}

	@Test
	public void deveRetornarUsuarioInexistente(){

		given()
		.when()
				.get("http://restapi.wcaquino.me/users/4")
		.then()
				.statusCode(404)
				.body("error", is("Usuário inexistente"))
		;

		//Imprimindo corpo e status code
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/users/4");
		System.out.println("Status Code: " + response.statusCode());
		System.out.println(response.body().asString());
	}

}
