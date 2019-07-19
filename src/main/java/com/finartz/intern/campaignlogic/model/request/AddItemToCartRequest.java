package com.finartz.intern.campaignlogic.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddItemToCartRequest {
  @NotNull(message = "itemId should not be empty")
  private Integer itemId;

  @NotNull(message = "count should not be empty")
  private Integer count;

  private Integer variantId;
}