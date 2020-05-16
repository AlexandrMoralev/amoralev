package ru.job4j.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountJsonMappingTest {

    private static final long ACC_ID = 1L;
    private static final String ACC_FIO = "Иванов Иван Иваныч";
    private static final String ACC_PHONE = "+7 911 000 11 22";

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void testExistingAccountJsonMapping() throws JsonProcessingException {
        Account account = Account.newBuilder()
                .setId(ACC_ID)
                .setFio(ACC_FIO)
                .setPhone(ACC_PHONE)
                .build();

        String accountAsJsonString = mapper.writeValueAsString(account);

        JsonNode jsonNode = mapper.readTree(accountAsJsonString);
        assertEquals(jsonNode.get("id").asLong(), ACC_ID);
        assertEquals(jsonNode.get("fio").asText(), ACC_FIO);
        assertEquals(jsonNode.get("phone").asText(), ACC_PHONE);

        Account accountFromJson = mapper.readValue(accountAsJsonString, Account.class);
        assertEquals(ACC_ID, accountFromJson.getId());
        assertEquals(ACC_FIO, accountFromJson.getFio());
        assertEquals(ACC_PHONE, accountFromJson.getPhone());
        assertEquals(account, accountFromJson);
    }

    @Test
    void testNewAccountJsonMapping() throws JsonProcessingException {

        String expected = "{\"id\":null,\"fio\":\"Иванов Иван Иваныч\",\"phone\":\"+7 911 000 11 22\"}";

        Account account = Account.newBuilder()
                .setFio(ACC_FIO)
                .setPhone(ACC_PHONE)
                .build();
        String mapped = mapper.writeValueAsString(account);

        assertEquals(expected, mapped);

        JsonNode jsonNode = mapper.readTree(mapped);
        assertTrue(jsonNode.get("id").isNull());
        assertEquals(jsonNode.get("fio").asText(), ACC_FIO);
        assertEquals(jsonNode.get("phone").asText(), ACC_PHONE);

        Account accountFromJson = mapper.readValue(mapped, Account.class);
        Account savedAccount = Account.newBuilder().of(accountFromJson).setId(ACC_ID).build();
        assertEquals(ACC_ID, savedAccount.getId());
        assertEquals(ACC_FIO, savedAccount.getFio());
        assertEquals(ACC_PHONE, savedAccount.getPhone());
        assertEquals(account, savedAccount);
    }
}