package ru.zendal;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.zendal.config.TradingPlatformConfiguration;

@Data
@Builder
@AllArgsConstructor
public class TradingPlatformCore {


    private final String pathToRoot;


}
