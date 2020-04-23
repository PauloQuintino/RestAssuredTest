package br.psouza.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class VerbosTest {
	
	@Test
	public void deveSalvarUsuario() {
		given()
			.log().all() //exibe na tela todo o log da requisição
			.contentType("application/json") //informa que o conteúdo é Json
			.body("{\"name\":\"Jose\",\"age\":50} ") //informa o conteúdo a ser enviado no body 
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
		;
	}
	
	//Usando MAP
	@Test
	public void deveSalvarUsuarioUsandoMap() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("name", "User Map");
		parametros.put("age", 25);
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(parametros)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("User Map"))
			.body("age", is(25))			
		;
		
	}
	
	// USANDO OBJECT
	@Test
	public void deveCriarUsuarioUsandoObjeto() {
		User user = new User("User Object", 99);
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is(user.getName()))
			.body("age", is(user.getAge()))
		;
		
	}
	
	@Test
	public void nãoDeveSalvarUsuarioSemNome() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"age\": 50}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))
		;
	}
	
	@Test
	public void deveSalvarUsuarioViaXML() {
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body("<user><name>Jose</name><age>50</age></user>") 
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Jose"))
			.body("user.age", is("50"))
		;
	}
	
	@Test
	public void deveAlterarUsuario() {
		given()
			.log().all() //exibe na tela todo o log da requisição
			.contentType("application/json") //informa que o conteúdo é Json
			.body("{\"name\":\"Jose Alterado\",\"age\":33} ") //informa o conteúdo a ser enviado no body 
		.when()
			.put("https://restapi.wcaquino.me/users/1") //verbo PUT para atualização de dados de um serviço
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Jose Alterado"))
			.body("age", is(33))
		;
	}
	
	@Test
	public void deveParametrizarURL() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\":\"Jose Alterado\",\"age\":33} ")
			.pathParam("parametro", "users") //pathParam vai passar os parametros na pré-condição (chave-valor)
			.pathParam("userId", 1)
		.when()
			.put("https://restapi.wcaquino.me/{parametro}/{userId}") //verbo PUT para atualização de dados de um serviço
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Jose Alterado"))
			.body("age", is(33))
		;
	}
	
	/* ---------- DELETE -----------  */
	
	@Test
	public void deveExcluirUsuarioPorId() {
		
		given()
			.log().all()
			.contentType("application/json")
			.pathParam("parametro", "users")
			.pathParam("id", 1)
		.when()
			.delete("https://restapi.wcaquino.me/{parametro}/{id}")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	
	@Test
	public void deveExcluirUsuarioPorIdComMap() {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", 1);
		
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.pathParam("id", param.get("id"))
		.when()
			.delete("https://restapi.wcaquino.me/users/{id}")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
}

