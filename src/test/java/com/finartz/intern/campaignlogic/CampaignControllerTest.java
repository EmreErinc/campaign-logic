package com.finartz.intern.campaignlogic;

import com.finartz.intern.campaignlogic.model.request.AddCampaignRequest;
import com.finartz.intern.campaignlogic.model.request.AddSellerRequest;
import com.finartz.intern.campaignlogic.model.response.CampaignResponse;
import com.finartz.intern.campaignlogic.model.response.ItemResponse;
import com.finartz.intern.campaignlogic.model.response.RegisterResponse;
import com.finartz.intern.campaignlogic.model.response.SellerResponse;
import com.finartz.intern.campaignlogic.service.AccountService;
import com.finartz.intern.campaignlogic.service.CampaignService;
import com.finartz.intern.campaignlogic.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
@TestPropertySource(locations = {"classpath:application-test.properties"})
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "test")
public class CampaignControllerTest extends BaseTestController{

  @Autowired
  private CampaignService campaignService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private SellerService sellerService;

  private RegisterResponse sellerAccountRegisterResponse;

  private ItemResponse itemResponse1;
  private Integer sellerAccountId;

  @Before
  public void initialize() {
    //create seller account
    sellerAccountRegisterResponse = generateSellerAccount();
    log.info("SELLER ACCOUNT CREATED : " + sellerAccountRegisterResponse.toString());

    SellerResponse sellerResponse = sellerService.addSeller(sellerAccountId, AddSellerRequest.builder()
        .name("Test Seller")
        .address("Test Mahallesi, Seller Sokak, No: 1, Daire: 1")
        .build());
    log.info("SELLER CREATED : " + sellerResponse.toString());

    //add item
    itemResponse1 = generateItem(sellerAccountId);
    log.info("ITEM CREATED : " + itemResponse1.toString());
  }

  @Test
  public void addCampaignTest() {
    AddCampaignRequest addCampaignRequest = AddCampaignRequest.builder()
        .title("sample campaign")
        .itemId(itemResponse1.getItemId())
        .cartLimit(3)
        .campaignLimit(4)
        .requirement(5)
        .gift(2)
        .startAt(1562939866630L)
        .endAt(1577653200000L)
        .build();

    CampaignResponse campaignResponse = campaignService.addCampaign(sellerAccountId, addCampaignRequest);
    log.info("CAMPAIGN CREATED : " + campaignResponse.toString());

    assertNotNull(campaignResponse);
    assertEquals(addCampaignRequest.getRequirement(), campaignResponse.getBadge().getRequirement());
    assertEquals(addCampaignRequest.getGift(), campaignResponse.getBadge().getGift());
    assertEquals(addCampaignRequest.getCartLimit(), campaignResponse.getCartLimit());
    assertEquals(addCampaignRequest.getCampaignLimit(), campaignResponse.getCampaignLimit());
    assertEquals(addCampaignRequest.getEndAt(), campaignResponse.getEndAt());
    assertEquals(addCampaignRequest.getItemId(), campaignResponse.getItemId());
    assertEquals(addCampaignRequest.getTitle(), campaignResponse.getTitle());
  }

  //activate campaign
  //deactivate campaign
}
