package com.techtalk.productsservice.application.commandgateway;

import com.techtalk.productsservice.domain.ProductLookupRepository;
import com.techtalk.productsservice.domain.commands.CreateProductCommand;
import com.techtalk.productsservice.domain.projection.ProductLookupEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

@Component
@Slf4j
@AllArgsConstructor
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookupRepository productLookupRepository;

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> messages) {
        return (index, command)->{
            log.info("Intercepted command: " + command.getPayloadType());
            if(CreateProductCommand.class.equals(command.getPayloadType())){
                CreateProductCommand createProductCommand= (CreateProductCommand)command.getPayload();
                ProductLookupEntity productLookupEntity= productLookupRepository.findByTitle(createProductCommand.getTitle());

                if (productLookupEntity!=null){
                    throw new IllegalStateException(String.format("Product with productId %s or title %s already exists",
                            createProductCommand.getProductId(), createProductCommand.getTitle()));
                }
            }
            return command;
        };
    }
}
