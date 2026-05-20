package com.kevin.growecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inventory")
@SQLDelete(sql = "UPDATE inventory SET delete_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("delete_at IS NULL")
public class Inventory extends BaseEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "ware_house_id")
  private WareHouse wareHouse;

  private Integer quantity;

  @Version
  private Integer version;
}
