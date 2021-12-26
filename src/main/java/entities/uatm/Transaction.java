package entities.uatm;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "transaction")
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @Column(name = "transaction_card_number")
    private Long transactionCardNumber;
    @Column(name = "transaction_account_number")
    private Long transactionAccountNumber;
    @Column(name = "transaction_date")
    private LocalDate transactionDate;
    @Column(name = "transaction_amount")
    private BigDecimal transactionAmount;
    @Column(name = "transaction_description")
    private String transactionDescription;
    @Column(name = "transaction_source")
    private String transactionSource;
}
