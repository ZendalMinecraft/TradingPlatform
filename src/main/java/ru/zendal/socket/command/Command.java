/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.socket.command;

import org.bson.Document;
import ru.zendal.socket.command.exception.ProcessCommandException;

/**
 * Command Server Interface
 */
public interface Command {


    /**
     * Process command
     *
     * @param incomingDocument
     * @return
     * @throws ProcessCommandException
     */
    Document process(Document incomingDocument) throws ProcessCommandException;

    /**
     * Can be this
     *
     * @param incomingDocument
     * @return
     */
    boolean canProcess(Document incomingDocument);
}
