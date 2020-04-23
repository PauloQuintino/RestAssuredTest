package br.psouza.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;

public class AuthTest {

	@Test
	public void deveAcessarPokeAPI() {
		given()
			.log().all()
		.when()
			.get("https://pokeapi.co/api/v2/pokemon-form/1")
		.then()
			.log().all()
			.contentType(ContentType.JSON)
			.body("pokemon.name", is("bulbasaur"))
		;
	}
	
	//Autenticação com Chave de usuário
	@Test
	public void deveObterClima() {
		
		given()
			.log().all()
			.queryParam("q", "Sao Paulo,BR")
			.queryParam("appid", "29a8b089622c1ee88956c944b02deb14") // -> chave de usuário
			.queryParam("units", "metric")
		.when()
			.get("https://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
			.statusCode(200)
			.body("sys.country", is("BR"))
			.body("name", is("São Paulo")) //lembrar sempre de verificar os níveis do Json
			.body("main.temp", lessThan(30.0f))
		;
	}
	
	/* Autenticação Básica */
	
	@Test
	public void deveRealizarAutenticaçãoBasica() {
		
		given()
			.log().all()
			.auth().basic("admin", "senha") // autentic. básica de usuário e senha
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveRealizarAutenticaçãoComChallenge() {
		
		given()
			.log().all()
			.auth().preemptive().basic("admin", "senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveRealizarLoginComTokenJWT() {
		
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "pauloqfs16@gmail.com");
		login.put("senha", "Paulo010203@");
		
			//Logando e obtendo o token
			String token = given()
				.log().all()
				.body(login)
				.contentType(ContentType.JSON)
			.when()
				.post("http://barrigarest.wcaquino.me/signin")
			.then()
				.log().all()
				.statusCode(200)
				.extract().path("token") // --> extraindo o token
			;		
			
			//Obtendo as contas do usuário logado
			given()
				.log().all()
				.header("Authorization","JWT " + token)
			.when()
				.get("http://barrigarest.wcaquino.me/contas")
			.then()
				.log().all()
				.statusCode(200)
				.body("nome", hasItem("Conta 1")) //Aqui foi usado hasItem() pois na coleção há mais de um item. O is() é usado para validar um campo específico
			;
	}
	
}
