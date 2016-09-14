package epizza.order;

import lombok.AllArgsConstructor;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CouponInitializer implements InitializingBean {

    private final CouponRepository couponRepository;

    @Override
    @Transactional
    public void afterPropertiesSet() {
        couponRepository.saveAndFlush(
                Coupon.builder()
                        .code("5EURO")
                        .value(Money.of(5.0, "EUR"))
                        .build()
        );
    }
}
