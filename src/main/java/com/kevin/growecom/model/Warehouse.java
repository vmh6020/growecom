package com.kevin.growecom.model;

import com.kevin.growecom.util.enums.LocationCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ware_house")
@SQLDelete(sql = "UPDATE ware_house SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Warehouse extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String address;

  @Enumerated(EnumType.STRING)
  private LocationCode locationCode;

  private Boolean isActive;

  @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
  private List<Inventory> inventories;

}
