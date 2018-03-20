package com.stocks.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocks.api.dal.StockRepository;
import com.stocks.api.exceptions.ResourceNotFoundException;
import com.stocks.api.exceptions.StockAlreadyExistsException;
import com.stocks.api.manipulator.StockService;
import com.stocks.api.model.ErrorResponse;
import com.stocks.api.model.Stock;
import org.hibernate.exception.ConstraintViolationException;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests {@link StocksController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StocksControllerTest {
    private MockMvc mockMvc;

    @MockBean private StockService stockService;
    @MockBean private StockRepository stockRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(new StocksController(stockService))
                .setControllerAdvice(new ApiErrorHandler())
                .build();
    }

    // HAPPY CASES
    @Test
    public void testGetStock() throws Exception {
        mockMvc.perform(get("/api/stocks/1"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void testGetStocks() throws Exception {
        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void testPutVerb() throws Exception {
        final Double price = 1.23;

        final String requestBody = new ObjectMapper().writeValueAsString(price);
        mockMvc.perform(put("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(405))
        ;
        verifyNoMoreInteractions(stockService);
    }

    @Test
    public void testPostStock() throws Exception {
        final Stock stock = new Stock().withPrice(1.23);
        when(stockService.create(any())).thenReturn(stock);

        final String requestBody = new ObjectMapper().writeValueAsString(stock);
        mockMvc.perform(post("/api/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(200))
        ;


        verify(stockService, times(1)).create(any());
    }

    @Test
    public void testGetAll() throws Exception {
        given(stockService.get(any(int.class))).willReturn(new JSONObject());

        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().is(200));

        mockMvc.perform(get("/api/stocks?page=2"))
                .andExpect(status().is(200));

    }

    // HTTP ERROR CODES and ERROR MESSAGES

    @Test
    public void testGetWithException() throws Exception {
        final String errorMessage = "Something is wrong";

        doThrow(new RuntimeException(errorMessage)).when(stockService).get(any());

        mockMvc.perform(get("/api/stocks/1"))
                .andExpect(status().is(500))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                    assertEquals(errorResponse.getErroMessage(), errorMessage);
                    assertEquals(errorResponse.getErrorCode(), "500");
                })
        ;
    }

    @Test
    public void testGetAllWithException() throws Exception {
        final String errorMessage = "Something is wrong";

        doThrow(new RuntimeException(errorMessage)).when(stockService).get(anyInt());

        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().is(500))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                    assertEquals(errorResponse.getErroMessage(), errorMessage);
                    assertEquals(errorResponse.getErrorCode(), "500");
                })
        ;
    }

    @Test
    public void testPostStockWithException() throws Exception {
        final Stock stock = new Stock().withPrice(2.3).withSymbol("a symbol").withName("a name");

        final String errorMessage = "Something is wrong";

        given(stockService.create(any())).willThrow(new RuntimeException(errorMessage));

        final String requestBody = new ObjectMapper().writeValueAsString(stock);
        mockMvc.perform(post("/api/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(500))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                    assertEquals(errorResponse.getErroMessage(), errorMessage);
                    assertEquals(errorResponse.getErrorCode(), "500");
                })
        ;

        verify(stockService, times(1)).create(any());
    }

    @Test
    public void testPostToUpdate() throws Exception {
        final Stock stock = new Stock().withPrice(1.23).withVersion(3L);

        final String errorMessage = "a post error message";

        final String requestBody = new ObjectMapper().writeValueAsString(stock);
        mockMvc.perform(post("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(200))
        ;

        Mockito.verify(stockService, times(1)).updatePrice(1l, 1.23d, 3l);
    }

    @Test
    public void testCreatingExistingStock() throws Exception {
        // GIVEN
        final String errorMessage= "It already exists";
        final Stock stock = new Stock().withPrice(2.3).withSymbol("a symbol").withName("a name");

        when(stockService.create(any(Stock.class))).thenThrow(new StockAlreadyExistsException(errorMessage));

        final String requestBody = new ObjectMapper().writeValueAsString(stock);

        mockMvc.perform(post("/api/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(409))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                    assertEquals(errorResponse.getErroMessage(), errorMessage);
                    assertEquals(errorResponse.getErrorCode(), "409");
                })
        ;
    }

    @Test
    public void testUpdateNonExistingStock() throws Exception {
        final String errorMessage= "It does not exists";
        doThrow(new ResourceNotFoundException(errorMessage)).when(stockService).updatePrice(anyLong(), anyDouble(), anyLong());

        final String requestBody = new ObjectMapper().writeValueAsString(new Stock().withPrice(1.23D).withVersion(3L));

        mockMvc.perform(post("/api/stocks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(404))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                    assertEquals(errorResponse.getErroMessage(), errorMessage);
                    assertEquals(errorResponse.getErrorCode(), "404");
                })
        ;
    }

    @Test
    public void testGetAllWithNegativePageNumber() throws Exception {
        given(stockService.get(any(int.class))).willReturn(new JSONObject());

        mockMvc.perform(get("/api/stocks?page=-2"))
                .andExpect(status().is(500))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                    assertEquals(errorResponse.getErroMessage(), "Page number can not be less than 1");
                    assertEquals(errorResponse.getErrorCode(), "500");
                })
        ;
    }

    @Test
    public void testDataIntegrityException() throws Exception {
        final String errorMessage= "Data integrity voilated";
        final Stock stock = new Stock().withPrice(2.3).withSymbol("a symbol").withName("a name");

        final SQLException exception = new SQLException("", "");
        final ConstraintViolationException constraintViolationException = new ConstraintViolationException("Primary key voilation.", exception , "Primary key voilation");
        given(stockService.create(any(Stock.class))).willThrow(new DataIntegrityViolationException(errorMessage, constraintViolationException));

        final String requestBody = new ObjectMapper().writeValueAsString(stock);

        mockMvc.perform(post("/api/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is(409))
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    ErrorResponse errorResponse = new ObjectMapper().readValue(content, ErrorResponse.class);
                    assertNotNull(errorResponse.getErroMessage());
                    assertEquals(errorResponse.getErrorCode(), "409");
                })
        ;
    }
}
