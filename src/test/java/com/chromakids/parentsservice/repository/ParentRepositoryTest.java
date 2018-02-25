package com.chromakids.parentsservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.chromakids.parentsservice.model.Parent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParentRepositoryTest {

  @Autowired
  private ParentRepository repository;

  private Parent peterParker;

  @Before
  public void setUp() throws ParseException {
    peterParker = new Parent.Builder()
        .setId(1L)
        .setName("Peter")
        .setSurname("Parker")
        .setAddress("5th Avenue")
        .setCreatedAt(new SimpleDateFormat("d-MMM-yyyy,HH:mm:ss aaa").parse("29-Apr-2010,13:00:14 PM"))
        .setUpdatedAt(new SimpleDateFormat("d-MMM-yyyy,HH:mm:ss aaa").parse("29-Apr-2010,13:00:14 PM"))
        .createParent();
  }

  @Test
  public void savesAParent() {
    repository.save(peterParker);
    assertThat(repository.findOne(peterParker.getId())).isNotNull();
  }

  @Test
  public void deletesAParent() {
    repository.save(peterParker);
    assertThat(repository.findOne(peterParker.getId())).isNotNull();

    repository.delete(peterParker);
    assertThat(repository.findOne(peterParker.getId())).isNull();
  }
}