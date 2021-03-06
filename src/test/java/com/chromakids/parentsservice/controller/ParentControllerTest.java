package com.chromakids.parentsservice.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chromakids.parentsservice.model.Parent;
import com.chromakids.parentsservice.repository.ParentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ParentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ParentRepository repository;

  private Parent peterParker;
  private Parent bruceWayne;

  @Before
  public void setUp() {
    ZoneOffset timeZone = ZoneOffset.UTC;
    peterParker = new Parent.Builder()
        .setId(1L)
        .setName("Peter")
        .setSurname("Parker")
        .setAddress("5th Avenue")
        .setCreatedAt(Date.from(ZonedDateTime.of(LocalDateTime.of(2017, 5, 12, 5, 45), timeZone).toInstant()))
        .setUpdatedAt(Date.from(ZonedDateTime.of(LocalDateTime.of(2017, 6, 1, 7, 55), timeZone).toInstant()))
        .createParent();

    bruceWayne = new Parent.Builder()
        .setId(2L)
        .setName("Bruce")
        .setSurname("Wayne")
        .setAddress("Gotham")
        .setCreatedAt(Date.from(ZonedDateTime.of(LocalDateTime.of(2016, 3, 23, 15, 38), timeZone).toInstant()))
        .setUpdatedAt(Date.from(ZonedDateTime.of(LocalDateTime.of(2018, 8, 21, 23, 49), timeZone).toInstant()))
        .createParent();
  }

  @Test
  public void shouldReturnAnEmptyListOfParents() throws Exception {
    when(repository.findAll()).thenReturn(Collections.emptyList());

    MvcResult mvcResult = mockMvc.perform(get("/parents"))
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(Collections.EMPTY_LIST))
        .andReturn();

    mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("[]"));
  }

  @Test
  public void shouldReturnAListOfParents() throws Exception {
    when(repository.findAll()).thenReturn(Arrays.asList(peterParker, bruceWayne));

    MvcResult mvcResult = mockMvc.perform(get("/parents"))
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(List.class)))
        .andReturn();

    mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("[{\"id\":1,\"name\":\"Peter\",\"surname\":\"Parker\",\"address\":\"5th Avenue\",\"createdAt\":1494567900000,\"updatedAt\":1496303700000},{\"id\":2,\"name\":\"Bruce\",\"surname\":\"Wayne\",\"address\":\"Gotham\",\"createdAt\":1458747480000,\"updatedAt\":1534895340000}]"));
  }

  @Test
  public void shouldReturnAnExistingParent() throws Exception {
    when(repository.findOne(any(Long.class))).thenReturn(peterParker);

    MvcResult mvcResult = mockMvc.perform(get("/parents/1"))
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
        .andReturn();

    mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("{\"id\":1,\"name\":\"Peter\",\"surname\":\"Parker\",\"address\":\"5th Avenue\",\"createdAt\":1494567900000,\"updatedAt\":1496303700000}"));
  }

  @Test
  public void shouldReturnNotFoundWhenGettingANonExistingParent() throws Exception {
    when(repository.findOne(any(Long.class))).thenReturn(null);

    MvcResult mvcResult = mockMvc.perform(get("/parents/1"))
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
        .andReturn();

    mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isNotFound());
  }

  @Test
  public void shouldReturnAParentAfterItsCreation() throws Exception {
    when(repository.save(any(Parent.class))).thenReturn(peterParker);

    MvcResult mvcResult = mockMvc.perform(post("/parents").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Peter\",\"surname\":\"Parker\",\"address\":\"5th Avenue\"}"))
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(Parent.class)))
        .andReturn();

    mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("{\"id\":1,\"name\":\"Peter\",\"surname\":\"Parker\",\"address\":\"5th Avenue\"}"));
  }

  @Test
  public void shouldReturnNotFoundWhenUpdatingANonExistingParent() throws Exception {
    when(repository.findOne(any(Long.class))).thenReturn(null);

    MvcResult mvcResult = mockMvc.perform(put("/parents/1").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Peter\",\"surname\":\"Griffin\",\"address\":\"5th Avenue\"}"))
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
        .andReturn();

    mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isNotFound());
  }

  @Test
  public void shouldReturnAnUpdatedParentWhenUpdatingAnExistingParent() throws Exception {
    when(repository.findOne(any(Long.class))).thenReturn(peterParker);
    when(repository.save(any(Parent.class))).thenReturn(new Parent(peterParker.getId(), peterParker.getName(), "Griffin", peterParker.getAddress(), peterParker.getCreatedAt(), peterParker.getUpdatedAt()));

    MvcResult mvcResult = mockMvc.perform(put("/parents/1").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Peter\",\"surname\":\"Griffin\",\"address\":\"5th Avenue\"}"))
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
        .andReturn();

    mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("{\"id\":1,\"name\":\"Peter\",\"surname\":\"Griffin\",\"address\":\"5th Avenue\",\"createdAt\":1494567900000,\"updatedAt\":1496303700000}"));
  }

  @Test
  public void shouldReturnNotFoundWhenDeletingANonExistingParent() throws Exception {
    when(repository.findOne(any(Long.class))).thenReturn(null);

    MvcResult mvcResult = mockMvc.perform(delete("/parents/1"))
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
        .andReturn();

    mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isNotFound());
  }

  @Test
  public void shouldReturnOkWhenDeletingAnExistingParent() throws Exception {
    when(repository.findOne(any(Long.class))).thenReturn(peterParker);

    MvcResult mvcResult = mockMvc.perform(delete("/parents/1"))
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
        .andReturn();

    mockMvc.perform(asyncDispatch(mvcResult))
        .andExpect(status().isOk());
  }
}