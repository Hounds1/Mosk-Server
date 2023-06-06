package team.mosk.api.server.domain.subscribe.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import team.mosk.api.server.ControllerIntegrationSupport;
import team.mosk.api.server.domain.store.util.WithAuthUser;
import team.mosk.api.server.domain.subscribe.dto.SubscribeHistoryResponse;
import team.mosk.api.server.domain.subscribe.dto.SubscribeResponse;
import team.mosk.api.server.domain.subscribe.service.SubscribeReadService;
import team.mosk.api.server.domain.subscribe.util.GivenSubscribe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class SubscribeControllerTest extends ControllerIntegrationSupport {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("현재 로그인 중인 계정의 모든 구독 기록을 가져온다.")
    @WithAuthUser
    void findAllSubHistoriesByLoginAccount() throws Exception {
        List<SubscribeHistoryResponse> list = new ArrayList<>();
        list.add(SubscribeHistoryResponse.of(GivenSubscribe.toEntityWithStoreAndCount(1L, 1000)));

        when(subscribeReadService.findAllByStoreId(any())).thenReturn(list);

        mockMvc.perform(get("/api/v1/subscribes"))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].storeId").value(1L))
                .andExpect(jsonPath("$.data[0].startDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.data[0].endDate").value(LocalDate.now().plusMonths(1L).toString()))
                .andExpect(jsonPath("$.data[0].amount").value(1000))
                .andDo(print());
    }

    @Test
    @DisplayName("결제와 함께 구독 정보를 생성한다.")
    @WithAuthUser
    void createNewSubscribeWithPayment() throws Exception {
        // TODO: 2023-06-06 결제 후 구독 생성 테스트 작성 
    }
}
