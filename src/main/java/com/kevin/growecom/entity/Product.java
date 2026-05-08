package com.kevin.growecom.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private BigDecimal price;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private List<Inventory> inventories;

  private Instant createdAt;

  private Instant updatedAt;

}
