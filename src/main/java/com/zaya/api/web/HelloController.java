package com.zaya.api.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
  @GetMapping("/health")
  public String health() {
    return "Zaya API up";
  }
}
