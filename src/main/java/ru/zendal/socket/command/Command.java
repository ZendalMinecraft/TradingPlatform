package ru.zendal.socket.command;

import org.bson.Document;

public interface Command {


    public Document process(Document incomingDocument);


    public boolean canProcess(Document incomingDocument);
}
