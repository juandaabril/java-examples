package com.juandaabril.producer.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juandaabril.producer.domain.DomainEvent;
import com.juandaabril.producer.http.BookEventProducer;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.juandaabril.producer.http.controller.SaveANewBookController.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SaveANewBookController.class)
@AutoConfigureMockMvc
public class SaveANewBookControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookEventProducer bookEventProducer;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void should_create_a_new_book() throws Exception {
        SaveANewBookRequest request = new SaveANewBookRequest();
        request.id = UUID.randomUUID();
        request.bookName = "Any BookName";
        request.author = "Any Author";

        when(bookEventProducer.send(isA(DomainEvent.class))).thenReturn(null);

        mockMvc.perform(
            post("/books/save-a-new-book")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    public void should_validate_id() throws Exception {
        SaveANewBookRequest request = new SaveANewBookRequest();
        request.bookName = "Any BookName";
        request.author = "Any Author";

        when(bookEventProducer.send(isA(DomainEvent.class))).thenReturn(null);

        mockMvc.perform(
                post("/books/save-a-new-book")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.id", Matchers.is("must not be null")));
    }

    @Test
    public void should_validate_bookName() throws Exception {
        SaveANewBookRequest request = new SaveANewBookRequest();
        request.id = UUID.randomUUID();
        request.author = "Any Author";

        when(bookEventProducer.send(isA(DomainEvent.class))).thenReturn(null);

        mockMvc.perform(
                post("/books/save-a-new-book")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.bookName", Matchers.is("must not be null")));
    }

    @Test
    public void should_validate_author() throws Exception {
        SaveANewBookRequest request = new SaveANewBookRequest();
        request.id = UUID.randomUUID();
        request.bookName = "Any BookName";

        when(bookEventProducer.send(isA(DomainEvent.class))).thenReturn(null);

        mockMvc.perform(
                post("/books/save-a-new-book")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.author", Matchers.is("must not be null")));
    }
}
