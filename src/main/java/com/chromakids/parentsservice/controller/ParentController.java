package com.chromakids.parentsservice.controller;

import com.chromakids.parentsservice.model.Parent;
import com.chromakids.parentsservice.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Callable;

import javax.validation.Valid;

@RestController
@RequestMapping("/parents")
public class ParentController {

  @Autowired
  ParentRepository parentRepository;

  @GetMapping("")
  public Callable<List<Parent>> getAllParents() {
    return () -> parentRepository.findAll();
  }

  @PostMapping("")
  public Callable<Parent> createParent(@Valid @RequestBody Parent parent) {
    return () -> parentRepository.save(parent);
  }

  @GetMapping("/{id}")
  public Callable<ResponseEntity<Parent>> getParentById(@PathVariable(value = "id") Long parentId) {
    return () -> {
      Parent parent = parentRepository.findOne(parentId);
      if (parent == null) {
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(parent);
    };
  }

  @PutMapping("/{id}")
  public Callable<ResponseEntity<Parent>> updateParent(@PathVariable(value = "id") Long parentId, @Valid @RequestBody Parent parentDetails) {
    return () -> {
      Parent parent = parentRepository.findOne(parentId);
      if (parent == null) {
        return ResponseEntity.notFound().build();
      }
      parent.setName(parentDetails.getName());
      parent.setSurname(parentDetails.getSurname());
      parent.setAddress(parentDetails.getAddress());

      Parent updatedNote = parentRepository.save(parent);
      return ResponseEntity.ok(updatedNote);
    };
  }

  @DeleteMapping("/{id}")
  public Callable<ResponseEntity<Parent>> deleteParent(@PathVariable(value = "id") Long parentId) {
    return () -> {
      Parent parent = parentRepository.findOne(parentId);
      if (parent == null) {
        return ResponseEntity.notFound().build();
      }

      parentRepository.delete(parent);
      return ResponseEntity.ok().build();
    };
  }

}
