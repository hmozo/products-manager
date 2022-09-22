package com.techtalk.productsservice.domain.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "productlookup")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductLookupEntity implements Serializable {
    @Id
    private String productId;
    @Column(unique = true)
    private String title;
}
