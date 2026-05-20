package com.kevin.growecom.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kevin.growecom.util.enum2.Gender;
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
@Table(name = "\"user\"")
@SQLDelete(sql = "UPDATE user SET delete_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("delete_at IS NULL")
public class User extends BaseEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Enumerated(EnumType.STRING)
  private Gender sex;

  private String email;

  private String phoneNumber;

  private String birthDate;

  private String address;

  @JsonProperty
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Order> orders;
}
