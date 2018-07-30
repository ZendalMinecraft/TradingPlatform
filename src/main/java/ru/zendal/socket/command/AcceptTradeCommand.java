/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.socket.command;

import org.bson.Document;
import ru.zendal.session.TradeSessionManager;
import ru.zendal.socket.command.exception.ProcessCommandException;

public class AcceptTradeCommand implements Command{
    public AcceptTradeCommand(TradeSessionManager sessionManager) {

    }

    @Override
    public Document process(Document incomingDocument) throws ProcessCommandException {
        Document document = (Document) incomingDocument.get("data");
        if (document==null){
            throw new ProcessCommandException()
        }
    }

    @Override
    public String getNameCommand() {
        return "acceptTrade";
    }
}
