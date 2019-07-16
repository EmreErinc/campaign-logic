package com.finartz.intern.campaignlogic.model.entity;

import com.finartz.intern.campaignlogic.model.value.CartItem;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "sold_carts")
public class SoldCartEntity {
  @Id
  private String id;
  private String relatedCartId;
  private Integer accountId;
  private List<CartItem> cartItems;
}
