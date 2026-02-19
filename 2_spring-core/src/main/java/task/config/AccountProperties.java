package task.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AccountProperties {

    @Value("${account.default-amount}")
    private double defaultAmount;

    @Value("${account.transfer-commission}")
    private double transferCommission;
}
