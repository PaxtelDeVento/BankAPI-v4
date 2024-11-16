package br.edu.utfpr.bankapi.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.edu.utfpr.bankapi.model.Account;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
class AccountControllerTest {

    @Autowired
    MockMvc mvc;

    // Gerenciador de persistência para os testes des classe
    @Autowired
    TestEntityManager entityManager;

    Account account, account2; // Conta para os testes

    @BeforeEach
    void setup() {
        account = new Account("Lauro Lima",
                12346, 1000, 0);
        account2 = new Account("Juca",
                777, 2000, 0);
        entityManager.persist(account); // salvando uma conta
        entityManager.persist(account2);
    }

    // GET ALL --------------------------------------------------------

    @Test
    void deveriaRetornarStatus200ParaRequisicaoOKGetAll() throws Exception {
        // ARRANGE

        // ACT
        var res = mvc.perform(
                MockMvcRequestBuilders.get("/account")).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, res.getStatus());
    }

    // GET BY NUMBER --------------------------------------------------------

    @Test
    void deveriaRetornarStatus404ParaRequisicaoInvalidaUsuarioNaoEncontradoGetByNumber() throws Exception {
        // ARRANGE

        // ACT
        var res = mvc.perform(
                MockMvcRequestBuilders.get("/account/66")).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(404, res.getStatus());
    }

    @Test
    void deveriaRetornarStatus200ParaRequisicaoUsuarioEncontradoGetByNumber() throws Exception {
        // ARRANGE

        // ACT
        var res = mvc.perform(
                MockMvcRequestBuilders.get("/account/777")).andReturn().getResponse();

        // ASSERT
        Assertions.assertEquals(200, res.getStatus());
    }

    // SAVE --------------------------------------------------------

    @Test
    void deveriaRetornarStatus201ParaRequisicaoOKSalvar() throws Exception {
        var json = """
            {
                    "name": "Ricardo Sobjak",
                    "number": 12345,
                    "balance": 1000,
                    "specialLimit": 1000
            }
                """;

    // ACT
    var res = mvc.perform(
            MockMvcRequestBuilders.post("/account")
                    .content(json).contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

    // ASSERT
    Assertions.assertEquals(201, res.getStatus());
    Assertions.assertEquals("application/json", res.getContentType());
    }

    @Test
    void deveriaRetornarStatus400ParaRequisicaoInvalidaSalva() throws Exception {
        var json = """
            {}
                """;

    // ACT
    var res = mvc.perform(
            MockMvcRequestBuilders.post("/account")
                    .content(json).contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

    // ASSERT
    Assertions.assertEquals( 400, res.getStatus());
    }

    // UPDATE --------------------------------------------------------

    @Test
    void deveriaRetornarStatus201ParaRequisicaoOKUpdate() throws Exception {
        var json = """
            {
                    "name": "Ricardo Subjuca",
                    "number": 66,
                    "balance": 1000,
                    "specialLimit": 1000
            }
                """;

    // ACT
    var res = mvc.perform(
            MockMvcRequestBuilders.put("/account/1")
                    .content(json).contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

    // ASSERT
    Assertions.assertEquals(200, res.getStatus());
    Assertions.assertEquals("application/json", res.getContentType());
    }

    @Test
    void deveriaRetornarStatus400ParaRequisicaoInvalidaUpdate() throws Exception {
        var json = "{}";

    // ACT
    var res = mvc.perform(
            MockMvcRequestBuilders.put("/account/1")
                    .content(json).contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

    // ASSERT
    Assertions.assertEquals(400, res.getStatus());
    }

}
