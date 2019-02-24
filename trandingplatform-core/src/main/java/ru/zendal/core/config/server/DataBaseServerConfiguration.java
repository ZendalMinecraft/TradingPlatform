/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package test.zendal.core.core.config.server;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class DataBaseServerConfiguration {

    /**
     * Type of source
     */
    private TypeSource typeSource;

    /**
     * Host for dataBase source
     */
    private String host;

    /**
     * Username for dataBase source
     */
    private String username;

    /**
     * Name database for dataBase source
     */
    private String nameDataBase;

    /**
     * Password for dataBase source
     */
    private String password;


    /**
     * Port for database source
     */
    private int port;


}
