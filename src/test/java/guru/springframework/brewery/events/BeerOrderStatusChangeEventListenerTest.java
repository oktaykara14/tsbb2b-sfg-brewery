package guru.springframework.brewery.events;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import guru.springframework.brewery.domain.BeerOrder;
import guru.springframework.brewery.domain.OrderStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@WireMockTest
class BeerOrderStatusChangeEventListenerTest {

    BeerOrderStatusChangeEventListener listener;

    @BeforeEach
    void setUp() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        listener = new BeerOrderStatusChangeEventListener(restTemplateBuilder);

    }

    @Test
    void listen(WireMockRuntimeInfo wireMockRuntimeInfo) {
        stubFor(post("/update").willReturn(ok()));

        WireMock wireMock = wireMockRuntimeInfo.getWireMock();
        wireMock.register(post("/update").willReturn(ok()));


        BeerOrder beerOrder = BeerOrder.builder()
                .orderStatus(OrderStatusEnum.READY)
                .orderStatusCallbackUrl("http://localhost:" + wireMockRuntimeInfo.getHttpPort() + "/update")
                .createdDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        BeerOrderStatusChangeEvent event = new BeerOrderStatusChangeEvent(beerOrder, OrderStatusEnum.NEW);

        listener.listen(event);

        verify(1, postRequestedFor(urlEqualTo("/update")));

    }
}