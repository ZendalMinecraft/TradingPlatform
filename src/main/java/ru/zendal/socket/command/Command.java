/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.socket.command;

import org.bson.Document;

public interface Command {


    public Document process(Document incomingDocument);


    public boolean canProcess(Document incomingDocument);
}